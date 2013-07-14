package simple.junit.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static java.io.File.separator;
import static java.lang.String.format;
import static simple.junit.utils.Arrays.isNullOrEmpty;
import static simple.junit.utils.Closeables.closeQuietly;
import static simple.junit.utils.Flushables.flushQuietly;
import static simple.junit.utils.Preconditions.checkNotNull;
import static simple.junit.utils.Strings.append;
import static simple.junit.utils.Strings.quote;

/**
 * Utility methods related to files.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Files {
	private Files() {
	}

	/**
	 * Returns the names of the files inside the specified directory.
	 * 
	 * @param dirName
	 *            the name of the directory to start the search from.
	 * @param recurse
	 *            if {@code true}, we will look in subdirectories.
	 * @return the names of the files inside the specified directory.
	 * @throws IllegalArgumentException
	 *             if the given directory name does not point to an existing
	 *             directory.
	 */
	public static @Nonnull
	List<String> fileNamesIn(@Nonnull String dirName, boolean recurse) {
		File dir = new File(dirName);
		if (!dir.isDirectory()) {
			throw new IllegalArgumentException(format("%s is not a directory",
					quote(dirName)));
		}
		return fileNamesIn(dir, recurse);
	}

	/**
	 * Returns the names of the files inside the specified directory.
	 * 
	 * @param dir
	 *            the name of the directory to start the search from.
	 * @param recurse
	 *            if {@code true}, we will look in subdirectories.
	 * @return the names of the files inside the specified directory.
	 */
	private static @Nonnull
	List<String> fileNamesIn(@Nonnull File dir, boolean recurse) {
		List<String> fileNames = new ArrayList<String>();
		File[] existingFiles = dir.listFiles();
		if (existingFiles == null) {
			return fileNames;
		}
		for (File existingFile : existingFiles) {
			if (existingFile.isDirectory()) {
				if (recurse) {
					fileNames.addAll(fileNamesIn(existingFile, recurse));
				}
				continue;
			}
			String filename = existingFile.getAbsolutePath();
			if (!fileNames.contains(filename)) {
				fileNames.add(filename);
			}
		}
		return fileNames;
	}

	/**
	 * @return the system's temporary directory.
	 * @throws IORuntimeException
	 *             if this method cannot find or create the system's temporary
	 *             directory.
	 */
	public static @Nonnull
	File temporaryFolder() {
		File temp = new File(temporaryFolderPath());
		if (!temp.isDirectory()) {
			throw new IORuntimeException("Unable to find temporary directory");
		}
		return temp;
	}

	/**
	 * Returns the path of the system's temporary directory. This method appends
	 * the system's file separator at the end of the path.
	 * 
	 * @return the path of the system's temporary directory.
	 */
	public static @Nonnull
	String temporaryFolderPath() {
		String fileSeparator = checkNotNull(separator);
		String tmpDirPath = checkNotNull(System.getProperty("java.io.tmpdir"));
		return append(fileSeparator).to(tmpDirPath);
	}

	/**
	 * Creates a new file in the system's temporary directory. The name of the
	 * file will be the result of:
	 * <p/>
	 * 
	 * <pre>
	 * concat(String.valueOf(System.currentTimeMillis()), &quot;.txt&quot;);
	 * </pre>
	 * 
	 * @return the created file.
	 */
	public static @Nonnull
	File newTemporaryFile() {
		String tempFileName = String.format("%d.%s",
				System.currentTimeMillis(), ".txt");
		return newFile(temporaryFolderPath() + tempFileName);
	}

	/**
	 * Creates a new directory in the system's temporary directory. The name of
	 * the directory will be the result of:
	 * <p/>
	 * 
	 * <pre>
	 * System.currentTimeMillis();
	 * </pre>
	 * 
	 * @return the created file.
	 */
	public static @Nonnull
	File newTemporaryFolder() {
		String tempFileName = String.valueOf(System.currentTimeMillis());
		return newFolder(temporaryFolderPath() + tempFileName);
	}

	/**
	 * Creates a new file using the given path.
	 * 
	 * @param path
	 *            the path of the new file.
	 * @return the new created file.
	 * @throws IORuntimeException
	 *             if the path belongs to an existing non-empty directory.
	 * @throws IORuntimeException
	 *             if the path belongs to an existing file.
	 * @throws IORuntimeException
	 *             if any I/O error is thrown when creating the new file.
	 */
	public static @Nonnull
	File newFile(@Nonnull String path) {
		File file = new File(path);
		if (file.isDirectory() && !isNullOrEmpty(file.list())) {
			throw cannotCreateNewFile(path,
					"a non-empty directory was found with the same path");
		}
		try {
			if (!file.createNewFile()) {
				throw cannotCreateNewFile(path,
						"a file was found with the same path");
			}
		} catch (IOException e) {
			throw cannotCreateNewFile(path, e);
		}
		return file;
	}

	/**
	 * Creates a new directory using the given path.
	 * 
	 * @param path
	 *            the path of the new directory.
	 * @return the new created directory.
	 * @throws IORuntimeException
	 *             if the path belongs to an existing non-empty directory.
	 * @throws IORuntimeException
	 *             if the path belongs to an existing file.
	 * @throws IORuntimeException
	 *             if any I/O error is thrown when creating the new directory.
	 */
	public static @Nonnull
	File newFolder(@Nonnull String path) {
		File file = new File(path);
		if (file.isDirectory() && !isNullOrEmpty(file.list())) {
			throw cannotCreateNewFile(path,
					"a non-empty directory was found with the same path");
		}
		if (!file.mkdir()) {
			throw cannotCreateNewFile(path,
					"a file was found with the same path");
		}
		return file;
	}

	private static @Nonnull
	IORuntimeException cannotCreateNewFile(@Nonnull String path,
			@Nonnull String reason) {
		throw cannotCreateNewFile(path, reason, null);
	}

	private static @Nonnull
	IORuntimeException cannotCreateNewFile(@Nonnull String path,
			@Nonnull IOException cause) {
		throw cannotCreateNewFile(path, null, cause);
	}

	private static @Nonnull
	IORuntimeException cannotCreateNewFile(@Nonnull String path,
			@Nullable String reason, @Nullable IOException cause) {
		String message = String.format("Unable to create the new file %s",
				quote(path));
		if (!Strings.isNullOrEmpty(reason)) {
			message = String.format("%s: %s", message, reason);
		}
		return new IORuntimeException(checkNotNull(message), cause);
	}

	/**
	 * Flushes and closes the given {@link Writer}. Any I/O errors caught by
	 * this method are ignored and not re-thrown.
	 * 
	 * @param writer
	 *            the writer to flush and close.
	 */
	public static void flushAndClose(@Nullable Writer writer) {
		if (writer == null) {
			return;
		}
		flushQuietly(writer);
		closeQuietly(writer);
	}

	/**
	 * Flushes and closes the given {@link OutputStream}. Any I/O errors caught
	 * by this method are ignored and not re-thrown.
	 * 
	 * @param out
	 *            the output stream to flush and close.
	 */
	public static void flushAndClose(@Nullable OutputStream out) {
		if (out == null) {
			return;
		}
		flushQuietly(out);
		closeQuietly(out);
	}

	/**
	 * Returns the current directory.
	 * 
	 * @return the current directory.
	 * @throws IORuntimeException
	 *             if the current directory cannot be obtained.
	 */
	public static @Nonnull
	File currentFolder() {
		try {
			return checkNotNull(new File(".").getCanonicalFile());
		} catch (IOException e) {
			throw new IORuntimeException("Unable to get current directory", e);
		}
	}

	/**
	 * Deletes the given file or directory.
	 * 
	 * @param file
	 *            the file or directory to delete.
	 */
	public static void delete(@Nonnull File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		for (File f : file.listFiles()) {
			delete(checkNotNull(f));
		}
		file.delete();
	}

	/**
	 * Loads the text content of a file into a character string.
	 * 
	 * @param file
	 *            the file.
	 * @param charsetName
	 *            the name of the character set to use.
	 * @return the content of the file.
	 * @throws IllegalArgumentException
	 *             if the given character set is not supported on this platform.
	 * @throws IORuntimeException
	 *             if an I/O exception occurs.
	 */
	public static @Nonnull
	String contentOf(@Nonnull File file, @Nonnull String charsetName) {
		if (!Charset.isSupported(charsetName)) {
			throw new IllegalArgumentException(String.format(
					"Charset:<'%s'> is not supported on this system",
					charsetName));
		}
		return contentOf(file, checkNotNull(Charset.forName(charsetName)));
	}

	/**
	 * Loads the text content of a file into a character string.
	 * 
	 * @param file
	 *            the file.
	 * @param charset
	 *            the character set to use.
	 * @return the content of the file.
	 * @throws NullPointerException
	 *             if the given charset is {@code null}.
	 * @throws IORuntimeException
	 *             if an I/O exception occurs.
	 */
	public static @Nonnull
	String contentOf(@Nonnull File file, @Nonnull Charset charset) {
		checkNotNull(charset);
		try {
			return loadContents(file, charset);
		} catch (IOException e) {
			throw new IORuntimeException("Unable to read "
					+ file.getAbsolutePath(), e);
		}
	}

	private static @Nonnull
	String loadContents(@Nonnull File file, @Nonnull Charset charset)
			throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), charset));
			StringWriter writer = new StringWriter();
			int c;
			while ((c = reader.read()) != -1) {
				writer.write(c);
			}
			return checkNotNull(writer.toString());
		} finally {
			closeQuietly(reader);
		}
	}
}

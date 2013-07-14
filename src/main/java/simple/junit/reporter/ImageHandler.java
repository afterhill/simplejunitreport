package simple.junit.reporter;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import static java.io.File.separator;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import static simple.junit.reporter.ScreenshotTaker.PNG_EXTENSION;
import static simple.junit.utils.Files.flushAndClose;
import static simple.junit.utils.Files.newFile;
import static simple.junit.utils.Strings.isNullOrEmpty;

public final class ImageHandler {

	private static final String UTF_8 = "UTF-8";
	private static final String EMPTY_STRING = "";

	private static Logger logger = Logger.getAnonymousLogger();

	public static String encodeBase64(BufferedImage image) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, PNG_EXTENSION, out);
			byte[] encoded = Base64.encodeBase64(out.toByteArray());
			return new String(encoded, UTF_8);
		} catch (IOException e) {
			logger.log(SEVERE, "Unable to encode image", e);
			return null;
		} finally {
			flushAndClose(out);
		}
	}

	public static BufferedImage decodeBase64(String encoded) {
		ByteArrayInputStream in = null;
		try {
			byte[] toDecode = encoded.getBytes(UTF_8);
			in = new ByteArrayInputStream(Base64.decodeBase64(toDecode));
			return ImageIO.read(in);
		} catch (IOException e) {
			logger.log(SEVERE, "Unable to decode image", e);
			return null;
		}
	}

	public static String decodeBase64AndSaveAsPng(String encoded,
			String imageFilePath) {
		if (isNullOrEmpty(encoded))
			return EMPTY_STRING;
		if (isNullOrEmpty(imageFilePath))
			return EMPTY_STRING;
		String realPath = imageFilePath.replace("/", separator);
		BufferedImage image = decodeBase64(encoded);
		File newFile = new File(realPath);
		if (newFile.exists())
			return EMPTY_STRING;
		try {
			newFile = newFile(realPath);
			ImageIO.write(image, PNG_EXTENSION, newFile);
		} catch (Exception ignored) {
			logger.log(WARNING, ignored.getMessage());
		}
		return EMPTY_STRING;
	}

	private ImageHandler() {
	}
}

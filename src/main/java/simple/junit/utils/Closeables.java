package simple.junit.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Closeable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility methods related to {@link Closeable}.
 * 
 * @author Yvonne Wang
 */
public final class Closeables {
	private static Logger logger = Logger.getLogger(Closeables.class
			.getCanonicalName());

	private Closeables() {
	}

	/**
	 * Closes the given {@link Closeable}s, ignoring any thrown exceptions.
	 * 
	 * @param closeables
	 *            the {@code Closeable}s to close.
	 */
	public static void closeQuietly(@Nonnull Closeable... closeables) {
		for (Closeable c : closeables) {
			close(c);
		}
	}

	private static void close(@Nullable Closeable c) {
		if (c == null) {
			return;
		}
		try {
			c.close();
		} catch (Throwable t) {
			logger.log(Level.WARNING, "Error occurred while closing " + c, t);
		}
	}
}

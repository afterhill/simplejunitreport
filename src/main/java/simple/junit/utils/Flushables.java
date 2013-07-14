package simple.junit.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Flushable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility methods related to {@link Flushable}.
 * 
 * @author Yvonne Wang
 */
public class Flushables {
	private static Logger logger = Logger.getLogger(Flushables.class
			.getCanonicalName());

	private Flushables() {
	}

	/**
	 * Flushes the given {@link Flushable}s, ignoring any thrown exceptions.
	 * 
	 * @param flushables
	 *            the {@code Flushable}s to flush.
	 */
	public static void flushQuietly(@Nonnull Flushable... flushables) {
		for (Flushable f : flushables) {
			flush(f);
		}
	}

	private static void flush(@Nullable Flushable f) {
		if (f == null) {
			return;
		}
		try {
			f.flush();
		} catch (Throwable t) {
			logger.log(Level.WARNING, "Error occurred while flushing " + f, t);
		}
	}
}

package simple.junit.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

/**
 * Signals that an I/O exception of some sort has occurred.
 * 
 * @author Yvonne Wang
 */
public final class IORuntimeException extends RuntimeException {

	private static final long serialVersionUID = -5554131979926598504L;

	/**
	 * Creates a new {@link IORuntimeException}.
	 * 
	 * @param message
	 *            the detail message.
	 */
	public IORuntimeException(@Nonnull String message) {
		super(message);
	}

	/**
	 * Creates a new {@link IORuntimeException}.
	 * 
	 * @param message
	 *            the detail message.
	 * @param cause
	 *            the cause of the error.
	 */
	public IORuntimeException(@Nonnull String message,
			@Nullable IOException cause) {
		super(message, cause);
	}
}

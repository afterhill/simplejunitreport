package simple.junit.reporter;

public final class ImageException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ImageException(String message) {
		super(message);
	}

	public ImageException(String message, Throwable cause) {
		super(message, cause);
	}
}

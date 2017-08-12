package main.com.swayam.json;

public class JsonException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public JsonException(final String message) {
		super(message);
	}
	
	public JsonException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	public JsonException(final Throwable cause) {
		this(cause.getMessage(), cause);
	}
	
}

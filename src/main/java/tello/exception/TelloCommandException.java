package tello.exception;

public class TelloCommandException extends RuntimeException {

  public TelloCommandException(String message) {
    super(message);
  }
  
  public TelloCommandException(String message, Throwable cause) {
	  super(message, cause);
  }

  public TelloCommandException(Throwable cause) {
	  super(cause.getMessage(), cause);
  }
}

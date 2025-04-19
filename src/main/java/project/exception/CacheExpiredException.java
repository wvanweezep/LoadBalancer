package project.exception;

public class CacheExpiredException extends RuntimeException {
    public CacheExpiredException(String message) {
        super(message);
    }
}

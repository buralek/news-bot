package buralek.newsbot.collect.exception;

public class CollectException extends Exception {
    public CollectException(String message) {
        super(message);
    }

    public CollectException(String message, Throwable cause) {
        super(message, cause);
    }
}

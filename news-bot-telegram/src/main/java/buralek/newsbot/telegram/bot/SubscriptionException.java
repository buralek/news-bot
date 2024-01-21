package buralek.newsbot.telegram.bot;

public class SubscriptionException extends Exception{
    public SubscriptionException(String message) {
        super(message);
    }

    public SubscriptionException(String message, Throwable cause) {
        super(message, cause);
    }
}

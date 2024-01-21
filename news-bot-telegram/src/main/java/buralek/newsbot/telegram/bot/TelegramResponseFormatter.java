package buralek.newsbot.telegram.bot;

import buralek.newsbot.data.entity.Page;
import buralek.newsbot.data.entity.Subscription;

import java.util.List;

public class TelegramResponseFormatter {

    public static String toTelegramMessage(Page page) {
        return String.format("""
                        <b>Post:</b> %s
                        <b>Author:</b> %s
                        <b>Published date:</b> %s
                        <b>Link:</b> %s
                        """,
                page.getName(),
                page.getAuthor(),
                page.getPublishedDate(),
                page.getUrl());
    }

    public static String toTelegramMessage(List<Subscription> subscriptions) {
        StringBuilder messageBuilder = new StringBuilder();
        subscriptions.forEach(subscription -> messageBuilder.append(toTelegramMessage(subscription)));
        return messageBuilder.toString();
    }

    public static String toTelegramMessage(Subscription subscription) {
        return String.format("""
                        <b>Subscription:</b> %s
                        <b>Url:</b> %s
                        
                        """,
                subscription.getName(),
                subscription.getUrl());
    }
}

package buralek.newsbot.telegram.bot;

import buralek.newsbot.data.entity.Page;

public class PageFormatter {

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
}

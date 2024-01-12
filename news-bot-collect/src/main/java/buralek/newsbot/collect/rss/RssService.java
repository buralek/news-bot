package buralek.newsbot.collect.rss;

import buralek.newsbot.collect.exception.CollectException;
import com.rometools.rome.feed.synd.SyndFeed;

public interface RssService {
    SyndFeed getLastNews(String url) throws CollectException;
}

package buralek.newsbot.logic;

import buralek.newsbot.collect.exception.CollectException;
import buralek.newsbot.collect.rss.RssService;
import buralek.newsbot.data.Subscription;
import buralek.newsbot.data.SubscriptionRepository;
import com.rometools.rome.feed.synd.SyndFeed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultCollectNewsService implements CollectNewsService {
    private final Logger LOG = LoggerFactory.getLogger(DefaultCollectNewsService.class);
    private final SubscriptionRepository subscriptionRepository;
    private final RssService rssService;

    public DefaultCollectNewsService(SubscriptionRepository subscriptionRepository, RssService rssService) {
        this.subscriptionRepository = subscriptionRepository;
        this.rssService = rssService;
    }

    public void receiveAllLastNews() {
        try {
            List<Subscription> subscriptions = subscriptionRepository.findAll();
            for (Subscription subscription : subscriptions) {
                SyndFeed syndFeed = rssService.getLastNews(subscription.getUrl());
                syndFeed.getAuthors();
            }
        } catch (CollectException e) {
            LOG.error("Can't receive last news. The reason is {}", e.getMessage());
        }
    }
}

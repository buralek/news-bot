package buralek.newsbot.logic.action;

import buralek.newsbot.collect.exception.CollectException;
import buralek.newsbot.collect.rss.RssService;
import buralek.newsbot.data.entity.Page;
import buralek.newsbot.data.entity.Subscription;
import buralek.newsbot.data.repository.PageRepository;
import buralek.newsbot.data.repository.SubscriptionRepository;
import buralek.newsbot.logic.collect.CollectNewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class DefaultActionService implements ActionService {
    private final Logger LOG = LoggerFactory.getLogger(DefaultActionService.class);
    private final CollectNewsService collectNewsService;
    private final PageRepository pageRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final RssService rssService;

    public DefaultActionService(CollectNewsService collectNewsService,
                                PageRepository pageRepository,
                                SubscriptionRepository subscriptionRepository,
                                RssService rssService) {
        this.collectNewsService = collectNewsService;
        this.pageRepository = pageRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.rssService = rssService;
    }

    @Override
    @Transactional
    public List<Page> getLastNews(int amount, ChronoUnit chronoUnit) {
        LOG.info("Get news for the last {} {}", amount, chronoUnit);
        collectNewsService.collectLastNews();
        Instant dateAfter = Instant.now().minus(amount, chronoUnit);
        List<Page> lastNews = pageRepository.findAllByPublishedDateAfter(Date.from(dateAfter));
        LOG.info("News for the last {} {} are: {}", amount, chronoUnit, lastNews);
        return lastNews;
    }

    @Override
    @Transactional
    public String addSubscription(String name, String url) {
        LOG.info("Add new subscription with name '{}' and url '{}'", name, url);
        // url validation
        try {
            rssService.getLastNews(url);
        } catch (CollectException e) {
            return String.format("Can't add subscription '%s' because url '%s' is invalid",
                    name, url);
        }

        Subscription newSubscription = new Subscription();
        newSubscription.setName(name);
        newSubscription.setUrl(url);
        subscriptionRepository.save(newSubscription);
        LOG.info("New subscription with name '{}' and url '{}' has been added successfully", name, url);
        return String.format("Subscription '%s' has been added successfully", name);
    }

    @Override
    @Transactional
    public String deleteSubscription(String name) {
        LOG.info("Delete subscription '{}'", name);
        subscriptionRepository.deleteSubscriptionByName(name);
        LOG.info("Subscription '{}' successfully deleted", name);
        return String.format("Subscription %s successfully deleted", name);
    }

    @Override
    @Transactional
    public List<Subscription> findAllSubscriptions() {
        return subscriptionRepository.findAll();
    }
}

package buralek.newsbot.logic;

import buralek.newsbot.collect.exception.CollectException;
import buralek.newsbot.collect.rss.RssService;
import buralek.newsbot.data.entity.Page;
import buralek.newsbot.data.entity.Subscription;
import buralek.newsbot.data.repository.PageRepository;
import buralek.newsbot.data.repository.SubscriptionRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultCollectNewsService implements CollectNewsService {
    private final Logger LOG = LoggerFactory.getLogger(DefaultCollectNewsService.class);

    private final PageRepository pageRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final RssService rssService;

    public DefaultCollectNewsService(PageRepository pageRepository, SubscriptionRepository subscriptionRepository, RssService rssService) {
        this.pageRepository = pageRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.rssService = rssService;
    }

    @Transactional
    @Scheduled(cron = "${service-settings.scheduler.cron}")
    public void receiveAllLastNews() {
        LOG.info("Start to receive news from all subscriptions.");
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        for (Subscription subscription : subscriptions) {
            try {
                LOG.info("Receive news from {}", subscription.getName());

                SyndFeed syndFeed = rssService.getLastNews(subscription.getUrl());
                Map<String, Page> newPages = syndFeed.getEntries()
                        .stream()
                        .map(syndEntry -> toPage(syndEntry, subscription))
                        .collect(Collectors.toMap(Page::getUrl, Function.identity()));

                Map<String, Page> existingPages = pageRepository.findByUrlIn(newPages.keySet())
                        .stream()
                        .collect(Collectors.toMap(Page::getUrl, Function.identity()));

                Set<Page> persistedPages = newPages.entrySet()
                        .stream()
                        .map(newPage -> getUpdatedPage(newPage.getValue(), existingPages.get(newPage.getKey())))
                        .collect(Collectors.toSet());

                pageRepository.saveAll(persistedPages);
                LOG.info("All news from {} subscription received and persisted.", subscription.getUrl());
            } catch (CollectException e) {
                LOG.error("Can't receive last news from {}. The reason is {}",
                        subscription.getName(),
                        e.getMessage());
            }
        }
    }

    private Page toPage(SyndEntry syndEntry, Subscription subscription) {
        Page page = new Page();
        page.setName(syndEntry.getTitle());
        page.setDescription(syndEntry.getDescription().getValue());
        page.setUrl(syndEntry.getUri());
        page.setAuthor(syndEntry.getAuthor());
        page.setPublishedDate(syndEntry.getPublishedDate());
        page.setSubscription(subscription);
        LOG.info("News with title '{}' received", page.getName());
        return page;
    }

    private Page getUpdatedPage(Page newPage, Page existingPage) {
        Page updatedPage = newPage.clone();
        if (existingPage != null) {
            LOG.info("The page with id '{}' and name '{}' has already existed and be updated",
                    existingPage.getId(),
                    existingPage.getName());
            updatedPage.setId(existingPage.getId());
        }
        return updatedPage;
    }
}

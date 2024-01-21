package buralek.newsbot.logic.command;

import buralek.newsbot.data.entity.Page;
import buralek.newsbot.data.repository.PageRepository;
import buralek.newsbot.logic.collect.CollectNewsService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class DefaultCommandHandler implements CommandHandler {
    private final CollectNewsService collectNewsService;
    private final PageRepository pageRepository;

    public DefaultCommandHandler(CollectNewsService collectNewsService, PageRepository pageRepository) {
        this.collectNewsService = collectNewsService;
        this.pageRepository = pageRepository;
    }

    @Override
    public List<Page> onReceive() {
        collectNewsService.collectLastNews();
        Instant dateAfter = Instant.now().minus(1, ChronoUnit.DAYS);
        return pageRepository.findAllByPublishedDateAfter(Date.from(dateAfter));
    }
}

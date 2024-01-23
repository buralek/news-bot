package buralek.newsbot.webapp.controller;

import buralek.newsbot.logic.collect.CollectNewsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//TODO this is a test endpoint and must be removed or secured before real deploy
@RestController
@RequestMapping("collect")
public class CollectNewsController {
    private final CollectNewsService collectNewsService;

    public CollectNewsController(CollectNewsService collectNewsService) {
        this.collectNewsService = collectNewsService;
    }

    @PostMapping
    public void collectNews() {
        collectNewsService.collectLastNews();
    }
}

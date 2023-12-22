package buralek.newsbot.webapp.webhook;

import buralek.newsbot.telegram.bot.NewsWebhookBot;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequestMapping("news-webhook")
public class NewsWebhookController {
    private final NewsWebhookBot newsWebhookBot;

    public NewsWebhookController(NewsWebhookBot newsWebhookBot) {
        this.newsWebhookBot = newsWebhookBot;
    }

    @PostMapping
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return newsWebhookBot.onWebhookUpdateReceived(update);
    }
}

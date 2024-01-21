package buralek.newsbot.webapp.controller;

import buralek.newsbot.telegram.bot.NewsTelegramWebhookBot;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequestMapping("telegram-webhook")
public class TelegramWebhookController {
    private final NewsTelegramWebhookBot newsTelegramWebhookBot;

    public TelegramWebhookController(NewsTelegramWebhookBot newsTelegramWebhookBot) {
        this.newsTelegramWebhookBot = newsTelegramWebhookBot;
    }

    @PostMapping
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return newsTelegramWebhookBot.onWebhookUpdateReceived(update);
    }
}

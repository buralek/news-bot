package buralek.newsbot.telegram.bot;

import buralek.newsbot.telegram.config.TelegramPropertiesConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class NewsWebhookBot extends TelegramWebhookBot {
    private final TelegramPropertiesConfig telegramPropertiesConfig;

    public NewsWebhookBot(TelegramPropertiesConfig telegramPropertiesConfig) {
        super(telegramPropertiesConfig.getBotToken());
        this.telegramPropertiesConfig = telegramPropertiesConfig;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(update.getMessage().getChatId().toString());
            sendMessage.setText("Well, all information looks like noise until you break the code.");
            return sendMessage;
        }
        return null;
    }

    @Override
    public String getBotPath() {
        return telegramPropertiesConfig.getWebhookPath();
    }

    @Override
    public String getBotUsername() {
        return telegramPropertiesConfig.getBotName();
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }
}

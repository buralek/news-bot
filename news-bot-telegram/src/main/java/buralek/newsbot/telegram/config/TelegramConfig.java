package buralek.newsbot.telegram.config;

import buralek.newsbot.telegram.bot.NewsTelegramWebhookBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
@Import({
        TelegramPropertiesConfig.class,
        NewsTelegramWebhookBot.class
})
public class TelegramConfig {
    private final TelegramPropertiesConfig telegramPropertiesConfig;

    public TelegramConfig(TelegramPropertiesConfig telegramPropertiesConfig) {
        this.telegramPropertiesConfig = telegramPropertiesConfig;
    }

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(telegramPropertiesConfig.getWebhookPath()).build();
    }
}

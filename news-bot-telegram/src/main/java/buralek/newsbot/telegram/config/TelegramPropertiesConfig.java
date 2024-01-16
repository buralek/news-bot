package buralek.newsbot.telegram.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramPropertiesConfig {
    @Value("${telegram.api-url}")
    private String apiUrl;
    @Value("${telegram.webhook-path}")
    private String webhookPath;
    @Value("${telegram.bot-name}")
    private String botName;
    @Value("${telegram.bot-token}")
    private String botToken;
    @Value("${telegram.bot-user}")
    private Long botUser;
    @Value("${telegram.bot-chat}")
    private Long botChat;

    public String getApiUrl() {
        return apiUrl;
    }

    public String getWebhookPath() {
        return webhookPath;
    }

    public String getBotName() {
        return botName;
    }

    public String getBotToken() {
        return botToken;
    }

    public Long getBotUser() {
        return botUser;
    }

    public Long getBotChat() {
        return botChat;
    }
}

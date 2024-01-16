package buralek.newsbot.telegram.bot;

import buralek.newsbot.logic.command.CommandHandler;
import buralek.newsbot.telegram.config.TelegramPropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class NewsTelegramWebhookBot extends TelegramWebhookBot {
    private final Logger LOG = LoggerFactory.getLogger(NewsTelegramWebhookBot.class);
    private final String BOT_COMMAND_TYPE = "bot_command";
    private final TelegramPropertiesConfig telegramPropertiesConfig;
    private final CommandHandler commandHandler;

    public NewsTelegramWebhookBot(TelegramPropertiesConfig telegramPropertiesConfig, CommandHandler commandHandler) {
        super(telegramPropertiesConfig.getBotToken());
        this.telegramPropertiesConfig = telegramPropertiesConfig;
        this.commandHandler = commandHandler;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        // TODO update.getMessage().getText() returns all text and it's possible to parse as parameters
        if (update.hasMessage() && update.getMessage().hasText()) {
            // verification
            if (!update.getMessage().getFrom().getId().equals(telegramPropertiesConfig.getBotUser())) {
                return getSimpleAnswer(update, "Well, I can't communicate with you.");
            }
            if (!update.getMessage().getEntities().getFirst().getType().equals(BOT_COMMAND_TYPE)) {
                return getSimpleAnswer(update, "Well, I don't know what to do on this message.");
            }

            CommandEnum command = CommandEnum.valueOfCommandText(
                    update.getMessage().getText().replace(telegramPropertiesConfig.getBotName(), ""));
            switch (command) {
                case RECEIVE:
                    commandHandler.onReceive().stream()
                            .map(PageFormatter::toTelegramMessage)
                            .forEach(this::publishNews);
                    return getSimpleAnswer(update, "Please, read news for the last 24 hours.");
                default:
                    return getSimpleAnswer(update, "Well, I don't know what to do on this command.");
            }
        }
        return null;
    }

    private void publishNews(String messageTest) {
        SendMessage publishedMessage = new SendMessage();
        publishedMessage.setChatId(telegramPropertiesConfig.getBotChat());
        publishedMessage.setParseMode(ParseMode.HTML);
        publishedMessage.setText(messageTest);
        try {
            execute(publishedMessage);
        } catch (TelegramApiException e) {
            LOG.error("Can't send the message {}. The exception is {}",
                    publishedMessage,
                    e.getMessage());
        }
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

    private SendMessage getSimpleAnswer(Update update, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(text);
        return sendMessage;
    }
}

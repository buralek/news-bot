package buralek.newsbot.telegram.bot;

import buralek.newsbot.logic.action.ActionService;
import buralek.newsbot.telegram.config.TelegramPropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.temporal.ChronoUnit;

import static buralek.newsbot.telegram.bot.CommandEnum.ADD_SUBSCRIPTION;
import static buralek.newsbot.telegram.bot.CommandEnum.DELETE_SUBSCRIPTION;

public class NewsTelegramWebhookBot extends TelegramWebhookBot {
    private final Logger LOG = LoggerFactory.getLogger(NewsTelegramWebhookBot.class);
    private final String BOT_COMMAND_TYPE = "bot_command";
    private final TelegramPropertiesConfig telegramPropertiesConfig;
    private final ActionService actionService;

    public NewsTelegramWebhookBot(TelegramPropertiesConfig telegramPropertiesConfig, ActionService actionService) {
        super(telegramPropertiesConfig.getBotToken());
        this.telegramPropertiesConfig = telegramPropertiesConfig;
        this.actionService = actionService;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            // verification
            if (!update.getMessage().getFrom().getId().equals(telegramPropertiesConfig.getBotUser())) {
                return getSimpleAnswer(update, "Well, I can't communicate with you");
            }
            if (!update.getMessage().getEntities().getFirst().getType().equals(BOT_COMMAND_TYPE)) {
                return getSimpleAnswer(update, "Well, I don't know what to do on this message");
            }

            CommandEnum command = CommandEnum.valueOfCommandText(
                    update.getMessage().getEntities().getFirst().getText().replace(telegramPropertiesConfig.getBotName(), ""));
            return switch (command) {
                case RECEIVE_24_HOURS_NEWS -> onReceive24hNews(update);
                case RECEIVE_7_DAYS_NEWS -> onReceive7daysNews(update);
                case ADD_SUBSCRIPTION -> onAddSubscription(update);
                case DELETE_SUBSCRIPTION -> onDeleteSubscription(update);
                case GET_ALL_SUBSCRIPTIONS -> onGetAllSubscriptions(update);
                default -> getSimpleAnswer(update, "Well, I don't know what to do on this command");
            };
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

    private SendMessage onReceive24hNews(Update update) {
        int chronoAmount = 1;
        ChronoUnit chronoUnit = ChronoUnit.DAYS;
        return receiveNews(update, chronoAmount, chronoUnit);
    }

    private SendMessage onReceive7daysNews(Update update) {
        int chronoAmount = 7;
        ChronoUnit chronoUnit = ChronoUnit.DAYS;
        return receiveNews(update, chronoAmount, chronoUnit);
    }

    private SendMessage onAddSubscription(Update update) {
        try {
            String messageBody = update.getMessage().getText();
            String result = actionService.addSubscription(
                    getAddSubscriptionName(messageBody),
                    getAddSubscriptionUrl(messageBody));
            return getSimpleAnswer(update, result);
        } catch (SubscriptionException e) {
            LOG.error(e.getMessage());
            return getSimpleAnswer(update, e.getMessage());
        }
    }

    private SendMessage onDeleteSubscription(Update update) {
        try {
            String messageBody = update.getMessage().getText();
            String deleteSubscriptionName = getDeleteSubscriptionName(messageBody);
            return getSimpleAnswer(update, actionService.deleteSubscription(deleteSubscriptionName));
        } catch (SubscriptionException e) {
            LOG.error(e.getMessage());
            return getSimpleAnswer(update, e.getMessage());
        }
    }

    private SendMessage onGetAllSubscriptions(Update update) {
        publishFormattedMessage(
                update, TelegramResponseFormatter.toTelegramMessage(actionService.findAllSubscriptions()));
        return getSimpleAnswer(update, "Please, check your subscriptions");
    }


    private SendMessage getSimpleAnswer(Update update, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(text);
        return sendMessage;
    }

    private SendMessage receiveNews(Update update, int chronoAmount, ChronoUnit chronoUnit) {
        actionService.getLastNews(chronoAmount, chronoUnit).stream()
                .map(TelegramResponseFormatter::toTelegramMessage)
                .forEach(message -> publishFormattedMessage(update, message));
        return getSimpleAnswer(update,
                String.format("Please, read news for the last %s %s", chronoAmount, chronoUnit));
    }

    private void publishFormattedMessage(Update update, String messageTest) {
        SendMessage publishedMessage = new SendMessage();
        publishedMessage.setChatId(update.getMessage().getChatId().toString());
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

    private String getAddSubscriptionName(String messageBody) throws SubscriptionException {
        String[] messageElements = messageBody.split("\\s+");
        if (messageElements.length == 3
                && messageElements[1].startsWith("-n")) {
            return messageElements[1].replaceFirst("-n", "");
        }
        throw new SubscriptionException(
                String.format("Can't parse the command, please use format '%s -nName -uUrl'",
                        ADD_SUBSCRIPTION.getText()));
    }

    private String getAddSubscriptionUrl(String messageBody) throws SubscriptionException {
        String[] messageElements = messageBody.split("\\s+");
        if (messageElements.length == 3
                && messageElements[2].startsWith("-u")) {
            return messageElements[2].replaceFirst("-u", "");
        }
        throw new SubscriptionException(
                String.format("Can't parse the command, please use format '%s -nName -uUrl'",
                        ADD_SUBSCRIPTION.getText()));
    }

    private String getDeleteSubscriptionName(String messageBody) throws SubscriptionException {
        String[] messageElements = messageBody.split("\\s+");
        if (messageElements.length == 2
                && messageElements[1].startsWith("-n")) {
            return messageElements[1].replaceFirst("-n", "");
        }
        throw new SubscriptionException(
                String.format("Can't parse the command, please use format '%s -nName'",
                        DELETE_SUBSCRIPTION.getText()));
    }
}

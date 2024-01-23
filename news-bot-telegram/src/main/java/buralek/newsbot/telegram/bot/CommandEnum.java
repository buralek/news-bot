package buralek.newsbot.telegram.bot;

public enum CommandEnum {
    RECEIVE_24_HOURS_NEWS("/receive_24h_news"),
    RECEIVE_7_DAYS_NEWS("/receive_7days_news"),
    ADD_SUBSCRIPTION("/add_subscription"),
    DELETE_SUBSCRIPTION("/delete_subscription"),
    GET_ALL_SUBSCRIPTIONS("/get_all_subscriptions"),
    UNKNOWN("unknown-command");
    private final String text;

    CommandEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static CommandEnum valueOfCommandText(String commandText) {
        for (CommandEnum e : values()) {
            if (e.text.equals(commandText)) {
                return e;
            }
        }
        return UNKNOWN;
    }
}

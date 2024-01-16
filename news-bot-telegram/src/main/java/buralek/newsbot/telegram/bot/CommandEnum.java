package buralek.newsbot.telegram.bot;

public enum CommandEnum {
    RECEIVE("/receive"),
    UNKNOWN("unknown-command");
    private String text;

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

package commander.memory;

import java.time.LocalDateTime;

public class Episode {

    private final LocalDateTime timestamp;
    private final String input;
    private final String intent;
    private final String action;
    private final String result;

    public Episode(
            LocalDateTime timestamp,
            String input,
            String intent,
            String action,
            String result) {

        this.timestamp = timestamp;
        this.input = input;
        this.intent = intent;
        this.action = action;
        this.result = result;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getInput() {
        return input;
    }

    public String getIntent() {
        return intent;
    }

    public String getAction() {
        return action;
    }

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {

        return timestamp
                + " | "
                + sanitize(intent)
                + " | "
                + sanitize(action)
                + " | "
                + sanitize(input)
                + " | "
                + sanitize(result);
    }

    private String sanitize(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
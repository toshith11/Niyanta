package commander.core;

public class ContextEntry {

    private final String input;
    private final Intent intent;
    private final Action action;

    public ContextEntry(
            String input,
            Intent intent,
            Action action) {

        this.input = input;
        this.intent = intent;
        this.action = action;
    }

    public String getInput() {
        return input;
    }

    public Intent getIntent() {
        return intent;
    }

    public Action getAction() {
        return action;
    }
}
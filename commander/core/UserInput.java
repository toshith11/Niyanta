package commander.core;

public class UserInput {

    private final String text;
    private final InputSource source;

    public UserInput(String text, InputSource source) {
        this.text = text;
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public InputSource getSource() {
        return source;
    }
}
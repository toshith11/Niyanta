package commander.core;
import java.util.ArrayDeque;
import java.util.Deque;


public class ConversationContext {

    private String lastInput;
    private Intent lastIntent;
    private Action lastAction;

    private String activeTopic;
    private String activeTask;

    private boolean awaitingInput;
    private String expectedInput;

    private final Deque<ContextEntry> recentHistory =
        new ArrayDeque<>();

private static final int MAX_HISTORY = 5;

    public String getLastInput() {
        return lastInput;
    }

    public Intent getLastIntent() {
        return lastIntent;
    }

    public Action getLastAction() {
        return lastAction;
    }

    public String getActiveTopic() {
        return activeTopic;
    }

    public String getActiveTask() {
        return activeTask;
    }

    public boolean isAwaitingInput() {
        return awaitingInput;
    }

    public String getExpectedInput() {
        return expectedInput;
    }

    public void update(
            String input,
            CommandUnderstanding command) {

        if (input != null) {
            lastInput = input;
        }

        if (command != null) {

            lastIntent = command.getIntent();
            lastAction = command.getAction();

            updateTask(command);
            updateTopic(command);
        }
        if (input != null && command != null) {

    recentHistory.addLast(
            new ContextEntry(
                    input,
                    command.getIntent(),
                    command.getAction()
            )
    );

    if (recentHistory.size() > MAX_HISTORY) {
        recentHistory.removeFirst();
    }
}
    }

    public void setAwaitingInput(
            boolean awaitingInput,
            String expectedInput) {

        this.awaitingInput = awaitingInput;
        this.expectedInput = expectedInput;
    }

    public void clearAwaitingInput() {

        awaitingInput = false;
        expectedInput = null;
    }

    private void updateTask(
            CommandUnderstanding command) {

        if (command.getIntent() == Intent.RESEARCH) {
            activeTask = "RESEARCH";
        }

        if (command.getIntent() == Intent.MEMORY) {
            activeTask = "MEMORY";
        }

        if (command.getIntent() == Intent.SYSTEM) {
            activeTask = "SYSTEM";
        }

        if (command.getIntent() == Intent.FILE) {
            activeTask = "FILE";
        }

        if (command.getIntent() == Intent.APPLICATION) {
            activeTask = "APPLICATION";
        }
    }

    private void updateTopic(
            CommandUnderstanding command) {

        if (command.getIntent() == Intent.RESEARCH
                && command.getParameter() != null
                && !command.getParameter().isBlank()) {

            activeTopic = command.getParameter();
        }
    }
    public Deque<ContextEntry> getRecentHistory() {
    return new ArrayDeque<>(recentHistory);
}
}
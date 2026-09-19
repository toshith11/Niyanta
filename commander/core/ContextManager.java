package commander.core;

public class ContextManager {

    private final ConversationContext context;

    public ContextManager() {
        context = new ConversationContext();
    }

    
    public void update(
        String input,
        CommandUnderstanding command) {

    if (command != null
            && command.getEntity() != null
            && command.getEntity().equals("CONTEXT")) {

        return;
    }

    context.update(input, command);
}

    public ConversationContext getContext() {
        return context;
    }

    public void clear() {

        context.update(
                null,
                new CommandUnderstanding(
                        Intent.UNKNOWN,
                        Action.NONE,
                        null,
                        null,
                        0.0,
                        null
                )
        );
    }
    public void setAwaitingInput(String expectedInput) {

    context.setAwaitingInput(
            true,
            expectedInput
    );
}

public void clearAwaitingInput() {
    context.clearAwaitingInput();
}
}
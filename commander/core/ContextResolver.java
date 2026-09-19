package commander.core;

public class ContextResolver {

    public CommandUnderstanding resolve(
            CommandUnderstanding command,
            ConversationContext context,
            String currentInput) {
                   if (command == null || context == null) {
        return command;
    }

    if (context.isAwaitingInput()) {

        String expectedInput =
                context.getExpectedInput();

        if ("RESEARCH_TOPIC".equals(expectedInput)) {

            context.clearAwaitingInput();

            return new CommandUnderstanding(
                    Intent.RESEARCH,
                    Action.SEARCH,
                    "TOPIC",
                    currentInput,
                    0.90,
                    null
            );
        }
    

    if ("APPLICATION_TARGET".equals(expectedInput)) {

        context.clearAwaitingInput();

        return new CommandUnderstanding(
                Intent.APPLICATION,
                Action.OPEN_APPLICATION,
                command.getEntity(),
                null,
                0.90,
                null
        );
    }
}

        if (command == null || context == null) {
            return command;
        }

        if (command.getAction() != Action.CONTINUE_TASK) {
            return command;
        }

        String activeTopic = context.getActiveTopic();

        if (activeTopic == null || activeTopic.isBlank()) {
            return command;
        }

        String parameter = command.getParameter();

        if (parameter == null) {
            parameter = "";
        }

        if (parameter.startsWith("focus on ")) {

            String focus =
                    parameter.substring("focus on ".length())
                            .trim();

            String resolvedTopic =
                    activeTopic + " - " + focus;

            return new CommandUnderstanding(
                    Intent.RESEARCH,
                    Action.SEARCH,
                    "TOPIC",
                    resolvedTopic,
                    0.90,
                    null
            );
        }

        if (parameter.startsWith("go deeper into ")) {

            String focus =
                    parameter.substring("go deeper into ".length())
                            .trim();

            String resolvedTopic =
                    activeTopic + " - " + focus;

            return new CommandUnderstanding(
                    Intent.RESEARCH,
                    Action.SEARCH,
                    "TOPIC",
                    resolvedTopic,
                    0.90,
                    null
            );
        }

        if (parameter.equals("continue")
                || parameter.startsWith("continue ")) {

            return new CommandUnderstanding(
                    Intent.RESEARCH,
                    Action.SEARCH,
                    "TOPIC",
                    activeTopic,
                    0.90,
                    null
            );
        }

        return command;
    }
}
package commander.core;
public class Understanding {
    private final IntentClassifier intentClassifier;

    public Understanding() {
        intentClassifier = new IntentClassifier();
    }

    public CommandUnderstanding understand(UserInput input) {

    return understand(input.getText());
}

   public CommandUnderstanding understand(String input) {

      String text = normalize(input);

    ClassificationResult classification =
            intentClassifier.classify(text);

    Intent intent = classification.getIntent();

    double confidence = classification.getConfidence();

    

    /*
     * EXIT
     */
    if (isExit(text)) {
        return new CommandUnderstanding(
                Intent.EXIT,
                Action.EXIT,
                null,
                null,
                1.0,
                null
        );
    }

    /*
     * GREETING
     */
    if (isGreeting(text)) {
        return new CommandUnderstanding(
                Intent.GREETING,
                Action.RESPOND,
                null,
                null,
                0.95,
                null
        );
    }

    if (isContextFollowUp(text)) {

    return new CommandUnderstanding(
            Intent.RESEARCH,
            Action.CONTINUE_TASK,
            "TOPIC",
            text,
            0.85,
            null
    );
}

    /*
     * RESEARCH
     */
    if (isResearch(text)) {

    String topic = extractResearchTopic(text);

    double researchConfidence = 0.90;

    if (topic == null || topic.isBlank()) {
        researchConfidence = 0.60;
    }

    return new CommandUnderstanding(
            Intent.RESEARCH,
            Action.SEARCH,
            "TOPIC",
            topic,
            researchConfidence,
            null
    );
}

    /*
     * MEMORY
     */
    if (isMemory(text)) {

        String memory = extractMemory(text);
        Knowledge knowledge = extractMemoryKnowledge(memory);

        return new CommandUnderstanding(
                Intent.MEMORY,
                Action.STORE_MEMORY,
                "MEMORY",
                memory,
                0.90,
                knowledge
        );
    }

    /*
     * FILE
     */
    if (isFile(text)) {

        return new CommandUnderstanding(
                Intent.FILE,
                Action.LIST_FILES,
                "FILES",
                null,
                0.85,
                null
        );
    }

    /*
     * APPLICATION
     */
    if (isApplication(text)) {

    String application = extractApplication(text);

    double applicationConfidence = 0.90;

    if ("UNKNOWN_APPLICATION".equals(application)) {
        applicationConfidence = 0.60;
    }

    return new CommandUnderstanding(
            Intent.APPLICATION,
            Action.OPEN_APPLICATION,
            application,
            null,
            applicationConfidence,
            null
    );
}

    /*
     * SYSTEM
     */
    if (isSystem(text)) {

        String target = extractSystemTarget(text);

         /*
     * CONTEXT
     */
    if ("CONTEXT".equals(target)) {

        return new CommandUnderstanding(
                Intent.SYSTEM,
                Action.NONE,
                "CONTEXT",
                null,
                0.90,
                null
        );
    }

        if ("MEMORY".equals(target)) {

            return new CommandUnderstanding(
                    Intent.SYSTEM,
                    Action.GET_MEMORY_STATUS,
                    "MEMORY",
                    null,
                    0.90,
                    null
            );
        }

        if ("CPU".equals(target)) {

            return new CommandUnderstanding(
                    Intent.SYSTEM,
                    Action.GET_CPU_STATUS,
                    "CPU",
                    null,
                    0.90,
                    null
            );
        }

        if ("STORAGE".equals(target)) {

            return new CommandUnderstanding(
                    Intent.SYSTEM,
                    Action.GET_STORAGE_STATUS,
                    "STORAGE",
                    null,
                    0.90,
                    null
            );
        }

        if ("PROCESSES".equals(target)) {

            return new CommandUnderstanding(
                    Intent.SYSTEM,
                    Action.GET_PROCESS_STATUS,
                    "PROCESSES",
                    null,
                    0.90,
                    null
            );
        }

        if ("SNAPSHOT".equals(target)) {

            return new CommandUnderstanding(
                    Intent.SYSTEM,
                    Action.GET_SYSTEM_SNAPSHOT,
                    "SNAPSHOT",
                    null,
                    0.90,
                    null
            );
        }

        /*
         * Fallback for generic system requests
         */
        return new CommandUnderstanding(
                Intent.SYSTEM,
                Action.NONE,
                "SYSTEM",
                null,
                0.70,
                null
        );
    }
    

    /*
     * MEMORY RECALL
     */
    if (isMemoryQuestion(text)) {

        return new CommandUnderstanding(
                Intent.MEMORY,
                Action.RECALL_MEMORY,
                "PROJECT",
                "NAME",
                0.90,
                null
        );
    }

    if (isContextFollowUp(text)) {

    return new CommandUnderstanding(
            Intent.RESEARCH,
            Action.CONTINUE_TASK,
            "TOPIC",
            text,
            0.85,
            null
    );
}

    /*
     * QUESTION
     */
    if (isQuestion(text)) {

        /*
         * Time question
         */
        if (text.contains("time")) {

            return new CommandUnderstanding(
                    Intent.QUESTION,
                    Action.GET_TIME,
                    "TIME",
                    text,
                    0.90,
                    null
            );
        }

        /*
         * Generic question
         * We understand the input, but don't have
         * a specific executable action yet.
         */
        return new CommandUnderstanding(
                Intent.QUESTION,
                Action.NONE,
                null,
                text,
                0.75,
                null
        );
    }

    

    /*
     * UNKNOWN
     */
    return new CommandUnderstanding(
            Intent.UNKNOWN,
            Action.NONE,
            null,
            null,
            0.20,
            null
    );
}

    private String normalize(String input) {
        return input.toLowerCase().trim();
    }

    private boolean isExit(String text) {
        return text.equals("exit")
                || text.equals("quit")
                || text.contains("shut down");
    }

    private boolean isGreeting(String text) {
        return text.equals("hello")
                || text.equals("hi")
                || text.equals("hey")
                || text.contains("good morning")
                || text.contains("good evening");
    }

    private boolean isResearch(String text) {
        return text.contains("research")
                || text.contains("investigate")
                || text.contains("study")
                || text.contains("learn about")
                || text.contains("find information about")
                || text.contains("look into");
    }

    private boolean isMemory(String text) {
        return text.contains("remember")
                || text.contains("memorize")
                || text.contains("save this");
    }

    private boolean isFile(String text) {
        return text.contains("file")
                || text.contains("folder")
                || text.contains("directory")
                || text.contains("document");
    }

    private boolean isApplication(String text) {
        return text.contains("open")
                || text.contains("launch")
                || text.contains("start");
    }

    private boolean isSystem(String text) {

    return text.contains("battery")
            || text.contains("memory")
            || text.contains("ram")
            || text.contains("storage")
            || text.contains("cpu")
            || text.contains("processor")
            || text.contains("process")
            || text.contains("system status")
            || text.contains("system snapshot")
            || text.contains("overall system")
            || text.contains("how is my system")
            || text.contains("show context")
            || text.contains("what are we working on");
}

private boolean isContextFollowUp(String text) {

    return text.startsWith("focus on ")
            || text.startsWith("continue the research")
            || text.startsWith("continue research")
            || text.startsWith("continue it")
            || text.startsWith("go deeper into ")
            || text.startsWith("tell me more about ")
            || text.equals("continue");
}

    private boolean isQuestion(String text) {
        return text.startsWith("what ")
                || text.startsWith("how ")
                || text.startsWith("why ")
                || text.startsWith("when ")
                || text.startsWith("where ")
                || text.startsWith("who ")
                || text.startsWith("can you ");
    }

    private String extractResearchTopic(String text) {

        String[] markers = {
                "research ",
                "investigate ",
                "study ",
                "learn about ",
                "find information about ",
                "look into "
        };

        for (String marker : markers) {
            int index = text.indexOf(marker);

            if (index != -1) {
                return text.substring(index + marker.length()).trim();
            }
        }

        return null;
    }

    private String extractMemory(String text) {

        String[] markers = {
                "remember that ",
                "remember ",
                "memorize ",
                "save this "
        };

        for (String marker : markers) {
            int index = text.indexOf(marker);

            if (index != -1) {
                return text.substring(index + marker.length()).trim();
            }
        }

        return null;
    }

    private Knowledge extractMemoryKnowledge(String memory) {

    if (memory == null) {
        return null;
    }

    if (memory.contains("my project is")) {

        String value = memory.substring(
                memory.indexOf("my project is")
                        + "my project is".length()
        ).trim();

        return new Knowledge(
                "PROJECT",
                "NAME",
                value
        );
    }

    return null;
}

    private String extractSystemTarget(String text) {
        if (text.contains("show context")
        || text.contains("what are we working on")) {

    return "CONTEXT";
}

    if (text.contains("system status")
            || text.contains("system snapshot")
            || text.contains("overall system")
            || text.contains("how is my system")) {

        return "SNAPSHOT";
    }

    if (text.contains("memory") || text.contains("ram")) {
        return "MEMORY";
    }

    if (text.contains("battery")) {
        return "BATTERY";
    }

    if (text.contains("storage")) {
        return "STORAGE";
    }

    if (text.contains("cpu") || text.contains("processor")) {
        return "CPU";
    }

    if (text.contains("process")) {
        return "PROCESSES";
    }

    return "SYSTEM";
}

    private String extractApplication(String text) {

        String[] applications = {
                "chrome",
                "browser",
                "camera",
                "youtube",
                "settings"
        };

        for (String application : applications) {
            if (text.contains(application)) {
                return application.toUpperCase();
            }
        }

        return "UNKNOWN_APPLICATION";
    }
    private boolean isMemoryQuestion(String text) {

    return text.contains("what is my project")
            || text.contains("what's my project")
            || text.contains("tell me my project")
            || text.contains("what project am i working on");
}
}


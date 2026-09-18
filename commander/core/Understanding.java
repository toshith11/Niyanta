package commander.core;
public class Understanding {

    public CommandUnderstanding understand(String input) {

        String text = normalize(input);

        if (isExit(text)) {
            return new CommandUnderstanding(
                    Intent.EXIT,
                    "EXIT",
                    null,
                    null,
                    1.0,
                    null
            );
        }

        if (isGreeting(text)) {
            return new CommandUnderstanding(
                    Intent.GREETING,
                    "RESPOND",
                    null,
                    null,
                    0.95,
                    null
            );
        }

        if (isResearch(text)) {
            String topic = extractResearchTopic(text);

            return new CommandUnderstanding(
                    Intent.RESEARCH,
                    "SEARCH",
                    "TOPIC",
                    topic,
                    0.90,
                    null
            );
        }

        if (isMemory(text)) {
            String memory = extractMemory(text);
            Knowledge knowledge = extractMemoryKnowledge(memory);

            return new CommandUnderstanding(
                    Intent.MEMORY,
                    "STORE",
                    "MEMORY",
                    memory,
                    0.90,
                    knowledge
            );
        }

        if (isFile(text)) {
            return new CommandUnderstanding(
                    Intent.FILE,
                    "LIST",
                    "FILES",
                    null,
                    0.85,
                    null
            );
        }

        if (isApplication(text)) {
            String application = extractApplication(text);

            return new CommandUnderstanding(
                    Intent.APPLICATION,
                    "OPEN",
                    application,
                    null,
                    0.90,
                    null
            );
        }

        if (isSystem(text)) {
            String target = extractSystemTarget(text);

            return new CommandUnderstanding(
                    Intent.SYSTEM,
                    "GET_STATUS",
                    target,
                    null,
                    0.90,
                    null
            );
        }
        if (isMemoryQuestion(text)) {

    return new CommandUnderstanding(
            Intent.MEMORY,
            "RECALL",
            "PROJECT",
            "NAME",
            0.90,
            null
    );
}

        if (isQuestion(text)) {
            return new CommandUnderstanding(
                    Intent.QUESTION,
                    "ANSWER",
                    null,
                    text,
                    0.75,
                    null
            );
        }

        return new CommandUnderstanding(
                Intent.UNKNOWN,
                "NONE",
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
            || text.contains("how is my system");
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


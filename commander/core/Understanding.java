public class Understanding {

    public UnderstandingResult understand(String input) {

        String text = normalize(input);

        if (isExit(text)) {
            return new UnderstandingResult(Intent.EXIT, null, 1.0);
        }

        if (isGreeting(text)) {
            return new UnderstandingResult(Intent.GREETING, null, 0.95);
        }

        if (isResearch(text)) {
            String topic = extractResearchTopic(text);
            return new UnderstandingResult(Intent.RESEARCH, topic, 0.90);
        }

        if (isMemory(text)) {
            return new UnderstandingResult(Intent.MEMORY, null, 0.90);
        }

        if (isFile(text)) {
            return new UnderstandingResult(Intent.FILE, null, 0.85);
        }

        if (isApplication(text)) {
            String application = extractApplication(text);
            return new UnderstandingResult(Intent.APPLICATION, application, 0.90);
        }

        if (isSystem(text)) {
            String target = extractSystemTarget(text);
            return new UnderstandingResult(Intent.SYSTEM, target, 0.90);
        }

        if (isQuestion(text)) {
            return new UnderstandingResult(Intent.QUESTION, null, 0.75);
        }

        return new UnderstandingResult(Intent.UNKNOWN, null, 0.20);
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
                || text.contains("system status");
    }

    private boolean isQuestion(String text) {
        return text.startsWith("what ")
                || text.startsWith("how ")
                || text.startsWith("why ")
                || text.startsWith("when ")
                || text.startsWith("where ")
                || text.startsWith("who ");
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

    private String extractSystemTarget(String text) {

        if (text.contains("memory") || text.contains("ram")) {
            return "MEMORY";
        }

        if (text.contains("battery")) {
            return "BATTERY";
        }

        if (text.contains("storage")) {
            return "STORAGE";
        }

        if (text.contains("cpu")) {
            return "CPU";
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

        return null;
    }
}

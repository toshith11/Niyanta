public class Understanding {

    public Intent understand(String input) {

        String text = input.toLowerCase().trim();

        if (text.equals("hello") || text.equals("hi")) {
            return Intent.GREETING;
        }

        if (text.equals("exit") || text.equals("quit")) {
            return Intent.EXIT;
        }

        if (text.contains("time")) {
            return Intent.QUESTION;
        }

        if (text.contains("status")) {
            return Intent.SYSTEM;
        }

        if (text.contains("file") || text.contains("folder")) {
            return Intent.FILE;
        }

        if (text.contains("open")) {
            return Intent.APPLICATION;
        }

        if (text.contains("remember")) {
            return Intent.MEMORY;
        }

        if (text.contains("research")) {
            return Intent.RESEARCH;
        }

        return Intent.UNKNOWN;
    }
}

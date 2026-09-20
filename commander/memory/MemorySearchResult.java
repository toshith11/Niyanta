package commander.memory;

public class MemorySearchResult {

    private final String source;
    private final String content;

    public MemorySearchResult(
            String source,
            String content) {

        this.source = source;
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return source + " → " + content;
    }
}
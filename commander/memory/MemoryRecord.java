package commander.memory;

public class MemoryRecord {

    private final MemoryType type;
    private final String subject;
    private final String relation;
    private final String value;

    public MemoryRecord(
            MemoryType type,
            String subject,
            String relation,
            String value) {

        this.type = type;
        this.subject = subject;
        this.relation = relation;
        this.value = value;
    }

    public MemoryType getType() {
        return type;
    }

    public String getSubject() {
        return subject;
    }

    public String getRelation() {
        return relation;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return type + " | "
                + subject + " | "
                + relation + " | "
                + value;
    }
}
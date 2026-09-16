public class Knowledge {

    private final String subject;
    private final String relation;
    private final String value;

    public Knowledge(String subject, String relation, String value) {
        this.subject = subject;
        this.relation = relation;
        this.value = value;
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
        return subject + " --" + relation + "--> " + value;
    }
}
public class CommandUnderstanding {

    private final Intent intent;
    private final String action;
    private final String entity;
    private final String parameter;
    private final double confidence;

    public CommandUnderstanding(
            Intent intent,
            String action,
            String entity,
            String parameter,
            double confidence) {

        this.intent = intent;
        this.action = action;
        this.entity = entity;
        this.parameter = parameter;
        this.confidence = confidence;
    }

    public Intent getIntent() {
        return intent;
    }

    public String getAction() {
        return action;
    }

    public String getEntity() {
        return entity;
    }

    public String getParameter() {
        return parameter;
    }

    public double getConfidence() {
        return confidence;
    }

    @Override
    public String toString() {
        return "Intent: " + intent +
                ", Action: " + action +
                ", Entity: " + entity +
                ", Parameter: " + parameter +
                ", Confidence: " + confidence;
    }
}

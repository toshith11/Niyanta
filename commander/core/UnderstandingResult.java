public class UnderstandingResult {

    private final Intent intent;
    private final String target;
    private final double confidence;

    public UnderstandingResult(Intent intent, String target, double confidence) {
        this.intent = intent;
        this.target = target;
        this.confidence = confidence;
    }

    public Intent getIntent() {
        return intent;
    }

    public String getTarget() {
        return target;
    }

    public double getConfidence() {
        return confidence;
    }

    @Override
    public String toString() {
        return "Intent: " + intent +
                ", Target: " + target +
                ", Confidence: " + confidence;
    }
}

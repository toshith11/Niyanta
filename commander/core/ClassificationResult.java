package commander.core;

public class ClassificationResult {

    private final Intent intent;
    private final double confidence;

    public ClassificationResult(
            Intent intent,
            double confidence) {

        this.intent = intent;
        this.confidence = confidence;
    }

    public Intent getIntent() {
        return intent;
    }

    public double getConfidence() {
        return confidence;
    }

    @Override
    public String toString() {

        return "Intent: "
                + intent
                + ", Confidence: "
                + confidence;
    }
}
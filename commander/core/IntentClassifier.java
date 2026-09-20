package commander.core;

import java.util.HashMap;
import java.util.Map;

public class IntentClassifier {

    public ClassificationResult classify(String input) {

        String text = normalize(input);

        if (text.isBlank()) {
            return new ClassificationResult(
                    Intent.UNKNOWN,
                    0.0
            );
        }

        Map<Intent, Integer> scores =
                new HashMap<>();

        for (Intent intent : Intent.values()) {
            scores.put(intent, 0);
        }

        scoreGreeting(text, scores);
        scoreResearch(text, scores);
        scoreMemory(text, scores);
        scoreFile(text, scores);
        scoreApplication(text, scores);
        scoreSystem(text, scores);
        scoreQuestion(text, scores);
        scoreExit(text, scores);

        Intent bestIntent = Intent.UNKNOWN;
        int bestScore = 0;
        int secondBestScore = 0;

        for (Map.Entry<Intent, Integer> entry : scores.entrySet()) {

            int score = entry.getValue();

            if (score > bestScore) {
                secondBestScore = bestScore;
                bestScore = score;
                bestIntent = entry.getKey();
            } else if (score > secondBestScore) {
                secondBestScore = score;
            }
        }

        if (bestScore == 0) {
            return new ClassificationResult(
                    Intent.UNKNOWN,
                    0.20
            );
        }

        double confidence =
                calculateConfidence(
                        bestScore,
                        secondBestScore
                );

        return new ClassificationResult(
                bestIntent,
                confidence
        );
    }

    private String normalize(String input) {

        return input
                .toLowerCase()
                .trim()
                .replaceAll("\\s+", " ");
    }

    private void scoreGreeting(
            String text,
            Map<Intent, Integer> scores) {

        addScore(scores, Intent.GREETING, text,
                "hello",
                "hi",
                "hey",
                "good morning",
                "good evening",
                "good afternoon");
    }

    private void scoreResearch(
            String text,
            Map<Intent, Integer> scores) {

        addScore(scores, Intent.RESEARCH, text,
                "research",
                "investigate",
                "study",
                "learn about",
                "look into",
                "find information about",
                "explore the topic",
                "find out about");
    }

    private void scoreMemory(
            String text,
            Map<Intent, Integer> scores) {

        addScore(scores, Intent.MEMORY, text,
                "remember",
                "memorize",
                "save this",
                "store this",
                "what is my project",
                "what's my project",
                "what project am i working on",
                "what was I researching",
                "what was i researching",
                "what did I research",
                "what have I researched",
                "show my research history",
                "what was my research");
    }

    private void scoreFile(
            String text,
            Map<Intent, Integer> scores) {

        addScore(scores, Intent.FILE, text,
                "file",
                "files",
                "folder",
                "folders",
                "directory",
                "directories",
                "document",
                "documents");
    }

    private void scoreApplication(
            String text,
            Map<Intent, Integer> scores) {

        addScore(scores, Intent.APPLICATION, text,
                "open",
                "launch",
                "start",
                "run",
                "close",
                "quit application");
    }

    private void scoreSystem(
        String text,
        Map<Intent, Integer> scores) {

    addScore(scores, Intent.SYSTEM, text,
            "memory",
            "ram",
            "cpu",
            "processor",
            "storage",
            "disk",
            "process",
            "processes",
            "battery",
            "system status",
            "system health",
            "system usage",
            "resource usage",
            "show recent episodes",
            "show history",
            "recent episodes");
}

    private void scoreQuestion(
            String text,
            Map<Intent, Integer> scores) {

        if (text.startsWith("what ")
                || text.startsWith("how ")
                || text.startsWith("why ")
                || text.startsWith("when ")
                || text.startsWith("where ")
                || text.startsWith("who ")
                || text.startsWith("can you ")
                || text.startsWith("could you ")
                || text.startsWith("would you ")) {

            scores.put(
                    Intent.QUESTION,
                    scores.get(Intent.QUESTION) + 2
            );
        }

        if (text.endsWith("?")) {
            scores.put(
                    Intent.QUESTION,
                    scores.get(Intent.QUESTION) + 1
            );
        }
    }

    private void scoreExit(
            String text,
            Map<Intent, Integer> scores) {

        addScore(scores, Intent.EXIT, text,
                "exit",
                "quit",
                "shut down",
                "shutdown",
                "close commander");
    }

    private void addScore(
            Map<Intent, Integer> scores,
            Intent intent,
            String text,
            String... patterns) {

        for (String pattern : patterns) {

            if (text.contains(pattern)) {

                int current =
                        scores.get(intent);

                scores.put(
                        intent,
                        current + 2
                );
            }
        }
    }

    private double calculateConfidence(
            int bestScore,
            int secondBestScore) {

        if (bestScore >= 6) {
            return 0.95;
        }

        if (bestScore >= 4
                && bestScore > secondBestScore) {
            return 0.90;
        }

        if (bestScore >= 2
                && bestScore > secondBestScore) {
            return 0.80;
        }

        return 0.60;
    }
}
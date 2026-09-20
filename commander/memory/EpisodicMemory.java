package commander.memory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EpisodicMemory {

    private final Path episodeFile;

    public EpisodicMemory() {

        episodeFile = Path.of(
                "commander/memory/episodes.txt"
        );

        try {

            Files.createDirectories(
                    episodeFile.getParent()
            );

            if (!Files.exists(episodeFile)) {
                Files.createFile(episodeFile);
            }

        } catch (IOException e) {

            throw new RuntimeException(
                    "Could not initialize episodic memory.",
                    e
            );
        }
    }

    public void record(
            String input,
            String intent,
            String action,
            String result) {

        Episode episode = new Episode(
                LocalDateTime.now(),
                input,
                intent,
                action,
                result
        );

        try {

            Files.writeString(
                    episodeFile,
                    episode + System.lineSeparator(),
                    StandardOpenOption.APPEND
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Could not store episode.",
                    e
            );
        }
    }

    public List<String> getRecentEpisodes(int limit) {

        try {

            List<String> lines =
                    Files.readAllLines(episodeFile);

            int start =
                    Math.max(0, lines.size() - limit);

            return new ArrayList<>(
                    lines.subList(start, lines.size())
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Could not read episodic memory.",
                    e
            );
        }
    }

    public List<String> search(
            String keyword,
            int limit) {

        List<String> results =
                new ArrayList<>();

        if (keyword == null || keyword.isBlank()) {
            return results;
        }

        String query =
                keyword.toLowerCase();

        try {

            List<String> lines =
                    Files.readAllLines(episodeFile);

            for (int i = lines.size() - 1;
                 i >= 0 && results.size() < limit;
                 i--) {

                String line = lines.get(i);

                if (line.toLowerCase().contains(query)) {
                    results.add(line);
                }
            }

        } catch (IOException e) {

            throw new RuntimeException(
                    "Could not search episodic memory.",
                    e
            );
        }

        return results;
    }
    public List<String> getRecentByIntent(
        String intent,
        int limit) {

    List<String> results = new ArrayList<>();

    if (intent == null || intent.isBlank()) {
        return results;
    }

    try {

        List<String> lines =
                Files.readAllLines(episodeFile);

        for (int i = lines.size() - 1;
             i >= 0 && results.size() < limit;
             i--) {

            String line = lines.get(i);

            String[] parts =
                    line.split("\\|", 5);

            if (parts.length == 5
                    && parts[1].trim()
                    .equalsIgnoreCase(intent)) {

                results.add(line);
            }
        }

    } catch (IOException e) {

        throw new RuntimeException(
                "Could not retrieve episodic memory.",
                e
        );
    }

    return results;
}
}
package commander.memory;

import java.util.ArrayList;
import java.util.List;

public class MemoryRetriever {

    private final MemoryManager memoryManager;
    private final EpisodicMemory episodicMemory;

    public MemoryRetriever(
            MemoryManager memoryManager,
            EpisodicMemory episodicMemory) {

        this.memoryManager = memoryManager;
        this.episodicMemory = episodicMemory;
    }

    public List<MemorySearchResult> retrieve(
            String query,
            int limit) {

        List<MemorySearchResult> results =
                new ArrayList<>();

        if (query == null || query.isBlank()) {
            return results;
        }

        String text = query.toLowerCase();

        /*
         * Long-term project memory
         */
        if (text.contains("project")) {

            String project =
                    memoryManager.recall(
                            "PROJECT",
                            "NAME"
                    );

            if (project != null) {

                results.add(
                        new MemorySearchResult(
                                "LONG_TERM",
                                "PROJECT = " + project
                        )
                );
            }
        }

        /*
         * Research history
         */
        if (text.contains("research")
                || text.contains("researching")
                || text.contains("studied")
                || text.contains("study")) {

            List<String> episodes =
                    episodicMemory.getRecentByIntent(
                            "RESEARCH",
                            limit
                    );

            for (String episode : episodes) {

                results.add(
                        new MemorySearchResult(
                                "EPISODIC",
                                episode
                        )
                );
            }
        }

        return results;
    }
}
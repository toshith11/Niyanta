package commander.memory;

import commander.core.Knowledge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MemoryManager {

    private final Path memoryFile;

    public MemoryManager() {

        memoryFile = Path.of(
                "commander/memory/memory.txt"
        );

        try {
            Files.createDirectories(memoryFile.getParent());

            if (!Files.exists(memoryFile)) {
                Files.createFile(memoryFile);
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not initialize memory.",
                    e
            );
        }
    }

    /*
     * Store memory with an explicitly selected memory type.
     */
    public void remember(
            Knowledge knowledge,
            MemoryType memoryType) {

        if (knowledge == null || memoryType == null) {
            return;
        }

        MemoryRecord newMemory = new MemoryRecord(
                memoryType,
                knowledge.getSubject(),
                knowledge.getRelation(),
                knowledge.getValue()
        );

        List<MemoryRecord> memories = loadMemories();

        boolean updated = false;

        for (int i = 0; i < memories.size(); i++) {

            MemoryRecord existing = memories.get(i);

            if (existing.getType() == memoryType
                    && existing.getSubject()
                    .equalsIgnoreCase(
                            newMemory.getSubject()
                    )
                    && existing.getRelation()
                    .equalsIgnoreCase(
                            newMemory.getRelation()
                    )) {

                memories.set(i, newMemory);
                updated = true;
                break;
            }
        }

        if (!updated) {
            memories.add(newMemory);
        }

        saveMemories(memories);
    }

    /*
     * Default memory storage.
     *
     * Existing Commander code can continue using:
     * remember(knowledge)
     */
    public void remember(Knowledge knowledge) {

        remember(
                knowledge,
                MemoryType.LONG_TERM
        );
    }

    /*
     * Explicit long-term memory storage.
     */
    public void rememberLongTerm(Knowledge knowledge) {

        remember(
                knowledge,
                MemoryType.LONG_TERM
        );
    }

    /*
     * Recall a memory by subject and relation.
     */
    public String recall(
            String subject,
            String relation) {

        List<MemoryRecord> memories = loadMemories();

        for (MemoryRecord memory : memories) {

            if (memory.getSubject()
                    .equalsIgnoreCase(subject)
                    &&
                memory.getRelation()
                    .equalsIgnoreCase(relation)) {

                return memory.getValue();
            }
        }

        return null;
    }

    /*
     * Load all persistent memories from disk.
     */
    private List<MemoryRecord> loadMemories() {

        List<MemoryRecord> memories = new ArrayList<>();

        try {

            List<String> lines =
                    Files.readAllLines(memoryFile);

            for (String line : lines) {

                String[] parts =
                        line.split("\\|", 4);

                if (parts.length != 4) {
                    continue;
                }

                try {

                    MemoryRecord memory =
                            new MemoryRecord(
                                    MemoryType.valueOf(
                                            parts[0].trim()
                                    ),
                                    parts[1].trim(),
                                    parts[2].trim(),
                                    parts[3].trim()
                            );

                    memories.add(memory);

                } catch (IllegalArgumentException e) {

                    /*
                     * Ignore malformed or unknown
                     * memory records instead of crashing
                     * the entire Commander.
                     */
                    continue;
                }
            }

        } catch (IOException e) {

            throw new RuntimeException(
                    "Could not load memory.",
                    e
            );
        }

        return memories;
    }

    /*
     * Save all memories back to disk.
     */
    private void saveMemories(
            List<MemoryRecord> memories) {

        List<String> lines = new ArrayList<>();

        for (MemoryRecord memory : memories) {
            lines.add(memory.toString());
        }

        try {

            Files.write(
                    memoryFile,
                    lines
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Could not save memory.",
                    e
            );
        }
    }
}
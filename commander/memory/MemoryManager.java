package commander.memory;
import commander.core.Knowledge;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class MemoryManager {

    private final Path memoryFile;

    public MemoryManager() {
        memoryFile = Path.of("commander/memory/memory.txt");

        try {
            Files.createDirectories(memoryFile.getParent());

            if (!Files.exists(memoryFile)) {
                Files.createFile(memoryFile);
            }

        } catch (IOException e) {
            throw new RuntimeException("Could not initialize memory.", e);
        }
    }

    public void remember(Knowledge knowledge) {

        if (knowledge == null) {
            return;
        }

        String record =
                knowledge.getSubject() + "|" +
                knowledge.getRelation() + "|" +
                knowledge.getValue();

        try {
            Files.writeString(
                    memoryFile,
                    record + System.lineSeparator(),
                    StandardOpenOption.APPEND
            );

        } catch (IOException e) {
            throw new RuntimeException("Could not store memory.", e);
        }
    }

    public String recall(String subject, String relation) {

        try {
            List<String> records = Files.readAllLines(memoryFile);

            for (String record : records) {

                String[] parts = record.split("\\|", 3);

                if (parts.length == 3 &&
                        parts[0].equalsIgnoreCase(subject) &&
                        parts[1].equalsIgnoreCase(relation)) {

                    return parts[2];
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Could not read memory.", e);
        }

        return null;
    }
}
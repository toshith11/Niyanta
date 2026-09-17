package commander.system;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SystemInterface {

    public String getCurrentTime() {

        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("HH:mm:ss");

        return now.format(formatter);
    }

    public String getSystemStatus() {

        return "System interface is operational.";
    }

    public String getMemoryStatus() {

        try {

            List<String> lines =
                    Files.readAllLines(
                            Path.of("/proc/meminfo")
                    );

            long totalKb = 0;
            long availableKb = 0;

            for (String line : lines) {

                if (line.startsWith("MemTotal:")) {
                    totalKb = extractValue(line);
                }

                if (line.startsWith("MemAvailable:")) {
                    availableKb = extractValue(line);
                }
            }

            long usedKb = totalKb - availableKb;

            double totalGb = totalKb / 1_048_576.0;
            double usedGb = usedKb / 1_048_576.0;
            double availableGb = availableKb / 1_048_576.0;

            return String.format(
                    "Total: %.2f GB, Used: %.2f GB, Available: %.2f GB",
                    totalGb,
                    usedGb,
                    availableGb
            );

        } catch (IOException e) {

            return "Unable to read system memory information.";
        }
    }

    private long extractValue(String line) {

        String[] parts = line.trim().split("\\s+");

        return Long.parseLong(parts[1]);
    }
}
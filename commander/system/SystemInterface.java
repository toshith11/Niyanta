package commander.system;

import java.io.IOException;
import java.nio.file.Files;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
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
    public String getCpuStatus() {

    OperatingSystemMXBean osBean =
            ManagementFactory.getPlatformMXBean(
                    OperatingSystemMXBean.class
            );

    double cpuLoad = osBean.getCpuLoad();

    if (cpuLoad < 0) {
        return "CPU usage information unavailable.";
    }

    double percentage = cpuLoad * 100;

    return String.format(
            "CPU usage: %.2f%%, Available processors: %d",
            percentage,
            Runtime.getRuntime().availableProcessors()
    );
}
public String getStorageStatus() {

    Path path = Path.of(".").toAbsolutePath();

    try {

        var fileStore = Files.getFileStore(path);

        long total = fileStore.getTotalSpace();
        long available = fileStore.getUsableSpace();
        long used = total - available;

        double totalGb = total / 1_073_741_824.0;
        double usedGb = used / 1_073_741_824.0;
        double availableGb = available / 1_073_741_824.0;

        return String.format(
                "Storage - Total: %.2f GB, Used: %.2f GB, Available: %.2f GB",
                totalGb,
                usedGb,
                availableGb
        );

    } catch (IOException e) {

        return "Unable to read storage information.";
    }
}
public String getProcessStatus() {

    long processCount =
            ProcessHandle.allProcesses().count();

    return "Running processes: " + processCount;
}
public SystemSnapshot getSystemSnapshot() {

    double totalMemoryGb = 0;
    double usedMemoryGb = 0;
    double availableMemoryGb = 0;

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

        totalMemoryGb = totalKb / 1_048_576.0;
        usedMemoryGb = usedKb / 1_048_576.0;
        availableMemoryGb = availableKb / 1_048_576.0;

    } catch (IOException e) {
        System.out.println(
                "Unable to read memory information."
        );
    }

    double cpuUsagePercent = 0;

    OperatingSystemMXBean osBean =
            ManagementFactory.getPlatformMXBean(
                    OperatingSystemMXBean.class
            );

    double cpuLoad = osBean.getCpuLoad();

    if (cpuLoad >= 0) {
        cpuUsagePercent = cpuLoad * 100;
    }

    int availableProcessors =
            Runtime.getRuntime().availableProcessors();

    double totalStorageGb = 0;
    double usedStorageGb = 0;
    double availableStorageGb = 0;

    try {

        Path path = Path.of(".").toAbsolutePath();

        var fileStore = Files.getFileStore(path);

        long total = fileStore.getTotalSpace();
        long available = fileStore.getUsableSpace();
        long used = total - available;

        totalStorageGb =
                total / 1_073_741_824.0;

        usedStorageGb =
                used / 1_073_741_824.0;

        availableStorageGb =
                available / 1_073_741_824.0;

    } catch (IOException e) {
        System.out.println(
                "Unable to read storage information."
        );
    }

    long processCount =
            ProcessHandle.allProcesses().count();

    return new SystemSnapshot(
            totalMemoryGb,
            usedMemoryGb,
            availableMemoryGb,
            cpuUsagePercent,
            availableProcessors,
            totalStorageGb,
            usedStorageGb,
            availableStorageGb,
            processCount
    );
}
}
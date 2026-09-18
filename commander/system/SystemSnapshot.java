package commander.system;

public class SystemSnapshot {

    private final double totalMemoryGb;
    private final double usedMemoryGb;
    private final double availableMemoryGb;

    private final double cpuUsagePercent;
    private final int availableProcessors;

    private final double totalStorageGb;
    private final double usedStorageGb;
    private final double availableStorageGb;

    private final long processCount;

    public SystemSnapshot(
            double totalMemoryGb,
            double usedMemoryGb,
            double availableMemoryGb,
            double cpuUsagePercent,
            int availableProcessors,
            double totalStorageGb,
            double usedStorageGb,
            double availableStorageGb,
            long processCount) {

        this.totalMemoryGb = totalMemoryGb;
        this.usedMemoryGb = usedMemoryGb;
        this.availableMemoryGb = availableMemoryGb;
        this.cpuUsagePercent = cpuUsagePercent;
        this.availableProcessors = availableProcessors;
        this.totalStorageGb = totalStorageGb;
        this.usedStorageGb = usedStorageGb;
        this.availableStorageGb = availableStorageGb;
        this.processCount = processCount;
    }

    public double getTotalMemoryGb() {
        return totalMemoryGb;
    }

    public double getUsedMemoryGb() {
        return usedMemoryGb;
    }

    public double getAvailableMemoryGb() {
        return availableMemoryGb;
    }

    public double getCpuUsagePercent() {
        return cpuUsagePercent;
    }

    public int getAvailableProcessors() {
        return availableProcessors;
    }

    public double getTotalStorageGb() {
        return totalStorageGb;
    }

    public double getUsedStorageGb() {
        return usedStorageGb;
    }

    public double getAvailableStorageGb() {
        return availableStorageGb;
    }

    public long getProcessCount() {
        return processCount;
    }

    @Override
    public String toString() {

        return String.format(
                "RAM: %.2f GB used / %.2f GB available of %.2f GB%n" +
                "CPU: %.2f%% usage, %d processors%n" +
                "Storage: %.2f GB used / %.2f GB available of %.2f GB%n" +
                "Processes: %d",
                usedMemoryGb,
                availableMemoryGb,
                totalMemoryGb,
                cpuUsagePercent,
                availableProcessors,
                usedStorageGb,
                availableStorageGb,
                totalStorageGb,
                processCount
        );
    }
}
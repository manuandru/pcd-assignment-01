package pcd.assignment01.stats;

import java.util.List;
import java.util.stream.Collectors;

public class StatisticForView {

    private final List<Interval> intervals;
    private final List<String> orderedFiles;
    private final int maxInterval;
    private final int intervalSize;

    public StatisticForView(List<Interval> stats, int maxInterval, List<String> orderedFiles) {
        this.intervals = stats;
        this.maxInterval = maxInterval;
        this.intervalSize = maxInterval / (stats.size() - 1);
        this.orderedFiles = orderedFiles;
    }

    public String getIntervals() {
        StringBuilder sb = new StringBuilder();
        var count = intervals.size() - 1;
        for (int i = 0; i < count; i++) {
            sb.append(logInterval(i * intervalSize, (i + 1) * intervalSize, intervals.get(i).getFileCount()));
            sb.append("\n");
        }
        sb.append(logInterval(maxInterval, intervals.get(count - 1).getFileCount()));
        return sb.toString();
    }

    private String logInterval(int lower, int upper, int count) {
        return "[" + lower + ", " + upper + "]: " + count + " files";
    }

    private String logInterval(int lower, int count) {
        return "[" + lower + ", inf]: " + count + " files";
    }

    public String getLongestFiles(int n) {
        return orderedFiles.stream()
                .limit(n)
                .collect(Collectors.joining("\n"));
    }

    public String getLongestFiles() {
        return String.join("\n", orderedFiles);
    }
}

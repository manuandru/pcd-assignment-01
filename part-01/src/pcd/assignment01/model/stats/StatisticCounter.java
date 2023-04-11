package pcd.assignment01.model.stats;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticCounter {

    private final List<Interval> stats = new ArrayList<>();

    public StatisticCounter(int interval) {
        for (int i = 0; i < interval; i++) {
            stats.add(new Interval());
        }
    }

    public synchronized void addFileStats(int interval, String file, int lines) {
        stats.get(interval).add(file, lines);
    }

    // this synchronized is not needed cause thread do not read it
    public synchronized List<Interval> getStats() {
        return this.stats; // maybe a defensive copy
    }

    // this is blocking -- do not call in GUI
    public synchronized List<String> getNLongestFiles(int n) {
        return this.stats.stream()
                .flatMap(i -> i.getFiles().stream())
                .sorted((f1, f2) -> f2.getY() - f1.getY())
                .map(Pair::getX)
                .limit(n)
                .collect(Collectors.toList());
    }
}

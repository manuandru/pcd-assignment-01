package pcd.assignment01.model.stats;

import java.util.ArrayList;
import java.util.List;

public class StatisticCounter {

    private final List<Interval> stats = new ArrayList<>();

    public StatisticCounter(int interval) {
        for (int i = 0; i < interval; i++) {
            stats.add(new Interval());
        }
    }

    public synchronized void addFileStats(int interval, String file) {
        stats.get(interval).add(file);
    }

    public synchronized List<Interval> getStats() {
        return this.stats; // maybe a defensive copy
    }
}

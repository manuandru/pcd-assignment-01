package pcd.assignment01.stats;

import pcd.assignment01.coordination.ModelObserver;
import pcd.assignment01.coordination.StopFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticCounter {

    private final List<Interval> intervals = new ArrayList<>();
    private final ModelObserver observer;
    private final int maxInterval;
    private final StopFlag stopFlag;

    public StatisticCounter(int intervals, int maxInterval, ModelObserver observer, StopFlag stopFlag) {
        for (int i = 0; i < intervals; i++) {
            this.intervals.add(new Interval());
        }
        this.maxInterval = maxInterval;
        this.observer = observer;
        this.stopFlag = stopFlag;
    }

    public synchronized void addFileStats(int interval, String file, int lines) {
        intervals.get(interval).add(file, lines);
        updateUI();
    }

    private void updateUI() {
        // getOrderedFiles() is cpu bound -- it's done by the Worker
        StatisticForView calculated = new StatisticForView(intervals, maxInterval, getOrderedFiles());

        if (!stopFlag.isStopped()) { // avoid updating model if stop is requested
            observer.modelUpdated(calculated);
        }
    }

    // this is blocking -- do not call in GUI
    private List<String> getOrderedFiles() {
        return this.intervals.stream()
                .flatMap(i -> i.getFiles().stream())
                .sorted((f1, f2) -> f2.getY() - f1.getY())
                .map(Pair::getX)
                .collect(Collectors.toList());
    }
}

package pcd.assignment01.coordination;

import pcd.assignment01.stats.StatisticForView;

public interface ModelObserver {
    void modelUpdated(StatisticForView stats);

    void requiredActionIsComplete();
}

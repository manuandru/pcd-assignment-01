package pcd.assignment01;

import pcd.assignment01.model.stats.StatisticCounter;

public interface StatisticsObserver {
    void modelUpdated(StatsForView stats);
}

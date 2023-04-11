package pcd.jpf.stats;

import gov.nasa.jpf.vm.Verify;
import pcd.jpf.coordination.ModelObserver;
import pcd.jpf.coordination.StopFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticCounter {

    private int intervals = 0;
    private final ModelObserver observer;
    private final StopFlag stopFlag;

    public StatisticCounter(ModelObserver observer, StopFlag stopFlag) {
        Verify.beginAtomic();
        this.observer = observer;
        this.stopFlag = stopFlag;
        Verify.endAtomic();
    }

    public synchronized void addFileStats(Integer stats) {
        intervals += stats;
        updateUI();
    }

    private void updateUI() {
        if (!stopFlag.isStopped()) {
            observer.modelUpdated(intervals);
        }
    }

}

package pcd.assignment01;

public interface ModelObserver {
    void modelUpdated(StatsForView stats);

    void requiredActionIsComplete();
}

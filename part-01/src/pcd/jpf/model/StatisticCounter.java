package pcd.jpf.model;

public class StatisticCounter {

    private int stats = 0;

    public synchronized void addFileStats(int infoOfAFile) {
        this.stats += infoOfAFile;
    }

    public synchronized int getStats() {
        return this.stats;
    }

}

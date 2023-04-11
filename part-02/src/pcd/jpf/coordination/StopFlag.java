package pcd.jpf.coordination;

public class StopFlag {

    private boolean stopped = false;

    public synchronized void stop() {
        stopped = true;
    }

    public synchronized boolean isStopped() {
        return stopped;
    }
}

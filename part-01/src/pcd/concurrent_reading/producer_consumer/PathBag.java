package pcd.concurrent_reading.producer_consumer;

import java.util.LinkedList;
import java.util.List;

public class PathBag {

    private final LinkedList<String> bag = new LinkedList<>();
    private boolean endInsert = false;

    public synchronized void addNewTask(String path) {
        bag.addLast(path);
        notify();
    }

    public synchronized void addAllNewTasks(List<String> paths) {
        bag.addAll(paths);
        notifyAll();
    }

    public synchronized String getATask() throws InterruptedException {
        while (bag.isEmpty()) {
            if (this.endInsert) {
                notifyAll();
                return null;
            }
            wait();
        }
        return bag.removeFirst();
    }

    public void noMore() {
        this.endInsert = true;
    }
}

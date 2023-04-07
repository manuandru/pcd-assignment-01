package pcd.assignment01.model.task;

import java.util.*;

public class TaskBag {

    private final LinkedList<Task> bag = new LinkedList<>();
    private boolean endInsert = false;

    public synchronized void addNewTask(Task task) {
        bag.addLast(task);
        notify();
    }

    public synchronized void addAllNewTasks(List<Task> tasks) {
        bag.addAll(tasks);
        notifyAll();
    }

    public synchronized Optional<Task> getATask() throws InterruptedException {
        while (bag.isEmpty()) {
            if (this.endInsert) {
                notifyAll();
                return Optional.empty();
            }
            wait();
        }
        return Optional.of(bag.removeFirst());
    }

    public void noMore() {
        this.endInsert = true;
    }
}

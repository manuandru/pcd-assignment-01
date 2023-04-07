package pcd.assignment01.model.task;

import java.util.*;

public class TaskBag {

    private final LinkedList<Task> bag = new LinkedList<>();
    private int inProcessTasks = 0;

    public synchronized void addNewTask(Task task) {
        bag.addLast(task);
        notify();
    }

    /**
     * Post protocol method -- call this after complete a task and before waiting for another one
     */
    public void completeAssignedTask() {
        inProcessTasks--;
    }

    public synchronized Optional<Task> getATask() throws InterruptedException {
        while (bag.isEmpty()) {
            if (inProcessTasks == 0) {
                notifyAll();
                return Optional.empty();
            }
            wait();
        }
        inProcessTasks++;
        return Optional.of(bag.removeFirst());
    }
}

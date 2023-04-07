package pcd.assignment01.model.task;

import java.util.*;

public class TaskBag {

    private final LinkedList<Task> bag = new LinkedList<>();
    private int inProcessTasks = 0;

    public synchronized void addNewTask(Task task) {
        bag.addLast(task);
        notifyAll();
    }

    public synchronized void addAllNewTasks(Collection<Task> tasks) {
        List<Task> copy = new ArrayList<>(tasks); // defensive copy
        bag.addAll(copy);
        notifyAll();
    }

    /**
     * Post protocol method
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

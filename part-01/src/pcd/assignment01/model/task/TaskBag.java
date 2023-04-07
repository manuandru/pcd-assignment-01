package pcd.assignment01.model.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TaskBag {

    private final LinkedList<Task> bag = new LinkedList<>();

    public synchronized void addNewTask(Task task) {
        bag.addLast(task);
        notifyAll();
    }

    public synchronized void addAllNewTasks(Collection<Task> tasks) {
        List<Task> copy = new ArrayList<>(tasks); // defensive copy
        bag.addAll(copy);
        notifyAll();
    }

    public synchronized Task getATask() throws InterruptedException {
        while (bag.isEmpty()) {
            wait();
        }
        return bag.removeFirst();
    }
}

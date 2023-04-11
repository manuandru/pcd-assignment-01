package pcd.jpf.coordination.agent.task;

import gov.nasa.jpf.vm.Verify;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class TaskBag {

    private final LinkedList<Integer> bag = new LinkedList<>();
    private boolean endInsert = false;

    public synchronized void addAllNewTasks(List<Integer> tasks) {
        Verify.beginAtomic();
        bag.addAll(tasks);
        notifyAll();
        Verify.endAtomic();
    }

    public synchronized Optional<Integer> getATask() throws InterruptedException {
        while (bag.isEmpty()) {
            if (this.endInsert) {
                notifyAll();
                return Optional.empty();
            }
            wait();
        }
        return Optional.of(bag.removeFirst());
    }

    public synchronized void noMore() {
        Verify.beginAtomic();
        this.endInsert = true;
        notifyAll();
        Verify.endAtomic();
    }

    public synchronized void clear() {
        this.bag.clear();
    }
}

package pcd.jpf.coordination.agent;

import pcd.jpf.coordination.StopFlag;
import pcd.jpf.coordination.agent.task.TaskBag;

import java.util.*;

public class ProducerAgent extends Thread {

    private final TaskBag bag;
    private final LinkedList<List<Integer>> folders = new LinkedList<>();
    private final StopFlag stopFlag;

    public ProducerAgent(TaskBag bag, StopFlag stopflag) {
        this.bag = bag;
        this.stopFlag = stopflag;

        folders.add(Collections.singletonList(1));

        List<Integer> l2 = new ArrayList<>();
        l2.add(2);
        l2.add(3);
        folders.add(l2);

        List<Integer> l3 = new ArrayList<>();
        folders.add(l3);
    }

    @Override
    public void run() {
        while (true) {

            if (stopFlag.isStopped()) {
                folders.clear();    // cause enter the next if and exiting
                bag.clear();        // remove the pending tasks -- consumers could exit too
            }

            if (folders.isEmpty()) {
                bag.noMore();
                return; // no more file to produce
            }

            List<Integer> files = folders.removeFirst();

            if (!files.isEmpty()) {
                bag.addAllNewTasks(files);
            }
        }
    }
}

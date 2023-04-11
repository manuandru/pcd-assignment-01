package pcd.jpf.coordination.agent;

import gov.nasa.jpf.vm.Verify;
import pcd.jpf.coordination.StopFlag;
import pcd.jpf.coordination.agent.task.TaskBag;
import pcd.jpf.stats.StatisticCounter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public class ConsumerAgent extends Thread {

    private final TaskBag bag;
    private final StatisticCounter stats;
    private final CountDownLatch latch;
    private final StopFlag stopFlag;

    public ConsumerAgent(TaskBag bag, StatisticCounter stats, CountDownLatch latch, StopFlag stopFlag) {
        Verify.beginAtomic();
        this.bag = bag;
        this.stats = stats;
        this.latch = latch;
        this.stopFlag = stopFlag;
        Verify.endAtomic();
    }

    @Override
    public void run() {
        while (true) {
            Optional<Integer> opt;
            try {
                opt = bag.getATask();  // blocking - wait for a task to do
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (!opt.isPresent()) {
                latch.countDown();      // need to notify his exit
                return;
            }

            Integer file = opt.get();
            stats.addFileStats(file);
        }
    }
}

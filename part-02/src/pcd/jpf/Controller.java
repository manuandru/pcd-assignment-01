package pcd.jpf;

import gov.nasa.jpf.vm.Verify;
import pcd.jpf.coordination.StopFlag;
import pcd.jpf.coordination.agent.ConsumerAgent;
import pcd.jpf.coordination.agent.ProducerAgent;
import pcd.jpf.coordination.agent.task.TaskBag;
import pcd.jpf.stats.StatisticCounter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Controller {

    private final int countOfThread;
    private View view;
    private StopFlag stopFlag;

    public Controller(int countOfThread){
        this.countOfThread = countOfThread;
    }

    public Thread startSearch() {
        stopFlag = new StopFlag();
        try {
            Thread t = new Thread(() -> {

                Verify.beginAtomic();
                TaskBag bag = new TaskBag();
                StatisticCounter stats = new StatisticCounter(view, stopFlag);

                CountDownLatch latch = new CountDownLatch(countOfThread);

                Thread producer = new ProducerAgent(bag, stopFlag);
                List<Thread> consumers = new ArrayList<>();
                for (int i = 0; i < countOfThread; i++) {
                    consumers.add(new ConsumerAgent(bag, stats, latch, stopFlag));
                }
                Verify.endAtomic();

                producer.start();
                for (Thread c : consumers) {
                    c.start();
                }

                try {
                    latch.await();
                } catch (InterruptedException e) { e.printStackTrace(); }

            });
            t.start();
            return t;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public void stopSearch() {
        stopFlag.stop();
    }

    public void setView(View view) {
        this.view = view;
    }
}

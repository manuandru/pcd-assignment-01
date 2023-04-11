package pcd.assignment01;

import pcd.assignment01.model.agent.ConsumerAgent;
import pcd.assignment01.model.agent.ProducerAgent;
import pcd.assignment01.model.stats.StatisticCounter;
import pcd.assignment01.model.task.TaskBag;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Controller {

    private final int countOfThread;
    private View view;
    private StopFlag stopFlag = new StopFlag();

    public Controller(int countOfThread){
        this.countOfThread = countOfThread;
    }

    public void startSearch(String directory, int nInterval, int maxInterval) {

        try {
            new Thread(() -> {
                TaskBag bag = new TaskBag();
                StatisticCounter stats = new StatisticCounter(nInterval, maxInterval, view);

                Thread producer = new ProducerAgent(bag, directory);
                List<Thread> consumers = new ArrayList<>();
                for (int i = 0; i < countOfThread; i++) {
                    consumers.add(new ConsumerAgent(bag, "Worker-" + i, stats, nInterval, maxInterval));
                }

                CountDownLatch latch = new CountDownLatch(countOfThread);

                producer.start();
                for (Thread c : consumers) {
                    c.start();
                }

                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                view.requiredActionIsComplete();


            }).start();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void stopSearch() {
        // TODO
    }

    public void setView(View view) {
        this.view = view;
    }
}

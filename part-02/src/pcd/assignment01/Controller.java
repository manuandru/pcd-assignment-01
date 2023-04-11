package pcd.assignment01;

import pcd.assignment01.model.agent.ConsumerAgent;
import pcd.assignment01.model.agent.ProducerAgent;
import pcd.assignment01.model.stats.StatisticCounter;
import pcd.assignment01.model.task.TaskBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class Controller {

    private final int countOfThread;
    private View view;
    private StopFlag stopFlag;

    public Controller(int countOfThread){
        this.countOfThread = countOfThread;
    }

    public void startSearch(String directory, int nInterval, int maxInterval, Set<String> extensions) {
        stopFlag = new StopFlag();
        try {
            new Thread(() -> {
                TaskBag bag = new TaskBag();
                StatisticCounter stats = new StatisticCounter(nInterval, maxInterval, view, stopFlag);

                // just need to wait Consumers because they end after the Producer
                CountDownLatch latch = new CountDownLatch(countOfThread);

                Thread producer = new ProducerAgent(bag, stopFlag, directory, extensions);
                List<Thread> consumers = new ArrayList<>();
                for (int i = 0; i < countOfThread; i++) {
                    consumers.add(new ConsumerAgent(bag, "Worker-" + i, stats, nInterval, maxInterval, latch, stopFlag));
                }

                producer.start();
                for (Thread c : consumers) {
                    c.start();
                }

                // Wait for workers to end their work is not mandatory,
                // but will avoid accumulate ghostly last operating workers
                try {
                    latch.await();
                } catch (InterruptedException e) { e.printStackTrace(); }

                view.requiredActionIsComplete();    // only now the user can run a new search
            }).start();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void stopSearch() {
        stopFlag.stop();
    }

    public void setView(View view) {
        this.view = view;
    }
}

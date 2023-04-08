package pcd.assignment01;

import pcd.assignment01.model.agent.ProducerAgent;
import pcd.assignment01.model.agent.WorkerAgent;
import pcd.assignment01.model.task.TaskBag;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        String directory = "./";    // D
        int nInterval = 5;          // NI
        int maxInterval = 1000;     // MAXL

        TaskBag bag = new TaskBag();
        int CONSUMERS_COUNT = Runtime.getRuntime().availableProcessors();

        Thread producer = new ProducerAgent(bag, "./");
        List<Thread> consumers = new ArrayList<>();

        for (int i = 0; i < CONSUMERS_COUNT; i++) {
            consumers.add(new WorkerAgent(bag, "Worker-" + i, nInterval, maxInterval));
        }

        long start = System.currentTimeMillis();
        producer.start();
        for (Thread c : consumers) {
            c.start();
        }

        producer.join();
        for (Thread c : consumers) {
            c.join();
        }
        long stop = System.currentTimeMillis();
        System.out.println("Time: " + (stop - start));
    }
}

package pcd.concurrent_reading.recursive_worker;

import pcd.concurrent_reading.recursive_worker.task.TaskBag;
import pcd.concurrent_reading.recursive_worker.task.FolderAnalyzerTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        int CONSUMER_COUNT = Runtime.getRuntime().availableProcessors();

        TaskBag bag = new TaskBag();
        bag.addNewTask(new FolderAnalyzerTask("../../../../../miniconda3"));

        List<Thread> consumers = new ArrayList<>();
        for (int i = 0; i < CONSUMER_COUNT; i++) {
            consumers.add(new WorkerAgent(bag));
        }

        long start = System.currentTimeMillis();

        for (Thread t : consumers) {
            t.start();
        }
        for (Thread t : consumers) {
            t.join();
        }

        long stop = System.currentTimeMillis();

        System.out.println("Time with " + CONSUMER_COUNT + " Consumers: " + (stop - start));
    }
}

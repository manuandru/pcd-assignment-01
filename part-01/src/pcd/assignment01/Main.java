package pcd.assignment01;

import pcd.assignment01.model.agent.ProducerAgent;
import pcd.assignment01.model.agent.WorkerAgent;
import pcd.assignment01.model.stats.Interval;
import pcd.assignment01.model.stats.StatisticCounter;
import pcd.assignment01.model.task.TaskBag;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        String directory;    // D
        int nInterval;       // NI
        int maxInterval;     // MAXL

        if (args.length == 3) {
            directory = args[0];
            nInterval = Integer.parseInt(args[1]);
            maxInterval = Integer.parseInt(args[2]);
        } else {
            directory = "../../../../../miniconda3";    // D
            nInterval = 5;          // NI
            maxInterval = 1000;     // MAXL
        }
        int intervalSize = maxInterval / (nInterval - 1);

        TaskBag bag = new TaskBag();
        StatisticCounter stats = new StatisticCounter(nInterval);

        int CONSUMERS_COUNT = Runtime.getRuntime().availableProcessors();

        Thread producer = new ProducerAgent(bag, directory);
        List<Thread> consumers = new ArrayList<>();

        for (int i = 0; i < CONSUMERS_COUNT; i++) {
            consumers.add(new WorkerAgent(bag, "Worker-" + i, stats, nInterval, maxInterval));
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

        // maybe some bugs if MAXL could not be divided by NI
        List<Interval> intervals = stats.getStats();
        for (int i = 0; i < (nInterval-1); i++) {
            logInterval(i * intervalSize, (i + 1) * intervalSize, intervals.get(i).getFileCount());
        }
        logInterval(maxInterval, intervals.get(nInterval-1).getFileCount());
    }

    private static void logInterval(int lower, int upper, int count) {
        System.out.println("[" + lower + ", " + upper + "]: " + count + " files");
    }
    private static void logInterval(int lower, int count) {
        System.out.println("[" + lower + ", inf]: " + count + " files");
    }
}

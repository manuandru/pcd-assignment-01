package pcd.assignment01.coordination.agent;

import pcd.assignment01.coordination.StopFlag;
import pcd.assignment01.stats.StatisticCounter;
import pcd.assignment01.coordination.agent.task.Task;
import pcd.assignment01.coordination.agent.task.TaskBag;

import java.io.*;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public class ConsumerAgent extends Thread {

    private final TaskBag bag;
    private final StatisticCounter stats;
    private final int nInterval;
    private final int intervalSize;
    private final CountDownLatch latch;
    private final StopFlag stopFlag;

    public ConsumerAgent(TaskBag bag, String name, StatisticCounter stats, int nInterval, int maxInterval, CountDownLatch latch, StopFlag stopFlag) {
        this.bag = bag;
        this.stats = stats;
        this.nInterval = nInterval;
        this.intervalSize = maxInterval / (nInterval - 1);
        setName(name);
        this.latch = latch;
        this.stopFlag = stopFlag;
    }

    @Override
    public void run() {
        while (true) {
            Optional<Task> opt;
            try {
                opt = bag.getATask();  // blocking - wait for a task to do
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Uncomment this and try stopping the system
//            try {
//                Thread.sleep((long) (Math.random()*1000));
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }

            if (!opt.isPresent()) {
                latch.countDown();      // need to notify his exit
                return;
            }

            Task task = opt.get();
            analyzeFile(task.path());
        }
    }

    private void analyzeFile(String file) {
        int lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // If stopFlag monitor is used here:
            // pro: we can escape a long reading file and instantly exit
            // cons: we need a check over each line...
            // based on the assumption that the (source) file to be read is short =>
            // I avoid this mechanism
            //
            // If the stop is requested: this thread normally end his work,
            // but model updates are not sent to the GUI
//            while (reader.ready() && !stopFlag.isStopped()){
//                reader.readLine();
//                lines++;
//            }
            lines = (int) reader.lines().count();
        } catch (IOException e) { e.printStackTrace(); }

        int fileInterval = getIntervalOfFile(lines, 0);
        stats.addFileStats(fileInterval, file, lines);
    }

    private int getIntervalOfFile(int lines, int interval) {
        if (interval >= nInterval) {
            return interval - 1;
        }
        if (lines < (interval + 1) * intervalSize) {
            return interval;
        }
        return getIntervalOfFile(lines, interval + 1);
    }
}

package pcd.assignment01.model.agent;

import pcd.assignment01.model.stats.StatisticCounter;
import pcd.assignment01.model.task.Task;
import pcd.assignment01.model.task.TaskBag;

import java.io.*;
import java.util.Optional;

public class ConsumerAgent extends Thread {

    private final TaskBag bag;
    private final StatisticCounter stats;
    private final int nInterval;
    private final int intervalSize;

    public ConsumerAgent(TaskBag bag, String name, StatisticCounter stats, int nInterval, int maxInterval) {
        this.bag = bag;
        this.stats = stats;
        this.nInterval = nInterval;
        this.intervalSize = maxInterval / (nInterval - 1);
        setName(name);
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

            if (!opt.isPresent()) {
                return;
            }

            Task task = opt.get();
            analyzeFile(task.path());
        }
    }

    private void analyzeFile(String file) {
        int lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            lines = (int) reader.lines().count();
        } catch (IOException e) {
//            throw new RuntimeException(e);
        }

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

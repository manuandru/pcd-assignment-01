package pcd.concurrent_reading.producer_consumer.agents;

import pcd.concurrent_reading.producer_consumer.PathBag;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ConsumerAgent extends Thread {

    private final PathBag bag;

    public ConsumerAgent(PathBag bag) {
        this.bag = bag;
    }

    @Override
    public void run() {
        while (true) {
            String path;
            try {
                path = bag.getATask();  // blocking - wait for a task to do
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (path == null) {
                return; // no more work to do
            }

            analyzeFile(path);
        }
    }

    private void analyzeFile(String file) {
        long lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            lines = reader.lines().count();
        } catch (IOException ignored) { }

        // Do something with lines...
        long sum = 0;
        for (int i = 0; i < lines; i++) {
            sum += lines;
        }
    }
}

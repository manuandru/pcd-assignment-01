package pcd.concurrent_reading.producer_consumer;

import pcd.concurrent_reading.producer_consumer.agents.ProducerAgent;
import pcd.concurrent_reading.producer_consumer.agents.ConsumerAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        int CONSUMER_COUNT = Runtime.getRuntime().availableProcessors();
        List<String> initialDirectory = new ArrayList<>(Collections.singleton("../../../../../miniconda3"));

        PathBag bag = new PathBag();

        Thread producer = new ProducerAgent(bag, initialDirectory);
        List<Thread> consumers = new ArrayList<>();
        for (int i = 0; i < CONSUMER_COUNT; i++) {
            consumers.add(new ConsumerAgent(bag));
        }

        long start = System.currentTimeMillis();

        producer.start();
        for (Thread t : consumers) {
            t.start();
        }

        producer.join();
        for (Thread t : consumers) {
            t.join();
        }

        long stop = System.currentTimeMillis();

        System.out.println("Time with " + CONSUMER_COUNT + " Consumers: " + (stop - start));

    }
}

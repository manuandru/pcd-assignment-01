package pcd.assignment01.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class TestConcurrentRead {
    public static void main(String[] args) throws InterruptedException {

        CyclicBarrier barrier = new CyclicBarrier(2);

        Thread reader1 = new WorkerReader(barrier, "data/file.java");
        Thread reader2 = new WorkerReader(barrier, "data/file2");

        Thread.sleep(10000);
        long start = System.currentTimeMillis();
        reader1.start();
        reader2.start();

        reader1.join();
        reader2.join();
        long stop = System.currentTimeMillis();
        System.out.println("Time: " + (stop - start));

    }
}

class WorkerReader extends Thread {

    private final CyclicBarrier barrier;
    private final String file;

    WorkerReader(CyclicBarrier barrier, String file) {
        this.barrier = barrier;
        this.file = file;
    }

    @Override
    public void run() {
        try {
            barrier.await();
        } catch (Exception ignored) { }

        System.out.println(getName() + ": init read");

        long lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            lines = reader.lines().count();
            while (reader.ready()) {
                reader.readLine();
                lines++;
            }
        } catch (IOException ignored) { }

        System.out.println(getName() + ": " + lines);
    }
}
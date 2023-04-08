package pcd.concurrent_reading.recursive_worker;

import pcd.concurrent_reading.recursive_worker.task.Task;
import pcd.concurrent_reading.recursive_worker.task.TaskBag;
import pcd.concurrent_reading.recursive_worker.task.FolderAnalyzerTask;
import pcd.concurrent_reading.recursive_worker.task.FileAnalyzerTask;


import java.io.*;
import java.util.Optional;

public class WorkerAgent extends Thread {

    private final TaskBag bag;

    public WorkerAgent(TaskBag bag) {
        this.bag = bag;
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
//                System.out.println(getName() + ": exiting...");
                return; // end the worker because no more work to do
            }
            Task task = opt.get();


            if (task instanceof FolderAnalyzerTask) {
                FolderAnalyzerTask t = (FolderAnalyzerTask) task;
                analyzeFolder(t.path());
            } else if (task instanceof FileAnalyzerTask) {
                FileAnalyzerTask t = (FileAnalyzerTask) task;
                analyzeFile(t.path());
            } else {
                throw new IllegalThreadStateException("Illegal task to perform");
            }

            bag.completeAssignedTask(); // Post protocol

        }
    }

    private void analyzeFolder(String folder) {
        String[] nodes = new File(folder).list();
        if (nodes == null) return;

        for (String element : nodes) {
            File node = new File(folder + "/" + element);
            if (node.isDirectory()) {
                bag.addNewTask(new FolderAnalyzerTask(node.getPath()));
            } else if (node.isFile()) {
                bag.addNewTask(new FileAnalyzerTask(node.getPath()));
            }
        }
    }

    private void analyzeFile(String file) {
        int lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) lines++;
        } catch (IOException ignored) { }
        long sum = 0;
        for (int i = 0; i < lines; i++) {
            sum += lines;
        }
    }
}

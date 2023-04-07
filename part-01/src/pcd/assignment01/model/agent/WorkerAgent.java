package pcd.assignment01.model.agent;

import pcd.assignment01.model.task.FileAnalyzerTask;
import pcd.assignment01.model.task.FolderAnalyzerTask;
import pcd.assignment01.model.task.Task;
import pcd.assignment01.model.task.TaskBag;

import java.io.*;
import java.util.Optional;

public class WorkerAgent extends Thread {

    public static final String FILE_EXTENSION = ".MP4";
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
            } else if (!node.getName().endsWith(FILE_EXTENSION)){
                bag.addNewTask(new FileAnalyzerTask(node.getPath()));
            }
        }
//        System.out.println(getName() + ": " + folder);
    }

    private void analyzeFile(String file) {
        int lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) lines++;
        } catch (IOException e) {
            //throw new RuntimeException(e);
        }
//        System.out.println(getName() + ": " + file + " -> " + lines);
    }
}

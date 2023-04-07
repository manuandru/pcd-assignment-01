package pcd.assignment01.model.agent;

import pcd.assignment01.model.task.FileAnalyzerTask;
import pcd.assignment01.model.task.FolderAnalyzerTask;
import pcd.assignment01.model.task.Task;
import pcd.assignment01.model.task.TaskBag;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
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
                return;
            }

            Task task = opt.get();

            analyzeFile(task.path());

        }
    }

    private void analyzeFile(String file) {
        long lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            lines = reader.lines().count();
        } catch (IOException e) {
            //throw new RuntimeException(e);
        }
//        System.out.println(getName() + ": " + file + " -> " + lines);
    }
}

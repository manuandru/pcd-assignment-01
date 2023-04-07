package pcd.assignment01.model.agent;

import pcd.assignment01.model.task.FileAnalyzerTask;
import pcd.assignment01.model.task.FolderAnalyzerTask;
import pcd.assignment01.model.task.Task;
import pcd.assignment01.model.task.TaskBag;

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
        // TODO
    }

    private void analyzeFile(String file) {
        // TODO
    }
}

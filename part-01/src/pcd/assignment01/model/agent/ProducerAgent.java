package pcd.assignment01.model.agent;

import pcd.assignment01.model.task.FileAnalyzerTask;
import pcd.assignment01.model.task.Task;
import pcd.assignment01.model.task.TaskBag;

import java.io.*;
import java.util.*;

public class ProducerAgent extends Thread {

    public static final String FILE_EXTENSION = ".java";
    private final TaskBag bag;
    private final LinkedList<String> folders = new LinkedList<>();

    public ProducerAgent(TaskBag bag, String startingPath) {
        this.bag = bag;
        setName("Producer");
        folders.add(startingPath);
    }

    @Override
    public void run() {
        while (true) {

            if (folders.isEmpty()) {
                System.out.println(getName() + ": no more folders -- exiting...");
                bag.noMore();
                return; // no more file to produce
            }

            String folder = folders.removeFirst();
            String[] nodes = new File(folder).list();

//            System.out.println(folder);

            if (nodes != null) {
                LinkedList<Task> tasks = new LinkedList<>();
                for (String element : nodes) {
                    File node = new File(folder + "/" + element);
                    if (node.isDirectory()) {
                        folders.addLast(node.getPath());
                    } else if (node.getName().endsWith(FILE_EXTENSION)) {
                        tasks.addLast(new FileAnalyzerTask(node.getPath()));
                    }
                }
                bag.addAllNewTasks(tasks);
            }
        }
    }
}

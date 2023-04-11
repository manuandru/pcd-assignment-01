package pcd.assignment01.model.agent;

import pcd.assignment01.StopFlag;
import pcd.assignment01.model.task.FileAnalyzerTask;
import pcd.assignment01.model.task.Task;
import pcd.assignment01.model.task.TaskBag;

import java.io.*;
import java.util.*;

public class ProducerAgent extends Thread {

    private final TaskBag bag;
    private final LinkedList<String> folders = new LinkedList<>();
    private final StopFlag stopFlag;
    private final Set<String> extensions;

    public ProducerAgent(TaskBag bag, StopFlag stopflag, String startingPath, Set<String> extensions) {
        this.bag = bag;
        setName("Producer");
        folders.add(startingPath);
        this.stopFlag = stopflag;
        this.extensions = extensions;
        System.out.println(extensions.isEmpty());
    }

    @Override
    public void run() {
        while (true) {

            if (stopFlag.isStopped()) {
                folders.clear();    // cause enter the next if and exiting
                bag.clear();        // remove the pending tasks -- consumers could exit too
            }

            if (folders.isEmpty()) {
                System.out.println(getName() + ": no more folders -- exiting...");
                bag.noMore();
                return; // no more file to produce
            }

            String folder = folders.removeFirst();
            String[] nodes = new File(folder).list();


            if (nodes != null) {
                LinkedList<Task> tasks = new LinkedList<>();
                for (String element : nodes) {
                    File node = new File(folder + "/" + element);
                    if (node.isDirectory()) {
                        folders.addLast(node.getPath());
                    } else if (extensions.isEmpty()) {
                        tasks.addLast(new FileAnalyzerTask(node.getPath()));
                    } else {
                        var splitted = node.getName().split("\\.");
                        if (splitted.length > 1 && extensions.contains(splitted[1])) {
                            System.out.println(Arrays.toString(splitted));
                            tasks.addLast(new FileAnalyzerTask(node.getPath()));
                        }
                    }
                }
                bag.addAllNewTasks(tasks);
            }
        }
    }
}

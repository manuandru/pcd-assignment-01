package pcd.concurrent_reading.producer_consumer.agents;

import pcd.concurrent_reading.producer_consumer.PathBag;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class ProducerAgent extends Thread {

    private final PathBag bag;
    private final LinkedList<String> folders = new LinkedList<>();

    public ProducerAgent(PathBag bag, List<String> startingPaths) {
        this.bag = bag;
        folders.addAll(startingPaths);
    }

    @Override
    public void run() {
        while (true) {

            if (folders.isEmpty()) {
                bag.noMore();
                System.out.println(getName() + ": Folder scanning end, exiting...");
                return; // no more file to produce
            }

            String folder = folders.removeFirst();
            String[] nodes = new File(folder).list();

            if (nodes != null) {
                LinkedList<String> files = new LinkedList<>();
                for (String element : nodes) {
                    File node = new File(folder + "/" + element);
                    if (node.isDirectory()) {
                        folders.addLast(node.getPath());
                    } else if (node.isFile()) {
                        files.addLast(node.getPath());
                    }
                }
                bag.addAllNewTasks(files);
            }
        }
    }
}

package pcd.jpf.model;

import gov.nasa.jpf.vm.Verify;

import java.util.*;

public class ProducerAgent extends Thread {

    private final TaskBag bag;
    private final LinkedList<String> folders = new LinkedList<>();
    private final Map<String, List<String>> fileSystem = new HashMap<>();

    public ProducerAgent(TaskBag bag) {
        Verify.beginAtomic();
        this.bag = bag;

        folders.add("folder1");

        fileSystem.put("folder1", Arrays.asList("file1", "file2", "folder2"));
        fileSystem.put("folder2", Arrays.asList("folder3", "file3"));
        fileSystem.put("folder3", Collections.emptyList());
        Verify.endAtomic();
    }

    @Override
    public void run() {
        while (true) {

            if (folders.isEmpty()) {
                bag.noMore();
                return; // no more file to produce
            }

            Verify.beginAtomic();
            String folder = folders.removeFirst();
            List<String> nodes = fileSystem.get(folder);
            Verify.endAtomic();


            if (nodes != null) {
                Verify.beginAtomic();
                LinkedList<String> tasks = new LinkedList<>();
                for (String node : nodes) {
                    if (fileSystem.containsKey(node)) {
                        folders.addLast(node);
                    } else {
                        tasks.addLast(node);
                    }
                }
                Verify.endAtomic();
                bag.addAllNewTasks(tasks);
            }
        }
    }
}

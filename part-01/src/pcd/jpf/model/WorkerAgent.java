package pcd.jpf.model;

import gov.nasa.jpf.vm.Verify;

import java.util.Optional;

public class WorkerAgent extends Thread {

    private final TaskBag bag;
    private final StatisticCounter stats;

    public WorkerAgent(TaskBag bag, StatisticCounter stats) {
        Verify.beginAtomic();
        this.bag = bag;
        this.stats = stats;
        Verify.endAtomic();
    }

    @Override
    public void run() {
        while (true) {
            Optional<String> opt;
            try {
                opt = bag.getATask();  // blocking - wait for a task to do
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (!opt.isPresent()) {
                return;
            }

            Verify.beginAtomic();
            String file = opt.get();
            int fileSize = file.length();
            Verify.endAtomic();

            stats.addFileStats(fileSize);
        }
    }
}

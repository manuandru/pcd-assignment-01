package pcd.jpf;

import gov.nasa.jpf.vm.Verify;
import pcd.jpf.model.ProducerAgent;
import pcd.jpf.model.StatisticCounter;
import pcd.jpf.model.TaskBag;
import pcd.jpf.model.WorkerAgent;


public class Main {
    public static void main(String[] args) throws InterruptedException {

        Verify.beginAtomic();
        TaskBag bag = new TaskBag();
        StatisticCounter stats = new StatisticCounter();

        Thread producer = new ProducerAgent(bag);
        Thread consumer1 = new WorkerAgent(bag, stats);
        Thread consumer2 = new WorkerAgent(bag, stats);
        Thread consumer3 = new WorkerAgent(bag, stats);

        producer.start();
        consumer1.start();
        consumer2.start();
        consumer3.start();
        Verify.endAtomic();

        producer.join();
        consumer1.join();
        consumer2.join();
        consumer3.join();

        Verify.beginAtomic();
        int result = stats.getStats();
        assert result == 15;
        Verify.endAtomic();
    }
}

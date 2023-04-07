package pcd.assignment01;

import pcd.assignment01.model.agent.WorkerAgent;
import pcd.assignment01.model.task.FolderAnalyzerTask;
import pcd.assignment01.model.task.TaskBag;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        TaskBag bag = new TaskBag();
        bag.addNewTask(new FolderAnalyzerTask("../../../../../Zotero"));
        bag.addNewTask(new FolderAnalyzerTask("../../../../../MacDocuments"));
        bag.addNewTask(new FolderAnalyzerTask("../../../../../Pictures"));
        bag.addNewTask(new FolderAnalyzerTask("../../../../../miniconda3"));
        bag.addNewTask(new FolderAnalyzerTask("../../../../../Documents"));



        Thread t = new WorkerAgent(bag);
        Thread t2 = new WorkerAgent(bag);
        Thread t3 = new WorkerAgent(bag);
        Thread t4 = new WorkerAgent(bag);
        Thread t5 = new WorkerAgent(bag);
        Thread t6 = new WorkerAgent(bag);
        Thread t7 = new WorkerAgent(bag);
        Thread t8 = new WorkerAgent(bag);
        Thread t9 = new WorkerAgent(bag);
        Thread t10 = new WorkerAgent(bag);

        long start = System.currentTimeMillis();
        t.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        t8.start();
        t9.start();
        t10.start();

        t.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
        t6.join();
        t7.join();
        t8.join();
        t9.join();
        t10.join();
        long stop = System.currentTimeMillis();
        System.out.println("Time: " + (stop - start));


//        new WorkerAgent(bag).start();
//        new WorkerAgent(bag).start();
//        new WorkerAgent(bag).start();
//        new WorkerAgent(bag).start();
//        new WorkerAgent(bag).start();
//        new WorkerAgent(bag).start();
//        new WorkerAgent(bag).start();
//        new WorkerAgent(bag).start();
//        new WorkerAgent(bag).start();

    }
}

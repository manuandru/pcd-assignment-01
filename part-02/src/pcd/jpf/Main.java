package pcd.jpf;

import gov.nasa.jpf.vm.Verify;

public class Main {

    static public void main(String[] args) throws InterruptedException {

        Verify.beginAtomic();
        int countOfThread = 2;
        Controller controller = new Controller(countOfThread);
        View fakeView = new View();
        controller.setView(fakeView);
        Verify.endAtomic();

        // If start and stop => model could be each number
        Thread toTest = controller.startSearch();
        controller.stopSearch();
        toTest.join();

        Verify.beginAtomic();
        System.out.println("Stopped: " + fakeView.result);
        assert(fakeView.result <= 6);
        Verify.endAtomic();

        // If start => end the process correctly
//        toTest = controller.startSearch();
//        toTest.join();
//
//        Verify.beginAtomic();
//        System.out.println("Not Stopped: " + fakeView.result);
//        assert(fakeView.result == 6);
//        Verify.endAtomic();
    }

}

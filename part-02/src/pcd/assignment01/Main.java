package pcd.assignment01;

import pcd.assignment01.model.stats.StatisticCounter;

public class Main {

    static public void main(String[] args){
        int countOfThread = Runtime.getRuntime().availableProcessors();
        Controller controller = new Controller(countOfThread);
        View view = new View(controller);
        controller.setView(view);
        view.setVisible(true);
    }

}

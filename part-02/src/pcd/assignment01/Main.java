package pcd.assignment01;

import pcd.assignment01.model.stats.StatisticCounter;

public class Main {

    static public void main(String[] args){
        Controller controller = new Controller();
        View view = new View(controller);
        view.setVisible(true);
    }

}

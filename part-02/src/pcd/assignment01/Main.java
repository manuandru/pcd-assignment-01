package pcd.assignment01;

public class Main {

    static public void main(String[] args){
        int countOfThread = Runtime.getRuntime().availableProcessors();
        Controller controller = new Controller(countOfThread);
        View view = new View(controller);
        controller.setView(view);
        view.setVisible(true);
    }

}

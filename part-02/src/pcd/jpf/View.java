package pcd.jpf;

import pcd.jpf.coordination.ModelObserver;

class View implements ModelObserver {

    public int result = 0;

    @Override
    public void modelUpdated(int stats) {
        result = stats;
    }
}

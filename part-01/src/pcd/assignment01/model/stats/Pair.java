package pcd.assignment01.model.stats;

class Pair<X, Y> {

    private final X x;
    private final Y y;

    Pair(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public X getX() {
        return x;
    }

    public Y getY() {
        return y;
    }
}

public class Location {

    private int x, y;
    private boolean hasTrafficSignal;

    public Location(int x, int y, boolean hasTrafficSignal) {
        this.x = x;
        this.y = y;
        this.hasTrafficSignal = hasTrafficSignal;
    }

    public int[] get() {
        return new int[] { x, y };
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Location move(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public double distance(Location other) {
        return Math.sqrt(Math.pow(x - other.getX(), 2) + Math.pow(y - other.getY(), 2));
    }

    public Location normalize(Location other) {
        return new Location((int) ((x - other.getX()) / distance(other)), (int) ((y - other.getY()) / distance(other)), hasTrafficSignal);
    }
}
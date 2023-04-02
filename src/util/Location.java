public class Location {

    public int x, y;
    public boolean hasTrafficSignal;

    public Location(int x, int y, boolean hasTrafficSignal) {
        this.x = x;
        this.y = y;
        this.hasTrafficSignal = hasTrafficSignal;
    }

    public int[] get() {
        return new int[] { x, y };
    }

    public Location move(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }
}
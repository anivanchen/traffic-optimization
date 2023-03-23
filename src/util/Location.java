public class Location {

    public int x, y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
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
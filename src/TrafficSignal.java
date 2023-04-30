import java.awt.Color;


public class TrafficSignal {

    private Color color;
    private int x, y;
    private Road road;
    private int time;

    private int slowDistance;
    private int stopDistance;
    private int slowFactor;

    public TrafficSignal(Road road, int slowDistance, int stopDistance, int slowFactor, boolean useDefault) {
        this.color = Color.GREEN;
        this.road = road;
        this.x = road.getEnd().getX();
        this.y = road.getEnd().getY();
        
        this.slowDistance = slowDistance;
        this.stopDistance = stopDistance;
        this.slowFactor = slowFactor;

        if(useDefault) setDefaultConfig();
    }

    public void setDefaultConfig() {
        color = Color.GREEN;
        x = road.getEnd().getX();
        y = road.getEnd().getY();

        slowDistance = 10;
        stopDistance = 5;
        slowFactor = 2;
    }

    public Color getCurrentCycle(){
        if(time % 10 < 4){
            color = Color.GREEN;
            return color;
        } else if (time % 10 > 5){
            color = Color.RED;
            return color;
        } else {
            color = Color.YELLOW;
            return color;
        }
    }

    private void tick() {
        time++;
    }

    public float getSlowDistance() {
        return slowDistance;
    }

    public float getStopDistance() {
        return stopDistance;
    }

    public float getSlowFactor() {
        return slowFactor;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
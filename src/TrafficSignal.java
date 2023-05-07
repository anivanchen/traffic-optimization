import java.awt.Color;
import java.util.*;

public class TrafficSignal {
    private ArrayList<Color> color;
    private int x, y;
    private Road road;
    private int time;

    private int slowDistance;
    private int stopDistance;
    private int slowFactor;

    public TrafficSignal(Road road, int slowDistance, int stopDistance, int slowFactor, boolean useDefault) {
        this.color = new ArrayList<Color>();
        this.color.add(Color.GREEN);
        this.color.add(Color.RED);
        this.color.add(Color.GREEN);
        this.color.add(Color.RED);
        this.road = road;
        this.x = road.getEnd().getX();
        this.y = road.getEnd().getY();
        
        this.slowDistance = slowDistance;
        this.stopDistance = stopDistance;
        this.slowFactor = slowFactor;
        // if(useDefault) setDefaultConfig();
    }

    public void tick(int current_seconds) {
        int seconds_mod = current_seconds % 60;
        if (seconds_mod <= 15) {
            this.color.set(0, Color.GREEN);
            this.color.set(1, Color.RED);
            this.color.set(2, Color.GREEN);
            this.color.set(3, Color.RED);
            //  = new ArrayList<Color>({Color.GREEN, Color.RED, Color.GREEN, Color.RED});
        } else if (seconds_mod <= 30) {
            this.color.set(0, Color.YELLOW);
            this.color.set(1, Color.RED);
            this.color.set(2, Color.YELLOW);
            this.color.set(3, Color.RED);
        } else if (seconds_mod <= 45) {
            this.color.set(0, Color.RED);
            this.color.set(1, Color.GREEN);
            this.color.set(2, Color.RED);
            this.color.set(3, Color.GREEN);
        } else {
            this.color.set(0, Color.RED);
            this.color.set(1, Color.YELLOW);
            this.color.set(2, Color.RED);
            this.color.set(3, Color.YELLOW);
        }
    }

    public ArrayList<Color> getColors() {
        return color;
    }

    // public void setDefaultConfig() {
    //     color = Color.GREEN;
    //     x = road.getEnd().getX();
    //     y = road.getEnd().getY();

    //     slowDistance = 10;
    //     stopDistance = 5;
    //     slowFactor = 2;
    // }

    // public Color getCurrentCycle(){
    //     if(time % 10 < 4){
    //         color = Color.GREEN;
    //         return color;
    //     } else if (time % 10 > 5){
    //         color = Color.RED;
    //         return color;
    //     } else {
    //         color = Color.YELLOW;
    //         return color;
    //     }
    // }

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
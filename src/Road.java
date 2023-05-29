import java.util.*;
import java.awt.geom.*;
import java.awt.*;
import java.lang.*;

public class Road {

    private Location start;
    private Location end;

    private double length;
    private Location normal;

    private TrafficSignal trafficSignal;
    private boolean hasTrafficSignal;
    private int trafficSignalGroup;

    private ArrayList<Car> vehicles;

    public Road(Location start, Location end) {
        this.start = start;
        this.end = end;
        // this.vehicles = new LinkedList<>();
        initProps();
    }

    public Location getStart() {
        return start;
    }

    public Location getEnd() {
        return end;
    }

    private void initProps() {
        this.length = Point2D.distance(start.getX(), start.getY(), end.getX(), end.getY());
        this.normal = end.normalize(start);
        this.hasTrafficSignal = false;
    }

    public void setTrafficSignal(TrafficSignal trafficSignal) {
        this.trafficSignal = trafficSignal;
        hasTrafficSignal = true;
    }

    public boolean getTrafficSignalState() {
        // if (hasTrafficSignal) {
        //     int i = trafficSignalGroup;
        //     return trafficSignal.getCurrentCycle() == Color.GREEN;
        // }
        return true;
    }

    public void update(double dt) {
        int n = this.vehicles.size();

        if (n > 0) {

            // update first vehicle
            vehicles.get(0).update(dt);

            // update other vehicles
            for (int i = 1; i < n; i++) {
                Car car = this.vehicles.get(i - 1);
                vehicles.get(i).update(dt);
            }

            if (getTrafficSignalState()) {
                vehicles.get(0).unstop();
                for (Car car : vehicles) {
                    car.unslow();
                }
            } else {
                if (vehicles.get(0).getLocation().getX() >= length - trafficSignal.getSlowDistance()) {
                    vehicles.get(0).slow(trafficSignal.getSlowFactor() * vehicles.get(0).getMaxVelocity());
                }
                if (vehicles.get(0).getLocation().getX() >= length - trafficSignal.getStopDistance() &&
                        this.vehicles.get(0).getLocation().getX() <= length - trafficSignal.getStopDistance() / 2) {
                    vehicles.get(0).stop();
                }
            }
        }
    }
}

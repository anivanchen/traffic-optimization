import java.util.*;
import java.awt.geom.*;

public class Road {

    public Location start;
    public Location end;

    private double length;
    private double sin;
    private double cos;

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

    private void initProps() {
        this.length = Point2D.distance(start.x, start.y, end.x, end.y);
        this.sin = (end.y - start.y) / length;
        this.cos = (end.x - start.x) / length;
        this.hasTrafficSignal = false;
    }

    public void setTrafficSignal(TrafficSignal trafficSignal) {
        this.trafficSignal = trafficSignal;
        hasTrafficSignal = true;
    }

    public boolean getTrafficSignalState() {
        if (hasTrafficSignal) {
            int i = trafficSignalGroup;
            return trafficSignal.getCurrentCycle();
        }
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
                if (vehicles.get(0) >= length - trafficSignal.getSlowDistance()) {
                    vehicles.get(0).slow(trafficSignal.getSlowFactor() * vehicles.get(0).getMaxVelocity());
                }
                if (vehicles.get(0).getLocation().x >= length - trafficSignal.getStopDistance() &&
                        this.vehicles.get(0).getLocation().x <= length - trafficSignal.getStopDistance() / 2) {
                    vehicles.get(0).stop();
                }
            }
        }
    }
}

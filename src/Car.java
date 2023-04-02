import java.util.*;
import java.io.*;
import java.lang.*;

public class Car {
  private Location start, current, end;
  private ArrayList<Location> path;
  private double length, angle, velocity, acceleration, maxAngle, maxVelocity, currentMaxVelocity, maxAcceleration;

  private boolean stopped;

  public Car(Location start, Location end, double length, double maxAngle, double maxVelocity, double maxAcceleration) {
    this.start = start;
    this.current = start;
    this.end = end;

    this.path = new ArrayList<Location>();

    this.length = length;

    this.maxAngle = maxAngle;
    this.maxVelocity = maxVelocity;
    this.currentMaxVelocity = maxVelocity;
    this.maxAcceleration = maxAcceleration;

    this.angle = 0;
    this.velocity = currentMaxVelocity;
    this.acceleration = 0;

    this.stopped = false;
  }

  public void update(Car leadCar, double dt, TrafficSignal trafficSignal) {
    if(leadCar.velocity > velocity){
      slow(leadCar.velocity);
    }
    if(trafficSignal.getCurrentCycle() == false){
      slow(0);
    }
    if (velocity + acceleration * dt < 0) {
      current.x -= 0.5 * velocity * velocity / acceleration;
      velocity = 0;
    } else {
      velocity += acceleration * dt;
      current.x += velocity * dt + acceleration * dt * dt / 2;
    }

    double alpha = 0;
    double dX = leadCar.getLocation().x - current.x - leadCar.getLength();
    double dV = velocity - leadCar.getVelocity();

    alpha = (0 + Math.max(0, 1 * velocity + dV * velocity / sqrtAB));

        current = current.move(0, 0);
  }

  public Location getLocation() {
    return this.current;
  }

  public double getLength() {
    return this.length;
  }

  public double getVelocity() {
    return this.velocity;
  }

  public double getMaxVelocity() {
    return this.maxVelocity;
  }

  public double getAngle() {
    return this.angle;
  }

  public double getAcceleration() {
    return this.acceleration;
  }

  public double getMaxAcceleration() {
    return this.maxAcceleration;
  }

  public void stop() {
    this.stopped = true;
  }

  public void unstop() {
    this.stopped = false;
  }

  public void slow(double velocity) {
    this.currentMaxVelocity = velocity;
  }

  public void unslow() {
    this.currentMaxVelocity = maxVelocity;
  }
}

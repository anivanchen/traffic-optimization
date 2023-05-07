import java.util.*;
import java.io.*;
import java.lang.*;
import java.util.TimerTask;

public class Simulation{

  private static ArrayList<TrafficSignal> traffic_signals; // this is cars + stoplights
  private static ArrayList<Car> cars; // this is cars + stoplights
  private static MyCanvas graphics; // this is 
  private static int max_seconds_elapsed = 75;

  public Simulation() {
    // Create road

    Road road = new Road(new Location(100,400,false), new Location(500,400,false));

    // init slowDistance, stopDistance, slowFactor, useDefault
    int slowDistance = 10;
    int stopDistance = 5;
    int slowFactor = 10;
    boolean useDefault = true;

    // Create traffic
    TrafficSignal traffic = new TrafficSignal(road, slowDistance,  stopDistance,  slowFactor,  useDefault);
    traffic_signals = new ArrayList<TrafficSignal>();
    traffic_signals.add(traffic);

    // Create cars
    Car car = new Car(new Location (200,200, false), new Location(600, 600, false), 10, 0.0, 5, 5);
    cars = new ArrayList<Car>();
    cars.add(car);

    // Create canvas
    graphics = new MyCanvas(cars, traffic_signals);
  }

  public static void main(String[] args) {
    int time_seconds = 0;

    while (time_seconds < max_seconds_elapsed) {
      // call tick on all of the variables that we defined above
      for (TrafficSignal current_ts : traffic_signals) {
        current_ts.tick(time_seconds);
      }
      for (Car current_car : cars) {
        current_car.tick();
      }
      graphics.tick();

      time_seconds++;
      try {
        Thread.sleep(1000); //pauses for one second 
      } catch (Exception e) {
        System.out.println("eerrrror!");
      }
    }
  }
}

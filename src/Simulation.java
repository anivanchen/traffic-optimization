import java.util.*;
import java.io.*;
import java.lang.*;
import java.util.TimerTask;

public class Simulation {

  public static ArrayList<Car> cars; // this is cars + stoplights
  private static MyCanvas graphics; // this is 
  private static int max_seconds_elapsed = 120;

  public Simulation() {
    // Create road
    System.out.println("Running constructor");
    Road road1 = new Road(new Location(400,0), new Location(400,800)); // x = 5
    Road road2 = new Road(new Location(0,400), new Location(800,400)); // y = 5
    Road road3 = new Road(new Location(0,160), new Location(800,160)); // y = 2
    ArrayList<Road> roads = new ArrayList<Road>();
    roads.add(road1);
    roads.add(road2);
    roads.add(road3);

    // Create cars

    // start: 5,1
    // dest: 11,17
    Car car1 = new Car(new Location (200,30), new Location(440, 680), 10, 0.0, 15, 5);
    Car car2 = new Car(new Location (200,70), new Location(440, 680), 10, 0.0, 15, 5);
    cars = new ArrayList<Car>();
    cars.add(car1);
    cars.add(car2);

    // Create canvas
    graphics = new MyCanvas(cars, roads);
  }

  public static void main(String[] args) {
    Simulation sim = new Simulation();
    int time_seconds = 0;
    sim.graphics.set_grid_representation();
    while (time_seconds < max_seconds_elapsed) {
      // call tick on all of the variables that we defined above
      System.out.println("tick");
      graphics.tellCarsThatAreBlockedToNotMove();
      Iterator<Car> iterator = sim.cars.iterator();
      while (iterator.hasNext()) {
        Car current_car = iterator.next();
        boolean should_still_exist = current_car.tick(time_seconds);
        if (!should_still_exist) {
          iterator.remove();
          graphics.cars = sim.cars;
        }
      }
      graphics.tick(time_seconds);
      time_seconds++;
      try {
        Thread.sleep(1000); //pauses for 0.1 second 
      } catch (Exception e) {
        System.out.println("eerrrror!");
      }

      if (sim.cars.size() == 0) {
        System.out.println("All cars found their destination.");
        break;
      }
    }
  }
}

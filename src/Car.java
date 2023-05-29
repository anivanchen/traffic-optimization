import java.util.*;
import java.io.*;
import java.lang.*;
import java.awt.*;

public class Car {
  private Location start, current, end, globalGoal;
  private ArrayList<Location> localGoals;
  private Map map;
  private double length, angle, velocity, acceleration, maxAngle, maxVelocity, currentMaxVelocity, maxAcceleration;
  private int[][] grid_rep;

  private boolean stopped;

  public Car(Location start, Location end, double length, double maxAngle, double maxVelocity, double maxAcceleration) {
    this.start = start;
    this.current = start;
    this.end = end;

    this.localGoals = new ArrayList<Location>();

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

  public void set_grid_rep(int[][] grid_rep) {
    this.grid_rep = grid_rep;
  }

  public void tick() {
    update(1);
    System.out.println("Does the car have an initialized grid_rep? " + (grid_rep.length > 0));
  }

  private boolean is_valid_transition(int start_grid_value, int end_grid_value, int direction) {
    // direction = 1 ==> down traffic
    // direction = 2 ==> up traffic
    // direction = 3 ==> left traffic
    // direction = 4 ==> right traffic
    if ((start_grid_value == end_grid_value) && (end_grid_value == direction)) {
      return true;
    }
    // entering intersection
    if ((start_grid_value == 1 && end_grid_value == 5) ||
      (start_grid_value == 4 && end_grid_value == 8) ||
      (start_grid_value == 2 && end_grid_value == 7) ||
      (start_grid_value == 3 && end_grid_value == 6)) {
        return true;
    }
    
    // moving straight in intersection
    if ((start_grid_value == 5 && end_grid_value == 8) ||
      (start_grid_value == 8 && end_grid_value == 7) ||
      (start_grid_value == 7 && end_grid_value == 6) ||
      (start_grid_value == 6 && end_grid_value == 5)) {
        return true;
    }

    // exiting intersection 
    if ((start_grid_value == 5 && end_grid_value == 3) ||
      (start_grid_value == 8 && end_grid_value == 1) ||
      (start_grid_value == 7 && end_grid_value == 4) ||
      (start_grid_value == 6 && end_grid_value == 2)) {
        return true;
    }

    return false;
  }

  private boolean is_legal_coordinate(int x, int y) {
    return (x >= 0 && x < this.grid_rep.length && y >= 0 && y < this.grid_rep[0].length);
  }

  public void update(double dt) {
    if (stopped) {
      return;
    }

    if (velocity > currentMaxVelocity) {
      velocity = currentMaxVelocity;
    }

    int canvasWidth = 800;
    int canvasHeight = 800;
    int squareSize = 80;

    // Lets determine our path
    ArrayList<Pair<Integer>> best_path = new ArrayList<Pair<Integer>>();
    Pair<Integer> goal_cell = new Pair<Integer>((this.end.getX()/squareSize)*2, (this.end.getY()/squareSize)*2);
    ArrayList<ArrayList<Pair<Integer>>> horizon = new ArrayList<ArrayList<Pair<Integer>>>();
    Pair<Integer> start = new Pair<Integer>((int)(2*this.current.getX()/squareSize), (int)(2*this.current.getY()/squareSize));
    System.out.println("Starting location: " + start.toString());
    ArrayList<Pair<Integer>> start_path = new ArrayList<Pair<Integer>>();
    start_path.add(start);
    Set<Pair<Integer>> visited = new HashSet<Pair<Integer>>();
    horizon.add(start_path);
    boolean found = false;
    while (horizon.size() > 0) {
      System.out.println("HORIZON:");
      for (ArrayList<Pair<Integer>> path : horizon) {
        System.out.println(path);
      }
      ArrayList<Pair<Integer>> current_path = horizon.remove(0);
      Pair<Integer> current_node = current_path.get(current_path.size()-1);
      int current_node_grid_rep = grid_rep[current_node.get(0)][current_node.get(1)];
      System.out.println("Current node: " + current_node.toString() + " has grid rep value " + current_node_grid_rep);

      visited.add(current_node);
      if ((current_node.get(0) == goal_cell.get(0) && current_node.get(1) == goal_cell.get(1)) ||
          (current_node.get(0) == goal_cell.get(0)+1 && current_node.get(1) == goal_cell.get(1)) ||
          (current_node.get(0) == goal_cell.get(0) && current_node.get(1) == goal_cell.get(1)+1) ||
          (current_node.get(0) == goal_cell.get(0)+1 && current_node.get(1) == goal_cell.get(1)+1)) {
        // Have we found the goal, we need to check the full 4-grid square
        System.out.println("Found best path!");
        best_path = current_path;
        break;
      }
      // Expand all of the neighbors of the current node and add them to the horizon
      Pair<Integer> node_left = new Pair<Integer>(current_node.get(0),current_node.get(1)-1);
      Pair<Integer> node_right = new Pair<Integer>(current_node.get(0),current_node.get(1)+1);
      Pair<Integer> node_up = new Pair<Integer>(current_node.get(0)-1,current_node.get(1));
      Pair<Integer> node_down = new Pair<Integer>(current_node.get(0)+1,current_node.get(1));

      ArrayList<Pair<Integer>> potential_neighbors = new ArrayList<Pair<Integer>>();
      potential_neighbors.add(node_down);
      potential_neighbors.add(node_up);
      potential_neighbors.add(node_left);
      potential_neighbors.add(node_right);
      // check if coordinates are INSIDE of the grid (not negative or anything)
      // check if the coordinates are in the visited set
      // check if the transition from current_node to this cell is valid
      for (int i = 1; i < 5; i++) {
        Pair<Integer> candidate_node = potential_neighbors.get(i-1);
        // values of i:
        // 1 -> down node
        // 2 -> up node
        // 3 -> left node
        // 4 -> right node
        // int candidate_cell_grid_rep = grid_rep[candidate_node.get(0)][candidate_node.get(1)];
        // System.out.println("Candidate node index " + i + " has position " + candidate_node.toString() + " and has grid rep of: " + candidate_cell_grid_rep);
        if (is_legal_coordinate(candidate_node.get(0),candidate_node.get(1)) &&
            !visited.contains(candidate_node) &&
            is_valid_transition(current_node_grid_rep, grid_rep[candidate_node.get(0)][candidate_node.get(1)], i)) {
              // We need to first create a NEW path, that concatenates the OLD Path that got us to the current_node + {any of (left node, right node, up node, down node)}
              ArrayList<Pair<Integer>> new_path_with_added_node = new ArrayList<Pair<Integer>>();
              new_path_with_added_node.addAll(current_path);
              new_path_with_added_node.add(candidate_node);
              horizon.add(new_path_with_added_node);
        }
      }
    }

    if (best_path.size() == 0) {
      System.out.println("Could not find path");

    }

    // We know best path is the set of directions we need, nice thing is that it tells us
    // If go straight, keep speed, if turn, lets just set the speed to be the same and direction to be turn
    // Look at first two cells in the path, determine if we go straight or turn
    if (best_path.size() == 1) {
      // Car is DONE!!!!! DO NOT MOVE ANYMORE, AT GOAL
      return;
    }
    Pair<Integer> current_position = best_path.get(0);
    Pair<Integer> next_position = best_path.get(1);
    int diff_x = next_position.get(0) - current_position.get(0);
    int diff_y = next_position.get(1) - current_position.get(1);
    current.move((int) (diff_x * (velocity * dt + acceleration * dt * dt / 2)), (int) (diff_y * (velocity * dt + acceleration * dt * dt / 2)));
  }

  public void update(Car leadCar, double dt, TrafficSignal trafficSignal) {
    if(leadCar.velocity > velocity){
      slow(leadCar.velocity);
    }
    //TODO: FIX THIS
    // if(trafficSignal.getCurrentCycle() != Color.GREEN){
    //   slow(0);
    // }
    if(velocity > maxVelocity){
      double d = velocity * dt;
      velocity -= acceleration * dt;
      current.move((int) (d + 0.5 * acceleration * dt * dt), 0);
    } else if (velocity < maxVelocity){
      double d = velocity * dt;
      velocity += acceleration * dt;
      current.move((int) (d + 0.5 * acceleration * dt * dt), 0);
    }
    // if (velocity + acceleration * dt < 0) {
    //   current.x -= 0.5 * velocity * velocity / acceleration;
    //   velocity = 0;
    // } else {
    //   velocity += acceleration * dt;
    //   current.x += velocity * dt + acceleration * dt * dt / 2;
    // }

    // double alpha = 0;
    // double dX = leadCar.getLocation().getX() - current.getX() - leadCar.getLength();
    // double dV = velocity - leadCar.getVelocity();

    // alpha = (0 + Math.max(0, 1 * velocity + dV * velocity / sqrtAB));

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

  public void bestPath(Location current){
    LinkedList<LinkedList<int[]>> paths = new LinkedList<LinkedList<int[]>>();
    if(current.getX()-1>=0&&map.location(current.getX()-1, current.getY())!=0){

    }
    if(current.getX()+1<map.row()&&map.location(current.getX()+1, current.getY())!=0){

    }
    if(current.getY()-1>=0&&map.location(current.getX(), current.getY()-1)!=0){

    }
    if(current.getY()+1<map.column()&&map.location(current.getX(), current.getY()+1)!=0){

    }
  }

}

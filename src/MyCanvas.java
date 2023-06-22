import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.*;
import java.lang.*;
import java.io.*;

import javax.swing.*;

public class MyCanvas extends JFrame{

    private Canvas c;
    public int current_time = 0;
    public ArrayList<Car> cars;
    private ArrayList<Road> roads;
    private int[][] grid_rep;

    public void set_grid_representation() {
        int canvasWidth = 800;
        int canvasHeight = 800;
        int squareSize = Math.min(canvasWidth, canvasHeight) / 10;

        int[][] grid_rep = new int[(canvasHeight/squareSize)*2][(canvasHeight/squareSize)*2];

        for (int x = 0; x < canvasWidth; x += squareSize) {
            for (int y = 0; y < canvasHeight; y += squareSize) {
                boolean already_found_road_at_given_position = false;
                for (Road road : this.roads) {
                    // Check stoplight
                    if ((already_found_road_at_given_position && x == road.getStart().getX() && x == road.getEnd().getX())
                        || (already_found_road_at_given_position && y == road.getStart().getY() && y == road.getEnd().getY())) {
                            // TODO: add four cells here for the traffic light
                            grid_rep[2*(y/squareSize)][2*(x/squareSize)]=5; // top left
                            grid_rep[2*(y/squareSize)][2*(x/squareSize)+1]=6;  // top right
                            grid_rep[2*(y/squareSize)+1][2*(x/squareSize)+1]=7;  // bottom right
                            grid_rep[2*(y/squareSize)+1][2*(x/squareSize)]=8; // bottom left

                    } else if (x == road.getStart().getX() && x == road.getEnd().getX() || y == road.getStart().getY() && y == road.getEnd().getY()) {
                        // Check road
                        //1 is top 2 is bottom
                        if(x == road.getStart().getX() && x == road.getEnd().getX()){
                            grid_rep[2*(y/squareSize)][2*(x/squareSize)]=1; 
                            grid_rep[2*(y/squareSize)+1][2*(x/squareSize)]=1; 
                            grid_rep[2*(y/squareSize)+1][2*(x/squareSize)+1]=2; 
                            grid_rep[2*(y/squareSize)][2*(x/squareSize)+1]=2;
                        } 
                        
                        //3 is left 4 is right
                        if(y == road.getStart().getY() && y == road.getEnd().getY()){
                            grid_rep[2*(y/squareSize)][2*(x/squareSize)]=3; 
                            grid_rep[2*(y/squareSize)+1][2*(x/squareSize)]=4; 
                            grid_rep[2*(y/squareSize)+1][2*(x/squareSize)+1]=4; 
                            grid_rep[2*(y/squareSize)][2*(x/squareSize)+1]=3;
                        } 

                        already_found_road_at_given_position = true;
                    }
                }
            }
        }

        System.out.println("GRID REPRESENTATION:");
        for (int[] elem : grid_rep) {
            System.out.println(Arrays.toString(elem));
        }
        for (Car car_instance: this.cars) {
            car_instance.set_grid_rep(grid_rep);
        }

        this.grid_rep = grid_rep;
    }

    public MyCanvas(ArrayList<Car> cars, ArrayList<Road> roads) {
        super("Canvas");
        this.cars = cars;
        this.roads = roads;

        c = new Canvas() {
            // paint the canvas
            public void paint(Graphics g) {
                int canvasWidth = 800;
                int canvasHeight = 800;
                int squareSize = Math.min(canvasWidth, canvasHeight) / 10;

                // set color to blue
                g.setColor(Color.blue);

                // draw squares
                Color[] colors = { Color.red, Color.blue, Color.green, Color.yellow };
                int colorIndex = 0;

                // Here are our three roads
                // // Road 1 is at x=5
                // int road_index_width = squareSize * 5; 

                // // Road 2 is at y=5
                // int road_index_height = squareSize * 5;

                // // Road 3 is at y=2
                // int road_index_height_2 = squareSize * 2;

                // // That means for us that we have two intersections, one at 5,2, one at 5,5
                int carSize = squareSize * 2;

                // TODO: Yonna tracey ivan
                // This for loop creates all of the roads and stoplights at every tick/repaint of the canvas.
                // Main priority is we need to create a 2x2 grid of integers that will represent this map.
                // We need to set the grid up so that each road has a width of 2 cells. The reason for this is that
                // we want a left lane and a right lane. Once we have a grid of integers for which, we use a different
                // number for each side of the road, for the four squares in a traffic light, and for out of bounds, non-road
                // area. 


                // We have a canvas that is 800, 800
                // In our for loop, x goes from 0 -> 720 and increments by 80
                // We want the dimensions of grid_rep to be (2 * (canvas_width / square size), 2 * (canvas_height / square size))

                //keeping track of traffic_signals
                ArrayList<TrafficSignal> traffic_signals= new ArrayList<TrafficSignal>();

                for (int y = 0; y < canvasWidth; y += squareSize) {
                    for (int x = 0; x < canvasHeight; x += squareSize) {
                        boolean already_found_road_at_given_position = false;
                        for (Road road : roads) {
                            // Check stoplight
                            if ((already_found_road_at_given_position && x == road.getStart().getX() && x == road.getEnd().getX())
                                || (already_found_road_at_given_position && y == road.getStart().getY() && y == road.getEnd().getY())) {
                                    // This checks if we have already identified a road at this position and we have now found another road here
                                    TrafficSignal new_traffic_signal = new TrafficSignal(new Location(x,y));
                                    traffic_signals.add(new_traffic_signal);
                                    new_traffic_signal.tick(current_time);
                                    // System.out.println("Ticking with value " + current_time);
                                    
                                    // Top
                                    Polygon triangle1 = new Polygon();
                                    triangle1.addPoint(x,y); // top left corner
                                    triangle1.addPoint(x+squareSize,y);
                                    triangle1.addPoint(x+squareSize/2,y+squareSize/2);                              

                                    // Right
                                    Polygon triangle2 = new Polygon();
                                    triangle2.addPoint(x+squareSize,y);
                                    triangle2.addPoint(x+squareSize,y+squareSize);
                                    triangle2.addPoint(x+squareSize/2,y+squareSize/2);

                                    // Bottom
                                    Polygon triangle3 = new Polygon();
                                    triangle3.addPoint(x+squareSize,y+squareSize);
                                    triangle3.addPoint(x,y+squareSize);
                                    triangle3.addPoint(x+squareSize/2,y+squareSize/2);
                                    
                                    // Left
                                    Polygon triangle4 = new Polygon();
                                    triangle4.addPoint(x,y);
                                    triangle4.addPoint(x,y+squareSize);
                                    triangle4.addPoint(x+squareSize/2,y+squareSize/2);

                                    ArrayList<Color> current_color_set = traffic_signals.get(traffic_signals.size() - 1).getColors();

                                    g.setColor(current_color_set.get(0));
                                    g.fillPolygon(triangle1);
                                    g.setColor(current_color_set.get(1));
                                    g.fillPolygon(triangle2);
                                    g.setColor(current_color_set.get(2));
                                    g.fillPolygon(triangle3);
                                    g.setColor(current_color_set.get(3));
                                    g.fillPolygon(triangle4);

                            } else if (x == road.getStart().getX() && x == road.getEnd().getX() || y == road.getStart().getY() && y == road.getEnd().getY()) {
                                // Check road
                                already_found_road_at_given_position = true;
                                g.setColor(Color.gray);
                                g.fillRect(x,y,squareSize,squareSize);
                            } else {
                                if (!already_found_road_at_given_position) {
                                    // Non-road
                                    g.setColor(Color.blue);
                                    g.fillRect(x,y,squareSize,squareSize);
                                }
                            }
                        }
                    }
                }

                for (int i = 0; i < cars.size(); i++) {
                    // Toolkit t=Toolkit.getDefaultToolkit();  
                    // Image i=t.getImage("CAR.png");  
                    // g.drawImage(i,50,50);  
                    Car car = cars.get(i);
                    int x = car.getLocation().getX();
                    int y = car.getLocation().getY();

                    int grid_x = (int) x*2/80;
                    int grid_y = (int) y*2/80;
                    g.setColor(Color.black); 
                    if (grid_rep[grid_x][grid_y] == 1) {
                        g.fillRect(y, x, 15, 15);
                        System.out.println("lane: "+1);
                        // cars.get(i).rendered_x = y;
                        // cars.get(i).rendered_y = x+12;
                    } else if (grid_rep[grid_x][grid_y] == 2) {
                        g.fillRect(y+28, x+12, 15, 15);
                        System.out.println("lane: "+2);
                        // cars.get(i).rendered_x = y+28;
                        // cars.get(i).rendered_y = x+12;
                    } else if (grid_rep[grid_x][grid_y] == 3) {
                        g.fillRect(y+12, x+48, 15, 15);
                        System.out.println("lane: "+3);
                        // cars.get(i).rendered_x = y+12;
                        // cars.get(i).rendered_y = x+28;
                    } else if (grid_rep[grid_x][grid_y] == 4) {
                        g.fillRect(y, x, 15, 15);
                        System.out.println("lane: "+4);
                        // cars.get(i).rendered_x = y+12;
                        // cars.get(i).rendered_y = x;
                    } else if (grid_rep[grid_x][grid_y] == 5) {
                        g.fillRect(y+12, x+12, 15, 15);
                        // cars.get(i).rendered_x = y+12;
                        // cars.get(i).rendered_y = x+12;
                    } else if (grid_rep[grid_x][grid_y] == 6) {
                        g.fillRect(y+12, x+12, 15, 15);
                        // cars.get(i).rendered_x = y+12;
                        // cars.get(i).rendered_y = x+12;
                    } else if (grid_rep[grid_x][grid_y] == 7) {
                        g.fillRect(y+12, x+12, 15, 15);
                        // cars.get(i).rendered_x = y+12;
                        // cars.get(i).rendered_y = x+12;
                    } else if (grid_rep[grid_x][grid_y] == 8) {
                        g.fillRect(y+12, x+12, 15, 15);
                        // cars.get(i).rendered_x = y+12;
                        // cars.get(i).rendered_y = x+12;
                    }
                }

                // set color to red
                g.setColor(Color.black);

                // set Font
                g.setFont(new Font("Bold", 1, 20));

                // draw a string
                g.drawString("Traffic Simulator", 100, 25);
            }
        };

        // set background
        c.setBackground(Color.black);

        add(c);
        setSize(400, 300);
        setVisible(true);
    }

    public void tellCarsThatAreBlockedToNotMove() {
        // loop over each of the cars, get its position in grid representation
        // internal loop over the other cars, check if they are in the grid "in front"
        // we know in front based on the value of grid_rep

        ArrayList<Boolean> car_should_move = new ArrayList<>(Collections.nCopies(this.cars.size(), true));

        for (int i = 0; i < this.cars.size(); i++) {
            Car current_car = this.cars.get(i);
            // get grid position
            int x = current_car.getLocation().getX();
            int y = current_car.getLocation().getY();
            int grid_square_x = (int)x*2/80;
            int grid_square_y = (int)y*2/80;
            int grid_rep_value = grid_rep[grid_square_x][grid_square_y];
            if (grid_rep_value == 1) {
                // going down, so lets check two grid squares in front
                Pair<Integer> one_below = new Pair<Integer>(grid_square_x + 1, grid_square_y);
                if (anyCarInSquares(one_below.get(0), one_below.get(1))) {
                    car_should_move.set(i, false);
                }

                Pair<Integer> two_below = new Pair<Integer>(grid_square_x + 2, grid_square_y);
                if (anyCarInSquares(two_below.get(0), two_below.get(1))) {
                    car_should_move.set(i, false);
                }
            } else if (grid_rep_value == 2) {
                Pair<Integer> one_below = new Pair<Integer>(grid_square_x - 1, grid_square_y);
                if (anyCarInSquares(one_below.get(0), one_below.get(1))) {
                    car_should_move.set(i, false);
                }

                Pair<Integer> two_below = new Pair<Integer>(grid_square_x - 2, grid_square_y);
                if (anyCarInSquares(two_below.get(0), two_below.get(1))) {
                    car_should_move.set(i, false);
                }
            } else if (grid_rep_value == 3) {
                Pair<Integer> one_below = new Pair<Integer>(grid_square_x, grid_square_y - 1);
                if (anyCarInSquares(one_below.get(0), one_below.get(1))) {
                    car_should_move.set(i, false);
                }

                Pair<Integer> two_below = new Pair<Integer>(grid_square_x, grid_square_y - 2);
                if (anyCarInSquares(two_below.get(0), two_below.get(1))) {
                    car_should_move.set(i, false);
                }

            } else if (grid_rep_value == 4) {
                Pair<Integer> one_below = new Pair<Integer>(grid_square_x, grid_square_y + 1);
                if (anyCarInSquares(one_below.get(0), one_below.get(1))) {
                    car_should_move.set(i, false);
                }

                Pair<Integer> two_below = new Pair<Integer>(grid_square_x, grid_square_y + 2);
                if (anyCarInSquares(two_below.get(0), two_below.get(1))) {
                    car_should_move.set(i, false);
                }
            }
        }

        System.out.println("Created car_should move which has size : " + car_should_move.size() + " and values: ");
        for (Boolean elem : car_should_move) {
            System.out.println("Element: " + elem);
        }

        for (int i = 0; i < car_should_move.size(); i++) {
            System.out.println("Car " + i + " should move? " + car_should_move.get(i));
            this.cars.get(i).car_can_move = car_should_move.get(i);
        }
    }

    public boolean anyCarInSquares(int grid_sq_x, int grid_sq_y) {
        // iterate through cars check if any are in either square
        boolean found = false;

        for (int i = 0; i < this.cars.size(); i++) {
            Car current_car = this.cars.get(i);
            int current_car_grid_x = (int)current_car.getLocation().getX()*2/80;
            int current_car_grid_y = (int)current_car.getLocation().getY()*2/80;
            System.out.println("Checking car with grid position " + current_car_grid_x + ", " + current_car_grid_y + ", comparing to " + grid_sq_x + " and " + grid_sq_y);
            if (current_car_grid_x == grid_sq_x  && current_car_grid_y == grid_sq_y) {
                System.out.println("Found a match");
                found = true;
            }
        }
        return found;
    }

    public void tick(int time_seconds) {
        current_time = time_seconds;
      c.repaint();
    }
}

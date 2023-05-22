import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.*;
import java.lang.*;
import java.io.*;

import javax.swing.*;

public class MyCanvas extends JFrame{

    private Canvas c;
    public int current_time = 0;

    public MyCanvas(ArrayList<Car> cars, ArrayList<Road> roads) {
        super("Canvas");
        // create a empty canvas
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

                // // draw squares
                // for (int x = 0; x < canvasWidth; x += squareSize) {
                // for (int y = 0; y < canvasHeight; y += squareSize) {
                // g.setColor(colors[colorIndex % colors.length]);
                // g.fillRect(x, y, squareSize, squareSize);
                // colorIndex++;
                // }
                // }
                // draw squares

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

                int[][] grid_rep = new int[canvasHeight/squareSize*2][canvasHeight/squareSize*2];

                //keeping track of traffic_signals
                ArrayList<TrafficSignal> traffic_signals= new ArrayList<TrafficSignal>();

                for (int x = 0; x < canvasWidth; x += squareSize) {
                    for (int y = 0; y < canvasHeight; y += squareSize) {
                        boolean already_found_road_at_given_position = false;
                        for (Road road : roads) {
                            // Check stoplight
                            if ((already_found_road_at_given_position && x == road.getStart().getX() && x == road.getEnd().getX())
                                || (already_found_road_at_given_position && y == road.getStart().getY() && y == road.getEnd().getY())) {
                                    // This checks if we have already identified a road at this position and we have now found another road here
                                    TrafficSignal new_traffic_signal = new TrafficSignal(new Location(x,y));
                                    traffic_signals.add(new_traffic_signal);
                                    new_traffic_signal.tick(current_time);
                                    System.out.println("Ticking with value " + current_time);
                                    
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


                                    // TODO: add four cells here for the traffic light
                                    grid_rep[2*y][2*x]=3; 
                                    grid_rep[2*y][2*x+1]=4; 
                                    grid_rep[2*y+1][2*x+1]=5; 
                                    grid_rep[2*y+1][2*x]=6; 





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

                                // TODO: add four cells for the road
                                grid_rep[2*y][2*x]=1; 
                                grid_rep[2*y][2*x+1]=1; 
                                grid_rep[2*y+1][2*x+1]=2; 
                                grid_rep[2*y+1][2*x]=2; 


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

                //System.out.println(traffic_signals);

                for (int i = 0; i < cars.size(); i++) {
                    Car car = cars.get(i);
                    int x = car.getLocation().getX();
                    int y = car.getLocation().getY();
                    g.setColor(Color.black);
                    g.fillRect(x, y, 30, 10);
                }
                // create a JPanel with a raised bevel border
                // JPanel panel = new JPanel();
                // Border border = BorderFactory.createRaisedBevelBorder();
                // panel.setBorder(border);

                // // add the panel to the canvas
                // c.add(panel);

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

    public void tick(int time_seconds) {
        current_time = time_seconds;
      c.repaint();
    }
}

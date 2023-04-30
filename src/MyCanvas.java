import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.*;
import java.lang.*;
import java.io.*;

import javax.swing.*;

public class MyCanvas extends JFrame{

    public MyCanvas(ArrayList<Car> cars, ArrayList<TrafficSignal> trafficSignals) {
        super("Canvas");
        // create a empty canvas
        Canvas c = new Canvas() {
            // paint the canvas
            public void paint(Graphics g) {
                int canvasWidth = getWidth();
                int canvasHeight = getHeight();
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
                int road_index_width = squareSize * 5;
                int road_index_height = squareSize * 5;
                int road_index_height_2 = squareSize * 2;
                int carSize = squareSize * 2;

                for (int x = 0; x < canvasWidth; x += squareSize) {
                    for (int y = 0; y < canvasHeight; y += squareSize) {
                        System.out.println("road_index_width " + road_index_width);
                        System.out.println("road_index_height " + road_index_height);
                        System.out.println("x is " + x);
                        System.out.println("y is " + y);

                        if (x == road_index_width && y == road_index_height
                                || x == road_index_width && y == road_index_height_2) {
                            // fill rectangle
                            g.setColor(Color.green);
                        } else if (x == road_index_width || y == road_index_height || y == road_index_height_2) {
                            g.setColor(Color.red);
                            // fill rectangle
                        } else {
                            // fill rectangle
                            g.setColor(Color.blue);
                        }
                        g.fillRect(x, y, squareSize, squareSize);
                        // draw border
                        g.setColor(Color.black);
                        g.drawRect(x, y, squareSize, squareSize);
                    }
                }

                for (int i = 0; i < cars.size(); i++) {
                    Car car = cars.get(i);
                    int x = car.getLocation().getX();
                    int y = car.getLocation().getY();
                    g.setColor(Color.black);
                    g.fillRect(x, y, carSize, carSize);
                }

                for (int i = 0; i < trafficSignals.size(); i++) {
                    TrafficSignal trafficSignal = trafficSignals.get(i);
                    int x = trafficSignal.getX();
                    int y = trafficSignal.getY();
                    g.setColor(trafficSignal.getCurrentCycle());
                    g.fillRect(x, y, carSize, carSize);
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

}

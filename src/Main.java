import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // StdDraw.setPenColor(Color.WHITE);
        // StdDraw.filledRectangle(50, 100, 10, 20);
        ArrayList<Car> cars = new ArrayList<Car>();
        ArrayList<TrafficSignal> trafficSignals = new ArrayList<TrafficSignal>();
        MyCanvas canvas = new MyCanvas(cars, trafficSignals);
        // canvas.setBackground(Color.BLACK);
        // StdDraw.setPenColor(Color.WHITE);

        // StdDraw.circle(5, 5, 10);
        // StdDraw.show();
    }

}
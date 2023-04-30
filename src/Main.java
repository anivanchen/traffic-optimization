import java.time.LocalTime;
import java.util.ArrayList;

public class Main {
    private int t;
    private ArrayList<Car> cars;
    private ArrayList<TrafficSignal> stoplights;
    private MyCanvas canvas;

    public void main(String[] args) {
        // StdDraw.setPenColor(Color.WHITE);
        // StdDraw.filledRectangle(50, 100, 10, 20);

        // canvas.setBackground(Color.BLACK);
        // StdDraw.setPenColor(Color.WHITE);

        // StdDraw.circle(5, 5, 10);
        // StdDraw.show();

		long ms  = System.currentTimeMillis();
		while (true) {  
			if ((int)ms % 100 == 0) {
                t += 1;
                for(Car i : cars){
                    i.update(System.currentTimeMillis()-ms);
                }
                for(TrafficSignal i : stoplights){
                    i.getCurrentCycle();
                }
                canvas = new MyCanvas(cars, stoplights);
            }
			ms = System.currentTimeMillis();
		}
	}

}
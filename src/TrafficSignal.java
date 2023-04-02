public class TrafficSignal {

    private int x,y;
    private Road road;
    private String color;
    private int time;

    public void setDefaultConfig() {
        color = "green";
        x = road.end.x;
        y = road.end.y;
    }

    public boolean getCurrentCycle(){
        if(time % 10 < 4){
            color = "green";
            return true;
        } else if (time % 10 > 5){
            color = "red";
            return false;
        } else {
            color = "yellow";
            return false;
        }
    }

    private void tick(){
        time++;
    }
}
import java.util.*;
import java.io.*;
import java.lang.*;

public class Map{

  private int[][] roads;
  private int[] trafficLight;
  private int time = 0;

  public Map(int x, int y){
    roads = new int[x][y];
    for(int i=0;i<x;i++){
      for(int j=0;j<y;j++){
        roads[i][j]=0;
      }
    }
    Random r = new Random();
    for(int i=0;i<x;i++){
      if(r.nextInt(3)<1){
        for(int j=0;j<y;j++){
          roads[i][j]=1;
        }
        i++;
      }
    }
    for(int i=0;i<y;i++){
      if(r.nextInt(3)<1){
        for(int j=0;j<x;j++){
          if(roads[j][i]==1){
            roads[j][i]=2;
          }else{
            roads[j][i]=1;
          }
        }
        i++;
      }
    }
  }

  private void tick(){
    time++;
  }

  public int location(int x, int y){
    return roads[x][y];
  }

  public int row(){
    return roads.length;
  }

  public int column(){
    return roads[0].length;
  }

}

import java.util.*;
import java.io.*;
import java.lang.*;
import java.awt.*;


public class Pair<T> {
  public ArrayList<T> elems;

  public Pair(T arg1, T arg2) {
    elems = new ArrayList<T>();
    elems.add(arg1);
    elems.add(arg2);
  }

  public T get(int i) {
    return elems.get(i);
  }

  public String toString() {
    return "x: " + elems.get(0) + ", y: " + elems.get(1);
  }
}


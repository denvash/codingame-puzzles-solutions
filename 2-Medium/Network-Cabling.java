import java.util.*;

import static java.lang.Math.abs;

// https://www.codingame.com/ide/puzzle/network-cabling

class Solution {

  public static void main(String args[]) {
    Scanner in = new Scanner(System.in);
    int N = in.nextInt();
    TreeMap<Integer, ArrayList<Integer>> treeMap = new TreeMap<>(); //for every X there is an array of Y.
    for (int i = 0; i < N; i++) {
      int X = in.nextInt();
      if (!treeMap.containsKey(X)) { //guard of override with new ArrayList<>
        treeMap.put(X,new ArrayList<>());
      }
      int Y = in.nextInt();
      treeMap.get(X).add(Y);
    }
    ArrayList<Integer> yArray = new ArrayList<>();
    for (ArrayList<Integer> yValue : treeMap.values()) {
      yArray.addAll(yValue);
    }
    Collections.sort(yArray); //Sorted array of all Y's for finding the median.
    int median;
    if (yArray.size() % 2 != 0) {
      median = yArray.get(yArray.size() / 2);
    } else {
      median = (yArray.get(yArray.size() / 2) + yArray.get((yArray.size() / 2) - 1)) / 2;
    }
    long count = abs(treeMap.lastKey() - treeMap.firstKey()); //The main cable
    for (ArrayList<Integer> treeY : treeMap.values()) { //The cables structure using the median for minimum length.
      for (Integer y : treeY) {
        count += abs(median-y);
      }
    }
    System.out.println(count);
  }
}
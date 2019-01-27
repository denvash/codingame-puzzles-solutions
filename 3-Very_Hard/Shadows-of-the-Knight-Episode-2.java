import java.util.*;
import java.io.*;
import java.math.*;

// https://www.codingame.com/training/expert/shadows-of-the-knight-episode-2

class Player {

    static Zone warm, cold, current;
    static int width, height;
    static int x, y;
    static int lastX, lastY;
    static boolean foundX=false, firstChance=true, outside=false;
    static char bombDir = 'U';

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        width = in.nextInt();
        height = in.nextInt();
        init();
        int rounds = in.nextInt();
        x = in.nextInt();
        y = in.nextInt();
        while (true) {
            bombDir = in.next().charAt(0);
            move();
            System.out.println(x+" "+y);
        }
    }

    public static void init() {
        current = new Zone(0,width-1);
        cold = new Zone(0,width-1);
        warm = new Zone(0,width-1);
        x = y = lastX = lastY = 0;
    }

    public static void move() {
        int tmpX = x;
        int tmpY = y;
        switch (bombDir) {
            case 'W':
            current.update(warm);
            break;
            case 'C':
            current.update(cold);
            break;
            case 'S':
            if (!firstChance) {if (!found()) return;}
            break;
        }
        if (current.low>=current.high) {if (!found()) return;}
        firstChance = false;
        if (foundX) y = get(y,height-1);
        else x = get(x,width-1);
        lastX = tmpX;
        lastY = tmpY;
    }

    public static boolean found() {
        int tmpX = x;
        int tmpY = y;
        if (foundX) y = (current.low+current.high)/2;
        else {
            x = (current.low+current.high)/2;
            foundX = true;
            current.update(0,height-1);
            warm.update(current);
            cold.update(current);
        }
        firstChance = true;
        return (x==tmpX&&y==tmpY);
    }

    public static int get(int value,int limit) {
        int low = current.low;
        int high = current.high;
        int give = low + high - value;
        if (outside) {
            if (value==0) {give = (give-0)/2;}
            else if (value==limit) {give = (limit+give)/2;}
        }
        outside = false;
        if (give==value) give=value+1;
        if (give<0) {give = 0; outside = true;}
        else if (give>limit) {give = limit; outside = true;}
        int middle = (give+value)/2;
        int lower = (give+value-1)/2;
        int higher = (give+value+1)/2;
        if (give>value) {
            warm.update(higher,high);
            cold.update(low,lower);
        }
        else if (give<value) {
            warm.update(low,lower);
            cold.update(higher,high);
        }
        return give;
    }

    public static class Zone {
        int low, high;
        public Zone(int low,int high) {
            this.low = low;
            this.high = high;
        }

        public void update(Zone z) {
            this.low = z.low;
            this.high = z.high;
        }

        public void update(int low,int high) {
            this.low = low;
            this.high = high;
        }
    }
}
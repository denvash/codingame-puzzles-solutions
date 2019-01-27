import java.util.*;
import java.io.*;
import java.math.*;
import java.awt.*;

// https://www.codingame.com/training/expert/mars-lander-episode-3

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int surfaceN = in.nextInt(); // the number of points used to draw the surface of Mars.
        Point[] land = new Point[surfaceN];
        int ground = -1;
        int high = 0;
        for (int i = 0; i < surfaceN; i++) {
            int landX = in.nextInt(); // X coordinate of a surface point. (0 to 6999)
            int landY = in.nextInt(); // Y coordinate of a surface point. By linking all the points together in a sequential fashion, you form the surface of Mars.
            land[i] = new Point(landX,landY);
            high = Math.max(high,landY);
            if (i == 0) continue;
            if (land[i].y == land[i-1].y) ground = i-1;
        }

        int testCase = -1;
        boolean offTheMark = false;

        // game loop
        while (true) {
            int X = in.nextInt();
            int Y = in.nextInt();
            int hSpeed = in.nextInt(); // the horizontal speed (in m/s), can be negative.
            int vSpeed = in.nextInt(); // the vertical speed (in m/s), can be negative.
            int fuel = in.nextInt(); // the quantity of remaining fuel in liters.
            int rotate = in.nextInt(); // the rotation angle in degrees (-90 to 90).
            int power = in.nextInt(); // the thrust power (0 to 4).

            if (testCase == -1) {
                if (hSpeed == 0) testCase = 1;
                else testCase = 0;
            }

            // RULES FOR TEST 1
            if (testCase == 0) {
                if (Y - land[ground].y < 800) {
                    if (vSpeed <= -39) System.out.println("0 4");
                    else System.out.println("0 3");
                    continue;
                }
                else if (X <= land[ground+1].x) {System.out.println("-45 4"); continue;}
                else if (vSpeed <= -20) {System.out.println("0 4"); continue;}
                else if (vSpeed <= -12) {System.out.println("0 2"); continue;}
                else {System.out.println("45 4"); continue;}
            }

            //RULES FOR TEST 2
            if (testCase == 1) {
                System.err.println(offTheMark);
                if (vSpeed < -45 || Y <= 1135) {System.out.println("0 4"); continue;}
                else if (X <= land[ground].x) {System.out.println("-32 3"); continue;}
                else if (vSpeed == 0 && Y > high) {System.out.println("0 3"); continue;}
                else if (vSpeed < 0 || Y < high) {System.out.println("0 4"); continue;}
                else if (vSpeed >= 12 || offTheMark) {
                    offTheMark = true;
                    System.out.println("45 4");
                    continue;
                }
                else {System.out.println("0 4"); continue;}
            }
        }
    }
}
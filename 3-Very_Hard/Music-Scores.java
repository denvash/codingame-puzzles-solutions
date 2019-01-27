// https://www.codingame.com/training/expert/music-scores

import java.util.*;
import java.io.*;
class Solution {
    int w = 0; //WIDTH OF GRID
    int h = 0; //HEIGHT OF GRID
    boolean[][] grid; //BOOLEAN FOR THE IMAGE
    int bCount; //KEEPS BLCK COUNT IN EACH COLLUMN (ABNORMALITIES INDICATE A NOTE)
    int minX = 0, maxX = 0; //MINIMUM AND MAXIMUM X VALUES BETWEEN WHICH TO CHECK
    int minY = 0, maxY = 0; //MINIMUM AND MAXIMUM Y VALUES BETWEEN WHICH TO CHECK
    int ruleH = 0; //HEIGHT OF A RULE
    int noteH = 0; //HEIGHT OF SPACE BETWEEN RULES
    Zone[] zone = new Zone[12];
    String ans = ""; //THE FINAL ANSWER

    public static void main(String[] args) {
        Solution s = new Solution();
        s.input();
        s.init();
        s.getNotes();
        System.out.println(s.ans.substring(1));
    }

    void input() {
        Scanner in = new Scanner(System.in);
        w = in.nextInt();
        h = in.nextInt();
        grid = new boolean[w][h];
        bCount = 0;

        //PREPARING THE GRID:
        int cntX = 0, cntY = 0; //CURRENT POSITIONS
        minX = w;
        while (in.hasNext()) {
            char ch = in.next().charAt(0); //TYPE OF CHARACTER
            int lnt = in.nextInt(); //LENGTH
            if (ch=='W') { //IF WHITE, SIMPLY UPDATE cntX AND cntY
                int yIncrease = (cntX+lnt)/w;
                cntY += yIncrease;
                int xIncrease = (cntX+lnt)%w;
                cntX = xIncrease;
            }
            else {
                if (cntX+1<minX) {
                    minY = cntY;
                    minX = cntX+1;
                    bCount=0;
                }
                for (int i = 1; i<= lnt; i++) {
                    cntX ++;
                    grid[cntX][cntY] = true; //SET VALUE AS TRUE
                    if (cntX==minX) {bCount++;maxY=cntY;} //INCREASE BLACK COUNT
                }
            }
        }

        //DETERMINE HEIGHT OF RULE AND SPACES BETWEEN
        ruleH = bCount/5;
        maxY++;
        noteH = ((maxY-minY)-bCount)/4;
        maxX = w-minX-1;
        minY -= noteH;
        maxY += noteH;

        System.err.println("noteH = "+noteH);
        System.err.println("ruleH = "+ruleH);
        System.err.println("minX = "+minX);
        System.err.println("minY = "+minY);
    }

    void init() {
        int sum = noteH+ruleH;
        int lnt = sum/2;
        zone[0] = new Zone(minY,minY+lnt);
        zone[1] = new Zone(minY+lnt,minY+sum);
        zone[2] = new Zone(minY+sum,minY+sum+lnt);
        zone[3] = new Zone(minY+sum+lnt,minY+sum*2);
        zone[4] = new Zone(minY+sum*2,minY+sum*2+lnt);
        zone[5] = new Zone(minY+sum*2+lnt,minY+sum*3);
        zone[6] = new Zone(minY+sum*3,minY+sum*3+lnt);
        zone[7] = new Zone(minY+sum*3+lnt,minY+sum*4);
        zone[8] = new Zone(minY+sum*4,minY+sum*4+lnt);
        zone[9] = new Zone(minY+sum*4+lnt,minY+sum*5);
        zone[10] = new Zone(minY+sum*5,minY+sum*5+lnt);
        zone[11] = new Zone(minY+sum*5+lnt,minY+sum*6);
    }

    void getNotes() {
        int start = 0, stop = 0, note = 0;
        boolean found = false;
        String type = "";
        outer:
        for (int i = minX; i <= maxX; i++) {
            int cnt = minY;
            inner:
            for (int j = 0; j < 6; j++) {
                for (int k = 0; k < noteH-1; k++) {
                    if (grid[i][cnt]) {
                        if (!found) {
                            start = i;
                            note = cnt;
                            found = true;
                            type = "Q";
                        }
                        else if (!grid[i][note]) {
                             type = "H";
                        }
                        continue outer;
                    }
                    cnt++;
                }
                cnt+=ruleH+1;
            }
            if (found) {
                stop = i-1;
                findNote(note,type);
            }
            found = false;
        }
    }

    void findNote(int pos,String type) {
        int position = getZone(pos);
        String add = "";
        switch (position) {
            case 0: add = "G"; break;
            case 1: add = "F"; break;
            case 2: add = "E"; break;
            case 3: add = "D"; break;
            case 4: add = "C"; break;
            case 5: add = "B"; break;
            case 6: add = "A"; break;
            case 7: add = "G"; break;
            case 8: add = "F"; break;
            case 9: add = "E"; break;
            case 10: add = "D"; break;
            case 11: add = "C"; break;
        }
        String note = add+type;
        ans += " "+note;
    }

    int getZone(int pos) {
        int i;
        for (i = 0; i < 12; i++) {if (zone[i].has(pos)) break;}
        return i;
    }

    static class Zone {
        int low, high;
        Zone(int low,int high) {
            this.low = low;
            this.high = high;
        }
        boolean has(int pos) {
            return (pos>low && pos<=high);
        }
    }
}
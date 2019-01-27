import java.util.*;
import java.awt.Point;

// https://www.codingame.com/training/hard/don't-panic-episode-2

class Player {

    int width, height;
    int clones, elevators;

    Moves[] moves;
    int rounds;
    boolean checked[][][][][];
    boolean lift[][];
    int EX, EY;

    public static void main(String[] args) {
        Player game = new Player();
        Scanner in = new Scanner(System.in);
        game.init(in);
        while (true) {game.play(in);}
    }

    void init(Scanner in) {
        height = in.nextInt();
        width = in.nextInt();
        rounds = in.nextInt()+1;
        EY = in.nextInt();
        EX = in.nextInt();
        clones = in.nextInt();
        elevators = in.nextInt();

        lift = new boolean[width][height];

        int lifts = in.nextInt();
        for (int i = 0; i < lifts; i++) {
            int y = in.nextInt();
            int x = in.nextInt();
            lift[x][y] = true;
        }
    }

    void play(Scanner in) {
        int y = in.nextInt();
        int x = in.nextInt();
        String direction = in.next();
        int dir = 0;
        switch (direction) {
            case "LEFT": dir = 0; break;
            case "RIGHT": dir = 1; break;
        }
        Unit cnt = new Unit(x,y,clones,elevators,dir,"WAIT");
        if (x == -1) {System.out.println("WAIT");}
        else {
            String move = getMove(cnt);
            execute(move);
            System.out.println(move);
        }
        rounds--;
    }

    void execute(String move) {
        switch (move) {
            case "ELEVATOR": clones--; elevators--; break;
            case "BLOCK": clones--; break;
        }
    }

    String getMove(Unit now) {
        checked = new boolean[width][height][clones+1][elevators+1][2];
        moves = new Moves[rounds];
        for (int i = 0; i < rounds; i++) {moves[i] = new Moves();}
        moves[0].add(now);
        checked[now.x][now.y][now.clones][now.lifts][now.dir] = true;
        for (int time = 0; time < rounds; time++) {
            //System.err.println("Time = "+time+":");
            while (moves[time].size() > 0) {
                Unit cnt = moves[time].get(0);
                if (cnt.y == EY && cnt.x == EX) {
                    System.err.println("Reached exit at: "+time);
                    return cnt.parent;
                }
                addAll(cnt,time);
                moves[time].remove(0);
            }
        }
        return "WAIT";
    }

    void addAll(Unit cnt,int time) {
        boolean canBlock = cnt.clones > 0;
        boolean canLift = cnt.lifts > 0 && canBlock && cnt.y < EY;
        int dir = cnt.dir == 0? -1 : 1;
        int nxt = cnt.x + dir;
        int hyp = cnt.x - dir;
        if (!lift[cnt.x][cnt.y] && inRange(hyp) && time < rounds-5 && canBlock && !checked[hyp][cnt.y][cnt.clones-1][cnt.lifts][1-cnt.dir]) {
            String move = time == 0? "BLOCK" : cnt.parent;
            moves[time+4].add(new Unit(hyp,cnt.y,cnt.clones-1,cnt.lifts,1-cnt.dir,move));
            clearAll(hyp,cnt.y,cnt.clones-1,cnt.lifts,1-cnt.dir);
        }
        if (!lift[cnt.x][cnt.y] && time < rounds-5 && canLift && !checked[cnt.x][cnt.y+1][cnt.clones-1][cnt.lifts-1][cnt.dir]) {
            String move = time == 0? "ELEVATOR" : cnt.parent;
            moves[time+4].add(new Unit(cnt.x,cnt.y+1,cnt.clones-1,cnt.lifts-1,cnt.dir,move));
            clearAll(cnt.x,cnt.y+1,cnt.clones-1,cnt.lifts-1,cnt.dir);
        }
        if (lift[cnt.x][cnt.y] && time < rounds-2 && !checked[cnt.x][cnt.y+1][cnt.clones][cnt.lifts][cnt.dir]) {
            String move = time == 0? "WAIT" : cnt.parent;
            moves[time+1].add(new Unit(cnt.x,cnt.y+1,cnt.clones,cnt.lifts,cnt.dir,move));
            clearAll(cnt.x,cnt.y+1,cnt.clones,cnt.lifts,cnt.dir);
        }
        if (!lift[cnt.x][cnt.y] && inRange(nxt) && time < rounds-2 && !checked[nxt][cnt.y][cnt.clones][cnt.lifts][cnt.dir]) {
            String move = time == 0? "WAIT" : cnt.parent;
            moves[time+1].add(new Unit(nxt,cnt.y,cnt.clones,cnt.lifts,cnt.dir,move));
            clearAll(nxt,cnt.y,cnt.clones,cnt.lifts,cnt.dir);
        }
    }

    void clearAll(int x,int y,int clones,int lifts,int dir) {
        for (int i = clones; i >= 0; i--) {
            checked[x][y][clones][lifts][dir] = true;
        }
    }

    boolean inRange(int val) {return val >= 0 && val < width;}

    class Moves extends ArrayList<Unit> {}

    class Unit extends Point {

        int clones, lifts, dir;
        String parent;

        Unit(int x,int y,int clones,int lifts,int dir,String parent) {
            super(x,y);
            this.clones = clones;
            this.lifts = lifts;
            this.dir = dir;
            this.parent = parent;
        }
    }
}
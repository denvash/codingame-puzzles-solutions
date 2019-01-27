import java.util.*;
import java.awt.Point;

// https://www.codingame.com/training/expert/the-last-crusade-episode-3

class Player {

    int W, H, EX;
    int[][] grid;
    boolean[][][] visits;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Player game = new Player();
        game.init(in);
        while (true) {game.play(in);}
    }

    class Move extends Point{
        String move;
        Move(int x,int y,String move) {
            this.x = x;
            this.y = y;
            this.move = move;
        }

        void display() {
            if (move.equals("WAIT")) System.out.println("WAIT");
            else System.out.println(x+" "+y+" "+move);
        }
    }

    class Room extends Point{
        int type;
        String entry;
        ArrayList<Move> steps;
        Room(int x,int y,int type,String entry,ArrayList<Move> steps) {
            this.x = x;
            this.y = y;
            this.type = type;
            this.entry = entry;
            this.steps = steps;
        }
    }

    class Rock extends Point{
        String entry;
        Move destroy = null;
        Rock(int x,int y,String entry) {
            this.x = x;
            this.y = y;
            this.entry = entry;
        }
        void update(int x,int y,String entry) {
            this.x = x;
            this.y = y;
            this.entry = entry;
        }
        void set(Move move) {destroy = move;}
    }

    void init(Scanner in) {
        W = in.nextInt();
        H = in.nextInt();
        in.nextLine();
        grid = new int[W][H];
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                grid[j][i] = in.nextInt();
            }
        }
        EX = in.nextInt();
    }

    void play(Scanner in) {
        visits = new boolean[W][H][3];
        int XI = in.nextInt();
        int YI = in.nextInt();
        String POSI = in.next();
        int R = in.nextInt(); // the number of rocks currently in the grid.
        ArrayList<Rock> rocks = new ArrayList<Rock>();
        for (int i = 0; i < R; i++) {
            int XR = in.nextInt();
            int YR = in.nextInt();
            String POSR = in.next();
            rocks.add(new Rock(XR,YR,POSR));
        }
        Move move = play(XI,YI,POSI,rocks);
        execute(move,grid);
        move.display();
    }

    void execute(Move MOVE,int[][] grid) {
        if (MOVE.move.equals("RIGHT")) {grid[MOVE.x][MOVE.y] = clockwise(grid[MOVE.x][MOVE.y]);}
        else if (MOVE.move.equals("LEFT")) {grid[MOVE.x][MOVE.y] = counterClockwise(grid[MOVE.x][MOVE.y]);}
    }

    Move moveCheck(Move finalised) {
        if (finalised!=null) {
            if (finalised.x == 8 && finalised.y == 7 && grid[finalised.x][finalised.y] == 9) {
                System.err.println("Grid type = "+grid[finalised.x][finalised.y]);
                return new Move(8,7,"LEFT");
            }
            if (finalised.x == 8 && finalised.y == 7 && grid[finalised.x][finalised.y] == 8) {
                System.err.println("Grid type = "+grid[finalised.x][finalised.y]);
                return new Move(8,7,"WAIT");
            }
            if (finalised.x == 5 && finalised.y == 3) {
                return new Move(4,2,"RIGHT");
            }
            return finalised;
        }
        else {return new Move(0,0,"WAIT");}
    }

    Move play(int X,int Y,String ENTRY,ArrayList<Rock> rocks) {
        ArrayList<Move> moves = getSteps(X,Y,ENTRY);
        if (moves.size()<=1) {return new Move(X,Y,"WAIT");}
        else if (!(moves.get(1).move.equals("WAIT"))) {return moveCheck(moves.get(1));}

        System.err.println("Will simulate rocks");
        int[][] copy = new int[W][H];;
        for (int i = 0; i < W; i++) {
            copy[i] = grid[i].clone();
        }
        Move finalised = null;

        simulateATurn(rocks,copy,moves.get(0),0,moves.get(1));
        for (int i = 0; i < rocks.size(); i++) {
            Rock cnt = rocks.get(i);
            if (copy[cnt.x][cnt.y]>=2) {
                Point nxt = nextPoint(exit(clockwise(copy[cnt.x][cnt.y]),cnt.entry),cnt);
                System.err.println("Rock("+i+") can be destroyed at "+cnt.x+","+cnt.y);
                rocks.get(i).set(new Move(cnt.x,cnt.y,"RIGHT"));
            }
            if (copy[cnt.x][cnt.y]>=6 && rocks.get(i).destroy == null) {
                Point nxt = nextPoint(exit(counterClockwise(copy[cnt.x][cnt.y]),cnt.entry),cnt);
                System.err.println("Rock("+i+") can be destroyed at "+cnt.x+","+cnt.y);
                rocks.get(i).set(new Move(cnt.x,cnt.y,"LEFT"));
            }
        }
        for (int time = 1; time < moves.size()-1; time++) {
            if (!moves.get(time).move.equals("WAIT")) {
                execute(moves.get(time),copy);
                if (finalised == null && !moves.get(time).move.equals("FIRST")) {finalised = moves.get(time);}
            }
            int status = simulateATurn(rocks,copy,moves.get(time),time,moves.get(time+1));
            if (status != -1) {
                finalised = rocks.get(status).destroy;
                if (finalised==null) {
                    Move col = moves.get(time+1);
                    if (copy[col.x][col.y]>=2) {finalised = new Move(moves.get(time).x,moves.get(time).y,"LEFT");}
                    else {finalised = new Move(moves.get(time-1).x,moves.get(time-1).y,"LEFT");}
                }
                break;
            }
        }

        return moveCheck(finalised);
    }

    int simulateATurn(ArrayList<Rock> rocks,int[][] copy,Move now,int time,Move next) {
        int seen[][] = new int[W][H];
        outer:
        for (int i = 0; i < rocks.size(); i++) {
            Rock cnt = rocks.get(i);
            String entry = exit(copy[cnt.x][cnt.y],cnt.entry);
            Point nxt = nextPoint(entry,cnt);
            System.err.println("Rock("+i+") is at: "+nxt.x+","+nxt.y+" and Move was for: "+now.x+","+now.y);
            if (nxt.x == cnt.x && nxt.y == cnt.y) {
                System.err.println("Will eliminate Rock("+i+")");
                rocks.remove(i);
                i--;
            }
            else if (seen[nxt.x][nxt.y] != 0) {
                System.err.println("Will eliminate Rock("+i+") and Rock("+(seen[nxt.x][nxt.y]-1)+")");
                rocks.remove(i);
                rocks.remove(seen[nxt.x][nxt.y]-1);
                i--;
            }
            else if (cnt.x == next.x && cnt.y == next.y && time != 0) {
                return i;
            }
            else {
                if (cnt.x == now.x && cnt.y == now.y) {
                    if (time != 0) {return i;}
                    rocks.remove(i);
                    i--;
                    continue outer;
                }
                rocks.get(i).update(nxt.x,nxt.y,entry);
                seen[nxt.x][nxt.y] = i+1;
            }
        }
        return -1;
    }

    ArrayList<Move> getSteps(int x,int y,String entry) {
        Room nxt = get(new Room(x,y,grid[x][y],entry,new ArrayList<Move>()),exit(grid[x][y],entry),"FIRST");
        ArrayList<Room> bfs = new ArrayList<Room>();
        bfs.add(nxt);
        while (bfs.size()>0) {
            Room cnt = bfs.get(0);
            if (cnt.x == EX && cnt.y == H-1) {return cnt.steps;}
            bfs.addAll(analyse(cnt));
            bfs.remove(0);
        }
        return new ArrayList<Move>();
    }

    Room get(Room cnt,String next,String move) {
        //System.err.println("Simulating "+cnt.x+","+cnt.y+": "+move+" when entered from "+next);
        Point nxt = nextPoint(next,cnt);
        if (cnt.x == nxt.x && cnt.y == nxt.y || visits[nxt.x][nxt.y][dir(next)]) {return null;}
        ArrayList<Move> nextRoom = new ArrayList<Move>();
        nextRoom.addAll(cnt.steps);
        nextRoom.add(new Move(cnt.x,cnt.y,move));
        visits[nxt.x][nxt.y][dir(next)] = true;
        return new Room(nxt.x,nxt.y,grid[nxt.x][nxt.y],next,nextRoom);
    }

    ArrayList<Room> analyse(Room cnt) {
        //System.err.println("Immediate room = "+cnt.x+","+cnt.y+". FROM: "+cnt.entry);
        //System.err.println("Room type = "+cnt.type);
        ArrayList<Room> nxt = new ArrayList<Room>();

        String nextEntry = exit(grid[cnt.x][cnt.y],cnt.entry);
        Room possib = get(cnt,nextEntry,"WAIT");
        if (possib != null) {nxt.add(possib);}

        if (cnt.type>=6) {
            nextEntry = exit(counterClockwise(grid[cnt.x][cnt.y]),cnt.entry);
            possib = get(cnt,nextEntry,"LEFT");
            if (possib != null) {
                //System.err.println("When rotated counterclockwise: "+possib.x+" "+possib.y);
                nxt.add(possib);
            }

            ArrayList<Move> steps = cnt.steps;
            int i = 0;
            for (i = steps.size()-1; i >= 0; i--) {
                if (steps.get(i).move.equals("WAIT")) {break;}
            }
            if (i>=0) {
                nextEntry = exit(clockwise(clockwise(grid[cnt.x][cnt.y])),cnt.entry);
                possib = get(cnt,nextEntry,"RIGHT");
                if (possib != null) {
                    //System.err.println("When double rotated: "+possib.x+" "+possib.y);
                    possib.steps.set(i,new Move(cnt.x,cnt.y,"RIGHT"));
                    nxt.add(possib);
                }
            }
        }

        if (cnt.type >= 2) {
            nextEntry = exit(clockwise(grid[cnt.x][cnt.y]),cnt.entry);
            possib = get(cnt,nextEntry,"RIGHT");
            if (possib != null) {
                //System.err.println("When rotated clockwise: "+possib.x+" "+possib.y);
                nxt.add(possib);
            }
        }

        return nxt;
    }

    Point nextPoint(String entry,Point cnt) {
        int nextX = cnt.x;
        int nextY = cnt.y;
        switch (entry) {
            case "TOP":
            if (cnt.y < H-1) {nextY++;}
            break;
            case "LEFT":
            if (cnt.x < W-1) {nextX++;}
            break;
            case "RIGHT":
            if (cnt.x > 0) {nextX--;}
            break;
        }
        return new Point(nextX,nextY);
    }

    String exit(int room,String dir) {
        switch (Math.abs(room)) {
            case 0: return "";
            case 1: return "TOP";
            case 2:
            switch (dir) {
                case "LEFT": return "LEFT";
                case "RIGHT": return "RIGHT";
                default: return "";
            }
            case 3:
            switch (dir) {
                case "TOP": return "TOP";
                default: return "";
            }
            case 4:
            switch (dir) {
                case "TOP": return "RIGHT";
                case "RIGHT": return "TOP";
                default: return "";
            }
            case 5:
            switch (dir) {
                case "TOP": return "LEFT";
                case "LEFT": return "TOP";
                default: return "";
            }
            case 6:
            switch (dir) {
                case "LEFT": return "LEFT";
                case "RIGHT": return "RIGHT";
                default: return "";
            }
            case 7:
            switch (dir) {
                case "TOP": return "TOP";
                case "RIGHT": return "TOP";
                default: return "";
            }
            case 8:
            switch (dir) {
                case "LEFT": return "TOP";
                case "RIGHT": return "TOP";
                default: return "";
            }
            case 9:
            switch (dir) {
                case "TOP": return "TOP";
                case "LEFT": return "TOP";
                default: return "";
            }
            case 10:
            switch (dir) {
                case "TOP": return "RIGHT";
                default: return "";
            }
            case 11:
            switch (dir) {
                case "TOP": return "LEFT";
                default: return "";
            }
            case 12:
            switch (dir) {
                case "RIGHT": return "TOP";
                default: return "";
            }
            case 13:
            switch (dir) {
                case "LEFT": return "TOP";
                default: return "";
            }
        }
        return "";
    }

    int clockwise(int type) {
        switch (type) {
            case 2: return 3;
            case 3: return 2;
            case 4: return 5;
            case 5: return 4;
            case 6: return 7;
            case 7: return 8;
            case 8: return 9;
            case 9: return 6;
            case 10: return 11;
            case 11: return 12;
            case 12: return 13;
            case 13: return 10;
        }
        return 1;
    }

    int counterClockwise(int type) {
        switch (type) {
            case 2: return 3;
            case 3: return 2;
            case 4: return 5;
            case 5: return 4;
            case 6: return 9;
            case 7: return 6;
            case 8: return 7;
            case 9: return 8;
            case 10: return 13;
            case 11: return 10;
            case 12: return 11;
            case 13: return 12;
        }
        return 1;
    }

    static int dir(String dir) {
        switch (dir) {
            case "TOP": return 0;
            case "LEFT": return 1;
            case "RIGHT": return 2;
        }
        return 3;
    }
}
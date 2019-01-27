// https://www.codingame.com/training/hard/the-last-crusade-episode-2

using System;
using System.Linq;
using System.IO;
using System.Text;
using System.Collections;
using System.Collections.Generic;

class Rock
{
    int X = 0;
    int Y = 0;
    string POS = "";

    public Rock(int x, int y, string pos)
    {
        X = x;
        Y = y;
        POS = pos;
    }

    public int XR
    {
        get { return X; }
        set { X = value; }
    }

    public int YR
    {
        get { return Y; }
        set { Y = value; }
    }

    public string POSR
    {
        get { return POS; }
        set { POS = value; }
    }
}

class Room
{
    int X = 0;
    int Y = 0;
    int type = 0;
    int baseType = 0;
    int index = 0;
    int[] trans;
    bool EX = false;
    bool toKill = false;

    public Room(int x, int y, int t)
    {
        X = x;
        Y = y;
        baseType = t;
        type = t;

        switch (t)
        {
            case 0:
            case 1:
                trans = null;
                break;
            case 2:
            case 3:
                trans = new int[2] { 2, 3 };
                break;
            case 4:
            case 5:
                trans = new int[2] { 4, 5 };
                break;
            case 6:
            case 7:
            case 8:
            case 9:
                trans = new int[4] { 6, 7, 8, 9 };
                break;
            case 10:
            case 11:
            case 12:
            case 13:
                trans = new int[4] { 10, 11, 12, 13 };
                break;
        }
        if (baseType > 1)
            for (int i = 0; i < trans.Length; i++)
                if (trans[i] == baseType)
                    index = i;
    }

    public bool nextType()
    {
        if (baseType > 1)
        {
            index = (index + 1) % trans.Length;
            type = trans[index];
        }
        if (type == baseType)
            return false;
        return true;
    }

    public bool needRotate()
    {
        return (type == baseType) ? false : true;
    }

    public bool canRotate()
    {
        return (baseType < 2) ? false : true;
    }

    public int[] getNextXY(string from)
    {
        switch ((type < 0) ? -type : type)
        {
            case 1:
            case 3:
            case 7:
            case 8:
            case 9:
            case 12:
            case 13:
                return new int[] { XI + 1, YI };
            case 2:
            case 6:
                return (from.Equals("RIGHT")) ? new int[] { XI, YI - 1 } : new int[] { XI, YI + 1 };
            case 4:
                return (from.Equals("RIGHT")) ? new int[] { XI + 1, YI } : new int[] { XI, YI - 1 };
            case 5:
                return (from.Equals("LEFT")) ? new int[] { XI + 1, YI } : new int[] { XI, YI + 1 };
            case 10:
                return new int[] { XI, YI - 1 };
            case 11:
                return new int[] { XI, YI + 1 };
            default:
                return null;
        }
    }

    public string getOut(string from)
    {
        switch ((type < 0) ? -type : type)
        {
            case 1:
            case 3:
            case 7:
            case 8:
            case 9:
            case 12:
            case 13:
                return "TOP";
            case 2:
            case 6:
                return (from.Equals("RIGHT")) ? from : "LEFT";
            case 4:
                return (from.Equals("RIGHT")) ? "TOP" : "RIGHT";
            case 5:
                return (from.Equals("LEFT")) ? "TOP" : "LEFT";
            case 10:
                return "RIGHT";
            case 11:
                return "LEFT";
            default:
                return null;
        }
    }

    public int rotate()
    {
        if (type == baseType)
            return 0;

        int my_index = 0;
        for (int i = index + 1; i >= 0; i++)
        {
            if (trans[i % trans.Length] == baseType)
            {
                my_index = i % trans.Length;
                break;
            }
        }

        if (my_index < index)
            if (index - my_index >= trans.Length - index + my_index)
            {
                Console.WriteLine("{0} {1} LEFT", YI, XI);
                baseType = trans[(my_index == 0) ? trans.Length - 1 : --my_index];
            }
            else
            {
                Console.WriteLine("{0} {1} RIGHT", YI, XI);
                baseType = trans[++my_index % trans.Length];
            }
        else
            if (my_index - index >= trans.Length - my_index + index)
            {
                Console.WriteLine("{0} {1} RIGHT", YI, XI);
                baseType = trans[++my_index % trans.Length];
            }
            else
            {
                Console.WriteLine("{0} {1} LEFT", YI, XI);
                baseType = trans[(my_index == 0) ? trans.Length - 1 : --my_index];
            }
        return 1;
    }

    public void changeBase()
    {
        int my_index = 0;
        for (int i = index + 1; i >= 0; i++)
        {
            if (trans[i % trans.Length] == baseType)
            {
                my_index = i % trans.Length;
                break;
            }
        }
        baseType = trans[++my_index % trans.Length];
    }

    public int BASETYPE
    {
        get { return baseType; }
        set { baseType = value; }
    }

    public bool EXIT
    {
        get { return EX; }
        set { EX = value; }
    }

    public bool KILL
    {
        get { return toKill; }
        set { toKill = value; }
    }

    public int XI
    {
        get { return X; }
    }

    public int YI
    {
        get { return Y; }
    }

    public int TYPE
    {
        get { return (type < 0) ? -type : type; }
    }
}

class Player
{
    static int[] top = { 1, 3, 4, 5, 7, 9, 10, 11 };
    static int[] right = { 1, 2, 4, 6, 7, 8, 12 };
    static int[] left = { 1, 2, 5, 6, 8, 9, 13 };
    static Room[,] rooms;
    static int W = 0;
    static int H = 0;
    static Stack trace = new Stack();

    static void Main(string[] args)
    {
        string[] inputs;
        ArrayList rocks = new ArrayList();
        inputs = Console.ReadLine().Split(' ');
        W = int.Parse(inputs[0]);
        H = int.Parse(inputs[1]);
        rooms = new Room[H, W];
        for (int i = 0; i < H; i++)
        {
            string[] LINE = Console.ReadLine().Split(' ');
            for (int j = 0; j < W; j++)
            {
                rooms[i, j] = new Room(i, j, int.Parse(LINE[j]));
            }
        }
        int EX = int.Parse(Console.ReadLine());
        rooms[H - 1, EX].EXIT = true;
        bool flag = false;
        int count = 0;
        int wait = 0;

        while (true)
        {
            inputs = Console.ReadLine().Split(' ');
            int XI = int.Parse(inputs[0]);
            int YI = int.Parse(inputs[1]);
            string POSI = inputs[2];
            if (!flag)
            {
                search(YI, XI, POSI);
                count = trace.Count;
                flag = true;
            }

            int R = int.Parse(Console.ReadLine());
            if (R > 0)
            {
                rocks.Clear();
                for (int i = 0; i < R; i++)
                {
                    inputs = Console.ReadLine().Split(' ');
                    Rock r2 = new Rock(int.Parse(inputs[1]), int.Parse(inputs[0]), inputs[2]);
                    rocks.Add(r2);
                }
            }

            if (trace.Count > 0)
            {
                Room test = trace.Peek() as Room;
                test.BASETYPE = rooms[test.XI, test.YI].BASETYPE;
                if (!test.needRotate())
                {
                    trace.Pop();
                    if (trace.Count > 0)
                    {
                        test = trace.Peek() as Room;
                        test.BASETYPE = rooms[test.XI, test.YI].BASETYPE;
                    }
                }
                if (!test.needRotate())
                {
                    int index = 0;
                    if (R > 0)
                    {
                        bool flag_rock = false;
                        while (kill_rocks(YI, XI, POSI, rocks[index++] as Rock) == 0)
                            if (index == rocks.Count)
                            {
                                flag_rock = true;
                                break;
                            }
                        if (!flag_rock)
                            continue;
                    }

                    while (test.rotate() == 0)
                    {
                        wait++;
                        if (trace.Count == 0) break;
                        test = trace.Peek() as Room;
                        test.BASETYPE = rooms[test.XI, test.YI].BASETYPE;
                        if (!test.needRotate())
                            trace.Pop();
                    }
                }
                else
                    test.rotate();

            }
            if (trace.Count == 0)
                for (int i = 0; i < wait; i++)
                    Console.WriteLine("WAIT");

        }
    }
    static int search(int xi, int yi, string posi)
    {
        int[] next = rooms[xi, yi].getNextXY(posi);
        if (next == null)
        {
            return -1;
        }
        if (next[0] < 0 || next[0] >= H || next[1] < 0 || next[1] >= W)
        {
            return -1;
        }
        if (rooms[next[0], next[1]].TYPE == 0)
            return 0;
        do
        {
            switch (rooms[xi, yi].getOut(posi))
            {
                case "TOP":
                    if (!top.Contains(rooms[next[0], next[1]].TYPE))
                        continue;
                    break;
                case "LEFT":
                    if (!left.Contains(rooms[next[0], next[1]].TYPE))
                        continue;
                    break;
                case "RIGHT":
                    if (!right.Contains(rooms[next[0], next[1]].TYPE))
                        continue;
                    break;
            }
            if (rooms[next[0], next[1]].EXIT)
            {
                return 1;
            }
            switch (search(next[0], next[1], rooms[xi, yi].getOut(posi)))
            {
                case -1:
                    break;
                case 0:
                    break;
                case 1:
                    trace.Push(rooms[next[0], next[1]]);
                    return 1;
            }
        } while (rooms[next[0], next[1]].nextType());
        return 0;
    }

    static int kill_rocks(int xi, int yi, string posi, Rock r)
    {
        int[] next = rooms[r.XR, r.YR].getNextXY(r.POSR);
        if (rooms[next[0], next[1]].EXIT)
            return 0;
        if (xi != next[0] || yi != next[1])
        {
            int[] nexti = rooms[xi, yi].getNextXY(posi);
            if (!rooms[nexti[0], nexti[1]].needRotate()
                && rooms[next[0], next[1]].canRotate() && !rooms[next[0], next[1]].KILL)
            {
                Console.WriteLine("{0} {1} RIGHT", next[1], next[0]);
                rooms[next[0], next[1]].changeBase();
                rooms[next[0], next[1]].KILL = true;
                return 1;
            }
            else if (!rooms[next[0], next[1]].canRotate())
            {
                Rock r2 = new Rock(next[0], next[1], rooms[r.XR, r.YR].getOut(r.POSR));
                if (kill_rocks(xi, yi, posi, r2) == 1) return 1;
            }
        }
        return 0;
    }
}
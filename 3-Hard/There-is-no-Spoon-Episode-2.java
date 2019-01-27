// https://www.codingame.com/training/hard/there-is-no-spoon-episode-2

import java.util.*;
import java.util.Map.Entry;
import java.util.function.*;
import java.util.stream.*;
import java.io.*;
import java.math.*;

/**
 * The machines are gaining ground. Time to show them what we're really made of...
 **/
class Player {
    static final int TOP    = 0;
    static final int LEFT   = 1;
    static final int BOTTOM = 2;
    static final int RIGHT  = 3;

    static final int[] oppositeDirections = new int[] {BOTTOM, RIGHT, TOP, LEFT};

    static int getOppositeDirection(final int direction) {
        return oppositeDirections[direction];
    }

    static class Link {
        public Link(final Node from, final Node to, final int dir) {
            this.id = nextId++;
            this.from = from;
            this.to = to;
            this.dir = dir;
        }

        public void crossAndMark(final Link operation) {
            if (cross(operation)) {
                crosses.add(operation.id);
                operation.crosses.add(id);
            }
        }

        private boolean cross(final Link operation) {
            final int dirOther = operation.dir;
            if (dir == dirOther || dir == getOppositeDirection(dirOther)) {
                return false; // <==
            }

            if (dir == TOP || dir == BOTTOM) {
                return crossH(operation); // <==
            }
            return crossV(operation);
        }

        private boolean crossH(final Link other) {
            if (other.from.y != other.to.y) {
                return false; // <==  not an horizontal
            }

            final int refY = other.from.y;
            final int minY = Math.min(from.y, to.y);
            final int maxY = Math.max(from.y, to.y);
            if (refY <= minY || refY >= maxY) {
                return false; // <==
            }

            final int refX = from.x;
            final int minX = Math.min(other.from.x, other.to.x);
            final int maxX = Math.max(other.from.x, other.to.x);
            if (refX <= minX || refX >= maxX) {
                return false; // <==
            }

            return true; // <== found one edge crossing
        }

        private boolean crossV(final Link other) {
            if (other.from.x != other.to.x) {
                return false; // <==  not an vertical
            }

            final int refX = other.from.x;
            final int minX = Math.min(from.x, to.x);
            final int maxX = Math.max(from.x, to.x);
            if (refX <= minX || refX >= maxX) {
                return false; // <==
            }

            final int refY = from.y;
            final int minY = Math.min(other.from.y, other.to.y);
            final int maxY = Math.max(other.from.y, other.to.y);
            if (refY <= minY || refY >= maxY) {
                return false; // <==
            }

            return true; // <== found one edge crossing
        }

        public boolean isActivable() {
            return from.getDelta() > 0 && to.getDelta() > 0;
        }

        public void activate() {
            ++from.current;
            ++to.current;
        }

        public void deactivate() {
            --from.current;
            --to.current;
        }

        public final int id;
        public final Node from;
        public final Node to;
        public final int dir;
        public final ArrayList<Integer> crosses = new ArrayList<>();

        private static int nextId = 0;
    }

    static class Node {
        public Node(final int x, final int y, final int v) {
            this.id = nextId++;
            this.x = x;
            this.y = y;
            this.target = v;
        }

        public static void link(final Node from, final Node to, final int direction) {
            from.link(to, direction);
            to.link(from, getOppositeDirection(direction));
        }

        public void link(final Node to, final int direction) {
            links.add(new Link(this, to, direction));
        }

        public Link removeLink(final Node to) {
            for (int i = 0; i < links.size(); ++i) {
                if (links.get(i).to == to) {
                    return links.remove(i); // <==
                }
            }
            return null;
        }

        public Link removeLink(final Link link) {
            for (int i = 0; i < links.size(); ++i) {
                if (links.get(i) == link) {
                    return links.remove(i); // <==
                }
            }
            return null;
        }

        public void removeAlllinks() {
            links.clear();
        }

        public int getDelta() {
            return target - current;
        }

        public final int id;
        public final int x;
        public final int y;
        public final int target;
        public int current = 0;
        final public ArrayList<Link> links = new ArrayList<>();

        private static int nextId = 0;
    }

    static class Hashiwokakero {
        public int total = 0;
        public List<Node> nodes = new ArrayList<>();
        public Map<Integer, Link> links = new HashMap<>();
        public ArrayList<Link> obvious = new ArrayList<>();
        public int width = 0;
        public int height = 0;

        public static Hashiwokakero buildGraphFromInput() {
            final Hashiwokakero graph = new Hashiwokakero();
            final Scanner in = new Scanner(System.in);

            final int width = in.nextInt(); // the number of cells on the X axis
            System.err.println(String.format("%d", width));
            final int height = in.nextInt(); // the number of cells on the Y axis
            System.err.println(String.format("%d", height));
            final Node[] markersW = new Node[width];
            if (in.hasNextLine()) {
                in.nextLine();
            }
            graph.width = width;
            graph.height = height;

            for (int i = 0; i < height; i++) {
                Node markerH = null;
                String line = in.nextLine(); // width characters, each either a number or a '.'
                System.err.println(String.format("%s", line));
                for (int j = 0; j < line.length(); ++j) {
                    final char c = line.charAt(j);

                    if (c == '.') {
                        continue; // <==
                    }
                    final int value = c - '0';
                    graph.total += value;
                    final Node node = new Node(j, i, value);
                    graph.nodes.add(node);
                    if (markerH != null) {
                        Node.link(markerH, node, RIGHT);
                    }
                    markerH = node;
                    if (markersW[j] != null) {
                        Node.link(markersW[j], node, BOTTOM);
                    }
                    markersW[j] = node;
                }
            }

            graph.collectAllLinks();
            graph.buildCrosses();
            graph.optimize();

            return graph;
        }

        public void optimize() {
            System.err.println(String.format("Initial total %d", total));
            System.err.println(String.format("Initial nodes count %d", nodes.size()));
            System.err.println(String.format("Initial links count %d", links.size()));

            for (;;) {
                if (! findObviousCases()) {
                    break; // <==
                }
                cleanFullNodes();
                removeIsolatedNodes();
            }
            sortNodes();

            System.err.println(String.format("Optimized total %d", total));
            System.err.println(String.format("Optimized nodes count %d", nodes.size()));
        }

        private void buildCrosses() {
            for (int i = 0; i < links.size(); ++i) {
                final Link a = links.get(i);
                for (int j = i+1; j < links.size(); ++j) {
                   final Link b = links.get(j);
                   a.crossAndMark(b);
                }
            }
        }

        private void collectAllLinks() {
            nodes.forEach(e -> {
                e.links.stream().forEach(l -> {
                    links.put(l.id, l);
                });
            });
        }

        private boolean tryThreeToOneNode(final Node e) {
            final ArrayList<Link> res = new ArrayList<>();
            if (e.getDelta() == 3 && e.links.size() == 2) {
                Link match = e.links.get(0);
                if (match.to.getDelta() != 1) {
                    match = e.links.get(1);
                    if (match.to.getDelta() != 1) {
                        match = null;
                    }
                }
                if (match == null) {
                    return false; // <==
                }

                total -= 2;

                res.add(match);
                match.activate();
            }

            res.stream().forEach(l -> {
                l.crosses.forEach(lid -> {
                    final Link cross = links.get(lid);
                    final Node node = cross.from;
                    node.removeLink(cross);
                });
            });

            obvious.addAll(res);

            return res.size() > 0;
        }

        private boolean tryIsolatedOneNode(final Node e) {
            final ArrayList<Link> res = new ArrayList<>();
            if (e.getDelta() == 1 && e.links.size() == 1) {
                total -= 2;
                final Link linkOut = e.links.get(0);
                e.links.clear();
                res.add(linkOut);
                linkOut.activate();

                final Link ol = linkOut.to.removeLink(e);
            }

            res.stream().forEach(l -> {
                l.crosses.forEach(lid -> {
                    final Link cross = links.get(lid);
                    final Node node = cross.from;
                    node.removeLink(cross);
                });
            });

            obvious.addAll(res);

            return res.size() > 0;
        }

        private boolean tryFullNodeCase(final Node e) {
            final ArrayList<Link> res = new ArrayList<>();
            final int delta = e.getDelta();
            if (delta > 0 && delta == e.links.size()*2) {
                total -= delta*2;
                e.links.stream().forEach(l -> {
                    res.add(l);
                    l.activate();

                    final Link ol = l.to.removeLink(e);
                    if (null != ol) {
                        res.add(ol);
                        ol.activate();
                    }
                });
                e.removeAlllinks();
            }

            res.stream().forEach(l -> {
                l.crosses.forEach(lid -> {
                    final Link cross = links.get(lid);
                    final Node node = cross.from;
                    node.removeLink(cross);
                });
            });

            obvious.addAll(res);

            return res.size() > 0;
        }

        private boolean tryQuiteFullNodeCase(final Node e) {
            final ArrayList<Link> res = new ArrayList<>();
            boolean action = false;

            final int delta = e.getDelta();
            if (delta > 0 && delta == e.links.size()*2-1) {
                e.links.stream().forEach(l -> {
                    res.add(l);
                });
            }

            for (final Link l : res) {
                for (final int lid : l.crosses) {
                    final Link cross = links.get(lid);
                    final Node node = cross.from;
                    if (node.removeLink(cross) != null) {
                        action = true;
                    }
                }
            }

            return action;
        }

        private final List<Function<Node, Boolean>> heuristics = Arrays.asList(this::tryFullNodeCase,
                                                                               this::tryIsolatedOneNode,
                                                                               this::tryThreeToOneNode,
                                                                               this::tryQuiteFullNodeCase);

        private boolean findObviousCases() {
            boolean done = false;
            for (int i = 0; i < nodes.size(); ++i) {
                final Node e = nodes.get(i);
                for (Function<Node, Boolean> f : heuristics) {
                    if (f.apply(e)) {
                        done = true;
                        break; // <==
                    }
                }
            }
            return done;
        }

        private void cleanFullNodes() {
            nodes
                .stream()
                .filter(n -> n.getDelta() == 0 && n.links.size() > 0)
                .forEach(n -> {
                    n.links.stream().forEach(l -> {
                        l.to.removeLink(n);
                    });
                    n.removeAlllinks();
                });
        }

        private void removeIsolatedNodes() {
            nodes = nodes
                .stream()
                .filter(n -> n.getDelta() > 0)
                .collect(Collectors.toCollection(ArrayList::new));
        }

        private void sortNodes() {
            nodes = nodes
                .stream()
                .sorted((a, b) -> b.target - a.target)
                .collect(Collectors.toCollection(ArrayList::new));
        }
    }

    static class Solver {
        public Solver(final Hashiwokakero hashi) {
            graph = hashi;
            activeNodes = new boolean[graph.width * graph.height];
            activeLinks = new boolean[graph.width * graph.height * 4];

            Arrays.fill(activeNodes, false);
            Arrays.fill(activeLinks, false);

            graph.obvious.stream().forEach(l -> {
                activeLinks[l.id] = true;
            });
        }

        public void solve()  {
            int total = graph.total;
            int maxSp = 0;

            if (total == 0) {
                dumpStack();
            }
            for (int i = 0; i < graph.nodes.size(); ++i) {
                final Node root = graph.nodes.get(i);
                final int stopPos = copylinks(root.links, 0);
                activeNodes[root.id] = true;
                ++sp;
                stackId[sp] = ctxtNextId++;
                stackOp[sp] = null;
                stackNode[sp] = root;
                stackStartPos[sp] = 0;
                stackStopPos[sp] = stopPos;
                while(total > 0 && sp >= 0) {
                    if (stackStartPos[sp] == stackStopPos[sp]) {
                        if (stackOp[sp] != null) {
                            rollbackOperation(stackOp[sp]);
                            total += 2;
                        }

                        if (stackNode[sp] != null) {
                            activeNodes[stackNode[sp].id] = false;
                        }

                        --sp;
                        continue; // <==
                    }

                    final Link choice = links[stackStartPos[sp]++];
                    if (!tryCommitOperation(choice)) {
                        continue; // <==
                    }

                    final Node target = choice.to;
                    Node nextNode = null;
                    int newStopPos;
                    if (isNodeInStack(target)) {
                        newStopPos = stackStopPos[sp];
                    }
                    else {
                        nextNode = target;
                        newStopPos = copylinks(target.links, stackStopPos[sp]);
                        activeNodes[target.id] = true;
                    }
                    total -= 2;
                    ++sp;
                    maxSp = Math.max(maxSp, sp);
                    stackId[sp] = ctxtNextId++;
                    stackOp[sp] = choice;
                    stackNode[sp] = nextNode;
                    stackStartPos[sp] = stackStartPos[sp-1];
                    stackStopPos[sp] = newStopPos;
                }

                if (total == 0) {
                    dumpStack();
                    break; // <==
                }
                activeNodes[root.id] = false;
            }
        }

        private boolean isNodeInStack(final Node node) {
            return activeNodes[node.id];
        }

        private void dumpStack() {
            final HashMap<Integer, Integer> res = new HashMap<>();
            for (int i = 0; i < graph.obvious.size(); ++i) {
                final Link link = graph.obvious.get(i);
                final int minX = Math.min(link.from.x, link.to.x);
                final int maxX = Math.max(link.from.x, link.to.x);
                final int minY = Math.min(link.from.y, link.to.y);
                final int maxY = Math.max(link.from.y, link.to.y);
                final int k = (minY * 40 + minX) * 10000 + (maxY * 40 + maxX);
                if (res.get(k) != null) {
                    res.replace(k, 2);
                }
                else {
                    res.put(k, 1);
                }
            }

            for (int i = 1; i <= sp; ++i) {
                final Link link = stackOp[i];
                final int minX = Math.min(link.from.x, link.to.x);
                final int maxX = Math.max(link.from.x, link.to.x);
                final int minY = Math.min(link.from.y, link.to.y);
                final int maxY = Math.max(link.from.y, link.to.y);
                final int k = (minY * 40 + minX) * 10000 + (maxY * 40 + maxX);
                if (res.get(k) != null) {
                    res.replace(k, 2);
                }
                else {
                    res.put(k, 1);
                }
            }

            res.entrySet().stream().forEach(e -> {
                int k = e.getKey();
                final int minX = (k / 10000) % 40;
                final int minY = (k / 10000) / 40;
                final int maxX = (k % 10000) % 40;
                final int maxY = (k % 10000) / 40;
                System.out.println(String.format("%d %d %d %d %d", minX, minY, maxX, maxY, e.getValue()));
            });
        }

        private boolean willCrossActiveLink(final Link operation) {
            for (int i = 0; i < operation.crosses.size(); ++i) {
                if (activeLinks[operation.crosses.get(i)]) {
                    return true;
                }
            }
            return false;
        }

        private boolean tryCommitOperation(final Link link) {
            if (!link.isActivable()) {
                return false;
            }

            if (link.crosses.size() > 0 && willCrossActiveLink(link)) {
                return false; // <== operation would cross a edge
            }

            activeLinks[link.id] = true;
            link.activate();

            return true;
        }

        private void rollbackOperation(final Link link) {
            link.deactivate();
            activeLinks[link.id] = false;
        }

        private int copylinks(final ArrayList<Link> from, final int base) {
            int i = 0;
            for (; i < from.size(); ++i) {
                links[base+i] = from.get(i);
            }
            return base+i;
        }

        private final Hashiwokakero graph;
        private final boolean[] activeNodes;
        private final boolean[] activeLinks;
        private int ctxtNextId = 0;
        private final Link[] links = new Link[31*31*4];

        private final int[] stackId = new int[1000];
        private final Link[] stackOp = new Link[1000];
        private final Node[] stackNode = new Node[1000];
        private final int[] stackStartPos = new int[1000];
        private final int[] stackStopPos = new int[1000];
        private int sp = -1;
    }

    public static void main(String args[]) {
        final long graphStartTime = System.nanoTime();
        final Hashiwokakero graph = Hashiwokakero.buildGraphFromInput();
        System.err.println(String.format("Build graph in : %f ms", (System.nanoTime()-graphStartTime)/1000000.0));

        final long solveStartTime = System.nanoTime();
        final Solver player = new Solver(graph);
        player.solve();
        System.err.println(String.format("Solved in in : %f ms", (System.nanoTime()-solveStartTime)/1000000.0));
    }
}
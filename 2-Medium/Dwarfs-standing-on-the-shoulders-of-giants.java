import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * https://www.codingame.com/ide/puzzle/dwarfs-standing-on-the-shoulders-of-giants
 * FIND MAX PATH IN TREE.
 */
public class Solution {

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        int numOfConnections = scanner.nextInt();

        //Map of Vertexes and Edges for building T(V,E) tree.
        HashMap<Integer, Set<Integer>> graph = new HashMap<>(numOfConnections);

        //Build T(V,E)
        for (int i = 0; i < numOfConnections; ++i) {
            int source = scanner.nextInt();
            int target = scanner.nextInt();
            graph.putIfAbsent(source, new HashSet<>());
            graph.get(source).add(target);
        }
        //In this puzzle, Number of influences are max tree path + the source vertex (+1).
        System.out.println(maxTreePath(graph) + 1);
    }

    //Get the max Tree path by comparing the max paths from each vertex.
    private static int maxTreePath(final HashMap<Integer, Set<Integer>> tree) {
        final AtomicInteger maxPath = new AtomicInteger(0);

        tree.keySet().forEach(V ->
                maxPath.set(
                        Math.max(getVertexMaxPath(tree, V), maxPath.get())
                )
        );

        return maxPath.get();
    }

    //For each vertex get its max Path.
    private static int getVertexMaxPath(final HashMap<Integer, Set<Integer>> tree, final int v) {
        if (!tree.containsKey(v)) {
            return 0;
        }
        Set<Integer> E = tree.get(v);

        final AtomicInteger maxPath = new AtomicInteger(0);

        E.forEach(e ->
                maxPath.set(
                        Math.max(getVertexMaxPath(tree, e), maxPath.get())
                )
        );

        return maxPath.incrementAndGet();
    }
}
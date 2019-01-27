import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

// https://www.codingame.com/training/medium/teads-sponsored-contest

class Solution {

    private static class Node {

        private final Integer id;
        private final Set<Node> adjacentNodes;

        private Node(Integer id) {
            this.id = id;
            this.adjacentNodes = new HashSet<>();
        }

        public Integer getId() {
            return id;
        }

        public void addAdjacentNode(Node adjacentNodeId) {
            adjacentNodes.add(adjacentNodeId);
        }

        public void removeAdjacentNode(Node adjacentNodeId) {
            adjacentNodes.remove(adjacentNodeId);
        }

        public void removeFromAdjacentNodes() {
            adjacentNodes.stream().forEach(adjacentNode -> adjacentNode.removeAdjacentNode(this));
        }

        public boolean isLeaf() {
            return adjacentNodes.size() == 1;
        }

    }

    public static void main(String args[]) {

        Scanner in = new Scanner(System.in);
        int n = in.nextInt(); // the number of adjacency relations

        Map<Integer, Node> graph = new HashMap<>();
        for (int i = 0; i < n; i++) {
            Integer xi = Integer.valueOf(in.nextInt()); // the ID of a person which is adjacent to yi
            Integer yi = Integer.valueOf(in.nextInt()); // the ID of a person which is adjacent to xi
            Node xiNode = graph.computeIfAbsent(xi, key -> new Node(key));
            Node yiNode = graph.computeIfAbsent(yi, key -> new Node(key));
            xiNode.addAdjacentNode(yiNode);
            yiNode.addAdjacentNode(xiNode);
        }

        System.out.println(calculateMinimumPropagationSteps(graph)); // The minimal amount of steps required to completely propagate the advertisement
    }

    private static int calculateMinimumPropagationSteps(Map<Integer, Node> graph) {
        int minimumPropagationSteps = 0;
        while (graph.size() > 1) {
            removeLeaves(graph);
            minimumPropagationSteps++;
        }
        return minimumPropagationSteps;
    }

    private static void removeLeaves(Map<Integer, Node> graph) {
        //use filter on stream to put in set
        Set<Node> leaves = graph.values().stream().filter(Node::isLeaf).collect(Collectors.toSet());
        //use lambda definitions to remove leaves
        leaves.stream().forEach(leaf -> {
            graph.remove(leaf.getId());
            leaf.removeFromAdjacentNodes();
        });
    }

}
import java.util.*;

// https://www.codingame.com/training/medium/telephone-numbers

class Solution {

  private static class Node {
    private int num;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<LinkedList<Node>> nodes = Optional.empty();

    Node(final int num) {
      this.num = num;
    }

    Node(final LinkedList<Node> phone) {
      num = phone.removeFirst().num;
      link(this, phone);
    }

    static void link(final Node node, final LinkedList<Node> phone) {
      if (phone.isEmpty()) { return; }
      node.nodes = Optional.ofNullable(node.nodes.orElseGet(LinkedList::new));
      Optional<Node> optional = node.nodes
              .get()
              .stream()
              .filter(n -> n.num == phone.getFirst().num)
              .findFirst();
      optional.ifPresent(n -> {
        phone.removeFirst();
        link(n, phone);
      });
      Node current = node;
      for (Node number : phone) {
        current.nodes = Optional.ofNullable(current.nodes.orElseGet(LinkedList::new));
        current.nodes.get().addLast(number);
        current = current.nodes.get().getLast();
      }
    }

    static void fill(Node node, LinkedList<Node> phone) {
      if (!phone.isEmpty()) {
        if (node.num == phone.getFirst().num) {
          phone.removeFirst();

          if (node.nodes.isPresent() && !phone.isEmpty()) {
            Node toAdded = phone.getFirst();
            LinkedList<Node> linkedNodes = node.nodes.get();
            Optional<Node> optional = linkedNodes.stream().filter(n -> n.num == toAdded.num).findFirst();

            if (!optional.isPresent()) {
              linkedNodes.addLast(phone.removeFirst());
              link(toAdded, phone);
            } else {
              fill(optional.get(), phone);
            }
          } else {
            node.nodes = Optional.ofNullable(node.nodes.orElseGet(LinkedList::new));
            link(node,phone);
          }
        }
      }
    }

  static int numOfNodes(Node node) {
    int sum = 1;
    if (node.nodes.isPresent()) {
      LinkedList<Node> LinkedNodes = node.nodes.get();
      for (Node n : LinkedNodes) {
        sum += numOfNodes(n);
      }
    }
    return sum;
  }

}

  public static void main(String args[]) {
    Scanner in = new Scanner(System.in);
    int N = in.nextInt(); //Number of telephone numbers.
    List<Node> mainNodes = new ArrayList<>();
    while (N-- != 0) {
      LinkedList<Node> phone = new LinkedList<>();
      in.next().chars().map(Character::getNumericValue).forEach(num -> phone.add(new Node(num)));
      Optional<Node> tree = mainNodes
              .stream()
              .filter(node -> node.num == phone.getFirst().num)
              .findAny();
      if (tree.isPresent()) {Node.fill(tree.get(), phone);} else {mainNodes.add(new Node(phone));}
    }

    System.out.println(countNodes(mainNodes));
  }

  private static int countNodes(final List<Node> mainNodes) {
    int sum = 0;
    for (Node node : mainNodes) {
      sum += Node.numOfNodes(node);
    }
    return sum;
  }
}
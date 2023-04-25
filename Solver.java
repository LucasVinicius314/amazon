import java.util.ArrayList;
import java.util.List;

// TODO: fix, docs
public class Solver {

  public static double getPathCost(List<Node> nodes, int maxCargo) {
    final var cargo = new ArrayList<Node>();

    var cost = 0.0;

    var currentNodeIndex = 0;

    Node lastNode = null;

    while (true) {
      if (currentNodeIndex == nodes.size()) {
        break;
      }

      final var currentNode = nodes.get(currentNodeIndex);

      if (cargo.contains(currentNode)) {
        cargo.remove(currentNode);
      }

      cargo.addAll(currentNode.neighbours);

      if (cargo.size() > maxCargo) {
        return -1;
      }

      if (lastNode != null) {
        final var deltaX = Math.pow(currentNode.x - lastNode.x, 2);
        final var deltaY = Math.pow(currentNode.y - lastNode.y, 2);

        cost += Math.sqrt(deltaX + deltaY);
      }

      lastNode = currentNode;

      currentNodeIndex++;
    }

    return cost;
  }

  public static List<List<Node>> getPermutations() {
    final var nodes = new ArrayList<>(App.nodes.values());

    final var rootNode = App.nodes.get(0);

    nodes.remove(rootNode);

    final var permutatedPaths = permutate(nodes);

    for (final var permutatedPath : permutatedPaths) {
      permutatedPath.add(0, rootNode);
      permutatedPath.add(rootNode);
    }

    return permutatedPaths;
  }

  static <T> List<List<T>> permutate(List<T> list) {
    final var result = new ArrayList<List<T>>();

    if (list.size() == 0) {
      result.add(new ArrayList<>());

      return result;
    }

    final var head = list.get(0);

    final var rest = list.subList(1, list.size());

    for (final var permutations : permutate(rest)) {
      final var subLists = new ArrayList<List<T>>();

      for (int i = 0; i <= permutations.size(); i++) {
        final var subList = new ArrayList<T>();

        subList.addAll(permutations.subList(0, i));
        subList.add(head);
        subList.addAll(permutations.subList(i, permutations.size()));
        subLists.add(subList);
      }

      result.addAll(subLists);
    }

    return result;
  }
}

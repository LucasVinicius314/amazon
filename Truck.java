import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class Truck {

  public double distance = 0;

  public double rendimento = 0.0;

  public Set<Integer> currentCargo = new HashSet<>();

  public Stack<Node> currentPath = new Stack<>();

  public void add(Node node, Node nextNode) {

    // distance += node.distanceTo(nextNode);
    // rendimento += node.getRend(nextNode, this);
    currentCargo.addAll(nextNode.items);
    currentPath.push(nextNode);

  }

  public void add(Node nextNode) {

    // distance += node.distanceTo(nextNode);
    // rendimento += node.getRend(nextNode, this);
    currentCargo.addAll(nextNode.items);
    currentPath.push(nextNode);

  }

  static Truck newInstance(Truck truck) {

    final var newTruck = new Truck();

    newTruck.distance = truck.distance;
    newTruck.rendimento = truck.rendimento;
    newTruck.currentCargo = new HashSet<>(truck.currentCargo);
    newTruck.currentPath.addAll(truck.currentPath);

    return newTruck;
  }

  public void remove(Node node, Node nextNode) {

    currentPath.pop();
    currentCargo.removeAll(nextNode.items);
    // distance -= node.distanceTo(nextNode);
    // rendimento -= node.getRend(nextNode, this);
  }

  public void remove(Node nextNode) {

    currentCargo.removeAll(nextNode.items);
    currentPath.pop();

    // distance -= node.distanceTo(nextNode);
    // rendimento -= node.getRend(nextNode, this);
  }

  @Override
  public String toString() {

    return String.format("Truck d: %.4f r: %.4f%n%s",
        distance,
        rendimento,
        currentPath
            .stream()
            .map(e -> String.valueOf(e.key))
            .collect(Collectors.joining(",")));
  }
}

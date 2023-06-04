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

    currentCargo.addAll(nextNode.items);
    currentPath.push(nextNode);

  }

  public void remove(Node nextNode) {

    currentCargo.removeAll(nextNode.items);
    currentPath.pop();

  }

  public void remove(Node node, Node nextNode) {

    currentPath.pop();
    currentCargo.removeAll(nextNode.items);
    // distance -= node.distanceTo(nextNode);
    // rendimento -= node.getRend(nextNode, this);
  }

  static Truck newInstance(Truck truck) {

    final var newTruck = new Truck();

    newTruck.distance = truck.distance;
    newTruck.rendimento = truck.rendimento;
    newTruck.currentCargo = new HashSet<>(truck.currentCargo);
    newTruck.currentPath.addAll(truck.currentPath);

    return newTruck;
  }

  // TODO: terminar a verificação se o caminhao ta valido.

  public Boolean valido() {
    int quantItens = 0;
    for (Node integer : currentPath) {
      // if(){

      // }
    }

    return true;
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

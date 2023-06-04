import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class Truck {

  public Truck() {
  }

  public Truck(double rendimento, List<Integer> path) {

    this.rendimento = rendimento;

    for (final var id : path) {

      currentPath.add(new Node(id, 0, 0));
    }
  }

  public double distance = 0;

  public double rendimento = 0;

  public long time = 0;

  public long calls = 0;

  public Set<Integer> currentCargo = new HashSet<>();

  public Stack<Node> currentPath = new Stack<>();

  public void add(Node nextNode) {

    currentCargo.addAll(nextNode.items);
    currentPath.push(nextNode);
  }

  public void remove(Node nextNode) {

    currentCargo.removeAll(nextNode.items);
    currentPath.pop();
  }

  static Truck newInstance(Truck truck) {

    final var newTruck = new Truck();

    newTruck.distance = truck.distance;
    newTruck.rendimento = truck.rendimento;
    newTruck.currentCargo = new HashSet<>(truck.currentCargo);
    newTruck.currentPath.addAll(truck.currentPath);

    return newTruck;
  }

  public boolean isEqualTo(Truck truck) {

    return toString().equals(truck.toString());
  }

  @Override
  public String toString() {

    return String.format("Truck r: %.4f%n%s",
        rendimento,
        currentPath
            .stream()
            .map(e -> String.valueOf(e.key))
            .collect(Collectors.joining(",")));
  }
}

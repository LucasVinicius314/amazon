import java.util.List;
import java.util.stream.Collectors;

public class Path {

  public Path(List<Node> nodes, int maxCargo) {
    this.nodes = nodes;
    this.cost = getCost(maxCargo);
  }

  List<Node> nodes;
  double cost;

  double getCost(int maxCargo) {
    return Solver.getPathCost(nodes, maxCargo);
  }

  @Override
  public String toString() {
    return nodes
      .stream()
      .map(e -> String.valueOf(e.key))
      .collect(Collectors.joining(","));
  }
}

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class Truck {

  public double distance = 0;

  public Set<Integer> currentCargo = new HashSet<Integer>();

  public Stack<Node> currentPath = new Stack<Node>();

  @Override
  public String toString() {
    return String.format("Truck %.2f\n%s",
        distance,
        currentPath
            .stream()
            .map(e -> String.valueOf(e.key))
            .collect(Collectors.joining(",")));
  }
}

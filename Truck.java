import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class Truck {

  public double distance = 0;

  public Set<Integer> currentCargo = new HashSet<Integer>();

  public Stack<Node> currentPath = new Stack<Node>();

  public double rendimento = 0.0;

  @Override
  public String toString() {
    return String.format("Truck d: %.2f r: %.2f\n%s",
        distance,
        rendimento,
        currentPath
            .stream()
            .map(e -> String.valueOf(e.key))
            .collect(Collectors.joining(",")));
  }
}

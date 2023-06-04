import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe que representa um node da network, com seu id, coordenadas x e y e sua
 * lista de vizinhos.
 */
public class Node {

  public Node(int key, double x, double y) {
    this.key = key;
    this.x = x;
    this.y = y;
  }

  // Id do node.
  int key;
  // Coordenada x do node.
  double x;
  // Coordenada y do node.
  double y;

  final List<Integer> items = new ArrayList<>();

  /**
   * Método para calcular a distância entre dois nodes.
   * 
   * @param newNode
   * @return
   */
  public double distanceTo(Node newNode) {
    var a = Math.sqrt(Math.pow(x - newNode.x, 2) + Math.pow(y - newNode.y, 2));
    return a;
  }

  /**
   * Método para calcular o rendimento entre dois nodes, a partir da distância
   * entre eles.
   * 
   * @param node
   * @param truck
   * @return
   */
  // public double getRend(Node node, Truck truck) {
  //   var a = distanceTo(node) / (10 - .5 * truck.currentCargo.size());
  //   return a;
  // }

  public double getRend(double distance, Truck truck) {
    var a = distance / (10 - .5 * truck.currentCargo.size());
    return a;
  }

  @Override
  public String toString() {
    // Mudar toString padrão do node para facilitar a visualização.
    return String.format(
        "Node(id: %d, x: %d, y: %d, neighbours: %s)",
        key,
        x,
        y,
        items
            .stream()
            .map(String::valueOf)
            .collect(Collectors.joining(",")));
  }
}

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe que representa um node da network, com seu id, coordenadas x e y e sua lista de vizinhos.
 */
public class Node {

  public Node(int key, int x, int y) {
    this.key = key;
    this.x = x;
    this.y = y;
  }

  // Id do node.
  int key;
  // Coordenada x do node.
  int x;
  // Coordenada y do node.
  int y;

  // Lista de vizinhos do node.
  final List<Node> neighbours = new ArrayList<>();

  @Override
  public String toString() {
    // Mudar toString padrão do node para facilitar a visualização.
    return String.format(
      "Node(id: %d, x: %d, y: %d, neighbours: %s)",
      key,
      x,
      y,
      neighbours
        .stream()
        .map(e -> String.valueOf(e.key))
        .collect(Collectors.joining(","))
    );
  }
}

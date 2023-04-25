import java.util.ArrayList;
import java.util.List;

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
}

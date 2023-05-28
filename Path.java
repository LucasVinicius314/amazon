import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe que representa um caminho de entrega encontrado, com os nodes que
 * fazem parte do caminho e o seu custo.
 */
public class Path {

  public Path(List<Node> nodes, int maxCargo) {
    this.nodes = nodes;
    // Calcular o custo do caminho, com base nos nodes presentes no caminho e a
    // carga máxima.
    this.cost = Solver.getPathCost(nodes, maxCargo, false);
    // inicializando rendimento
    this.rend = 0.0;
  }

  public Path() {
    this.nodes = null;
    this.cost = 0.0;
    this.rend = 0.0;
  }

  // Nodes que fazem parte do caminho.
  List<Node> nodes;
  // Custo total do caminho.
  double cost;
  // Custo total com rendimento
  double rend;

  @Override
  public String toString() {
    // Alteração do comportamento padrão do toString para facilitar a visualização.
    return nodes
        .stream()
        .map(e -> String.valueOf(e.key))
        .collect(Collectors.joining(","));
  }

  public void getRend(int maxCargo) {
    this.rend = Solver.getPathCost(nodes, maxCargo, true);
  }
}

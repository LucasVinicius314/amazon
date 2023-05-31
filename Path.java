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

    // Pegar o caminho em KM e o rendimento total.
    Atalho receberAtalho = new Atalho();
    receberAtalho = Solver.getPathCost(nodes, maxCargo);
    // carga máxima.
    this.cost = receberAtalho.cost;
    // inicializando rendimento
    this.rend = receberAtalho.rendimento;
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

}

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pelos cálculos de resolução do problema.
 */
public class Solver {

  /**
   * Método para calcular o custo de um caminho, levando em consideração os nodes
   * e a carga máxima.
   * 
   * @param nodes
   * @param maxCargo
   * @return
   */
  public static double getPathCost(List<Node> nodes, int maxCargo) {
    // Declarar o array de saída.
    final var cargo = new ArrayList<Node>();

    // Declarar o custo inicial do caminho como 0.
    var cost = 0.0;

    // Declarar o índice do node atual como 0.
    var currentNodeIndex = 0;

    // Referência para último node visitado, utilizado nos cálculos de distância.
    Node lastNode = null;

    // Iterar sobre cada node do caminho.
    while (true) {
      // Enquanto o índice atual do node não chegar ao tamanho do caminho.
      if (currentNodeIndex == nodes.size()) {
        break;
      }

      // Pegar o node atual baseado no índice do node atual.
      final var currentNode = nodes.get(currentNodeIndex);

      // TODO: fix
      if (cargo.contains(currentNode)) {
        cargo.remove(currentNode);
      }

      cargo.addAll(currentNode.neighbours);

      if (cargo.size() > maxCargo) {
        return -1;
      }

      if (lastNode != null) {
        final var deltaX = Math.pow(currentNode.x - lastNode.x, 2);
        final var deltaY = Math.pow(currentNode.y - lastNode.y, 2);

        cost += Math.sqrt(deltaX + deltaY);
      }

      lastNode = currentNode;

      currentNodeIndex++;
    }

    return cost;
  }

  public static List<List<Node>> getPermutations() {
    final var nodes = new ArrayList<>(App.nodes.values());

    final var rootNode = App.nodes.get(0);

    nodes.remove(rootNode);

    final var permutatedPaths = permutate(nodes);

    for (final var permutatedPath : permutatedPaths) {
      permutatedPath.add(0, rootNode);
      permutatedPath.add(rootNode);

      boolean isValid = validatePermu(permutatedPath);

    }

    return permutatedPaths;
  }

  static <T> List<List<T>> permutate(List<T> list) {
    final var result = new ArrayList<List<T>>();

    if (list.size() == 0) {
      result.add(new ArrayList<>());

      return result;
    }

    final var head = list.get(0);

    final var rest = list.subList(1, list.size());

    for (final var permutations : permutate(rest)) {
      final var subLists = new ArrayList<List<T>>();

      for (int i = 0; i <= permutations.size(); i++) {
        final var subList = new ArrayList<T>();

        subList.addAll(permutations.subList(0, i));
        subList.add(head);
        subList.addAll(permutations.subList(i, permutations.size()));
        subLists.add(subList);
      }

      result.addAll(subLists);
    }

    return result;
  }

  static boolean validatePermu(List<Node> permu) {

    // "0,1,6,8,7,9,4,5,3,2,0"

    List<Integer> locaisPassados = new ArrayList<>();

    List<Node> precisaDeEntregar = new ArrayList<>();

    for (int i = 0; i < permu.size() - 1; i++) {

      Node nodeAnalisado = permu.get(i);
      locaisPassados.add(nodeAnalisado.key);

      if (!nodeAnalisado.neighbours.isEmpty()) {
        precisaDeEntregar.add(nodeAnalisado);
      }

      // testar agora se o local passado é um lugar que tem que entregar um elemento,
      // se tiver remove do vizinhos

    }

    "".toString();
    return true;

  }

}

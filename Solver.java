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

  public static List<List<Node>> getPermutations(boolean validate, int maxCargo) {
    final var nodes = new ArrayList<>(App.nodes.values());

    final var rootNode = App.nodes.get(0);

    nodes.remove(rootNode);

    final var permutatedPaths = permutate(nodes);

    Path pa = new Path(permutatedPaths.get(0), maxCargo);

    List<Node> saveUltimoPath = new ArrayList<>();

    double costMin = pa.cost * 1000;

    for (final var permutatedPath : new ArrayList<>(permutatedPaths)) {

      permutatedPath.add(0, rootNode);
      permutatedPath.add(rootNode);

      if (validate) {
        boolean isValid = validatePermu(permutatedPath);

        // Caso a solucao seja validada pela funcao validatePermu, ele gera o custo e
        // assim, testa se tem um custo menor, se não tiver ele exclui da lista, caso
        // tenha o menor custo ja encontrado ele mantem na lista (permutatedPaths).
        if (isValid) {

          pa = new Path(permutatedPath, maxCargo);

          if (pa.cost < costMin) {

            costMin = pa.cost;

            if (saveUltimoPath.size() != 0) {
              permutatedPaths.remove(saveUltimoPath);
            }

            saveUltimoPath = permutatedPath;

          } else {
            permutatedPaths.remove(permutatedPath);
          }

        } else {
          permutatedPaths.remove(permutatedPath);
        }
      }

    }

    return permutatedPaths;
  }

  static <T> List<List<T>> permutate(List<T> list) {
    final var result = new ArrayList<List<T>>();

    // "0,1,6,8,7,9,4,5,3,2,0"

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

  // validatePermu testa se cada entrega é feita em todas as localidades, caso ele
  // mantenha alguma entre pendente a permutacao é descartada
  // Retorna true caso a lista de entregas termine vazia e retorna false caso
  // tenha entregas pendentes
  static boolean validatePermu(List<Node> permu) {

    List<Integer> locaisPassados = new ArrayList<>();

    List<Integer> precisaDeEntregar = new ArrayList<>();

    for (int i = 0; i < permu.size(); i++) {

      Node nodeAnalisado = permu.get(i);
      locaisPassados.add(nodeAnalisado.key);

      for (int j = 0; j < precisaDeEntregar.size(); j++) {

        if (precisaDeEntregar.get(j) == nodeAnalisado.key) {
          precisaDeEntregar.remove(Integer.valueOf(precisaDeEntregar.get(j)));

        }
      }

      if (!nodeAnalisado.neighbours.isEmpty()) {

        for (Node neighboursNode : nodeAnalisado.neighbours) {

          precisaDeEntregar.add(neighboursNode.key);

        }

      }

    }

    if (precisaDeEntregar.isEmpty()) {
      return true;
    } else {
      return false;
    }

  }

}

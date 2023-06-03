import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Classe responsável pelos cálculos de resolução do problema.
 */
public class Solver {

  public static void bruteForce(Map<Integer, Node> nodes, Node node, Truck truck, int maxCargo) {

    final var g = App.allItems;

    // Remover node atual do map de nodes.
    nodes.remove(node.key);

    // Remover o node atual da carga do caminhão.
    truck.currentCargo.remove(node.key);

    // Clonar lista de nodes.
    final var newNodes = new ArrayList<>(nodes.values());

    // Iterar sobre a lista de nodes clonada.
    for (final var newNode : newNodes) {

      // final var a = node.key == 7;

      // final var b = App.allItems.contains(node.key);

      // if (a) {
      // "".toString();
      // }

      // if (b) {
      // // Violação de restrição: indo para um node que deve receber itens, sem ter
      // os
      // // itens no caminhão.

      // return;
      // }

      // Calcular a distância para o próximo node.
      final var distance = node.distanceTo(newNode);

      // Remover os itens que foram pegos da lista de itens global.
      App.allItems.removeAll(newNode.items);

      // Contar com a adição do item.
      truck.currentCargo.addAll(newNode.items);
      truck.currentPath.push(newNode);
      truck.distance += distance;
      truck.rendimento += node.getRend(truck);

      // Expandir.
      bruteForce(nodes, newNode, truck, maxCargo);

      // Desfazer adição do item.
      truck.currentCargo.removeAll(newNode.items);
      truck.currentPath.pop();
      truck.distance -= distance;

      // Colocar os itens que foram colocados de volta na lista de itens global.
      App.allItems.addAll(newNode.items);
    }

    // if (!truck.currentCargo.isEmpty()) {
    // // Violação da restrição: não foi possível fazer todas as entregas.

    // return;
    // }

    // Print caso não há mais nodes para ir.
    if (nodes.isEmpty()) {

      System.out.println(truck);
    }

    // Desfazer a remoção do node atual.
    nodes.put(node.key, node);
  }

  /**
   * Método para calcular o custo de um caminho, levando em consideração os nodes
   * e a carga máxima, e o withRend consiste em caso queira calcular a distancia
   * com rendimento passa true sem redimento passa false.
   * 
   * @param nodes
   * @param maxCargo
   * @param withRend
   * @return
   */
  public static Atalho getPathCost(List<Node> nodes, int maxCargo) {
    // Declarar o array de saída.
    final var cargo = new ArrayList<Node>();
    List<Node> entregas = new ArrayList<>();
    // Declarar o custo inicial do caminho como 0.
    var cost = 0.0;
    var costMedio = 0.0;
    var rendimento = 0.0;

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

      // cargo.addAll(currentNode.items);

      if (cargo.size() > maxCargo) {
        Atalho atalhoRetVazio = new Atalho(-1, -1);
        return atalhoRetVazio;
      }

      if (lastNode != null) {
        final var deltaX = Math.pow(currentNode.x - lastNode.x, 2);
        final var deltaY = Math.pow(currentNode.y - lastNode.y, 2);

        cost += Math.sqrt(deltaX + deltaY);
        costMedio = Math.sqrt(deltaX + deltaY);
      }

      // fazer o custo com rendimento
      if (lastNode != null) {

        if (lastNode.items.size() != 0) {

          // for (Node node : lastNode.items) {
          // entregas.add(node);
          // }

        }

        rendimento += costMedio / (10 - (0.5 * entregas.size()));

        if (entregas.size() != 0) {

          for (Node node : new ArrayList<Node>(entregas)) {

            if (node.key == currentNode.key) {
              entregas.remove(node);
            }

          }

        }

      }

      lastNode = currentNode;

      currentNodeIndex++;
    }

    Atalho atalhoRetSolucao = new Atalho(cost, rendimento);

    return atalhoRetSolucao;
  }

  public static List<List<Node>> getPermutations(boolean validate, int maxCargo) {
    final var nodes = new ArrayList<>(App.nodes.values());

    final var rootNode = App.nodes.get(0);

    nodes.remove(rootNode);

    final var permutatedPaths = permutate(nodes);

    Path pa = new Path(permutatedPaths.get(0), maxCargo);

    List<Node> saveUltimoPath = new ArrayList<>();

    double costMin = 1000000.00;

    for (final var permutatedPath : new ArrayList<>(permutatedPaths)) {

      permutatedPath.add(0, rootNode);
      permutatedPath.add(rootNode);

      // Calcula o custo e rendimento para todas as permutações
      pa = new Path(permutatedPath, maxCargo);

      if (validate) {
        boolean isValid = validatePermu(permutatedPath, maxCargo);

        // Caso a solucao seja validada pela funcao validatePermu, ele gera o custo e
        // assim, testa se tem um custo menor, se não tiver ele exclui da lista, caso
        // tenha o menor custo ja encontrado ele mantem na lista (permutatedPaths).
        if (isValid) {

          if (pa.cost >= 0) {

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
  static boolean validatePermu(List<Node> permu, int maxCargo) {

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

      if (!nodeAnalisado.items.isEmpty()) {

        // for (Node neighboursNode : nodeAnalisado.items) {

        // if (locaisPassados.contains(Integer.valueOf(neighboursNode.key))) {
        // return false;
        // }

        // if (precisaDeEntregar.size() < maxCargo) {
        // precisaDeEntregar.add(neighboursNode.key);
        // } else {
        // return false;
        // }

        // }

      }

    }

    if (precisaDeEntregar.isEmpty()) {
      return true;
    } else {
      return false;
    }

  }

}

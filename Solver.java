import java.util.ArrayList;
import java.util.Map;

/**
 * Classe responsável pelos cálculos de resolução do problema.
 */
public class Solver {
  public static int podas = 0;

  private Solver() {
  }

  public static void branchBound(Map<Integer, Node> nodes, Node node, Truck truck, int maxCargo) {

    // Remover node atual do map de nodes.
    nodes.remove(node.key);

    // Remover o node atual da carga do caminhão.
    var colocaCaminhao = truck.currentCargo.remove(node.key);

    // Print caso não há mais nodes para ir.
    if (nodes.isEmpty()) {

      // Criar um clone do caminhão atual, para não modificá-lo incorretamente.
      final var newTruck = Truck.newInstance(truck);

      var d = newTruck.currentPath.lastElement().distanceTo(newTruck.currentPath.firstElement());

      // Adicionar a volta ate o no 0.
      newTruck.distance += d;
      newTruck.rendimento += newTruck.currentPath.lastElement().getRend(d, truck);
      newTruck.add(newTruck.currentPath.lastElement(), App.nodes.get(0));

      // Verificar se o novo caminhão é melhor que o melhor caminhão até agora
      if (App.bestSolution == null || newTruck.rendimento < App.bestSolution.rendimento) {
        // Utils.log(newTruck);
        App.bestSolution = newTruck;
      }
    } else

    // Viola restrição de carga max
    // Viola restrição: se o redimento atual é maior que o melhor rendimento,
    // não adianta fazer o no, pois é infrutifero
    if (truck.currentCargo.size() <= maxCargo
        && (App.bestSolution == null || App.bestSolution.rendimento > truck.rendimento)) {

      // Clonar lista de nodes.
      final var newNodes = new ArrayList<>(nodes.values());

      // Iterar sobre a lista de nodes clonada.
      for (final var newNode : newNodes) {

        // Violação de restrição: indo para um node que deve receber itens, sem ter os
        // itens no caminhão.
        if (App.allItems.contains(newNode.key)) {
          continue;
        }

        // Chamada recursiva do branchBound
        chamadaBranchBound(nodes, node, newNode, truck, maxCargo);

      }

    }

    // Voltar com o node atual para carga do caminhão.
    if (colocaCaminhao) {
      truck.currentCargo.add(node.key);
    }

    // Voltar com o node atual para o map.
    nodes.put(node.key, node);
  }

  static void chamadaBranchBound(Map<Integer, Node> nodes, Node node, Node newNode, Truck truck, int maxCargo) {

    // Remover os itens que foram pegos da lista de itens global.
    App.allItems.removeAll(newNode.items);

    // Calcular distancia e rendimento do no para o novoNo.
    var d = node.distanceTo(newNode);
    var r = node.getRend(d, truck);

    truck.add(newNode); // Fazer a adição do node.

    truck.distance += d;
    truck.rendimento += r;

    // Expandir.
    branchBound(nodes, newNode, truck, maxCargo);

    truck.rendimento -= r;
    truck.distance -= d;

    truck.remove(newNode); // Desfazer a adição do node.

    // Colocar os itens que foram pegos devolta, na lista de itens global.
    App.allItems.addAll(newNode.items);
  }

  static void chamadaBruteForce(Map<Integer, Node> nodes, Node node, Node newNode, Truck truck, int maxCargo) {

    // Remover os itens que foram pegos da lista de itens global.
    App.allItems.removeAll(newNode.items);

    // Calcular distancia e rendimento do no para o novoNo.
    var d = node.distanceTo(newNode);
    var r = node.getRend(d, truck);

    truck.add(newNode); // Fazer a adição do node.

    truck.distance += d;
    truck.rendimento += r;

    // Expandir.
    bruteForce(nodes, newNode, truck, maxCargo);

    truck.rendimento -= r;
    truck.distance -= d;

    truck.remove(newNode); // Desfazer a adição do node.

    // Colocar os itens que foram pegos devolta, na lista de itens global.
    App.allItems.addAll(newNode.items);
  }

  public static void bruteForce(Map<Integer, Node> nodes, Node node, Truck truck, int maxCargo) {

    // Remover node atual do map de nodes.
    nodes.remove(node.key);

    // Remover o node atual da carga do caminhão.
    var colocaCaminhao = truck.currentCargo.remove(node.key);

    // Print caso não há mais nodes para ir.
    if (nodes.isEmpty()) {

      // Criar um clone do caminhão atual, para não modificá-lo incorretamente.
      final var newTruck = Truck.newInstance(truck);

      var d = newTruck.currentPath.lastElement().distanceTo(newTruck.currentPath.firstElement());

      // Adicionar a volta ate o no 0.
      newTruck.distance += d;
      newTruck.rendimento += newTruck.currentPath.lastElement().getRend(d, truck);
      newTruck.add(newTruck.currentPath.lastElement(), App.nodes.get(0));

      // Verificar se o novo caminhão é melhor que o melhor caminhão até agora

      if (truck.currentCargo.size() == 0) {
        if (App.bestSolution == null || newTruck.rendimento < App.bestSolution.rendimento) {
          Utils.log(newTruck);
          App.bestSolution = newTruck;
        }
      }
    }

    // Clonar lista de nodes.
    final var newNodes = new ArrayList<>(nodes.values());

    // Iterar sobre a lista de nodes clonada.
    for (final var newNode : newNodes) {

      chamadaBruteForce(nodes, node, newNode, truck, maxCargo);

    }

    // Voltar com o node atual para carga do caminhão.
    if (colocaCaminhao) {
      truck.currentCargo.add(node.key);
    }

    // Voltar com o node atual para o map.
    nodes.put(node.key, node);
  }

}

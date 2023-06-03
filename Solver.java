import java.util.ArrayList;
import java.util.Map;

/**
 * Classe responsável pelos cálculos de resolução do problema.
 */
public class Solver {

  private Solver() {
  }

  public static void bruteForce(Map<Integer, Node> nodes, Node node, Truck truck, int maxCargo) {

    // Remover node atual do map de nodes.
    nodes.remove(node.key);

    // Remover o node atual da carga do caminhão.
    truck.currentCargo.remove(node.key);

    // Clonar lista de nodes.
    final var newNodes = new ArrayList<>(nodes.values());

    // Iterar sobre a lista de nodes clonada.
    for (final var newNode : newNodes) {

      if (App.allItems.contains(newNode.key)) {
        // Violação de restrição: indo para um node que deve receber itens, sem ter os
        // itens no caminhão.

        continue;
      }

      // Remover os itens que foram pegos da lista de itens global.
      App.allItems.removeAll(newNode.items);

      // Adicionar o node.
      truck.add(node, newNode);

      if (nodes.isEmpty()) {

        truck.add(newNode, App.nodes.get(0));
      }

      // Expandir.
      bruteForce(nodes, newNode, truck, maxCargo);

      if (nodes.isEmpty()) {

        truck.remove(newNode, App.nodes.get(0));
      }

      // Desfazer a adição do node.
      truck.remove(node, newNode);

      // Colocar os itens que foram colocados de volta na lista de itens global.
      App.allItems.addAll(newNode.items);
    }

    // Print caso não há mais nodes para ir.
    if (nodes.isEmpty()) {

      final var newTruck = Truck.newInstance(truck);

      newTruck.add(newTruck.currentPath.lastElement(), App.nodes.get(0));

      Utils.log(newTruck);

      if (App.bestSolution == null || newTruck.rendimento < App.bestSolution.rendimento) {

        App.bestSolution = newTruck;
      }
    }

    // Desfazer a remoção do node atual.
    nodes.put(node.key, node);
  }
}

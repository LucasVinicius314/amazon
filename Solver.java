import java.util.ArrayList;
import java.util.Map;

/**
 * Classe responsável pelos cálculos de resolução do problema.
 */
public class Solver {

  private Solver() {
  }

  public static void branchBound(Map<Integer, Node> nodes, Node node, Truck truck, int maxCargo, boolean verbose) {

    App.calls++;

    // Remover node atual do map de nodes.
    nodes.remove(node.key);

    // Remover o node atual da carga do caminhão.
    final var colocaCaminhao = truck.currentCargo.remove(node.key);

    // Print caso não há mais nodes para ir.
    if (nodes.isEmpty()) {

      // Criar um clone do caminhão atual, para não modificá-lo incorretamente.
      final var newTruck = Truck.newInstance(truck);

      final var distancia = newTruck.currentPath.lastElement().distanceTo(newTruck.currentPath.firstElement());

      // Adicionar a volta ate o no 0.
      newTruck.distance += distancia;
      newTruck.rendimento += newTruck.currentPath.lastElement().getRend(distancia, truck);
      newTruck.add(App.nodes.get(0));

      // Verificar se o novo caminhão é melhor que o melhor caminhão até agora
      if (App.bestSolution == null || newTruck.rendimento < App.bestSolution.rendimento) {

        if (verbose) {

          Utils.log(newTruck);
        }

        App.bestSolution = newTruck;
      }
    } else if (truck.currentCargo.size() <= maxCargo
        && (App.bestSolution == null || App.bestSolution.rendimento > truck.rendimento)) {
      // Viola restrição de carga max
      // Viola restrição: se o redimento atual é maior que o melhor rendimento,
      // não adianta fazer o no, pois é infrutifero

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
        chamadaBranchBound(nodes, node, newNode, truck, maxCargo, verbose);
      }
    }

    // Voltar com o node atual para carga do caminhão.
    if (colocaCaminhao) {

      truck.currentCargo.add(node.key);
    }

    // Voltar com o node atual para o map.
    nodes.put(node.key, node);
  }

  static void chamadaBranchBound(Map<Integer, Node> nodes, Node node, Node newNode, Truck truck, int maxCargo,
      boolean verbose) {

    // Remover os itens que foram pegos da lista de itens global.
    App.allItems.removeAll(newNode.items);

    // Calcular distancia e rendimento do no para o novoNo.
    final var distancia = node.distanceTo(newNode);
    final var rendimento = node.getRend(distancia, truck);

    truck.add(newNode); // Fazer a adição do node.

    truck.distance += distancia;
    truck.rendimento += rendimento;

    // Expandir.
    branchBound(nodes, newNode, truck, maxCargo, verbose);

    truck.rendimento -= rendimento;
    truck.distance -= distancia;

    truck.remove(newNode); // Desfazer a adição do node.

    // Colocar os itens que foram pegos devolta, na lista de itens global.
    App.allItems.addAll(newNode.items);
  }

  static void chamadaBruteForce(Map<Integer, Node> nodes, Node node, Node newNode, Truck truck, int maxCargo,
      boolean verbose) {

    // Remover os itens que foram pegos da lista de itens global.
    App.allItems.removeAll(newNode.items);

    // Calcular distancia e rendimento do no para o novoNo.
    final var distancia = node.distanceTo(newNode);
    final var rendimento = node.getRend(distancia, truck);

    truck.add(newNode); // Fazer a adição do node.

    truck.distance += distancia;
    truck.rendimento += rendimento;

    // Expandir.
    bruteForce(nodes, newNode, truck, maxCargo, verbose);

    truck.rendimento -= rendimento;
    truck.distance -= distancia;

    truck.remove(newNode); // Desfazer a adição do node.

    // Colocar os itens que foram pegos devolta, na lista de itens global.
    App.allItems.addAll(newNode.items);
  }

  public static void bruteForce(Map<Integer, Node> nodes, Node node, Truck truck, int maxCargo, boolean verbose) {

    App.calls++;

    // Remover node atual do map de nodes.
    nodes.remove(node.key);

    // Remover o node atual da carga do caminhão.
    final var colocaCaminhao = truck.currentCargo.remove(node.key);

    // Print caso não há mais nodes para ir.
    if (nodes.isEmpty()) {

      // Criar um clone do caminhão atual, para não modificá-lo incorretamente.
      final var newTruck = Truck.newInstance(truck);

      final var distancia = newTruck.currentPath.lastElement().distanceTo(newTruck.currentPath.firstElement());

      // Adicionar a volta ate o no 0.
      newTruck.distance += distancia;
      newTruck.rendimento += newTruck.currentPath.lastElement().getRend(distancia, truck);
      newTruck.add(App.nodes.get(0));

      // Verificar se o novo caminhão é melhor que o melhor caminhão até agora

      if (truck.currentCargo.isEmpty() && (App.bestSolution == null
          || newTruck.rendimento < App.bestSolution.rendimento)) {

        if (verbose) {

          Utils.log(newTruck);
        }

        App.bestSolution = newTruck;
      }
    } else if (truck.currentCargo.size() <= maxCargo) {

      // Clonar lista de nodes.
      final var newNodes = new ArrayList<>(nodes.values());

      // Iterar sobre a lista de nodes clonada.
      for (final var newNode : newNodes) {

        chamadaBruteForce(nodes, node, newNode, truck, maxCargo, verbose);
      }
    }

    // Voltar com o node atual para carga do caminhão.
    if (colocaCaminhao) {

      truck.currentCargo.add(node.key);
    }

    // Voltar com o node atual para o map.
    nodes.put(node.key, node);
  }
}

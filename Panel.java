/*                                     
Autores: João Pedro Barroso da Silva Neto
         Lucas Vinicius do Santos Coelho
         Vinícius Henrique Giovanini
*/

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.*;

/**
 * Painel principal de visualização da network.
 */
public class Panel extends JPanel {

  int steps;
  int maxCargo;
  SolverMode solverMode;
  Truck truck;

  String getItemsList() {

    final var itemsList = new ArrayList<java.util.List<Integer>>();

    final var cargo = new ArrayList<Integer>();

    for (final var node : truck.currentPath) {

      cargo.addAll(node.items);

      final var index = cargo.indexOf(node.key);

      if (index != -1) {

        cargo.remove(index);
      }

      itemsList.add(new ArrayList<>(cargo));
    }

    itemsList.remove(itemsList.size() - 1);

    return itemsList.stream()
        .map(e -> String.format("[%s]", e.stream().map(String::valueOf).collect(Collectors.joining(","))))
        .collect(Collectors.joining(","));
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    final var path = truck.currentPath
        .stream()
        .map(e -> String.valueOf(e.key))
        .collect(Collectors.joining(","));

    g.drawString(
        String.format(
            "%d ms, carga máxima: %d, modo: %s, chamadas: %d",
            truck.time,
            maxCargo,
            solverMode == SolverMode.BRANCH_AND_BOUND ? "Branch and Bound" : "Brute Force",
            truck.calls),
        8, 16);

    g.drawString(
        String.format(
            "rendimento: %.4f, distância: %.4f",
            truck.rendimento,
            truck.distance),
        8, 32);

    g.drawString(String.format("caminho: %s", path), 8, 48);

    g.drawString(String.format("itens no caminho: %s", getItemsList()), 8, 64);

    // Iterar sobre cada node da network.
    for (final var node : App.nodes.values()) {
      // Desenhar um círculo que representa o node.
      g.drawOval((int) Math.round(node.x - 3), (int) Math.round(node.y - 3), 6, 6);

      g.setColor(Color.red);

      // Escrever o id do node.
      g.drawString(String.valueOf(node.key), (int) Math.round(node.x), (int) Math.round(node.y - 6));

      g.setColor(Color.blue);

      final var items = node.items
          .stream()
          .map(String::valueOf)
          .collect(Collectors.joining(","));

      // Escrever os itens do node.
      g.drawString(items, (int) Math.round(node.x), (int) Math.round(node.y + 16));

      g.setColor(Color.black);
    }

    var stepsCounter = steps;

    Node lastNode = null;

    // Iterar sobre cada node da network.
    for (final var node : App.bestSolution.currentPath) {

      if (stepsCounter == 0) {

        break;
      }

      if (lastNode == null) {

        lastNode = node;

        continue;
      }

      // Desenhar uma linha entre o node e o vizinho.
      g.drawLine(
          (int) Math.round(node.x),
          (int) Math.round(node.y),
          (int) Math.round(lastNode.x),
          (int) Math.round(lastNode.y));

      lastNode = node;

      stepsCounter--;
    }
  }
}

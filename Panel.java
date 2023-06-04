import java.awt.*;
import java.util.stream.Collectors;

import javax.swing.*;

/**
 * Painel principal de visualização da network.
 */
public class Panel extends JPanel {

  int steps;
  Truck truck;

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    final var path = truck.currentPath
        .stream()
        .map(e -> String.valueOf(e.key))
        .collect(Collectors.joining(","));

    g.drawString(
        String.format(
            "%d ms, rendimento: %.4f, distância: %.4f, caminho: %s",
            truck.time,
            truck.rendimento,
            truck.distance,
            path),
        8, 16);

    // Iterar sobre cada node da network.
    for (final var node : App.nodes.values()) {
      // Desenhar um círculo que representa o node.
      g.drawOval((int) Math.round(node.x - 3), (int) Math.round(node.y - 3), 6, 6);

      // Escrever o id do node.
      g.drawString(String.valueOf(node.key), (int) Math.round(node.x), (int) Math.round(node.y - 6));

      final var items = node.items
          .stream()
          .map(String::valueOf)
          .collect(Collectors.joining(","));

      // Escrever os itens do node.
      g.drawString(items, (int) Math.round(node.x), (int) Math.round(node.y + 16));
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

import java.awt.*;
import javax.swing.*;

/**
 * Painel principal de visualização da network.
 */
public class Panel extends JPanel {

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Iterar sobre cada node da network.
    for (final var node : App.nodes.values()) {
      // Desenhar um círculo que representa o node.
      g.drawOval((int) Math.round(node.x - 3), (int) Math.round(node.y - 3), 6, 6);

      // Escrever o id do node.
      g.drawString(String.valueOf(node.key), (int) Math.round(node.x), (int) Math.round(node.y - 6));
    }

    Node lastNode = null;

    // Iterar sobre cada node da network.
    for (final var node : App.bestSolution.currentPath) {

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
    }
  }
}

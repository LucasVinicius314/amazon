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
      g.drawOval(node.x - 3, node.y - 3, 6, 6);

      // Escrever o id do node.
      g.drawString(String.valueOf(node.key), node.x, node.y - 6);

      // Para cada vizinho do node.
      for (final var key : node.items) {

        final var neighbour = App.nodes.get(key);

        // Desenhar uma linha entre o node e o vizinho.
        g.drawLine(node.x, node.y, neighbour.x, neighbour.y);
      }
    }
  }
}

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.swing.*;

class Node {

  public Node(int key, int x, int y) {
    this.key = key;
    this.x = x;
    this.y = y;
  }

  int key;
  int x;
  int y;

  final List<Node> neighbours = new ArrayList<>();
}

public class App {

  static Map<Integer, Node> nodes = new HashMap<>();

  public static void main(String[] args) {
    initNetwork();

    initUI();
  }

  static void createNode(int key, int x, int y, String neighbours) {
    final var node = new Node(key, x, y);

    if (neighbours != null) {
      for (final var item : neighbours.split(",")) {
        final var neighbour = nodes.get(Integer.parseInt(item));

        node.neighbours.add(neighbour);
        neighbour.neighbours.add(node);
      }
    }

    nodes.put(key, node);
  }

  static void initNetwork() {
    // createNode(0, 10, 10, null);
    // createNode(1, 10, 20, "0");
    // createNode(2, 30, 10, "0,1");

    final var lines = readInput();

    for (final var line : lines) {
      final var args = line.split(" ");

      final var index = Integer.parseInt(args[0]);
      final var x = Integer.parseInt(args[1]);
      final var y = Integer.parseInt(args[2]);

      var neighbours = "";

      int i = args.length - 1;
      while (i >= 3) {
        neighbours += args[i];
      }

      createNode(index, x, y, neighbours);
    }

    lines.toString();
  }

  static void initUI() {
    final var frame = new JFrame("My Window");

    final var panel = new Panel();
    panel.setSize(200, 200);

    frame.add(panel);

    frame.pack();
    frame.setVisible(true);
    frame.setSize(400, 400);
  }

  static List<String> readInput() {
    final var list = new ArrayList<String>();

    try {
      final var file = new File("in.dat");
      final var scanner = new Scanner(file);

      while (scanner.hasNextLine()) {
        list.add(scanner.nextLine());
      }

      scanner.close();
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
      e.printStackTrace();
    }

    return list;
  }
}

class Panel extends JPanel {

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    for (final var set : App.nodes.entrySet()) {
      final var node = set.getValue();

      g.drawOval(node.x - 3, node.y - 3, 6, 6);

      for (final var neighbour : node.neighbours) {
        g.drawLine(node.x, node.y, neighbour.x, neighbour.y);
      }
    }
  }
}

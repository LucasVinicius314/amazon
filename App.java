import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Classe principal do app.
 */
public class App {

  static long calls = 0;

  static int gridOffset = 96;

  // Map principal com os nodes indexados por id.
  static Map<Integer, Node> nodes = new HashMap<>();

  static Set<Integer> allItems = new HashSet<>();

  static Truck bestSolution = null;

  public static void main(String[] args) {

    // Nome do arquivo de entrada.
    final var filePath = "in.dat";

    // Carga máxima do caminhão.
    final var maxCargo = 3;

    // Inicializar a UI.
    initUI(filePath, maxCargo);

    // runTests();
  }

  static Truck run(String filePath, int maxCargo, boolean isTest, SolverMode solverMode) {

    nodes.clear();
    allItems.clear();
    bestSolution = null;

    // Inicializar a rede de nodes.
    initNetwork(filePath);

    final var truck = new Truck();

    final var newNodes = new HashMap<>(nodes);

    for (final var node : newNodes.values()) {

      if (maxCargo < node.items.size()) {

        Utils.log("Max cargo inválido.");

        return null;
      }

      allItems.addAll(node.items);
    }

    final var rootNode = newNodes.get(0);

    truck.currentCargo.addAll(rootNode.items);
    truck.currentPath.push(rootNode);

    final var then = System.currentTimeMillis();

    if (solverMode == SolverMode.BRANCH_AND_BOUND) {

      Utils.log("\n(Branch and bound)");

      App.calls = 0;

      Solver.branchBound(newNodes, rootNode, truck, maxCargo, !isTest);

      Utils.log("\n");
    } else {

      Utils.log("\n(Brute force)");

      App.calls = 0;

      Solver.bruteForce(newNodes, rootNode, truck, maxCargo, !isTest);

      Utils.log("\n");
    }

    final var now = System.currentTimeMillis();

    final var diff = now - then;

    Utils.log(String.format("Solucao (%d ms):%n%s", diff, bestSolution));

    if (bestSolution != null) {

      bestSolution.time = diff;
      bestSolution.calls = App.calls;
    }

    return bestSolution;
  }

  /**
   * Método para criar um node a partir do id, posição e nodes vizinhos.
   * 
   * @param key
   * @param x
   * @param y
   * @param neighbours
   * @return
   */
  static Node createNode(int key, double x, double y, String neighbours) {

    // Tentar recuperar o node já existente com esse id.
    var node = nodes.get(key);

    if (node == null) {
      // Caso o node não exista, criá-lo.
      node = new Node(key, x, y);
    } else {
      // Caso o node exista, atualizar suas coordenadas.
      node.x = x;
      node.y = y;
    }

    // Caso o node tenha vizinhos.
    if (neighbours != null) {
      // Dividir e iterar sobre cada elemento da string de vizinhos do node.
      for (final var item : neighbours.split(",")) {
        final var neighbourKey = Integer.parseInt(item);

        // Tentar recuperar o vizinho já existente.
        var neighbour = nodes.get(neighbourKey);

        // Criar o vizinho caso ele não exista.
        if (neighbour == null) {
          neighbour = createNode(neighbourKey, 0, 0, null);
        }

        // Associar node -> vizinho.
        node.items.add(neighbour.key);
      }
    }

    // Adicionar o node criado ao map de nodes.
    nodes.put(key, node);

    // Retornar o node criado.
    return node;
  }

  /**
   * Método para inicializar a network de nodes a partir do arquivo de entrada.
   * 
   * @return
   */
  static void initNetwork(String filePath) {

    // Buscar linhas de entrada do arquivo de entrada.
    final var lines = readInput(filePath);

    // Iterar sobre as linhas do arquivo de entrada.
    for (final var line : lines) {
      // Dividir a linha em cada espaço.
      final var args = Arrays.asList(line.split(" "));

      // Pegar o primeiro elemento da linha, o id do node.
      final var index = Integer.parseInt(args.get(0));
      // Pegar o segundo elemento da linha, a coordenada x do node.
      final var x = Double.parseDouble(args.get(1)) + gridOffset;
      // Pegar o terceiro elemento da linha, a coordenada y do node.
      final var y = Double.parseDouble(args.get(2)) + gridOffset;

      // Caso a linha de entrada tenha vizinhos.
      if (args.size() > 3) {
        // Pegar os vizinhos da linha e colocar numa string separada por vírgula.
        final var neighbours = args
            .subList(3, args.size())
            .stream()
            .collect(Collectors.joining(","));

        // Criar node com vizinhos.
        createNode(index, x, y, neighbours);
      } else {
        // Criar node sem vizinhos.
        createNode(index, x, y, null);
      }
    }
  }

  /**
   * Método para inicializar a UI a partir dos nodes da network.
   * 
   * @return
   */
  static void initUI(String filePath, int maxCargo) {

    // Instanciar janela principal.
    final var frame = new JFrame("Main window");
    frame.setLayout(new FlowLayout(0));

    // Instanciar coluna.
    final var column = new JPanel();
    column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
    column.setBorder(new EmptyBorder(8, 8, 8, 8));
    frame.add(column);

    // Instanciar título.
    final var label = new JLabel("Entrega de produtos com Branch and Bound e Brute Force");
    label.setBorder(new EmptyBorder(0, 0, 8, 0));
    column.add(label);

    // Instanciar botão de branch and bound.
    final var branchAndBoundButton = new JButton("Branch and Bound");
    branchAndBoundButton.addActionListener(e -> {

      final var truck = run(filePath, maxCargo, false, SolverMode.BRANCH_AND_BOUND);

      showSimulationWindow(truck, maxCargo, SolverMode.BRANCH_AND_BOUND);
    });
    column.add(branchAndBoundButton);

    // Instanciar separador para adicionar um espaço entre os botões.
    final var spacer = new JPanel();
    spacer.setSize(0, 8);
    column.add(spacer);

    // Instanciar botão de brute force.
    final var bruteForceButton = new JButton("Brute Force");
    bruteForceButton.addActionListener(e -> {

      final var truck = run(filePath, maxCargo, false, SolverMode.BRUTE_FORCE);

      showSimulationWindow(truck, maxCargo, SolverMode.BRUTE_FORCE);
    });
    column.add(bruteForceButton);

    frame.setVisible(true);
    frame.pack();
  }

  static void showSimulationWindow(Truck truck, int maxCargo, SolverMode solverMode) {

    // Instanciar janela secundária.
    final var simulationFrame = new JFrame("Simulation window");
    simulationFrame.setSize(500 + 100 + gridOffset, 500 + 100 + gridOffset);
    simulationFrame.setVisible(true);

    // Instanciar janela do gráfico.
    final var panel = new Panel();
    panel.truck = truck;
    panel.maxCargo = maxCargo;
    panel.solverMode = solverMode;
    panel.setSize(500 + gridOffset, 500 + gridOffset);
    simulationFrame.add(panel);

    // Referência para alterar o valor da animação evitando erro de escopo.
    final var intRef = new IntRef();

    // Instanciar time da animação
    final var timer = new javax.swing.Timer(1000, y -> {
    });

    // Definir listener do timer.
    timer.addActionListener(y -> {

      // Atualizar estado da animação.
      panel.steps = intRef.value;

      // Re renderizar a janela.
      simulationFrame.repaint();

      // Parar animação caso o caminho chegue ao último ponto.
      if (intRef.value == App.bestSolution.currentPath.size() - 1) {

        timer.stop();
      }

      intRef.value++;
    });

    timer.start();
  }

  /**
   * Método para ler e retornar o conteúdo do arquivo de entrada.
   * 
   * @return
   */
  static List<String> readInput(String filePath) {
    // Declarar o array de saída.
    final var list = new ArrayList<String>();

    try {
      // Ler o arquivo de entrada.

      final var file = new File(filePath);
      final var scanner = new Scanner(file);

      while (scanner.hasNextLine()) {
        list.add(scanner.nextLine());
      }

      scanner.close();
    } catch (FileNotFoundException e) {
      Utils.log("File not found.");
      e.printStackTrace();
    }

    // Retornar o array de saída.
    return list;
  }

  static void runTests() {

    test(
        "in-test-0.dat",
        10,
        new Truck(65.71809694373285,
            new ArrayList<>(Arrays.asList(
                0, 1, 2, 3, 4, 5, 0))));

    test(
        "in-test-1.dat",
        10,
        new Truck(246.2858,
            new ArrayList<>(Arrays.asList(
                0, 4, 7, 6, 13, 12, 2, 10, 1, 11, 3, 9, 8, 5, 0))));

    test(
        "in-test-2.dat",
        10,
        new Truck(102.7023,
            new ArrayList<>(Arrays.asList(
                0, 1, 2, 3, 5, 4, 9, 7, 8, 6, 0))));

    test(
        "in-test-3.dat",
        3,
        new Truck(257.5714,
            new ArrayList<>(Arrays.asList(
                0, 8, 10, 9, 11, 12, 3, 2, 13, 1, 14, 15, 6, 7, 5, 4, 0))));
  }

  static void test(String filePath, int maxCargo, Truck expectedResult) {

    Utils.log(String.format("%n>>> <AVISO> Rodando teste para %s, carga máxima: %d%n", filePath, maxCargo));

    final var branchAndBound = run(filePath, maxCargo, true, SolverMode.BRANCH_AND_BOUND);

    if (!expectedResult.isEqualTo(branchAndBound)) {

      throw new IllegalStateException("Test failed.");
    }

    final var bruteForce = run(filePath, maxCargo, true, SolverMode.BRUTE_FORCE);

    if (!expectedResult.isEqualTo(bruteForce)) {

      throw new IllegalStateException("Test failed.");
    }

    Utils.log(String.format("%n>>> <SUCESSO> Teste finalizado para %s, carga máxima: %d%n", filePath, maxCargo));
  }
}

class IntRef {

  public int value = 0;
}
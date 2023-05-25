import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import javax.swing.*;

/**
 * Classe principal do app.
 */
public class App {

  // Map principal com os nodes indexados por id.
  static Map<Integer, Node> nodes = new HashMap<>();

  public static void main(String[] args) {
    // Nome do arquivo de entrada.
    final var filePath = "in.dat";
    // Carga máxima do caminhão.
    final var maxCargo = 10;

    // Inicializar a rede de nodes.
    initNetwork(filePath);

    // Inicializar a UI.
    initUI();

    // Calcular todos os caminhos possíveis.
    final var allPaths = Solver
        .getPermutations()
        .stream()
        .map(e -> new Path(e, maxCargo))
        .sorted((a, b) -> (int) Math.round(a.cost - b.cost))
        .collect(Collectors.toList());

    // Filtrar caminhos possíveis, que permitem todas as entregas sem estourar o
    // limite de carga do caminhão e sem passar em um node mais de uma vez.
    // final var possiblePaths = allPaths
    // .stream()
    // .filter(e -> e.cost > 0)
    // .collect(Collectors.toList());

    // possiblePaths.toString();

    // Iniciando branch and bound
    // final var allPathsBranchBound = Bound
    // .getPermutations()
    // .stream()
    // .map(e -> new Path(e, maxCargo))
    // .collect(Collectors.toList());

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
  static Node createNode(int key, int x, int y, String neighbours) {
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
        node.neighbours.add(neighbour);
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
      final var x = Integer.parseInt(args.get(1));
      // Pegar o terceiro elemento da linha, a coordenada y do node.
      final var y = Integer.parseInt(args.get(2));

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
  static void initUI() {
    // Declarar a janela principal do app.
    final var frame = new JFrame("Main window");

    // Declarar o painel responsável pela visualização da network.
    final var panel = new Panel();
    // Alterar as dimensões do painel.
    panel.setSize(400, 400);

    // Colocar o painel dentro da janela principal.
    frame.add(panel);

    frame.pack();
    // Tornar a janela principal visível.
    frame.setVisible(true);
    // Alterar as dimensões da janela principal.
    frame.setSize(400, 400);
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
      System.out.println("File not found");
      e.printStackTrace();
    }

    // Retornar o array de saída.
    return list;
  }
}

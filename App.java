// Autores:
// - João Pedro Barroso da Silva Neto
// - Lucas Vinicius do Santos Gonçalves Coelho
// - Vinícius Henrique Giovanini

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
 * Classe principal do programa.
 */
public class App {

  static long chamadas = 0;

  static int offset = 96;

  // Map principal com as lojas indexadas por id.
  static Map<Integer, Loja> lojas = new HashMap<>();

  static Set<Integer> todosOsProdutos = new HashSet<>();

  static Caminhao melhorSolucao = null;

  /**
   * Método principal do programa.
   * 
   * @param args
   */
  public static void main(String[] args) {

    // Nome do arquivo de entrada.
    final var caminhoDoArquivo = "in.dat";

    // Carga máxima do caminhão.
    final var cargaMaxima = 2;

    // Inicializar a UI.
    // inicializarUI(caminhoDoArquivo, cargaMaxima);

    executarTestes();
  }

  /**
   * Método para resolver o problema a partir de uma entrada e de um algoritmo.
   * 
   * @param caminhoDoArquivo
   * @param cargaMaxima
   * @param teste
   * @param algoritmo
   * @return
   */
  static Caminhao executar(String caminhoDoArquivo, int cargaMaxima, boolean teste, Algoritmo algoritmo) {

    lojas.clear();
    todosOsProdutos.clear();
    melhorSolucao = null;

    // Inicializar a rede de lojas.
    inicializarRedeDeLojas(caminhoDoArquivo);

    final var caminhao = new Caminhao();

    final var novaListaDeLojas = new HashMap<>(lojas);

    for (final var loja : novaListaDeLojas.values()) {

      if (cargaMaxima < loja.produtos.size()) {

        Utilitarios.log("Carga máxima inválida.");

        return null;
      }

      todosOsProdutos.addAll(loja.produtos);
    }

    final var lojaInicial = novaListaDeLojas.get(0);

    caminhao.cargaAtual.addAll(lojaInicial.produtos);
    caminhao.caminhoAtual.push(lojaInicial);

    final var antes = System.currentTimeMillis();

    if (algoritmo == Algoritmo.BRANCH_AND_BOUND) {

      Utilitarios.log("\n(Branch and bound)");

      App.chamadas = 0;

      Solucao.branchAndBound(novaListaDeLojas, lojaInicial, caminhao, cargaMaxima, !teste);

      Utilitarios.log("\n");
    } else {

      Utilitarios.log("\n(Brute force)");

      App.chamadas = 0;

      Solucao.bruteForce(novaListaDeLojas, lojaInicial, caminhao, cargaMaxima, !teste);

      Utilitarios.log("\n");
    }

    final var agora = System.currentTimeMillis();

    final var tempoDecorrido = agora - antes;

    Utilitarios.log(String.format("Solucao (%d ms):%n%s", tempoDecorrido, melhorSolucao));

    if (melhorSolucao != null) {

      melhorSolucao.tempoDecorrido = tempoDecorrido;
      melhorSolucao.chamadas = App.chamadas;
    }

    return melhorSolucao;
  }

  /**
   * Método para criar uma loja a partir do id, posição e produtos.
   * 
   * @param id
   * @param x
   * @param y
   * @param produtos
   * @return
   */
  static Loja criarLoja(int id, double x, double y, String produtos) {

    // Tentar recuperar a loja já existente com esse id.
    var loja = lojas.get(id);

    if (loja == null) {
      // Caso a loja não exista, criá-la.
      loja = new Loja(id, x, y);
    } else {
      // Caso a loja exista, atualizar suas coordenadas.
      loja.x = x;
      loja.y = y;
    }

    // Caso a loja tenha produtos.
    if (produtos != null) {
      // Dividir e iterar sobre cada produto da string de produtos da loja.
      for (final var elemento : produtos.split(",")) {
        final var produto = Integer.parseInt(elemento);

        // Tentar recuperar a loja já existente.
        var lojaDestino = lojas.get(produto);

        // Criar a loja destino caso ela não exista.
        if (lojaDestino == null) {
          lojaDestino = criarLoja(produto, 0, 0, null);
        }

        // Associar loja -> loja destino.
        loja.produtos.add(lojaDestino.id);
      }
    }

    // Adicionar a loja criada ao map de lojas.
    lojas.put(id, loja);

    // Retornar a loja criada.
    return loja;
  }

  /**
   * Método para inicializar a rede de lojas a partir do arquivo de entrada.
   * 
   * @return
   */
  static void inicializarRedeDeLojas(String caminhoDoArquivo) {

    // Buscar linhas de entrada do arquivo de entrada.
    final var linhas = lerEntrada(caminhoDoArquivo);

    // Iterar sobre as linhas do arquivo de entrada.
    for (final var linha : linhas) {
      // Dividir a linha em cada espaço.
      final var partes = Arrays.asList(linha.split(" "));

      // Pegar o primeiro elemento da linha, o id da loja.
      final var id = Integer.parseInt(partes.get(0));
      // Pegar o segundo elemento da linha, a coordenada x da loja.
      final var x = Double.parseDouble(partes.get(1)) + offset;
      // Pegar o terceiro elemento da linha, a coordenada y da loja.
      final var y = Double.parseDouble(partes.get(2)) + offset;

      // Caso a linha de entrada tenha produtos.
      if (partes.size() > 3) {
        // Pegar os produtos da linha e colocar numa string separada por vírgula.
        final var produtos = partes
            .subList(3, partes.size())
            .stream()
            .collect(Collectors.joining(","));

        // Criar loja com produtos.
        criarLoja(id, x, y, produtos);
      } else {
        // Criar loja sem produtos.
        criarLoja(id, x, y, null);
      }
    }
  }

  /**
   * Método para inicializar a UI a partir das lojas da rede de lojas.
   * 
   * @return
   */
  static void inicializarUI(String caminhoDoArquivo, int cargaMaxima) {

    // Instanciar janela principal.
    final var janela = new JFrame("Janela Principal");
    janela.setLayout(new FlowLayout(0));

    // Instanciar coluna.
    final var coluna = new JPanel();
    coluna.setLayout(new BoxLayout(coluna, BoxLayout.Y_AXIS));
    coluna.setBorder(new EmptyBorder(8, 8, 8, 8));
    janela.add(coluna);

    // Instanciar título.
    final var titulo = new JLabel("Entrega de produtos com Branch and Bound e Brute Force");
    titulo.setBorder(new EmptyBorder(0, 0, 8, 0));
    coluna.add(titulo);

    // Instanciar botão de branch and bound.
    final var botaoBranchAndBound = new JButton("Branch and Bound");
    botaoBranchAndBound.addActionListener(e -> {

      final var caminhao = executar(caminhoDoArquivo, cargaMaxima, false, Algoritmo.BRANCH_AND_BOUND);

      mostrarJanelaDeSimulacao(caminhao, cargaMaxima, Algoritmo.BRANCH_AND_BOUND);
    });
    coluna.add(botaoBranchAndBound);

    // Instanciar separador para adicionar um espaço entre os botões.
    final var espacador = new JPanel();
    espacador.setSize(0, 8);
    coluna.add(espacador);

    // Instanciar botão de brute force.
    final var botaoDeBruteForce = new JButton("Brute Force");
    botaoDeBruteForce.addActionListener(e -> {

      final var caminhao = executar(caminhoDoArquivo, cargaMaxima, false, Algoritmo.BRUTE_FORCE);

      mostrarJanelaDeSimulacao(caminhao, cargaMaxima, Algoritmo.BRUTE_FORCE);
    });
    coluna.add(botaoDeBruteForce);

    janela.setVisible(true);
    janela.pack();
  }

  /**
   * Método para mostrar a janela de simulação do programa.
   * 
   * @param caminhao
   * @param cargaMaxima
   * @param algoritmo
   */
  static void mostrarJanelaDeSimulacao(Caminhao caminhao, int cargaMaxima, Algoritmo algoritmo) {

    // Instanciar janela secundária.
    final var janela = new JFrame("Janela de Simulação");
    janela.setSize(500 + 100 + offset, 500 + 100 + offset);
    janela.setVisible(true);

    // Instanciar janela do gráfico.
    final var painel = new Painel();
    painel.caminhao = caminhao;
    painel.cargaMaxima = cargaMaxima;
    painel.algoritmo = algoritmo;
    painel.setSize(500 + offset, 500 + offset);
    janela.add(painel);

    // Referência para alterar o valor da animação evitando erro de escopo.
    final var referenciaInt = new ReferenciaInt();

    // Instanciar temporizador da animação.
    final var temporizador = new javax.swing.Timer(1000, y -> {
    });

    // Definir listener do timer.
    temporizador.addActionListener(y -> {

      // Atualizar estado da animação.
      painel.passos = referenciaInt.valor;

      // Re renderizar a janela.
      janela.repaint();

      // Parar animação caso o caminho chegue ao último ponto.
      if (referenciaInt.valor == App.melhorSolucao.caminhoAtual.size() - 1) {

        temporizador.stop();
      }

      referenciaInt.valor++;
    });

    temporizador.start();
  }

  /**
   * Método para ler e retornar o conteúdo do arquivo de entrada.
   * 
   * @return
   */
  static List<String> lerEntrada(String caminhoDoArquivo) {
    // Declarar a lista de saída.
    final var lista = new ArrayList<String>();

    try {
      // Ler o arquivo de entrada.

      final var arquivo = new File(caminhoDoArquivo);
      final var scanner = new Scanner(arquivo);

      while (scanner.hasNextLine()) {
        lista.add(scanner.nextLine());
      }

      scanner.close();
    } catch (FileNotFoundException e) {
      Utilitarios.log("Arquivo não encontrado.");
      e.printStackTrace();
    }

    // Retornar a lista de saída.
    return lista;
  }

  /**
   * Método para a execução de testes básicos.
   */
  static void executarTestes() {

    testar(
        "in-test-0.dat",
        10,
        new Caminhao(65.71809694373285,
            new ArrayList<>(Arrays.asList(
                0, 1, 2, 3, 4, 5, 0))));

    testar(
        "in-test-1.dat",
        10,
        new Caminhao(246.2858,
            new ArrayList<>(Arrays.asList(
                0, 4, 7, 6, 13, 12, 2, 10, 1, 11, 3, 9, 8, 5, 0))));

    testar(
        "in-test-2.dat",
        10,
        new Caminhao(102.7023,
            new ArrayList<>(Arrays.asList(
                0, 1, 2, 3, 5, 4, 9, 7, 8, 6, 0))));

    testar(
        "in-test-3.dat",
        3,
        new Caminhao(257.5714,
            new ArrayList<>(Arrays.asList(
                0, 8, 10, 9, 11, 12, 3, 2, 13, 1, 14, 15, 6, 7, 5, 4, 0))));
  }

  /**
   * Método para a execução de um teste a partir de uma entrada e o valor para
   * comparação de resultados.
   * 
   * @param caminhoDoArquivo
   * @param cargaMaxima
   * @param resultadoEsperado
   */
  static void testar(String caminhoDoArquivo, int cargaMaxima, Caminhao resultadoEsperado) {

    Utilitarios
        .log(String.format("%n>>> <AVISO> Rodando teste para %s, carga máxima: %d%n", caminhoDoArquivo, cargaMaxima));

    final var branchAndBound = executar(caminhoDoArquivo, cargaMaxima, true, Algoritmo.BRANCH_AND_BOUND);

    if (!resultadoEsperado.igualA(branchAndBound)) {

      throw new IllegalStateException("Teste falhou.");
    }

    final var bruteForce = executar(caminhoDoArquivo, cargaMaxima, true, Algoritmo.BRUTE_FORCE);

    if (!resultadoEsperado.igualA(bruteForce)) {

      throw new IllegalStateException("Teste falhou.");
    }

    Utilitarios.log(
        String.format("%n>>> <SUCESSO> Teste finalizado para %s, carga máxima: %d%n", caminhoDoArquivo, cargaMaxima));
  }
}

/**
 * Classe que representa uma referência para um número inteiro.
 */
class ReferenciaInt {

  public int valor = 0;
}
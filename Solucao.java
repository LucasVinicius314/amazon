// Autores:
// - João Pedro Barroso da Silva Neto
// - Lucas Vinicius do Santos Gonçalves Coelho
// - Vinícius Henrique Giovanini

import java.util.ArrayList;
import java.util.Map;

/**
 * Classe responsável pelos cálculos de resolução do problema.
 */
public class Solucao {

  private Solucao() {
  }

  /**
   * Método que aplica branch and bound para resolver o problema.
   * 
   * @param lojas
   * @param loja
   * @param caminhao
   * @param cargaMaxima
   * @param printar
   */
  public static void branchAndBound(Map<Integer, Loja> lojas, Loja loja, Caminhao caminhao, int cargaMaxima,
      boolean printar) {

    App.chamadas++;

    // Remover loja atual do map de lojas.
    lojas.remove(loja.id);

    // Remover o produto para a loja atual da carga do caminhão.
    final var colocaCaminhao = caminhao.cargaAtual.remove(loja.id);

    // Print caso não há mais lojas para ir.
    if (lojas.isEmpty()) {

      // Criar um clone do caminhão atual, para não modificá-lo incorretamente.
      final var novoCaminhao = Caminhao.newInstance(caminhao);

      final var distancia = novoCaminhao.caminhoAtual.lastElement()
          .distanciaAte(novoCaminhao.caminhoAtual.firstElement());

      // Adicionar a volta ate a loja inicial.
      novoCaminhao.distancia += distancia;
      novoCaminhao.rendimento += novoCaminhao.caminhoAtual.lastElement().rendimentoEntre(distancia, caminhao);
      novoCaminhao.adicionar(App.lojas.get(0));

      // Verificar se a nova solução é melhor que a melhor solução até agora.
      if (App.melhorSolucao == null || novoCaminhao.rendimento < App.melhorSolucao.rendimento) {

        if (printar) {

          Utilitarios.log(novoCaminhao);
        }

        App.melhorSolucao = novoCaminhao;
      }
    } else if (caminhao.cargaAtual.size() <= cargaMaxima
        && (App.melhorSolucao == null || App.melhorSolucao.rendimento > caminhao.rendimento)) {
      // Viola restrição de carga máxima.
      // Viola restrição: se o redimento atual é maior que o melhor rendimento, não
      // adianta visitar a loja, pois o caminho é infrutífero.

      // Clonar lista de lojas.
      final var novasLojas = new ArrayList<>(lojas.values());

      // Iterar sobre a lista de lojas clonada.
      for (final var novaLoja : novasLojas) {

        // Viola restrição: indo para uma loja que deve receber produtos, sem ter os
        // produtos no caminhão.
        if (App.todosOsProdutos.contains(novaLoja.id)) {

          continue;
        }

        // Chamada recursiva do branchAndBound.
        chamadaBranchAndBound(lojas, loja, novaLoja, caminhao, cargaMaxima, printar);
      }
    }

    // Voltar com o produto atual para a carga do caminhão.
    if (colocaCaminhao) {

      caminhao.cargaAtual.add(loja.id);
    }

    // Voltar com a loja atual para o map.
    lojas.put(loja.id, loja);
  }

  /**
   * Método utilitário que procede com o método e chama o método de branch and
   * bound novamente.
   * 
   * @param lojas
   * @param loja
   * @param novaLoja
   * @param caminhao
   * @param cargaMaxima
   * @param printar
   */
  static void chamadaBranchAndBound(Map<Integer, Loja> lojas, Loja loja, Loja novaLoja, Caminhao caminhao,
      int cargaMaxima, boolean printar) {

    // Remover os produtos que foram pegos da lista de produtos global.
    App.todosOsProdutos.removeAll(novaLoja.produtos);

    // Calcular distancia e rendimento da loja atual para a próxima loja.
    final var distancia = loja.distanciaAte(novaLoja);
    final var rendimento = loja.rendimentoEntre(distancia, caminhao);

    // Contabilizar a loja no caminho.
    caminhao.adicionar(novaLoja);

    caminhao.distancia += distancia;
    caminhao.rendimento += rendimento;

    // Expandir.
    branchAndBound(lojas, novaLoja, caminhao, cargaMaxima, printar);

    caminhao.rendimento -= rendimento;
    caminhao.distancia -= distancia;

    // Desfazer a contabilização da loja no caminho.
    caminhao.remover(novaLoja);

    // Colocar os produtos que foram pegos de volta na lista de produtos global.
    App.todosOsProdutos.addAll(novaLoja.produtos);
  }

  /**
   * Método que aplica brute force para resolver o problema.
   * 
   * @param lojas
   * @param loja
   * @param caminhao
   * @param cargaMaxima
   * @param printar
   */
  public static void bruteForce(Map<Integer, Loja> lojas, Loja loja, Caminhao caminhao, int cargaMaxima,
      boolean printar) {

    App.chamadas++;

    // Remover loja atual do map de lojas.
    lojas.remove(loja.id);

    // Remover o produto para a loja atual da carga do caminhão.
    final var colocaCaminhao = caminhao.cargaAtual.remove(loja.id);

    // Print caso não há mais lojas para ir.
    if (lojas.isEmpty()) {

      // Criar um clone do caminhão atual, para não modificá-lo incorretamente.
      final var novoCaminhao = Caminhao.newInstance(caminhao);

      final var distancia = novoCaminhao.caminhoAtual.lastElement()
          .distanciaAte(novoCaminhao.caminhoAtual.firstElement());

      // Adicionar a volta ate a loja inicial.
      novoCaminhao.distancia += distancia;
      novoCaminhao.rendimento += novoCaminhao.caminhoAtual.lastElement().rendimentoEntre(distancia, caminhao);
      novoCaminhao.adicionar(App.lojas.get(0));

      // Verificar se a nova solução é melhor que a melhor solução até agora.
      if (caminhao.cargaAtual.isEmpty() && (App.melhorSolucao == null
          || novoCaminhao.rendimento < App.melhorSolucao.rendimento)) {

        if (printar) {

          Utilitarios.log(novoCaminhao);
        }

        App.melhorSolucao = novoCaminhao;
      }
    } else if (caminhao.cargaAtual.size() <= cargaMaxima) {

      // Clonar lista de lojas.
      final var novasLojas = new ArrayList<>(lojas.values());

      // Iterar sobre a lista de lojas clonada.
      for (final var novaLoja : novasLojas) {

        chamadaBruteForce(lojas, loja, novaLoja, caminhao, cargaMaxima, printar);
      }
    }

    // Voltar com o produto atual para a carga do caminhão.
    if (colocaCaminhao) {

      caminhao.cargaAtual.add(loja.id);
    }

    // Voltar com a loja atual para o map.
    lojas.put(loja.id, loja);
  }

  /**
   * Método utilitário que procede com o método e chama o método de brute force
   * novamente.
   * 
   * @param lojas
   * @param loja
   * @param novaLoja
   * @param caminhao
   * @param cargaMaxima
   * @param printar
   */
  static void chamadaBruteForce(Map<Integer, Loja> lojas, Loja loja, Loja novaLoja, Caminhao caminhao, int cargaMaxima,
      boolean printar) {

    // Remover os produtos que foram pegos da lista de produtos global.
    App.todosOsProdutos.removeAll(novaLoja.produtos);

    // Calcular distancia e rendimento da loja atual para a próxima loja.
    final var distancia = loja.distanciaAte(novaLoja);
    final var rendimento = loja.rendimentoEntre(distancia, caminhao);

    // Contabilizar a loja no caminho.
    caminhao.adicionar(novaLoja);

    caminhao.distancia += distancia;
    caminhao.rendimento += rendimento;

    // Expandir.
    bruteForce(lojas, novaLoja, caminhao, cargaMaxima, printar);

    caminhao.rendimento -= rendimento;
    caminhao.distancia -= distancia;

    // Desfazer a contabilização da loja no caminho.
    caminhao.remover(novaLoja);

    // Colocar os produtos que foram pegos de volta na lista de produtos global.
    App.todosOsProdutos.addAll(novaLoja.produtos);
  }
}

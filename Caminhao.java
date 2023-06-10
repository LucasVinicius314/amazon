// Autores:
// - João Pedro Barroso da Silva Neto
// - Lucas Vinicius do Santos Gonçalves Coelho
// - Vinícius Henrique Giovanini

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Classe que representa um caminhão com sua carga e atributos.
 */
public class Caminhao {

  public Caminhao() {
  }

  public Caminhao(double rendimento, List<Integer> caminho) {

    this.rendimento = rendimento;

    for (final var id : caminho) {

      caminhoAtual.add(new Loja(id, 0, 0));
    }
  }

  public double distancia = 0;

  public double rendimento = 0;

  public long tempoDecorrido = 0;

  public long chamadas = 0;

  public Set<Integer> cargaAtual = new HashSet<>();

  public Stack<Loja> caminhoAtual = new Stack<>();

  public void adicionar(Loja loja) {

    cargaAtual.addAll(loja.produtos);
    caminhoAtual.push(loja);
  }

  public void remover(Loja loja) {

    cargaAtual.removeAll(loja.produtos);
    caminhoAtual.pop();
  }

  static Caminhao newInstance(Caminhao caminhao) {

    final var novoCaminhao = new Caminhao();

    novoCaminhao.distancia = caminhao.distancia;
    novoCaminhao.rendimento = caminhao.rendimento;
    novoCaminhao.cargaAtual = new HashSet<>(caminhao.cargaAtual);
    novoCaminhao.caminhoAtual.addAll(caminhao.caminhoAtual);

    return novoCaminhao;
  }

  public boolean igualA(Caminhao caminhao) {

    return toString().equals(caminhao.toString());
  }

  @Override
  public String toString() {

    return String.format("Caminhão r: %.4f%n%s",
        rendimento,
        caminhoAtual
            .stream()
            .map(e -> String.valueOf(e.id))
            .collect(Collectors.joining(",")));
  }
}

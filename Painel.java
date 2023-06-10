// Autores:
// - João Pedro Barroso da Silva Neto
// - Lucas Vinicius do Santos Gonçalves Coelho
// - Vinícius Henrique Giovanini

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.*;

/**
 * Painel de simulação.
 */
public class Painel extends JPanel {

  int passos;
  int cargaMaxima;
  Algoritmo algoritmo;
  Caminhao caminhao;

  String listaDeTodosOsProdutos() {

    final var listaDeProdutos = new ArrayList<java.util.List<Integer>>();

    final var carga = new ArrayList<Integer>();

    for (final var loja : caminhao.caminhoAtual) {

      carga.addAll(loja.produtos);

      final var indice = carga.indexOf(loja.id);

      if (indice != -1) {

        carga.remove(indice);
      }

      listaDeProdutos.add(new ArrayList<>(carga));
    }

    listaDeProdutos.remove(listaDeProdutos.size() - 1);

    return listaDeProdutos.stream()
        .map(e -> String.format("[%s]", e.stream().map(String::valueOf).collect(Collectors.joining(","))))
        .collect(Collectors.joining(","));
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    final var caminho = caminhao.caminhoAtual
        .stream()
        .map(e -> String.valueOf(e.id))
        .collect(Collectors.joining(","));

    g.drawString(
        String.format(
            "%d ms, carga máxima: %d, modo: %s, chamadas: %d",
            caminhao.tempoDecorrido,
            cargaMaxima,
            algoritmo == Algoritmo.BRANCH_AND_BOUND ? "Branch and Bound" : "Brute Force",
            caminhao.chamadas),
        8, 16);

    g.drawString(
        String.format(
            "rendimento: %.4f, distância: %.4f",
            caminhao.rendimento,
            caminhao.distancia),
        8, 32);

    g.drawString(String.format("caminho: %s", caminho), 8, 48);

    g.drawString(String.format("produtos no caminho: %s", listaDeTodosOsProdutos()), 8, 64);

    // Iterar sobre cada loja da rede.
    for (final var loja : App.lojas.values()) {
      // Desenhar um círculo que representa a loja.
      g.drawOval((int) Math.round(loja.x - 3), (int) Math.round(loja.y - 3), 6, 6);

      g.setColor(Color.red);

      // Escrever o id da loja.
      g.drawString(String.valueOf(loja.id), (int) Math.round(loja.x), (int) Math.round(loja.y - 6));

      g.setColor(Color.blue);

      final var produtos = loja.produtos
          .stream()
          .map(String::valueOf)
          .collect(Collectors.joining(","));

      // Escrever os produtos da loja.
      g.drawString(produtos, (int) Math.round(loja.x), (int) Math.round(loja.y + 16));

      g.setColor(Color.black);
    }

    var contadorDePassos = passos;

    Loja ultimaLoja = null;

    // Iterar sobre cada loja da rede.
    for (final var loja : App.melhorSolucao.caminhoAtual) {

      if (contadorDePassos == 0) {

        break;
      }

      if (ultimaLoja == null) {

        ultimaLoja = loja;

        continue;
      }

      // Desenhar uma linha entre a loja anterior e próxima.
      g.drawLine(
          (int) Math.round(loja.x),
          (int) Math.round(loja.y),
          (int) Math.round(ultimaLoja.x),
          (int) Math.round(ultimaLoja.y));

      ultimaLoja = loja;

      contadorDePassos--;
    }
  }
}

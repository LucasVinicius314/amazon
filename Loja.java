// Autores:
// - João Pedro Barroso da Silva Neto
// - Lucas Vinicius do Santos Gonçalves Coelho
// - Vinícius Henrique Giovanini

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe que representa uma loja com seu id, coordenadas x e y e seus produtos.
 */
public class Loja {

  public Loja(int id, double x, double y) {
    this.id = id;
    this.x = x;
    this.y = y;
  }

  // Id da loja.
  int id;
  // Coordenada x da loja.
  double x;
  // Coordenada y da loja.
  double y;

  final List<Integer> produtos = new ArrayList<>();

  /**
   * Método para calcular a distância entre duas lojas.
   * 
   * @param novaLoja
   * @return
   */
  public double distanciaAte(Loja novaLoja) {
    return Math.sqrt(Math.pow(x - novaLoja.x, 2) + Math.pow(y - novaLoja.y, 2));
  }

  /**
   * Método para calcular o rendimento entre duas lojas, a partir da distância
   * entre elas.
   * 
   * @param distancia
   * @param caminhao
   * @return
   */
  public double rendimentoEntre(double distancia, Caminhao caminhao) {
    return distancia / (10 - .5 * caminhao.cargaAtual.size());
  }

  @Override
  public String toString() {

    // Mudar toString padrão da loja para facilitar a visualização.
    return String.format(
        "Loja(id: %d, x: %d, y: %d, produtos: %s)",
        id,
        x,
        y,
        produtos
            .stream()
            .map(String::valueOf)
            .collect(Collectors.joining(",")));
  }
}

import java.util.ArrayList;
import java.util.List;

// Classe para realizar branch and bound.
// Verificar elemento durante a realização das permutacoes
// Mofificar a classe para realizar a permutacao testando qual é o menor elemento
public class Bound {

  public static List<List<Node>> getPermutations() {
    final var nodes = new ArrayList<>(App.nodes.values());

    final var rootNode = App.nodes.get(0);

    nodes.remove(rootNode);

    final var permutatedPaths = permutate(nodes);

    for (final var permutatedPath : permutatedPaths) {
      permutatedPath.add(0, rootNode);
      permutatedPath.add(rootNode);
    }

    return permutatedPaths;
  }

  // Realizar teste no coast nessa func
  static <T> List<List<T>> permutate(List<T> list) {
    final var result = new ArrayList<List<T>>();

    if (list.size() == 0) {
      result.add(new ArrayList<>());

      return result;
    }

    final var head = list.get(0);

    final var rest = list.subList(1, list.size());

    for (final var permutations : permutate(rest)) {
      final var subLists = new ArrayList<List<T>>();

      for (int i = 0; i <= permutations.size(); i++) {
        final var subList = new ArrayList<T>();

        subList.addAll(permutations.subList(0, i));
        subList.add(head);
        subList.addAll(permutations.subList(i, permutations.size()));
        subLists.add(subList);
      }

      result.addAll(subLists);
    }

    return result;
  }

}

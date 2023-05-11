import java.util.*;

public class bound {

  // Init branch and bound (nao iniciado)
  void searchFinalElement(List<Path> list) {
    double menorElemento = 10000000;
    int count = 0;
    Path nodeFinal = list.get(0);
    for (Path i : list) {
      double qtdNode = i.cost;

      if (qtdNode < menorElemento) {
        menorElemento = qtdNode;
        nodeFinal = i;
      }
      count++;
    }

    System.out.println(menorElemento);
    System.out.println(count);
    System.out.println(nodeFinal);
  }

}

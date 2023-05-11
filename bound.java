import java.util.List;

public class bound {

  // Init branch and bound (nao iniciado)
  void searchFinalElement(List<Path> list) {
    double menorElemento = 10000000;
    int count = 0;

    for (int i = 0; i < list.size(); i++) {

      Path element = list.get(i);

      if (element.cost < menorElemento) {
        menorElemento = element.cost;
      } else {
        list.remove(i);
        i--;
      }
      count++;
    }

    System.out.println(menorElemento);
    System.out.println(count);
  }

}

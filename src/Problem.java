import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * @author LukaszWasiura
 */
public class Problem {

    public Problem(int width, int height, int sI, int sJ, int eI, int eJ) throws FileNotFoundException, UnsupportedEncodingException {
        Metoda metoda = new Metoda(width, height, sI, sJ, eI, eJ);

        PrintWriter save = new PrintWriter("out.txt", "UTF-8");

        metoda.displayGrid();

        System.out.println("Odległość po przekątnej: ");
        save.println("Diagonal distance: ");
        save.println();
        metoda.search(metoda.current, metoda.closed, metoda.open, false, metoda.grid);
        save.print("Przeszukiwanie: ");
        save.println(metoda.przeszukiwanie);
        System.out.println();
        System.out.print("Koszt calkowity: ");
        System.out.printf("%.2f ", metoda.grid[metoda.endI][metoda.endJ].cost);
        System.out.println();
        save.printf("Koszt calkowity: ");
        save.printf("%.2f ", metoda.grid[metoda.endI][metoda.endJ].cost);
        save.println();
        metoda.displayPath(metoda.grid);
        save.print("Scieżka: ");
        save.println(metoda.sciezka);
        save.println();
        save.println();
        System.out.println();

        System.out.println("Odległość Czebyszewa: ");
        save.println("Odległość Czebyszewa: ");
        save.println();
        metoda.search(metoda.current1, metoda.closed1, metoda.open1, true, metoda.grid1);
        save.print("Przeszukiwanie: ");
        save.println(metoda.przeszukiwanie);
        System.out.println();
        System.out.print("Koszt calkowity: ");
        System.out.printf("%.2f ", metoda.grid1[metoda.endI][metoda.endJ].cost);
        System.out.println();
        save.printf("Koszt calkowity: ");
        save.printf("%.2f ", metoda.grid1[metoda.endI][metoda.endJ].cost);
        metoda.displayPath(metoda.grid1);
        save.println();
        save.print("Scieżka: ");
        save.println(metoda.sciezka);

        if((metoda.grid1[metoda.endI][metoda.endJ].cost) > (metoda.grid[metoda.endI][metoda.endJ].cost)) {
            save.println();
            save.println("Dla heurystyki Diagonal cost koszt całkowity jest mniejszy.");
        }
        else {
            save.println();
            save.println("Dla heurystyki Odległość Czebyszewa (koszt jednolity) koszt całkowity jest mniejszy.");
        }

        save.close();
    }

}

public class Sterowanie {
    public static void main(String[] args) {

        Metoda metoda = new Metoda(8, 8, 0, 0, 7, 7);

        metoda.displayGrid();

        System.out.println("Diagonal distance: ");
        metoda.search(metoda.current, metoda.closed, metoda.open);
//        metoda.displayHeuristic(metoda.grid);
        metoda.displayPath(metoda.grid);
        System.out.println("Koszt calkowity: ");
        System.out.printf("%.2f ", metoda.grid[metoda.endI][metoda.endJ].cost);
        System.out.println();
        System.out.println();

        System.out.println("Chebyshev cost: ");
        metoda.search1(metoda.current1, metoda.closed1, metoda.open1);
//        metoda.displayHeuristic(metoda.grid1);
        metoda.displayPath(metoda.grid1);
        System.out.println("Koszt calkowity: ");
        System.out.printf("%.2f ", metoda.grid1[metoda.endI][metoda.endJ].cost);
    }

}

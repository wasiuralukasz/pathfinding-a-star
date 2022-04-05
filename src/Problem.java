public class Problem {

    public Problem(int width, int height, int sI, int sJ, int eI, int eJ) {
        Metoda metoda = new Metoda(width, height, sI, sJ, eI, eJ);

        metoda.displayGrid();

        System.out.println("Diagonal distance: ");
        metoda.search(metoda.current, metoda.closed, metoda.open);
        metoda.displayPath(metoda.grid);
        System.out.print("Koszt calkowity: ");
        System.out.printf("%.2f ", metoda.grid[metoda.endI][metoda.endJ].cost);
        System.out.println();
        System.out.println("Ruchy: " + metoda.moveCount);
        System.out.println();
        System.out.println();

        System.out.println("Chebyshev cost: ");
        metoda.search1(metoda.current1, metoda.closed1, metoda.open1);
        metoda.displayPath(metoda.grid1);
        System.out.print("Koszt calkowity: ");
        System.out.printf("%.2f ", metoda.grid1[metoda.endI][metoda.endJ].cost);
        System.out.println();
        System.out.println("Ruchy: " + metoda.moveCount1);
    }

}

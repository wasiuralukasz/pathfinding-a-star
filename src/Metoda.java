/**
* @author LukaszWasiura
*/
import javax.swing.*;
import java.util.PriorityQueue;
import java.util.Stack;

public class Metoda {

    //koszt dla ruchu w jednym z ośmiu kierunków
    public static int VER_HOR_COST  = 1;
    public static double DIAG_COST = Math.sqrt(2);

    //koszt w momencie gdy znajduje się tam przeszkoda
    public static final int BLOCK_COST = 3;

    public Cell[][] grid, grid1;

    //Obecne pole
    public Cell current, current1;

    //Lista OPEN, open dla grid, open1 dla grid1
    public PriorityQueue<Cell>  open, open1;
    //Lista CLOSED, closed dla grid, closed1 dla grid1
    public boolean[][] closed, closed1;

    //Start
    public int startI, startJ;

    //Koniec
    public int endI, endJ;

    //Ilość ruchów
    int moveCount = 0;

    //Koszt całkowity
    double koszt;

    String sciezka, przeszukiwanie;

    //Tworzy świat o konkretnych parametrach
    public Metoda(int width, int height, int sI, int sJ, int eI, int eJ) {
        grid = new Cell[width][height];
        grid1 = new Cell[width][height];

        closed = new boolean[width][height];
        closed1 = new boolean[width][height];

        open = new PriorityQueue<Cell>((Cell p1, Cell p2) -> {
            return p1.cost < p2.cost ? -1 : p1.cost > p2.cost ? 1 :0;
        });
        open1 = new PriorityQueue<Cell>((Cell p1, Cell p2) -> {
            return p1.cost < p2.cost ? -1 : p1.cost > p2.cost ? 1 :0;
        });

        startCell(sI, sJ);
        endCell(eI, eJ);

        //liczenie heurysytyki dla wszystkich pól
        addHeuristic();

        grid[startI][startJ].cost = 0;
        grid1[startI][startJ].cost = 0;

        //umieszczamy bloki w gridzie
        addRandomBlock();
    }

    //Odpowiada za liczenie heurystyki: Diagonal Cost i Chebyshev Cost
    public void addHeuristic() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {

                int dx = Math.abs(i - endI);
                int dy = Math.abs(j - endJ);
                grid[i][j] = new Cell(i, j);
                grid1[i][j] = new Cell(i, j);
                grid[i][j].heuristic = VER_HOR_COST * (dx + dy) + (DIAG_COST - (2 * VER_HOR_COST)) *  Math.min(dx, dy);
                grid1[i][j].heuristic = Math.max(dx , dy);
                grid[i][j].isSolution = false;
                grid1[i][j].isSolution = false;
            }
        }
    }

    //Odpowiada za dodanie przeszkód w losowych miejscach
    public void addRandomBlock() {
        int min = 0;
        int maxI = grid.length-1;
        int maxJ = grid[0].length-1;

        int n = (int)(grid.length * grid[0].length - 0.5);

        for (int a = 0; a < n; a++) {
                int i = (int) Math.floor(Math.random() * (maxI - min + 1) + min);
                int j = (int) Math.floor(Math.random() * (maxJ - min + 1) + min);
                if (!((i == startI && j == startJ) || (i == endI && j == endJ))) {
                    grid[i][j].isBlock = true;
                    grid1[i][j].isBlock = true;
                }
        }
    }

    //Przypisuje współrzędne pozycji startowej
    public void startCell(int i, int j) {
        startI = i;
        startJ = j;
    }

    //Przypisuje współrzędne pozycji końcowej
    public void endCell(int i, int j) {
        endI = i;
        endJ = j;
    }

    //Algorytm a star
    public void updateCost(Cell current, Cell t, double cost, boolean[][] closed, PriorityQueue<Cell>  open) {

        double tFinalCost;
        if ((t == null) || closed[t.i][t.j]) {
            return;
        }
        if(t.i == startI && t.j == startJ)
            tFinalCost = 0;
        else
            tFinalCost = t.heuristic + cost;

        boolean isOpen = open.contains(t);

        if (!isOpen || (tFinalCost < t.cost)) {

            t.cost = tFinalCost;
            t.parent = current;

            if (!isOpen) {
                open.add(t);
            }
        }
    }
    public void search(Cell current, boolean[][] closed, PriorityQueue<Cell>  open, boolean isEqual, Cell grid[][]) {

        if (isEqual) {
            DIAG_COST = VER_HOR_COST;
//            moveCount = 0;
        }

        open.add(grid[startI][startJ]);
        przeszukiwanie = "";
        System.out.println();
        System.out.print("Przeszukiwanie: ");
        while (true) {

            current = open.poll();

            if (current == null) {
                break;
            }

            closed[current.i][current.j] = true;

            if(current.equals(grid[endI][endJ])) {
                System.out.print("[" + current.i + ", " + current.j + "]");
                przeszukiwanie = przeszukiwanie + " " + "[" + current.i + ", " + current.j + "]";
            }
            else {
                System.out.print("[" + current.i + ", " + current.j + "]" + " -> ");
                przeszukiwanie = przeszukiwanie + " " + "[" + current.i + ", " + current.j + "]" + " -> ";
            }


            if (current.equals(grid[endI][endJ])) {
                return;
            }

            Cell t;

            if (current.i - 1 >= 0) {

                t = grid[current.i - 1][current.j];
                if(t.isBlock)
                    updateCost(current, t, current.cost + (VER_HOR_COST*BLOCK_COST), closed, open);
                else
                    updateCost(current, t, current.cost + VER_HOR_COST, closed, open);

                if (current.j - 1 >= 0) {
                    t = grid[current.i - 1][current.j - 1];
                    if(t.isBlock)
                        updateCost(current, t, current.cost + (DIAG_COST*BLOCK_COST), closed, open);
                    else
                        updateCost(current, t, current.cost + DIAG_COST, closed, open);
                }

                if (current.j + 1 < grid[0].length) {
                    t = grid[current.i - 1][current.j + 1];
                    if(t.isBlock)
                        updateCost(current, t, current.cost + (DIAG_COST*BLOCK_COST), closed, open);
                    else
                        updateCost(current, t, current.cost + DIAG_COST, closed, open);
                }
            }

            if (current.j - 1 >= 0) {
                t = grid[current.i][current.j - 1];
                if(t.isBlock)
                    updateCost(current, t, current.cost + (VER_HOR_COST*BLOCK_COST), closed, open);
                else
                    updateCost(current, t, current.cost + VER_HOR_COST, closed, open);
            }

            if (current.j + 1 < grid[0].length) {
                t = grid[current.i][current.j + 1];
                if(t.isBlock)
                    updateCost(current, t, current.cost + (VER_HOR_COST*BLOCK_COST), closed, open);
                else
                    updateCost(current, t, current.cost + VER_HOR_COST, closed, open);

            }

            if (current.i + 1 < grid.length) {

                t = grid[current.i + 1][current.j];
                if(t.isBlock)
                    updateCost(current, t, current.cost + (VER_HOR_COST*BLOCK_COST), closed, open);
                else
                    updateCost(current, t, current.cost + VER_HOR_COST, closed, open);

                if (current.j - 1 >= 0) {
                    t = grid[current.i + 1][current.j - 1];
                    if(t.isBlock)
                        updateCost(current, t, current.cost + DIAG_COST, closed, open);
                    else
                        updateCost(current, t, current.cost + (DIAG_COST*BLOCK_COST), closed, open);
                }

                if (current.j + 1 < grid[0].length) {
                    t = grid[current.i + 1][current.j + 1];
                    if (t.isBlock)
                        updateCost(current, t, current.cost + (DIAG_COST*BLOCK_COST), closed, open);
                    else
                        updateCost(current, t, current.cost + DIAG_COST, closed, open);
                }
            }
//            moveCount = moveCount + 1;
        }
    }

    //Wyświetla świat
    public void displayGrid() {

        System.out.println("Swiat: ");

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if ((i == startI) && (j == startJ)) {
                    System.out.print("SP  "); // start cell
                } else if ((i == endI) && (j == endJ)) {
                    System.out.print("KP  "); // end cell
                } else if (grid[i][j].isBlock) {
                    System.out.print("BK  "); // blocked cell
                } else if (grid[i][j] != null) {
                    System.out.printf("%-3d ", 0); // open cell
                }
            }
            System.out.println();
        }
        System.out.println("0: Wolne pole BK: Przeszkoda KP: Pole końcowe SP: Pole startowe\n");
    }

    //Wyświetla rozwiązanie
    public void displayPath(Cell grid[][]) {

        moveCount = 0;
        if (closed[endI][endJ]) {

            Stack<Cell> stack = new Stack<Cell>();
            Cell current = grid[endI][endJ];
            stack.add(current);
            grid[current.i][current.j].isSolution = true;

            while (current.parent != null) {

                stack.add(current.parent);

                grid[current.parent.i][current.parent.j].isSolution = true;

                current = current.parent;
            }

            sciezka = "";
            System.out.println();
            System.out.print("Scieżka: ");
            while (!stack.isEmpty()) {
                Cell c = stack.pop();
                if (stack.size() > 0) {
                    System.out.print(c + " -> ");
                    sciezka = sciezka + " " + c + " -> ";
                } else {
                    System.out.print(c);
                    sciezka = sciezka + " " + c;
                }
                moveCount = moveCount + 1;
            }

            System.out.println();
            System.out.println("Ruchy1: " + (moveCount-1));

            System.out.println("\n\nRozwiązanie:");

            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if ((i == startI) && (j == startJ)) {
                        System.out.print("SP  "); // start cell
                    } else if (grid[i][j].isBlock) {
                        System.out.print("BK  "); // blocked cell
                    } else if ((i == endI) && (j == endJ)) {
                        System.out.print("KP  "); // end cell
                    } else if (grid[i][j] != null) {
                        System.out.printf("%-3s ", grid[i][j].isSolution ? "X" : "0");
                    }
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public void displayHeuristic(Cell grid[][]) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.printf("%.2f ", grid[i][j].heuristic);
            }
            System.out.println();
        }
        System.out.println();
    }

    public void displayScore(Cell grid[][]) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.printf("%.2f ", grid[i][j].cost);
            }
            System.out.println();
        }
        System.out.println();
    }
}

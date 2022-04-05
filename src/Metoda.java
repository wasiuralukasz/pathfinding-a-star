import java.util.PriorityQueue;
import java.util.Stack;

public class Metoda {

    //koszt dla ruchu w jednym z ośmiu kierunków
    public static final int VER_HOR_COST  = 1;
    public static final double DIAG_COST = Math.sqrt(2);

    //koszt w momencie gdy znajduje się tam przeszkoda
    public static final int BLOCK_COST = 3;

    public Cell[][] grid, grid1;

    public Cell current, current1;

    public PriorityQueue<Cell>  open, open1;
    public boolean[][] closed, closed1;

    //Start
    public int startI, startJ;

    //Koniec
    public int endI, endJ;

    int moveCount = 0;
    int moveCount1 = 1;

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

    public void startCell(int i, int j) {
        startI = i;
        startJ = j;
    }

    public void endCell(int i, int j) {
        endI = i;
        endJ = j;
    }

    public void updateCost(Cell current, Cell t, double cost, boolean[][] closed, PriorityQueue<Cell>  open) {

        if ((t == null) || closed[t.i][t.j]) {
            return;
        }
        double tFinalCost = t.heuristic + cost;

        boolean isOpen = open.contains(t);

        if (!isOpen || (tFinalCost < t.cost)) {

            t.cost = tFinalCost;
            t.parent = current;

            if (!isOpen) {
                open.add(t);
            }
        }
    }

    public void search(Cell current, boolean[][] closed, PriorityQueue<Cell>  open) {

        open.add(grid[startI][startJ]);

        while (true) {

            current = open.poll();

            if (current == null) {
                break;
            }

            closed[current.i][current.j] = true;

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
            moveCount = moveCount + 1;
        }
    }

    public void search1(Cell current1, boolean[][] closed, PriorityQueue<Cell>  open) {

        open.add(grid1[startI][startJ]);


        while (true) {

            current1 = open.poll();

            if (current1 == null) {
                break;
            }

            closed[current1.i][current1.j] = true;

            if (current1.equals(grid1[endI][endJ])) {
                return;
            }

            Cell t;

            if (current1.i - 1 >= 0) {

                t = grid1[current1.i - 1][current1.j];
                if(t.isBlock)
                    updateCost(current1, t, current1.cost + (VER_HOR_COST*BLOCK_COST), closed, open);
                else
                    updateCost(current1, t, current1.cost + VER_HOR_COST, closed, open);

                if (current1.j - 1 >= 0) {
                    t = grid1[current1.i - 1][current1.j - 1];
                    if(t.isBlock)
                        updateCost(current1, t, current1.cost + (VER_HOR_COST*BLOCK_COST), closed, open);
                    else
                        updateCost(current1, t, current1.cost + VER_HOR_COST, closed, open);
                }

                if (current1.j + 1 < grid1[0].length) {
                    t = grid1[current1.i - 1][current1.j + 1];
                    if(t.isBlock)
                        updateCost(current1, t, current1.cost + (VER_HOR_COST*BLOCK_COST), closed, open);
                    else
                        updateCost(current1, t, current1.cost + VER_HOR_COST, closed, open);
                }
            }

            if (current1.j - 1 >= 0) {
                t = grid1[current1.i][current1.j - 1];
                if(t.isBlock)
                    updateCost(current1, t, current1.cost + (VER_HOR_COST*BLOCK_COST), closed, open);
                else
                    updateCost(current1, t, current1.cost + VER_HOR_COST, closed, open);
            }

            if (current1.j + 1 < grid1[0].length) {
                t = grid1[current1.i][current1.j + 1];
                if(t.isBlock)
                    updateCost(current1, t, current1.cost + (VER_HOR_COST*BLOCK_COST), closed, open);
                else
                    updateCost(current1, t, current1.cost + VER_HOR_COST, closed, open);

            }

            if (current1.i + 1 < grid1.length) {

                t = grid1[current1.i + 1][current1.j];
                if(t.isBlock)
                    updateCost(current1, t, current1.cost + (VER_HOR_COST*BLOCK_COST), closed, open);
                else
                    updateCost(current1, t, current1.cost + VER_HOR_COST, closed, open);

                if (current1.j - 1 >= 0) {
                    t = grid1[current1.i + 1][current1.j - 1];
                    if(t.isBlock)
                        updateCost(current1, t, current1.cost + VER_HOR_COST, closed, open);
                    else
                        updateCost(current1, t, current1.cost + (VER_HOR_COST*BLOCK_COST), closed, open);
                }

                if (current1.j + 1 < grid1[0].length) {
                    t = grid1[current1.i + 1][current1.j + 1];
                    if (t.isBlock)
                        updateCost(current1, t, current1.cost + (VER_HOR_COST*BLOCK_COST), closed, open);
                    else
                        updateCost(current1, t, current1.cost + VER_HOR_COST, closed, open);
                }
            }
            moveCount1 = moveCount1 + 1;
        }
    }

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

    public void displayPath(Cell grid[][]) {

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

            System.out.print("Scieżka: ");
            while (!stack.isEmpty()) {
                Cell c = stack.pop();
                if (stack.size() > 0) {
                    System.out.print(c + " -> ");
                } else {
                    System.out.print(c);
                }
            }

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

}

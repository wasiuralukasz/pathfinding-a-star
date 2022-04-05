public class Cell {

    //Współrzędne
    public int i;
    public int j;

    //pole rodizca dla trasy ?
    public Cell parent;

    //heurystyka
    public double heuristic;

    //koszt całkowity
    public double cost;

    //czy pole jest blokiem
    public boolean isBlock;

    //jeśli prawda to jest to rozwiązanie
    public boolean isSolution;

    public Cell(int i, int j) {
    this.i = i;
    this.j = j;
    }

    @Override
    public String toString() {
        return "[" + i + ", " + j + "]";
    }
}

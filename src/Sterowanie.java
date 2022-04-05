import java.util.Scanner;

public class Sterowanie {
    public static void main(String[] args) {

        int width, height, sI, sJ, eI, eJ;
        boolean isCorrect = false;
        Scanner in = new Scanner(System.in);

        System.out.print("Wprowadz szerokość świata: ");
        width = in.nextInt();
        System.out.print("Wprowadz wysokość świata: ");
        height = in.nextInt();

        do {
            System.out.print("Wprowadz współrzędną X pola startowego: ");
            sJ = in.nextInt();
            if(sJ > width - 1) {
                System.out.println("Niepoprawna wpsółrzędna!!! Spróbuj ponownie");
                isCorrect = false;
            }
            else
                isCorrect = true;
        } while (!isCorrect);

        do {
            isCorrect = false;
            System.out.print("Wprowadz współrzędną Y pola startowego: ");
            sI = in.nextInt();
            if(sI > height - 1) {
                System.out.println("Niepoprawna wpsółrzędna!!! Spróbuj ponownie");
                isCorrect = false;
            }
            else
                isCorrect = true;
        } while (!isCorrect);

        do {
            isCorrect = false;
            System.out.print("Wprowadz współrzędną X pola końcowego: ");
            eJ = in.nextInt();
            if(eJ > width - 1) {
                System.out.println("Niepoprawna wpsółrzędna!!! Spróbuj ponownie");
                isCorrect = false;
            }
            else
                isCorrect = true;
        } while (!isCorrect);

        do {
            isCorrect = false;
            System.out.print("Wprowadz współrzędną Y pola końcowego: ");
            eI = in.nextInt();
            if(eI > height - 1 || (eI == sI && eJ == sJ)) {
                System.out.println("Niepoprawna wpsółrzędna!!! Spróbuj ponownie");
                isCorrect = false;
            }
            else
                isCorrect = true;
        } while (!isCorrect);
        System.out.println();

        Problem problem = new Problem(height, width, sI, sJ, eI, eJ);
    }
}

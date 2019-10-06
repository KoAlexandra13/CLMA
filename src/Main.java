import Functions.Functions;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Введите \"k\": ");
        int k = in.nextInt();
        in.close();
        Double alpha = 0.5 * k;
        Double[][] initialMatrix = {
                {3.81, 0.25, 1.28, 0.75 + alpha},
                {2.25, 1.32, 4.58 + alpha, 0.49},
                {5.31, 6.28 + alpha, 0.98, 1.04},
                {9.39 + alpha, 2.45, 3.35, 2.28}};
        Double[][] freeTerms = {
                {4.21},
                {6.47 + alpha},
                {2.38},
                {10.48 + alpha}};
        System.out.println("\nИсходная матрица");
        Functions.matrixOut(initialMatrix);

        Double[][] matrixTranspose = Functions.transpose(initialMatrix);
        System.out.println("\nТранспонированная матрица:");
        Functions.matrixOut(matrixTranspose);

        System.out.println("\n(матрица * транспонированная):");
        Double[][] mainMatrix = Functions.multiplyMatrix(initialMatrix, matrixTranspose);
        Functions.matrixOut(mainMatrix);

        System.out.println("\n(матрица транспонированная * f):");
        Functions.matrixOut(Functions.multiplyMatrix(matrixTranspose, freeTerms));

        // Прямой ход
        ArrayList<Object> matrixList = Functions.forward(mainMatrix, freeTerms);
        Double[][] matrixS = (Double[][])matrixList.get(0);
        Double[][] matrixD = (Double[][])matrixList.get(1);
        Double determinant = (Double)matrixList.get(2);
        System.out.println("\nB:");
        Functions.matrixOut(Functions.multiplyMatrix(matrixD, matrixS));

        ArrayList<Object> matrixList2 = Functions.backward(matrixS, matrixD,freeTerms);

        System.out.println("\nY:");
        Functions.matrixOut((Double[][])matrixList2.get(0));

        System.out.println("\nX:");
        Functions.matrixOut((Double[][])matrixList2.get(1));

        System.out.println("\nОпределитель: " + determinant);
    }
}

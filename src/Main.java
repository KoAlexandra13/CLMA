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
        // Прямой ход
        ArrayList<Object> matrixList = Functions.gaussianElimination(initialMatrix, freeTerms);
        Double[][] triangulisedMatrix = (Double[][])matrixList.get(0);
        Double[][] triangulisedFreeTerms = (Double[][])matrixList.get(1);
        Double[][] unitMatrixAfterTriangularization = (Double[][])matrixList.get(2);
        Double determinant = (Double)matrixList.get(3);
        System.out.print("\nТреугольная матрица:\n");
        Functions.matrixOut(triangulisedMatrix, triangulisedFreeTerms);

        // Обратный ход
        Double[][] xVector = Functions.findVariables(triangulisedMatrix, triangulisedFreeTerms);
        System.out.println("\nВектор неизвестных:");
        Functions.matrixOut(xVector);

        // Обратная матрица
        Double[][] inverseMatrix = Functions.findReverseMatrix(triangulisedMatrix, unitMatrixAfterTriangularization);
        System.out.print("\nВектор невязки:\n");

        // Невязка
        Double[][] misalignment = Functions.misalignment(initialMatrix, xVector, freeTerms);
        for (int i = 0; i < misalignment.length; i++)
            System.out.printf("dx[%d] = %E\n", i, misalignment[i][0]);
        System.out.println("\nОбратная матрица: ");
        Functions.matrixOut(inverseMatrix);
        System.out.print("\nОпределитель: ");
        System.out.println(determinant);

        // Проверка
        System.out.println("\nПроверка (матрица * обратная):");
        Functions.matrixOut(Functions.multiplyMatrix(initialMatrix, inverseMatrix));
    }
}

package Functions;
import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;

import static java.lang.Math.sqrt;

public class Functions {

    private static Double[][] deepcopy(Double[][] arrayToCopy) {
        Double[][] array = new Double[arrayToCopy.length][arrayToCopy[0].length];
        for (int i = 0; i < array.length; i++)
            for (int j = 0; j < array[0].length; j++)
                array[i][j] = arrayToCopy[i][j];
        return array;
    }

    private static void filling(Double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = 0.d;
            }
        }
    }

    public static Double[][] transpose(Double[][] matrix) {
        Double[][] transposeMatrix = new Double[matrix.length][matrix[0].length];
        for (int i = 0; i < transposeMatrix.length; i++) {
            for (int j = 0; j < transposeMatrix[0].length; j++) {
                transposeMatrix[i][j] = matrix[j][i];
            }
        }
        return transposeMatrix;
    }

    public static Double[][] multiplyMatrix(Double[][] ar1, Double[][] ar2) {
        Double[][] result = new Double[ar1.length][ar2[0].length];
        for (int i = 0; i < result.length; i++)
            for (int j = 0; j < result[0].length; j++)
                result[i][j] = 0.;
        for (int i = 0; i < ar1.length; i++)
            for (int j = 0; j < ar2[0].length; j++)
                for (int k = 0; k < ar2.length; k++)
                    result[i][j] += ar1[i][k] * ar2[k][j];
        return result;
    }

    public static void matrixOut(Double[][] A, Double[][] F) {
        for (int i = 0; i < A.length; i++) {
            System.out.print("[");
            for (int j = 0; j < A[0].length; j++)
                System.out.printf("% 8.5f", A[i][j]);
            System.out.print("|");
            for (int j = 0; j < F[0].length; j++)
                System.out.printf("%8.5f", F[i][j]);
            System.out.println("]");
        }
    }

    public static void matrixOut(Double[][] A) {
        for (Double[] row : A) {
            System.out.print("[");
            for (Double element : row)
                System.out.printf("% 8.5f", element);
            System.out.println("]");
        }
    }

    public static ArrayList<Object> forward(Double[][] matrix, Double[][] f) {
        Double[][] S = new Double[matrix.length][matrix[0].length];
        Functions.filling(S);
        Double[][] SPrime = new Double[matrix.length][matrix[0].length];
        Functions.filling(SPrime);
        Double[][] D = new Double[matrix.length][matrix[0].length];
        Functions.filling(D);
        Double det = 1.d;
        for (int i = 0; i < S.length; i++) {
            Double underSquareValue = matrix[i][i];
            for (int k = 0; k < i; k++) {
                underSquareValue -= S[k][i] * S[k][i] * D[k][k];
            }
            D[i][i] = Math.signum(underSquareValue);
            SPrime[i][i] = S[i][i] = sqrt(underSquareValue * D[i][i]);
            det *= S[i][i] * S[i][i] * D[i][i];
            Double div = D[i][i] * S[i][i];
            for (int j = i + 1; j < S[0].length; j++) {
                Double nom = matrix[i][j];
                for (int k = 0; k < i; k++)
                    nom -= S[k][i] * S[k][j] * D[k][k];
                S[i][j] = SPrime[j][i] = nom / div;
            }
        }
        ArrayList<Object> matrixList = new ArrayList<Object>();
        matrixList.add(S);
        matrixList.add(D);
        matrixList.add(sqrt(det));
        return matrixList;
    }

    public static ArrayList<Object> findMatrixXAndY(Double[][] S, Double[][] D, Double[][] f) {
        Double[][] X = new Double[S.length][1];
        Functions.filling(X);
        Double[][] Y = new Double[S.length][1];
        Functions.filling(Y);
        for (int i = 0; i < S.length; i++) {
            Double nom = f[i][0];
            for (int k = 0; k < i; k++)
                nom -= S[k][i] * Y[k][0];
            Y[i][0] = nom / S[i][i];

            for (int j = S.length - 1; j > -1; j--) {
                nom = Y[j][0];
                for (int k = j + 1; k < S.length; k++)
                    nom -= D[j][j] * S[j][k] * X[k][0];
                X[j][0] = nom / (D[j][j] * S[j][j]);
            }
        }
        ArrayList<Object> matrixList = new ArrayList<Object>();
        matrixList.add(Y);
        matrixList.add(X);
        return matrixList;
    }
}



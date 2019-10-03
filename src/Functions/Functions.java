package Functions;
import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;

public class Functions {

    private static Double[][] deepcopy(Double[][] arrayToCopy){
        Double[][] array = new Double[arrayToCopy.length][arrayToCopy[0].length];
        for (int i = 0; i < array.length; i++)
            for (int j = 0; j < array[0].length; j++)
                array[i][j] = arrayToCopy[i][j];
        return array;
    }

    public static Double[][] unitMatrix (int size){
        Double[][] matrix = new Double[size][size];
        for( int i = 0; i < size; i++){
            for (int j= 0; j < size; j++ ){
                matrix[i][j] = 0.;
            }
        }
        for (int j= 0; j < size; j++ ){
            matrix[j][j] = 1.;
        }
        return matrix;
    }

    public static Integer maxElementIndex(Double [][] matrix, int k){
        Double maxElement = matrix [k][k];
        Integer maxIndex = k;
        for (int i = k; i < matrix.length; i++){
            if (matrix[i][k] > maxElement){
                maxElement = matrix[i][k];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public static void rearrangement(Double[][] matrix, Integer line1, Integer line2){
        Double[] tmp = matrix[line1];
        matrix[line1] = matrix[line2];
        matrix[line2] = tmp;
    }

    public static ArrayList<Object> gaussianElimination (Double[][] matrix, Double[][] f){
        Double[][] A = deepcopy(matrix);
        Double[][] F = deepcopy(f);
        Double[][] E = Functions.unitMatrix(A.length);
        Double determinant = 1.;
        for (int i = 0; i < A.length; i++){
             Integer maxElementIndex = maxElementIndex(A,i);
             rearrangement(A, i, maxElementIndex);
             rearrangement(F, i, maxElementIndex);
             rearrangement(E, i, maxElementIndex);
            double keyElement = A[i][i];
            determinant *= keyElement;
            if (i != maxElementIndex)
                determinant *= -1;
            for (int j = i+1; j < A.length; j++){
                double firstInRow = A[j][i];
                for (int k=0; k<A.length; k++) {
                    A[j][k] -= firstInRow * A[i][k] / keyElement;
                    E[j][k] -= firstInRow * E[i][k] / keyElement;
                }
                for (int k=0; k < F[0].length; k++)
                    F[j][k] -= firstInRow * F[i][k] / keyElement;
            }
            for (int l = A.length - 1; l > -1; l--) {
                A[i][l] /= keyElement;
                E[i][l] /= keyElement;
            }
            for (int l = F[0].length - 1; l > -1; l--)
                F[i][l] /= keyElement;
        }
        ArrayList<Object> matrixList = new ArrayList<Object>();
        matrixList.add(A); matrixList.add(F); matrixList.add(E); matrixList.add(determinant);
        return matrixList;
    }

    public static Double[][] findVariables(Double[][] A, Double[][] F){
        int n = A.length;
        Double[][] X = new Double[n][F[0].length];
        for( int i = 0; i < n; i++){
            for (int j= 0; j < X[0].length; j++ ){
                X[i][j] = 0.;
            }
        }

        for (int i=0; i<F[0].length; i++){
            X[n-1][i] = F[n-1][i];
            for (int j=n-1; j>-1; j--){
                X[j][i] = F[j][i];
                for (int k = j+1; k<n; k++)
                    X[j][i] -= A[j][k] * X[k][i];
            }
        }
        return X;
    }

    public static Double[][] findReverseMatrix(Double[][] initialMatrixAfterTriangularization, Double[][] unitMatrixAfterTriangularization){
        Double[][] freeTermsColumn = new Double[unitMatrixAfterTriangularization.length][1];
        Double[][] reverseMatrix = new Double[unitMatrixAfterTriangularization.length][unitMatrixAfterTriangularization[0].length];
        for (int i=0; i < initialMatrixAfterTriangularization.length; i++){
            for (int j = 0; j < initialMatrixAfterTriangularization[0].length; j++){
                freeTermsColumn[j][0] = unitMatrixAfterTriangularization[j][i];
            }
            Double[][] XColumn = findVariables(initialMatrixAfterTriangularization, freeTermsColumn);
            for (int j = 0; j < reverseMatrix[0].length; j++){
                reverseMatrix[j][i] = XColumn[j][0];
            }
        }
        return reverseMatrix;
    }

    public static void matrixOut(Double[][] A, Double[][] F){
        for (int i=0; i < A.length; i++){
            System.out.print("[");
            for (int j = 0; j < A[0].length; j++)
                System.out.printf("% 8.5f", A[i][j]);
            System.out.print("|");
            for (int j = 0; j < F[0].length; j++)
                System.out.printf("%8.5f", F[i][j]);
            System.out.println("]");
        }
    }

    public static void matrixOut(Double[][] A){
        for(Double[] row: A){
            System.out.print("[");
            for (Double element: row)
                System.out.printf("% 8.5f", element);
            System.out.println("]");
        }
    }
    public static Double[][] multiplyMatrix(Double[][] ar1, Double[][] ar2){
        Double[][] result = new Double[ar1.length][ar2[0].length];
        for (int i = 0; i<result.length; i++)
            for (int j = 0; j< result[0].length; j++)
                result[i][j] = 0.;
        for (int i=0; i<ar1.length; i++)
            for (int j = 0; j<ar2[0].length; j++)
                for (int k = 0; k<ar2.length; k++)
                    result[i][j] += ar1[i][k] * ar2[k][j];
        return result;
    }

    public static Double[][] misalignment(Double[][] matrix, Double[][] array,Double[][] f){
        Double[][] A = deepcopy(matrix);
        Double[][] X = deepcopy(array);
        Double[][] F = deepcopy(f);
        Double[][] AX = multiplyMatrix(A,X);
        Double [][] misalignment = new Double[X.length][X[0].length];
        for (int i =0; i < F.length; i++)
            for (int j = 0; j<F[0].length; j++) {
                misalignment[i][j] = F[i][j] - AX[i][j];
            }
        return misalignment;
    }
}



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

    private static void filling(Double[][] matrix){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = 0.d;
            }
        }
    }

    public static Double[][] transpose(Double[][] matrix){
        Double[][] transposeMatrix = new Double[matrix.length][matrix[0].length];
        for (int i = 0; i < transposeMatrix.length; i++){
            for (int j = 0; j < transposeMatrix[0].length; j++){
                transposeMatrix[i][j] = matrix[j][i];
            }
        }
        return transposeMatrix;
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

    public static ArrayList<Object> forward ( Double[][] matrix, Double[][] f){
        Double[][] S = new Double[matrix.length][matrix[0].length];
        Functions.filling(S);
        Double[][] SPrime = new Double[matrix.length][matrix[0].length];
        Functions.filling(SPrime);
        Double[][] D = new Double[matrix.length][matrix[0].length];
        Functions.filling(D);
        Double det = 1.d;
        for ( int i = 0; i < S.length; i++){
            Double underSquareValue = matrix[i][i];
            for ( int k = 0; k < i; k++) {
                underSquareValue -= S[k][i] * S[k][i] * D[k][k];
            }
            D[i][i] = Math.signum(underSquareValue);
            SPrime[i][i] = S[i][i] = Math.sqrt(underSquareValue*D[i][i]);
            det *= S[i][i] * S[i][i] * D[i][i];
            Double div = D[i][i] * S[i][i];
            for ( int j = i+1; j < S[0].length; j++){
                Double nom = matrix[i][j];
                for ( int k = 0; k < i; k++)
                    nom -= S[k][i] * S[k][j] * D[k][k];
                S[i][j] = SPrime[j][i] = nom/div;
            }
        }
        ArrayList<Object> matrixList = new ArrayList<Object>();
        matrixList.add(S); matrixList.add(D); matrixList.add(det);
        return matrixList;
    }

    public static ArrayList<Object> backward ( Double[][] S, Double[][] D, Double[][] f) {
        Double[][] X = new Double[S.length][1];
        Functions.filling(X);
        Double[][] Y = new Double[S.length][1];
        Functions.filling(Y);
        for ( int i = 0; i < S.length; i++){
            Double nom = f[i][0];
            for (int k = 0; k < i; k++)
                nom -= S[k][i] * Y[k][0];
            Y[i][0] = nom/S[i][i];

            for ( int j = S.length - 1; j > -1; j--) {
                nom = Y[j][0];
                for (int k = j + 1; k < S.length; k++)
                    nom -= D[j][j] *  S[j][k] * X[k][0];
                X[j][0] = nom / (D[j][j] * S[j][j]);
            }
        }
        ArrayList<Object> matrixList = new ArrayList<Object>();
        matrixList.add(Y); matrixList.add(X);
        return matrixList;
    }
    /*public static Double[][] unitMatrix (int size){
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
    }*/

    /*public static Integer maxElementIndex(Double [][] matrix, int k){
        Double maxElement = matrix [k][k];
        Integer maxIndex = k;
        for (int i = k; i < matrix.length; i++){
            if (matrix[i][k] > maxElement){
                maxElement = matrix[i][k];
                maxIndex = i;
            }
        }
        return maxIndex;
    }*/

    /*public static void rearrangement(Double[][] matrix, Integer line1, Integer line2){
        Double[] tmp = matrix[line1];
        matrix[line1] = matrix[line2];
        matrix[line2] = tmp;
    }*/

    /*public static ArrayList<Object> gaussianElimination (Double[][] matrix, Double[][] f){
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
    }*/

    /*public static Double[][] findVariables(Double[][] A, Double[][] F){
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
    }*/

    /*public static Double[][] findReverseMatrix(Double[][] initialMatrixAfterTriangularization, Double[][] unitMatrixAfterTriangularization){
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
    }*/


   /* public static Double[][] misalignment(Double[][] matrix, Double[][] array,Double[][] f){
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
    }*/
}



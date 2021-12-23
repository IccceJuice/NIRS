package sample.model.algorithms;

import sample.utils.Utils;

public class PerceprHashAlgorithm implements Algorithm{
    @Override
    public int[][] process(int[][] matrix, int windowSize) {

        int[][] newMatrix = new int[matrix.length][matrix.length];
        matrix = Utils.matrixExpansionsZeros(matrix, windowSize/2);
        for (int i = 0; i < newMatrix.length; i++) {
            for (int j = 0; j < newMatrix.length; j++) {
                int pi = i;
                int pj = j;
                int midI = pi + windowSize / 2;
                int midJ = pj + windowSize / 2;
                int midPixel = matrix[midI][midJ];

                newMatrix[i][j] = getPerceptHashFromPixel(Utils.getWindowFromMatrix(matrix, midI, midJ, windowSize), midPixel);
            }
        }
        return newMatrix;
    }


    private int getPerceptHashFromPixel(int[][] window, int midP) {
//        return Utils.perceptHashForWindow(window, midP);
        return 0;
    }
}

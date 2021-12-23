package sample.model.algorithms;

import sample.utils.Utils;

public class AVGAlgorithm implements Algorithm{
    @Override
    public int[][] process(int[][] matrix, int windowSize) {
        int sum = 0;
        int abs = 0;
        int[][] newMatrix = new int[matrix.length][matrix.length];
        matrix = Utils.matrixExpansionsZeros(matrix, windowSize/2);
        for (int i = 0; i < newMatrix.length; i++) {
            for (int j = 0; j < newMatrix.length; j++) {
                sum = 0;
                for (int pi = i; pi < i + windowSize; pi++) {
                    for (int pj = j; pj < j + windowSize; pj++) {
                        sum += matrix[pi][pj];
                    }
                    abs = sum/(windowSize*windowSize);
                }
                newMatrix[i][j] = abs;
            }
        }
        return newMatrix;
    }

}

package sample.model.algorithms;

import sample.model.algorithms.Algorithm;
import sample.utils.Utils;

public class LBPAlgorithm implements Algorithm {

    @Override
    public int[][] process(int[][] matrix, int windowSize) {
        int[][] newMatrix = new int[matrix.length][matrix.length];
        matrix = Utils.matrixExpansionsZeros(matrix, windowSize/2);
        for (int i = 0; i < newMatrix.length; i++) {
            for (int j = 0; j < newMatrix.length; j++) {
                int pi = i;
                int pj = j;
                int midI = pi + windowSize/2;
                int midJ = pj + windowSize/2;
                int midPixel = matrix[midI][midJ];
                StringBuilder sb = new StringBuilder();
                for (; pi < i + windowSize; pi++) {
                    pj = j;
                    for (; pj < j + windowSize; pj++) {
                        if (pj != midJ || pi != midI)
                            if (matrix[pi][pj] < midPixel)
                                sb.append("0");
                            else sb.append("1");
                    }
                }
                newMatrix[i][j] = Integer.parseInt(sb.toString(), 2);
            }
        }
        return newMatrix;
    }
}

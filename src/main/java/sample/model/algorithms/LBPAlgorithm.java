package sample.model.algorithms;

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

                newMatrix[i][j] = getDigitLBPFromPixel(Utils.getWindowFromMatrix(matrix, midI, midJ, windowSize), midPixel);

//                StringBuilder sb = new StringBuilder();
//                for (; pi < i + windowSize; pi++) {
//                    pj = j;
//                    for (; pj < j + windowSize; pj++) {
//                        if (pj != midJ || pi != midI) {
//                            if (matrix[pi][pj] < midPixel) {
//                                sb.append("0");
//                            } else {
//                                sb.append("1");
//                            }
//                        }
//                    }
//                }
//                newMatrix[i][j] = Integer.parseInt(sb.toString(), 2);

            }
        }
        return newMatrix;
    }


    private int getDigitLBPFromPixel(int[][] window, int midP) {
        StringBuilder sb = new StringBuilder();
        int N = window.length;
        int M = window.length;
        int pij;

        int Ibeg = 0, Ifin = 0, Jbeg = 0, Jfin = 0;

        int k = 1;
        int i = 0;
        int j = 0;

        while (k < N * M){
            pij = window[i][j];
            if (i == Ibeg && j < M - Jfin - 1)
                ++j;
            else if (j == M - Jfin - 1 && i < N - Ifin - 1)
                ++i;
            else if (i == N - Ifin - 1 && j > Jbeg)
                --j;
            else
                --i;

            if ((i == Ibeg + 1) && (j == Jbeg) && (Jbeg != M - Jfin - 1)){
                ++Ibeg;
                ++Ifin;
                ++Jbeg;
                ++Jfin;
            }
            if (pij < midP) {
                sb.append("0");
            } else {
                sb.append("1");
            }
            ++k;
        }
        return Integer.parseInt(sb.toString(), 2);
    }
}

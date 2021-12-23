package sample.model.algorithms;

import sample.utils.Convolution;
import sample.utils.Utils;

public class LPDPAlgorithm implements Algorithm{

    @Override
    public int[][] process(int[][] matrix1, int windowSize) {
        double[][] matrix = Utils.convertIntToDoubleMat(matrix1);
        windowSize = 5;
        double[][] newMatrix = new double[matrix.length][matrix.length];
        matrix = Utils.matrixExpansionsZeros(matrix, windowSize/2);
        for (int i = 0; i < newMatrix.length; i++) {
            for (int j = 0; j < newMatrix.length; j++) {
                int pi = i;
                int pj = j;
                int midI = pi + windowSize / 2;
                int midJ = pj + windowSize / 2;
                int midPixel = (int) matrix[midI][midJ];

                newMatrix[i][j] = getDigitFromPixel(Utils.getWindowFromMatrix(matrix, midI, midJ, windowSize), midPixel);
            }
        }

        return Utils.convertDoubleToIntMat(newMatrix);
    }

    private double getDigitFromPixel(double[][] srcWindow, int midP) {
        StringBuilder sb = new StringBuilder();
        double[][] window = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                window[i][j] = srcWindow[i+1][j+1];
            }
        }
        int N = window.length;
        int M = window.length;
        double[][] newRegion = new double[window.length][window.length];
        int Ibeg = 0, Ifin = 0, Jbeg = 0, Jfin = 0;
        int k = 1;
        int i = 0;
        int j = 0;

        while (k < N * M){
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

            newRegion[i][j] = getDigitFromRegion(Utils.getWindowFromMatrix(srcWindow, i+1, j+1, 3));
            sb.append((int) newRegion[i][j]);
            ++k;
        }

        return Integer.parseInt(sb.toString(), 2);
    }

    private double getDigitFromRegion(double[][] region) {
        StringBuilder sb = new StringBuilder();
        int N = region.length;
        int M = region.length;
        double pij;
        double[][] newRegion = new double[region.length][region.length];
        int Ibeg = 0, Ifin = 0, Jbeg = 0, Jfin = 0;
        int positiveCount = 0;
        int k = 1;
        int i = 0;
        int j = 0;

        while (k < N * M){
            pij = region[i][j];
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

            newRegion[i][j] = Convolution.convolution2D(region, 3, 3, KirschMat.KirschMasks[k-1], 3, 3)[0][0];
            if (newRegion[i][j] > 0) positiveCount++;
            ++k;
        }

        return positiveCount > 3 ? 1 : 0;
    }


    static class KirschMat {
        static double[][] KM0 = {
                {-3, -3, 5},
                {-3, 0, 5},
                {-3, -3, 5}};
        static double[][] KM1 = {
                {-3, 5, 5},
                {-3, 0, 5},
                {-3, -3, -3}};
        static double[][] KM2 = {
                {5, 5, 5},
                {-3, 0, -3},
                {-3, -3, -3}};
        static double[][] KM3 = {
                {5, 5, -3},
                {5, 0, -3},
                {-3, -3, -3}};
        static double[][] KM4 = {
                {5, -3, -3},
                {5, 0, -3},
                {5, -3, -3}};
        static double[][] KM5 = {
                {-3, -3, -3},
                {5, 0, -3},
                {5, 5, -3}};
        static double[][] KM6 = {
                {-3, -3, -3},
                {-3, 0, -3},
                {5, 5, 5}};
        static double[][] KM7 = {
                {-3, -3, -3},
                {-3, 0, 5},
                {-3, 5, 5}};
        static double[][][] KirschMasks= {KM0, KM1, KM2, KM3, KM4, KM5, KM6, KM7};
    }
}

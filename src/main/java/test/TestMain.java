package test;

import sample.utils.Utils;

public class TestMain {
    public static void main(String[] args) {
        double[][] matrix = new double[][] {{1,3,0}, {0,3,1}, {4,1,2}};

        double[][] newMatrix = Utils.slidingWindowTreatment(matrix, 3);
        Utils.printMatrix(Utils.matrixRoundToInt(newMatrix));

    }
}

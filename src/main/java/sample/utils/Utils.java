package sample.utils;

public class Utils {



    public static double[][] getZeroSquareMatrix(int size) {
        double[][] array = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0 ; j < size; j++) {
                array[i][j] = 0;
            }
        }
        return array;
    }

    public static double[][] slidingWindowTreatment(double[][] matrix, int windowSize) {
        double sum = 0;
        double abs = 0;
        double[][] newMatrix = new double[matrix.length][matrix.length];
        matrix = matrixExpansionsZeros(matrix, windowSize/2);
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

    public static double[][] filterWindowTreatment(double[][] matrix, double[][] kernel) {
        double sum = 0;
        int windowSize = kernel.length;
        double[][] newMatrix = new double[matrix.length][matrix.length];
        matrix = matrixExpansionsZeros(matrix, windowSize/2);
        for (int i = 0; i < newMatrix.length; i++) {
            for (int j = 0; j < newMatrix.length; j++) {
                sum = 0;
                for (int pi = 0; pi < windowSize; pi++) {
                    for (int pj = 0; pj < windowSize; pj++) {
                        sum += matrix[i + pi][j + pj] * kernel[pi][pj];
                    }
                }
                newMatrix[i][j] = Math.abs(sum);
            }
        }
        return newMatrix;
    }

    public static int[][] matrixRoundToInt(double[][] matrix) {
        int[][] newMatrix = new int[matrix.length][matrix.length];
        for (int i = 0; i < newMatrix.length; i++) {
            for (int j = 0; j < newMatrix.length; j++) {
                newMatrix[i][j] = (int) Math.round(matrix[i][j]);
            }
        }
        return newMatrix;
    }


    /**
     *
     * @param matrix - расширяемая матрица
     * @param onWidth - сколько строк/столбцов с каждой стороны нужно дополнить нулями
     * @return
     */
    public static double[][] matrixExpansionsZeros(double[][] matrix, int onWidth) {
        double[][] newMatrix = getZeroSquareMatrix(matrix.length + onWidth*2);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                newMatrix[i + onWidth][j + onWidth] =  matrix[i][j];
            }
        }
        return newMatrix;
    }

    public static void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
    public static int[][] convertArrayToMatrix(int[] array, int sideLength) {
        int[][] matrix = new int[sideLength][sideLength];
        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength; j++) {
                matrix[i][j] = array[i*sideLength + j];
            }
        }
        return matrix;
    }

    public static double[][] intToDoubleMatrix(int[][] matrix) {
        double[][] newMatrix = new double[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                newMatrix[i][j] = matrix[i][j];
            }
        }
        return newMatrix;
    }

    public static int[] matrixToArray(int[][] matrix) {
        int[] newMatrix = new int[matrix.length * matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                newMatrix[i*matrix.length + j] = matrix[i][j];
            }
        }
        return newMatrix;
    }
}

package sample.utils;

import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import com.sun.imageio.plugins.jpeg.JPEGImageWriterSpi;
import com.sun.imageio.plugins.png.PNGImageWriter;
import com.sun.imageio.plugins.png.PNGImageWriterSpi;

import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static int getRed(int color)        { return color >> 16; }         // получить красную составляющую цвета
    public static int getGreen(int color)      { return (color >> 8) & 0xFF; } // получить зеленую составляющую цвета
    public static int getBlue(int color)       { return color & 0xFF;}        // получить синюю составляющую цвета

    public static double[][] getZeroSquareMatrix(int size) {
        double[][] array = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0 ; j < size; j++) {
                array[i][j] = 0;
            }
        }
        return array;
    }

    public static int[][] getZeroSquareMatrixInt(int size) {
        int[][] array = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0 ; j < size; j++) {
                array[i][j] = 0;
            }
        }
        return array;
    }

    public static HashMap<Short, Short>[] slidingWindowForHistograms(double[][] matrix, int windowSize) {
        int matrixSize = matrix.length;
        double[][] tempWinMatrix = new double[windowSize][windowSize];
        matrix = matrixExpansionsZeros(matrix, windowSize/2);
        int countFields = matrixSize/windowSize * matrixSize/windowSize;
        ArrayList<HashMap<Short, Short>> histograms = new ArrayList<>();
        for (int i = 0; i < matrixSize; i+=windowSize) {
            for (int j = 0; j < matrixSize; j+=windowSize) {
                for (int pi = i; pi < i + windowSize; pi++) {
                    for (int pj = j; pj < j + windowSize; pj++) {
                        tempWinMatrix[pi - i][pj - j] = matrix[pi][pj];
                    }
                }
                histograms.add(Utils.createHistFromWindow(Utils.matrixRoundToShort(tempWinMatrix)));
            }
        }
        HashMap<Short, Short>[] arr = new HashMap[countFields];
        System.arraycopy(histograms.toArray(), 0, arr, 0, countFields);
        return arr;
    }

    public static int[][] filterWindowTreatment(int[][] matrix, int[][] kernel) {
        double sum = 0;
        int windowSize = kernel.length;
        int[][] newMatrix = new int[matrix.length][matrix.length];
        matrix = matrixExpansionsZeros(matrix, windowSize/2);
        for (int i = 0; i < newMatrix.length; i++) {
            for (int j = 0; j < newMatrix.length; j++) {
                sum = 0;
                for (int pi = 0; pi < windowSize; pi++) {
                    for (int pj = 0; pj < windowSize; pj++) {
                        sum += matrix[i + pi][j + pj] * kernel[pi][pj];
                    }
                }
                newMatrix[i][j] = (int) Math.abs(sum);
            }
        }
        return newMatrix;
    }

    public static short[][] matrixRoundToShort(double[][] matrix) {
        short[][] newMatrix = new short[matrix.length][matrix.length];
        for (int i = 0; i < newMatrix.length; i++) {
            for (int j = 0; j < newMatrix.length; j++) {
                newMatrix[i][j] = (short) Math.round(matrix[i][j]);
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

    public static int[][] matrixExpansionsZeros(int[][] matrix, int onWidth) {
        int[][] newMatrix = getZeroSquareMatrixInt(matrix.length + onWidth*2);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                newMatrix[i + onWidth][j + onWidth] =  matrix[i][j];
            }
        }
        return newMatrix;
    }

    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
    public static int[][] arrayToMatrix(int[] array, int sideLength) {
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

    public static int[][] pixelsToNorm(int[][] pixelsMatrix) {
        int matrixSize = pixelsMatrix.length;
        int[][] normPixelsMatrix = new int[pixelsMatrix.length][pixelsMatrix.length];
        for (int i = 0; i < matrixSize; i++)
            for (int j = 0; j < matrixSize; j++) {
                // находим среднюю арифметическую интенсивность пикселя по всем цветам
                int intens = (getRed(pixelsMatrix[i][j]) +
                        getGreen(pixelsMatrix[i][j]) +
                        getBlue(pixelsMatrix[i][j])) / 3;
                normPixelsMatrix[i][j] = intens;
            }
        return normPixelsMatrix;
    }

    public static int[][] pixelsFromNormalPixels(int[][] normalPixels) {
        int[][] pixelsMatrix = new int[normalPixels.length][normalPixels.length];
        for (int i = 0; i < normalPixels.length; i++) {
            for (int j = 0; j < normalPixels.length; j++) {
                pixelsMatrix[i][j] = normalPixels[i][j] + (normalPixels[i][j] << 8) + (normalPixels[i][j] << 16);
            }
        }
        return pixelsMatrix;
    }

    public static void printMap(HashMap<Number, Number> map) {
        for(Map.Entry<Number, Number> entry: map.entrySet()) {
            System.out.print(entry.getKey() + ": " + entry.getValue() + "; ");
        }
    }

    public static HashMap createHistFromWindow(short[][] winValues) {
        int winSize = winValues.length;
        HashMap<Short, Short> map = new HashMap<>();
        for (int i = 0; i < winSize; i++) {
            for(int j = 0; j < winSize; j++) {
                if (map.containsKey(winValues[i][j])) {
                    short curDensity = map.get(winValues[i][j]).shortValue();
                    curDensity++;
                    map.put(winValues[i][j], curDensity);
                } else {
                    map.put(winValues[i][j], (short) 1);
                }
            }
        }
        return map;
    }

    public static void saveHistogramsToFile(Map<Short, Short>[] histograms, String filePath) throws IOException {
        FileOutputStream fout = new FileOutputStream(filePath);
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(histograms);
    }

    public static HashMap<Short, Short>[] readHistogramsFromFile(String filePath) throws IOException, ClassNotFoundException {
        FileInputStream fout = new FileInputStream(filePath);
        ObjectInputStream oos = new ObjectInputStream(fout);
//        Map<Short, Map<Short, Short>> histograms = (Map<Short, Map<Short, Short>>) oos.readObject();
        HashMap<Short, Short>[] histograms = (HashMap<Short, Short>[]) oos.readObject();
        return histograms;
    }

    public static void saveToImageFile(ImageWriter iw, String fileName, int[][] pixelMatrix) throws IOException {
        iw.setOutput(new FileImageOutputStream(new File(fileName)));
//        this.pixels = copyFromBufferedImage(this.bufferedImage);

        iw.write(copyToBufferedImage(matrixToArray(pixelsFromNormalPixels(pixelMatrix))));
        ((FileImageOutputStream) iw.getOutput()).close();
    }

    // Формирование массива пикселей из BufferedImage
    public static int[] copyFromBufferedImage(BufferedImage bi)  {
        int size = bi.getHeight();
        int[] pict = new int[size*size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                pict[i*size + j] = bi.getRGB(j, i) & 0xFFFFFF; // 0xFFFFFF: записываем только 3 младших байта RGB
            }
        return pict;
    }

    // Запись изображения в jpeg-формате
    public static void saveAsJpeg(String fileName, int[][] pixelMatrix) throws IOException {
        ImageWriter writer = new JPEGImageWriter(new JPEGImageWriterSpi());
        saveToImageFile(writer, fileName, pixelMatrix);
    }

    // Запись изображения в png-формате (другие графические форматы по аналогии)
    public static void saveAsPng(String fileName, int[][] pixelMatrix) throws IOException {
        ImageWriter writer = new PNGImageWriter(new PNGImageWriterSpi());
        saveToImageFile(writer, fileName, pixelMatrix);
    }

    // Формирование BufferedImage из массива pixels
    public static BufferedImage copyToBufferedImage(int[] pixels)  {
        int size = (int) Math.sqrt(pixels.length);
        BufferedImage bi = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++)
                bi.setRGB(j, i, pixels[i * size + j]);
        }
        return bi;
    }
}

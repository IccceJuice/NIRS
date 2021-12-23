package sample.utils;

import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import com.sun.imageio.plugins.jpeg.JPEGImageWriterSpi;
import com.sun.imageio.plugins.png.PNGImageWriter;
import com.sun.imageio.plugins.png.PNGImageWriterSpi;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

    public static short[] slidingWindowForHistograms(double[][] matrix, int windowSize) {
        int matrixSize = matrix.length;
        double[][] tempWinMatrix = new double[windowSize][windowSize];
//        matrix = matrixExpansionsZeros(matrix, windowSize/2);
        int countFields = matrixSize/windowSize * matrixSize/windowSize;
        short[] resultHist = new short[countFields];
        for (int i = 0; i < matrixSize; i+=windowSize) {
            for (int j = 0; j < matrixSize; j+=windowSize) {
                for (int pi = i; pi < i + windowSize && pi < matrixSize; pi++) {
                    for (int pj = j; pj < j + windowSize && pj < matrixSize; pj++) {
                        tempWinMatrix[pi - i][pj - j] = matrix[pi][pj];
                    }
                }
                resultHist = ArrayUtils.addAll(resultHist, Utils.createArrHistFromWindow(Utils.matrixRoundToShort(tempWinMatrix)));
            }
        }
//        HashMap<Short, Short>[] arr = new HashMap[countFields];
//        System.arraycopy(histograms.toArray(), 0, arr, 0, countFields);
        return resultHist;
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
    public static void printMatrix(double[][] matrix) {
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

    public static short[] createArrHistFromWindow(short[][] winValues) {
        int winSize = winValues.length;
        short[] hist = new short[256];
        for (int i = 0; i < winSize; i++) {
            for(int j = 0; j < winSize; j++) {
                hist[winValues[i][j]]++;
            }
        }
        return hist;
    }

    public static String perceptHashForWindow(int[][] window, int midPixValue) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < window.length; i++) {
            for (int j = 0; j < window.length; j++) {
                sb.append(window[i][j] < midPixValue ? "0" : "1");
            }
        }
        return sb.toString();
    }


    public static void saveHistogramsToFile(short[] histograms, String filePath) throws IOException {
        FileOutputStream fout = new FileOutputStream(filePath);
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(histograms);
    }

    public static short[] readHistogramsFromFile(String filePath) throws IOException, ClassNotFoundException {
        FileInputStream fout = new FileInputStream(filePath);
        ObjectInputStream oos = new ObjectInputStream(fout);
//        Map<Short, Map<Short, Short>> histograms = (Map<Short, Map<Short, Short>>) oos.readObject();
        short[] histograms = (short[]) oos.readObject();
        return histograms;
    }

    public static void saveToImageFile(ImageWriter iw, String fileName, int[][] pixelMatrix) throws IOException {
        iw.setOutput(new FileImageOutputStream(new File(fileName)));
//        this.pixels = copyFromBufferedImage(this.bufferedImage);

        iw.write(copyToBufferedImage(matrixToArray(pixelsFromNormalPixels(pixelMatrix))));
        ((FileImageOutputStream) iw.getOutput()).close();
    }


    /**
     * @param matrix - расширенная матрица (с нулями вокруг)
     * @param pi - i-ый индекс текущего пикселя (настоящий, а не нули расширенные)
     * @param pj - j-ый индекс текущего пикселя (настоящий, а не нули расширенные)
     * @param winSize - размер окна
     * @return - матрица размерами winSize X winSize, с центральным пикселем [pi][pj]
     */
    public static int[][] getWindowFromMatrix(int[][] matrix, int pi, int pj, int winSize) {

        int[][] newMatrix = new int[winSize][winSize];
        for (int oi = pi - winSize/2, ni = 0; oi <= pi + winSize/2 && ni < winSize; oi++, ni++) {
            for (int oj = pj - winSize/2, nj = 0; oj <= pj + winSize/2 && nj < winSize; oj++, nj++) {
                newMatrix[ni][nj] = matrix[oi][oj];
            }
        }
        return newMatrix;
    }

    public static int[][] convertDoubleToIntMat(double [][] mat) {
        int[][] newMat = new int[mat.length][mat.length];
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                newMat[i][j] = (int) mat[i][j];
            }
        }
        return newMat;
    }

    public static double[][] convertIntToDoubleMat(int [][] mat) {
        double[][] newMat = new double[mat.length][mat.length];
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                newMat[i][j] = mat[i][j];
            }
        }
        return newMat;
    }

    public static double[][] getWindowFromMatrix(double[][] matrix, int pi, int pj, int winSize) {

        double[][] newMatrix = new double[winSize][winSize];
        for (int oi = pi - winSize/2, ni = 0; oi <= pi + winSize/2 && ni < winSize; oi++, ni++) {
            for (int oj = pj - winSize/2, nj = 0; oj <= pj + winSize/2 && nj < winSize; oj++, nj++) {
                newMatrix[ni][nj] = matrix[oi][oj];
            }
        }
        return newMatrix;
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

    public static int[][] getWindowFromMatrix(int[][] matrix, int nWin, int winSize) {
        int pi = (nWin/(matrix.length/winSize))*winSize + winSize/2;
        int pj = (nWin%(matrix.length/winSize))*winSize + winSize/2;
        int[][] newMatrix = new int[winSize][winSize];
        for (int oi = pi - winSize/2, ni = 0; oi <= pi + winSize/2 && ni < winSize && oi < matrix.length; oi++, ni++) {
            for (int oj = pj - winSize/2, nj = 0; oj <= pj + winSize/2 && nj < winSize && oj < matrix.length; oj++, nj++) {
                newMatrix[ni][nj] = matrix[oi][oj];
            }
        }
        return newMatrix;
    }

    public static String[] getHashHistogram (int[][] proceedMat, int winSize) {

        int midPixValue = getMidFromMatrix(proceedMat);
        int nWindows = proceedMat.length/winSize * proceedMat.length/winSize;
        String[] hist = new String[nWindows];
        for (int i = 0; i < nWindows; i++) {
            hist[i] = perceptHashForWindow(getWindowFromMatrix(proceedMat, i, winSize), midPixValue);
        }
        return hist;
    }

    public static int getMidFromMatrix(int[][] matrix) {
        int sum = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                sum += matrix[i][j];
            }
        }
        return sum/(matrix.length*matrix.length);
    }

    public static LinkedHashMap<Integer, Integer> getIndexesOfEqualWins(String[] hashes) {
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < hashes.length; i++) {
            String curHash = hashes[i];
            for (int j = i + 1; j < hashes.length; j++) {
                if (getHemmingDistance(curHash, hashes[j]) <= 5) map.put(i, j);
            }
        }
        return map;
    }

    public static int getHemmingDistance(String s1, String s2) {
        String ns1 = new BigInteger(s1, 2).toString(16);
        String ns2 = new BigInteger(s2, 2).toString(16);
        char[] arr1 = ns1.toCharArray();
        char[] arr2 = ns2.toCharArray();
        int distance = 0;
        for (int i = 0; i < arr1.length && i < arr2.length; i++) {
            if (arr1[i] != arr2[i]) distance++;
        }
        return distance;
    }


}

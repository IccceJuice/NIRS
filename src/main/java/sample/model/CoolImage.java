package sample.model;

import com.sun.imageio.plugins.jpeg.JPEGImageReaderSpi;
import com.sun.imageio.plugins.png.PNGImageReader;
import sample.utils.Utils;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CoolImage {
    private int     height;             // высота изображения
    private int     width;              // ширина изображения
    private int[]   pixels;// собственно массив цветов точек составляющих изображение
    private int[][] pixelsMatrix;
    private int[][] normPixelsMatrix;

    public int getPixel(int x, int y)   { return pixels[y*width+x]; }   // получить пиксель в формате RGB
    public int getRed(int color)        { return color >> 16; }         // получить красную составляющую цвета
    public int getGreen(int color)      { return (color >> 8) & 0xFF; } // получить зеленую составляющую цвета
    public int getBlue(int color)       { return color & 0xFF;}        // получить синюю составляющую цвета

    // Конструктор - создание изображения из файла
    public CoolImage(String fileName) throws IOException {
        BufferedImage img = readFromTifFile(fileName);
        this.height = img.getHeight();
        this.width  = img.getWidth();
        this.pixels = Utils.copyFromBufferedImage(img);
        this.pixelsMatrix = Utils.arrayToMatrix(pixels, width);
        this.normPixelsMatrix = Utils.pixelsToNorm(pixelsMatrix);
    }

    public CoolImage(BufferedImage img) {
        this.height = img.getHeight();
        this.width  = img.getWidth();
        this.pixels = Utils.copyFromBufferedImage(img);
        this.pixelsMatrix = Utils.arrayToMatrix(pixels, width);
        this.normPixelsMatrix = Utils.pixelsToNorm(pixelsMatrix);
    }

    public CoolImage(int[][] sourcePixels) {
        this.height = sourcePixels.length;
        this.width  = sourcePixels.length;
        this.pixels = getPixelFromNormalPixels(sourcePixels);
        this.pixelsMatrix = Utils.arrayToMatrix(pixels, width);
        System.arraycopy(sourcePixels, 0, normPixelsMatrix, 0, sourcePixels.length);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    // Чтение изображения из файла в BufferedImage
    private BufferedImage readFromFile(String fileName) throws IOException {
        ImageReader r  = new PNGImageReader(new JPEGImageReaderSpi());
        r.setInput(new FileImageInputStream(new File(fileName)));
        BufferedImage  bi = r.read(0, new ImageReadParam());
        ((FileImageInputStream) r.getInput()).close();
        return bi;
    }

    private BufferedImage readFromTifFile(String fileName) throws IOException {
        BufferedImage image = ImageIO.read(new File(fileName));
        return image;
    }

    private int[] getPixelFromNormalPixels(int[][] normalPixels) {
        int width = normalPixels.length;
        int[] pixels = new int[width * width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                pixels[i * width + j] = normalPixels[i][j] + (normalPixels[i][j] << 8) + (normalPixels[i][j] << 16);
            }
        }
        return pixels;
    }


    // конвертация изображения в негатив
    private void  convertToNegative() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                // Применяем логическое отрицание и отбрасываем старший байт
                pixels[i*width + j] = ~pixels[i*width + j] & 0xFFFFFF;
    }

    // конвертация изображения в черно-белый вид
    private void convertToBlackAndWhite() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                // находим среднюю арифметическую интенсивность пикселя по всем цветам
                int intens = (getRed(pixels[i * width + j]) +
                        getGreen(pixels[i * width + j]) +
                        getBlue(pixels[i * width + j])) / 3;
                // ... и записываем ее в каждый цвет за раз , сдвигая байты RGB на свои места
                pixels[i * width + j] = intens + (intens << 8) + (intens << 16);
            }
    }

    private void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int intens = (getRed(pixels[i * width + j]) +
                            getGreen(pixels[i * width + j]) +
                            getBlue(pixels[i * width + j])) / 3;
                    System.out.print(intens + " ");
                }
                System.out.println();
            }
    }


    // изменяем интесивность зеленого цвета
    private void addColorGreenChannel(int delta) {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int newGreen =  getGreen(pixels[i * width + j]) + delta;
                if (newGreen > 255) newGreen=255;  // Отсекаем при превышении границ байта
                if (newGreen < 0)   newGreen=0;
                // В итоговом пикселе R и B цвета оставляем без изменений: & 0xFF00FF
                // Полученный новый G (зеленый) засунем в "серединку" RGB: | (newGreen << 8)
                pixels[i * width + j] = pixels[i * width + j] & 0xFF00FF | (newGreen << 8);
            }
    }

    public int[][] getHalftonePixelMatrix() {
        return normPixelsMatrix;
    }

}

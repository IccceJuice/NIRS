package sample.controller;

import com.sun.imageio.plugins.jpeg.JPEGImageReaderSpi;
import com.sun.imageio.plugins.png.PNGImageReader;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.model.CoolImage;
import sample.utils.Utils;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public TextField tfWinSize;
    public Button btnReadHist;
    public TextField tfHistPath;
    CoolImage coolImage;

    public TextField imagePathTf;
    public Button openImageBtn;
    public ImageView mainImgView;
    public Button highlightContour;
    FileChooser fileChooser;
    FileChooser histFileChooser;
    Stage primaryStage;
    private Desktop desktop = Desktop.getDesktop();
    String resultImgDir = "C:\\Users\\Pavel\\Desktop\\nirs\\fxNirs\\NIRS\\src\\main\\resources\\resultImages";
    String testImgDir = "C:\\Users\\Pavel\\Desktop\\nirs\\fxNirs\\NIRS\\src\\main\\resources\\testImages";
    String resultHistDir = "C:\\Users\\Pavel\\Desktop\\nirs\\fxNirs\\NIRS\\src\\main\\resources\\resultHistograms";
    String imgName;
    String histName;
    Map<Short, Short>[] histograms;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(testImgDir));
        histFileChooser = new FileChooser();
        histFileChooser.setInitialDirectory(new File(resultHistDir));
        tfWinSize.setText("15");

    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void openImage(ActionEvent actionEvent) {
        imagePathTf.clear();
        File file = fileChooser.showOpenDialog(primaryStage);
        String imagePath;
        if (file != null) {
            String filePath = file.getAbsolutePath();
            imagePath = filePath;
            imagePathTf.setText(filePath);
            String[] splitPath = imagePath.split("\\\\");
            imgName = splitPath[splitPath.length - 1];
            histName = imgName;
            histName = histName.split("\\.")[0] + "_hist";
            try {
                coolImage = new CoolImage(filePath);
                // An image file on the hard drive.
                String localUrl = file.toURI().toURL().toString();
                mainImgView.setImage(new Image(localUrl));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void highlightContour(ActionEvent actionEvent) throws IOException {
        int[][] newIntPixels = coolImage.getPixelsMatrix();
        int[][] kernel = new int[][] {{0, 1, 0}, {1, -4, 1}, {0, 1, 0}};
        int[] newArrayPixels = processPixelsWithKernel(newIntPixels, kernel);
        coolImage.copyToBufferedImage(newArrayPixels);
        coolImage.saveAsPng(resultImgDir + "\\" + imgName);
        mainImgView.setImage(new Image(pathToUrl(resultImgDir + "\\" + imgName)));
    }

    public void collectHistograms (ActionEvent actionEvent) throws IOException {
        int[][] newIntPixels = coolImage.getPixelsMatrix();
        histograms = collectHistogramsFromPixels(newIntPixels);
        Utils.saveHistogramsToFile(histograms, resultHistDir + "\\" + histName);
    }



    private int[] processPixelsWithKernel(int[][] pixels, int[][] kernel) {
        pixels = Utils.pixelsToNorm(pixels);
        pixels = Utils.filterWindowTreatment(pixels, kernel);
        pixels = Utils.pixelsFromNormalPixels(pixels);
        return Utils.matrixToArray(pixels);
    }

    private HashMap<Short, Short>[] collectHistogramsFromPixels(int[][] pixels) throws IOException {
        pixels = Utils.pixelsToNorm(pixels);
        double[][] doublePixels = Utils.intToDoubleMatrix(pixels);
        int winSize = tfWinSize.getText().isEmpty() ? 15 : Integer.valueOf(tfWinSize.getText());
        HashMap<Short, Short>[] histograms = Utils.slidingWindowForHistograms(doublePixels, winSize);
//        for (Map.Entry entry: histograms.entrySet()) {
//            Utils.printMap((HashMap<Number, Number>) entry.getValue());
//            System.out.println();
//        }
        return histograms;
    }





    private int[][] thresholding(int[][] pixels, int threshold) {
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0;j < pixels.length; j++) {
                if (pixels[i][j] < threshold) pixels[i][j] = 0;
                else pixels[i][j] = 255;
            }
        }
        return pixels;
    }



    public BufferedImage readFromFile(String fileName) throws IOException {
        ImageReader r  = new PNGImageReader(new JPEGImageReaderSpi());
        r.setInput(new FileImageInputStream(new File(fileName)));
        BufferedImage  bi = r.read(0, new ImageReadParam());
        ((FileImageInputStream) r.getInput()).close();
        return bi;
    }


    private String pathToUrl(String path) throws MalformedURLException {
        File file = new File(path);
        return file.toURI().toURL().toString();
    }


    public void readHistogramsAction(ActionEvent actionEvent) {
        File file = histFileChooser.showOpenDialog(primaryStage);
        String histPath;
        if (file != null) {
            String filePath = file.getAbsolutePath();
            histPath = filePath;

            try {
                histograms = Utils.readHistogramsFromFile(histPath);
//                for (Map.Entry entry: histograms.entrySet()) Utils.printMap((HashMap<Number, Number>) entry.getValue());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

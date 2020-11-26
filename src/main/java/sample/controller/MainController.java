package sample.controller;

import com.sun.imageio.plugins.jpeg.JPEGImageReaderSpi;
import com.sun.imageio.plugins.png.PNGImageReader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.util.Arrays;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    CoolImage coolImage;

    public TextField imagePathTf;
    public Button openImageBtn;
    public ImageView mainImgView;
    public Button highlightContour;
    FileChooser fileChooser;
    Stage primaryStage;
    private Desktop desktop = Desktop.getDesktop();
    String imagePath;
    String resultImgDir = "C:\\Users\\Pavel\\Desktop\\nirs\\fxNirs\\NIRS\\src\\main\\resources\\resultImages";
    String testImgDir = "C:\\Users\\Pavel\\Desktop\\nirs\\fxNirs\\NIRS\\src\\main\\resources\\testImages";
    String imgName;
    private final int WHITE_3_BYTES = 16777215;
    private final int BLACK_3_BYTES = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(testImgDir));
        openImageBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imagePathTf.clear();
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    String filePath = file.getAbsolutePath();
                    imagePath = filePath;
                    imagePathTf.setText(filePath);
                    String[] splitPath = imagePath.split("\\\\");
                    imgName = splitPath[splitPath.length - 1];
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
        });
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void openImage(ActionEvent actionEvent) {

    }

    public void highlightContour(ActionEvent actionEvent) throws IOException {
        double[][] newDoublePixels = Utils.intToDoubleMatrix(coolImage.getPixelsMatrix());
//        newDoublePixels = Utils.slidingWindowTreatment(newDoublePixels, 3);
        double[][] kernel = new double[][] {{0, 1, 0}, {1, -4, 1}, {0, 1, 0}};
        newDoublePixels = Utils.filterWindowTreatment(newDoublePixels, kernel);
        int[][] newIntPixels = Utils.matrixRoundToInt(newDoublePixels);
        newIntPixels = coolImage.pixelsToNorm(newIntPixels);
        Utils.printMatrix(newIntPixels);
        newIntPixels = thresholding(newIntPixels, 60);
        int[] newArrayPixels = coolImage.setPixelFromNormalPixels(newIntPixels);
        coolImage.copyToBufferedImage(newArrayPixels);
        coolImage.saveAsPng(resultImgDir + "\\" + imgName);
        mainImgView.setImage(new Image(pathToUrl(resultImgDir + "\\" + imgName)));
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

    private void openFile(File file) {
        try {
            this.desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String pathToUrl(String path) throws MalformedURLException {
        File file = new File(path);
        return file.toURI().toURL().toString();
    }
}

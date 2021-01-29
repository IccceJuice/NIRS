package sample.controller;

import com.sun.imageio.plugins.jpeg.JPEGImageReaderSpi;
import com.sun.imageio.plugins.png.PNGImageReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.model.algorithms.AVGAlgorithm;
import sample.model.algorithms.Algorithm;
import sample.model.CoolImage;
import sample.model.algorithms.LBPAlgorithm;
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
import java.util.*;

public class MainController implements Initializable {

    public TextField tfWinSize;
    public Button btnReadHist;
    public TextField tfHistPath;
    public Button printHistBtn;
    public TextField pixIndTf;
    public ComboBox<String> algorithmBox;
    CoolImage coolImage;

    public TextField imagePathTf;
    public Button openImageBtn;
    public ImageView mainImgView;
    FileChooser fileChooser;
    FileChooser histFileChooser;
    Stage primaryStage;
    private Desktop desktop = Desktop.getDesktop();
    String resultImgDir = "D:\\desktop\\Univer\\nirs\\fxNirs\\NIRS\\src\\main\\resources\\resultImages";
    String testImgDir = "D:\\desktop\\Univer\\nirs\\fxNirs\\NIRS\\src\\main\\resources\\testImages";
    String resultHistDir = "D:\\desktop\\Univer\\nirs\\fxNirs\\NIRS\\src\\main\\resources\\resultHistograms";
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

        ObservableList<String> langs = FXCollections.observableArrayList("AVGAlgorithm", "LBPAlgorithm");
        algorithmBox.setItems(langs);
        algorithmBox.setValue("AVGAlgorithm");
//        algorithmBox.setOnAction(event -> lbl.setText(langsChoiceBox.getValue()));
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

    public void process(ActionEvent actionEvent) throws IOException {
        Algorithm algorithm = chooseAlgorithm(algorithmBox.getValue());
        int[][] newIntPixels = coolImage.getHalftonePixelMatrix();
        newIntPixels = algorithm.process(newIntPixels, 3);
        Utils.saveAsPng(resultImgDir + "\\" + imgName, newIntPixels);
        mainImgView.setImage(new Image(pathToUrl(resultImgDir + "\\" + imgName)));
    }

    public void collectHistograms(ActionEvent actionEvent) throws IOException {
        int[][] newIntPixels = coolImage.getHalftonePixelMatrix();
        histograms = collectHistogramsFromPixels(newIntPixels);
        Utils.saveHistogramsToFile(histograms, resultHistDir + "\\" + histName);
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

    public void printHist(ActionEvent actionEvent) {
        CategoryAxis xAxis = new CategoryAxis();
        int xInd = Integer.valueOf(pixIndTf.getText());
//        int yInd = Integer.valueOf(pixIndTf.getText().split(",")[1]);
        xAxis.setLabel("Histogram №" + xInd);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Count");

        // Create a BarChart
        BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);

        // Series 1 - Data of 2014
        XYChart.Series<String, Number> dataSeries1 = new XYChart.Series<String, Number>();
        HashMap<Short, Short> d = (HashMap<Short, Short>) histograms[xInd];
        Map<Short, Short> sortedMap = new TreeMap<Short, Short>();
        sortedMap.putAll(d);
        for (Map.Entry entry: sortedMap.entrySet()) {
            dataSeries1.getData().add(new XYChart.Data<String, Number>(String.valueOf(entry.getKey()), (short) entry.getValue()));
        }

        // Add Series to BarChart.
        barChart.getData().add(dataSeries1);
        barChart.setTitle("Histogram");

        VBox vbox = new VBox(barChart);

        Scene scene = new Scene(vbox, 400, 200);

        Stage imageStage = new Stage();
        imageStage.setTitle(imgName);
        imageStage.setScene(scene);

        openInAWindow(imageStage, vbox, false);
    }

    public static void openInAWindow(Stage stage, Parent parent, boolean resizable){
        stage.setResizable(resizable);
        stage.show();
        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());
    }

    private Algorithm chooseAlgorithm(String algByChooseBox) {
        switch (algByChooseBox) {
            case "LBPAlgorithm" :
                return new LBPAlgorithm();
            case "AVGAlgorithm" :
                return new AVGAlgorithm();
            default: return new AVGAlgorithm();
        }
    }
}

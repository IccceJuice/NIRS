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

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileChooser = new FileChooser();

        openImageBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imagePathTf.clear();
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    String filePath = file.getAbsolutePath();
                    imagePathTf.setText(filePath);
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

    public void highlightContour(ActionEvent actionEvent) {

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
}

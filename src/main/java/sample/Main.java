package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.controller.MainController;
import sample.model.CoolImage;

import java.awt.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/sample.fxml"));
        Parent fxmlMain = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();
        mainController.setPrimaryStage(primaryStage);

//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));
//        primaryStage.setTitle("Hello World");

        primaryStage.setScene(new Scene(fxmlMain, 549, 478));
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);
//        try {
//            CoolImage picture = new CoolImage("lena2.png");
//            picture.saveAsPng("lena3.png");
////        picture.printArray(picture.getPixels());
//            int[] arr = picture.getPixels();
//            Color[] colors = picture.getColors();
//            for (int i = 0; i < 3; i++) {
////            System.out.println(colors[i] + " ");
//                System.out.println(colors[i].brighter());
////            System.out.println("Color " + i + " = " + Integer.toBinaryString(arr[i]));
////            System.out.println("Red " + i + " = " + picture.getRed(arr[i]));
////            System.out.println("Green " + i + " = " + picture.getGreen(arr[i]));
////            System.out.println("Blue " + i + " = " + picture.getBlue(arr[i]));
//            }
////        int r = 0xffff;
////        int a = 30;
////        int b = a >> 3;
////        System.out.println(Integer.toBinaryString(r));
//        } catch (Exception e) {
//
//        }
    }
}

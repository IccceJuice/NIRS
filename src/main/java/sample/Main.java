package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.controller.MainController;

public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/sample.fxml"));
        Parent fxmlMain = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();
        mainController.setPrimaryStage(primaryStage);
        primaryStage.setScene(new Scene(fxmlMain, 797, 754));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package mainapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/start.fxml")), 600, 400));
        primaryStage.setTitle("JavaFX Quiz Game");
        primaryStage.show();
    }
    public static void main(String[] args) { launch(args); }
}


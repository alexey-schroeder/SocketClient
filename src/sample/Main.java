package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static  Controller controller;
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("sample.fxml"));
         controller = fxmlLoader.getController();
        primaryStage.setTitle("SocketClient");
        primaryStage.setScene(new Scene(root, 200, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package com.example;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/com/example/Interface.fxml"));
            Scene scene = new Scene(root);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {e.printStackTrace();}
    }

    public static void main(String[] args) {launch(args);}
}
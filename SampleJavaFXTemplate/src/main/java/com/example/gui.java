package com.example;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.*;

public class gui extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button startGame = new Button("Start Game");
        Image enter_screen = new Image("file:/Users/alakkur923/PokerProjectFX/SampleJavaFXTemplate/src/main/java/com/example/images/poker.jpg");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package com.example;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.*;

public class gui extends Application {

    private VBox vbox;
    private Stage primaryStage;
    private StackPane menuPage;
    @Override
    public void start(Stage primaryStag) {
        //poker image start screen
        this.primaryStage = primaryStag;
        ImageView enter_screen = enterScreen("/Users/akshaylakkur/PokerProjectFX/SampleJavaFXTemplate/src/main/java/com/example/images/poker.jpg");
        //second layout
        StackPane stackpane = enterButton(enter_screen);

        
        
        //launch the gui
        vbox = new VBox(10, stackpane);
        Scene scene = new Scene(vbox);
        primaryStage.setTitle("Poker GUI");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    private ImageView enterScreen(String fileName){
        Image enter = new Image("file:"+fileName);
        ImageView enter_screen = new ImageView(enter);
        return enter_screen;
    }
    private StackPane enterButton(ImageView enter_screen){
        Button enter_game = new Button("Enter Game");
        enter_game.setFont(Font.font("Verdana", 30));
        enter_game.setStyle(
            "-fx-background-color: #2E8B57;" +  // background color
            "-fx-text-fill: white;" +           // text color         // font size
            "-fx-padding: 10 20;" +             // top-bottom, left-right padding
            "-fx-background-radius: 10;"        // rounded corners
        );
        enter_game.setOnAction(e -> {
            ImageView menuScreen = enterScreen("/Users/akshaylakkur/PokerProjectFX/SampleJavaFXTemplate/src/main/java/com/example/images/MenuScreen.jpg");
            Button back_home = new Button("Back");
            back_home.setFont(Font.font("Verdana", 20));
            back_home.setStyle(
                "-fx-background-color:rgb(139, 46, 130);" +  // background color
                "-fx-text-fill: white;" +           // text color         // font size
                "-fx-padding: 10 20;" +             // top-bottom, left-right padding
                "-fx-background-radius: 10;"        // rounded corners
            );
            StackPane.setAlignment(back_home,Pos.BOTTOM_LEFT);
            StackPane l2 = new StackPane(menuScreen, back_home);
            menuPage = l2;
            primaryStage.getScene().setRoot(l2);
            back_home.setOnAction(ev -> {
                primaryStage.getScene().setRoot(vbox);
            });
        });
        StackPane.setAlignment(enter_game,Pos.BOTTOM_CENTER);
        StackPane stackpane = new StackPane();
        stackpane.getChildren().addAll(enter_screen, enter_game);
        return stackpane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

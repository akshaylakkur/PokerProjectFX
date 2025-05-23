package com.example;
import java.util.Stack;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;

public class gui extends Application {

    private VBox vbox;
    private Stage primaryStage;
    private Pane menuPage;
    private TextField playerName;
    private ImageView menuScreen;
    private ImageView rulesScreen;
    private AnchorPane rulesPage;
    @Override
    public void start(Stage primaryStage) {
        //poker image start screen
        this.primaryStage = primaryStage;
        ImageView enter_screen = enterScreen("SampleJavaFXTemplate/src/main/java/com/example/images/poker.jpg");
        //second layout
        StackPane stackpane = enterButton(enter_screen);

        //launch the gui
        vbox = new VBox(10, stackpane);
        Scene scene = new Scene(vbox);
        primaryStage.setTitle("Poker GUI");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    public void imHandler(ImageView menuScreen){
        menuScreen.setPreserveRatio(false); // Allow stretching
        menuScreen.setFitWidth(primaryStage.getWidth());
        menuScreen.setFitHeight(primaryStage.getHeight());
        menuScreen.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            double x = event.getX();
            double y = event.getY();
            System.out.printf("Clicked at: (%.2f, %.2f)%n", x, y);
        });
    }
    public void style(Button button, String color, Pos position, int size){
        button.setFont(Font.font("Verdana", 30));
        button.setStyle(
            "-fx-background-color:" + color + "#2E8B57;" +  // background color
            "-fx-text-fill: white;" +           // text color         // font size
            "-fx-padding: 10 20;" +             // top-bottom, left-right padding
            "-fx-background-radius: 10;"        // rounded corners
        );
        StackPane.setAlignment(button, position);
    }
    public void absoluteStyle(Button button, String color, double x, double y, int size){
        button.setFont(Font.font("Verdana", size=size));
        button.setStyle(
            "-fx-background-color:" + color + "#2E8B57;" +  // background color
            "-fx-text-fill: white;" +           // text color         // font size
            "-fx-padding: 10 20;" +             // top-bottom, left-right padding
            "-fx-background-radius: 10;"        // rounded corners
        );
        button.setLayoutX(x);
        button.setLayoutY(y);
    }
    private ImageView enterScreen(String fileName){
        Image enter = new Image("file:"+fileName);
        ImageView enter_screen = new ImageView(enter);
        return enter_screen;
    }
    
    private StackPane enterButton(ImageView enter_screen){
        Button enterButton = new Button("Enter Game");
        enterButton(enterButton);
        StackPane stackpane = new StackPane(enter_screen, enterButton);
        return stackpane;
    
    }

    public void enterButton(Button enterButton){
        style(enterButton, "green", Pos.BOTTOM_CENTER, 30);
        enterButton.setOnAction(e -> {
            ImageView mScreen = enterScreen("SampleJavaFXTemplate/src/main/java/com/example/images/MenuScreen.jpg");
            imHandler(mScreen);
            menuScreen = mScreen;
            Button backToHomeButton = new Button("Back to Home");
            BackHomeButton(backToHomeButton, vbox);
            Button rulesButton = new Button("Rules");
            rulesPage = rulesButton(rulesButton);
            rulesPage.setPickOnBounds(false);
            Button quickPlayButton = new Button("Quick Play");
            
            
            
            menuPage = new StackPane(menuScreen, backToHomeButton, rulesPage);
            primaryStage.getScene().setRoot(menuPage);
        });
    }
    public void BackHomeButton(Button backToHomeButton, Object back){
        style(backToHomeButton, "black", Pos.BOTTOM_LEFT, 30);
        backToHomeButton.setOnAction(ex -> {
            primaryStage.getScene().setRoot((Parent) back);
        });

    }

    public AnchorPane rulesButton(Button rulesButton){
        absoluteStyle(rulesButton, "red", 716, 545, 30);
        rulesButton.setPrefWidth(166);
        rulesButton.setPrefHeight(65);
        rulesButton.setOnAction(er -> {
            Image enter = new Image("file:"+"/Users/akshaylakkur/PokerProjectFX/SampleJavaFXTemplate/src/main/java/com/example/images/poker_rules.jpg");
            if (enter.isError()) {
                System.out.println("Image failed to load: " + enter.getException());
                return;
            }
            ImageView rScreen = new ImageView(enter);
            imHandler(rScreen);
            rulesScreen = rScreen;
            Button backToMenuButton = new Button("Back to Menu");
            BackHomeButton(backToMenuButton, menuPage);
            StackPane rp = new StackPane(rulesScreen, backToMenuButton);
            primaryStage.getScene().setRoot(rp);
            // rp.setPickOnBounds(false);
        });
        AnchorPane rulesPane = new AnchorPane();
        rulesPane.getChildren().add(rulesButton);
        return rulesPane;
    }
    public static void main(String[] args) {
        launch(args);
    }
}

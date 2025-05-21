package com.example;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    @Override
    public void start(Stage primaryStage) {
        //poker image start screen
        this.primaryStage = primaryStage;
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
    public void style(Button button, String color){
        button.setFont(Font.font("Verdana", 30));
        button.setStyle(
            "-fx-background-color:" + color + "#2E8B57;" +  // background color
            "-fx-text-fill: white;" +           // text color         // font size
            "-fx-padding: 10 20;" +             // top-bottom, left-right padding
            "-fx-background-radius: 10;"        // rounded corners
        );
    }
    private ImageView enterScreen(String fileName){
        Image enter = new Image("file:"+fileName);
        ImageView enter_screen = new ImageView(enter);
        return enter_screen;
    }
    private StackPane enterButton(ImageView enter_screen){
        Button enter_game = new Button("Enter Game");
        style(enter_game,"green");
        enter_game.setOnAction(e -> {
            ImageView menuScreen = enterScreen("/Users/akshaylakkur/PokerProjectFX/SampleJavaFXTemplate/src/main/java/com/example/images/MenuScreen.jpg");
            menuScreen.setPreserveRatio(true);
            menuScreen.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                double x = event.getX();
                double y = event.getY();
                System.out.printf("Clicked at: (%.2f, %.2f)%n", x, y);
            });
            Button back_home = new Button("Back");
            Button rules = new Button("Rules");
            rules.setLayoutX(895.00);
            rules.setLayoutY(578.50);
            Pane rulesPane = new Pane();
            rulesPane.getChildren().add(rules);
            style(back_home, "purple");
            StackPane.setAlignment(back_home,Pos.BOTTOM_LEFT);
            StackPane l2 = new StackPane();
            rulesPane.setMouseTransparent(true);
            back_home.setMouseTransparent(true);
            l2.getChildren().addAll(menuScreen, rulesPane, back_home);
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

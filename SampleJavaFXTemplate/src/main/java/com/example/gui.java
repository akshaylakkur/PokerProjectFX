package com.example;

import java.io.OutputStream;
import java.io.PrintStream;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class gui extends Application {

    private VBox vbox;
    private Stage primaryStage;
    private Pane menuPage;
    private TextField playerName;
    private ImageView menuScreen;
    private ImageView rulesScreen;
    private AnchorPane rulesPage;
    private gameV2 game;
    private String name;
    private TextArea messageBox;
    private TextArea textInput;
    

    @Override
    public void start(Stage primaryStage) {
        this.messageBox = new TextArea();
        this.textInput = new TextArea();
        this.primaryStage = primaryStage;
        this.game = null;

        // Create chatbox / message area
        messageBox.setEditable(false);
        messageBox.setWrapText(true);
        messageBox.setPrefWidth(300);
        messageBox.setPrefHeight(200);
        messageBox.setStyle("-fx-control-inner-background: black; -fx-text-fill: white;");


        textInput.setPromptText("Type your message here...");
        textInput.setPrefWidth(300);
        textInput.setPrefHeight(15);
        textInput.setStyle("-fx-control-inner-background: white; -fx-text-fill: black;");
        textInput.setLayoutX(890);
        textInput.setLayoutY(189);

        textInput.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    String command = textInput.getText(); // capture command before clearing
                    textInput.clear(); // then clear
                    event.consume();
                    chatCommands(command); // pass the command
                    break;
                default:
                    break;
            }
        });
        

        // Redirect System.out to messageBox
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                javafx.application.Platform.runLater(() -> {
                    messageBox.appendText(String.valueOf((char) b));
                });
            }
        };
        System.setOut(new PrintStream(out, true));

        ImageView enter_screen = enterScreen("SampleJavaFXTemplate/src/main/java/com/example/images/poker.jpg");
        StackPane stackpane = enterButton(enter_screen);

        vbox = new VBox(10, stackpane);
        Scene scene = new Scene(vbox);
        primaryStage.setTitle("Poker GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void imHandler(ImageView menuScreen){
        menuScreen.setPreserveRatio(false);
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
            "-fx-background-color:" + color + "#2E8B57;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 10;"
        );
        StackPane.setAlignment(button, position);
    }

    public void absoluteStyle(Button button, String color, double x, double y, int size){
        button.setFont(Font.font("Verdana", size));
        button.setStyle(
            "-fx-background-color:" + color + "#2E8B57;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 10;"
        );
        button.setLayoutX(x);
        button.setLayoutY(y);
    }

    public void absoluteText(TextField t, String color, double x, double y, int size){
        t.setFont(Font.font("Verdana", size));
        t.setStyle(
            "-fx-background-color:" + color + "#2E8B57;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 10;"
        );
        t.setLayoutX(x);
        t.setLayoutY(y);
    }

    private ImageView enterScreen(String fileName){
        Image enter = new Image("file:"+fileName);
        return new ImageView(enter);
    }

    private StackPane enterButton(ImageView enter_screen){
        Button enterButton = new Button("Enter Game");
        enterButton(enterButton);
        return new StackPane(enter_screen, enterButton);
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
            AnchorPane quickPlayPage = quickPlay(quickPlayButton);
            quickPlayPage.setPickOnBounds(false);

            Button notAvailableButton = new Button("Not Available");
            AnchorPane notAvailablePage = NotAvailable(notAvailableButton);
            notAvailablePage.setPickOnBounds(false);

            playerName = new TextField("ENTER NAME");
            AnchorPane namePage = pName(playerName);
            namePage.setPickOnBounds(false);

            menuPage = new StackPane(menuScreen, backToHomeButton, rulesPage, quickPlayPage, notAvailablePage, namePage);
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
        absoluteStyle(rulesButton, "Green", 716, 545, 30);
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
        });
        AnchorPane rulesPane = new AnchorPane();
        rulesPane.getChildren().add(rulesButton);
        return rulesPane;
    }



    public AnchorPane quickPlay(Button qb){
        absoluteStyle(qb, "Blue", 314, 545, 20);
        qb.setPrefWidth(168);
        qb.setPrefHeight(65);
        qb.setOnAction(e -> {
    
            if (game == null || name == null || name.isEmpty()) {
                playerName.setFont(Font.font("Verdana", 15));
                playerName.setText("Please enter your Name");
                return;
            }
    
            ImageView gScreen = enterScreen("SampleJavaFXTemplate/src/main/java/com/example/images/poker_table.jpg");
            imHandler(gScreen);
    
            Button backToMenuButton = new Button("Back to Menu");
            BackHomeButton(backToMenuButton, menuPage);
    
            Button fold = new Button("Fold");
            absoluteStyle(fold, "red", 540, 729, 30);
            fold.setOnAction(t -> {
                game.processHumanMove("fold", 0);
            });
    
            Button check = new Button("Check");
            absoluteStyle(check, "Blue", 740, 729, 30);
            check.setOnAction(t -> {
                game.processHumanMove("check", 0);
            });
    
            Button call = new Button("Call");
            absoluteStyle(call, "Orange", 340, 729, 30);
            call.setOnAction(t -> {
                game.processHumanMove("call", 0);
            });
    
            TextField raise = new TextField("Raise Amount");
            absoluteText(raise, "Black", 13, 729, 18);
            raise.setOnAction(t -> {
                try {
                    String amt = raise.getText();
                    int raiseAmount = Integer.parseInt(amt);
                    game.processHumanMove("raise", raiseAmount);
                    raise.setText("Raised $" + amt);
                } catch (NumberFormatException ex) {
                    raise.setText("Invalid amount");
                }
            });
            raise.setPrefWidth(171);
            raise.setPrefHeight(59);
    
            // Button to start new game
            Button startGameButton = new Button("Start Game");
            absoluteStyle(startGameButton, "Green", 13, 650, 20);
            startGameButton.setOnAction(t -> {
                game.startNewGame();
            });
    
            AnchorPane boxPane = new AnchorPane(gScreen, messageBox, fold, check, call, raise, textInput, startGameButton);
            AnchorPane.setTopAnchor(messageBox, 10.0);
            AnchorPane.setRightAnchor(messageBox, 10.0);
            
            primaryStage.getScene().setRoot(boxPane);    
    
            // Start the game automatically
            game.startNewGame();
    
            // Welcome message
            System.out.println("Welcome to Texas Hold'em Poker!");
            System.out.println("You are playing against 5 CPU players.");
            System.out.println("Available Chat Commands:");
            System.out.println("1. pot - Current pot amount");
            System.out.println("2. players - Current players");
            System.out.println("3. gamestate - Current game state");
            System.out.println("4. turn - Whose turn it is");
            System.out.println("5. cards - Your current cards");
        });
    
        AnchorPane gamePane = new AnchorPane();
        gamePane.getChildren().add(qb);
        return gamePane;
    }







    public AnchorPane NotAvailable(Button na){
        absoluteStyle(na, "red", 518, 545, 18);
        na.setPrefWidth(166);
        na.setPrefHeight(65);
        AnchorPane notAvailablePane = new AnchorPane();
        notAvailablePane.getChildren().add(na);
        return notAvailablePane;
    }

    public AnchorPane pName(TextField pn){
        absoluteText(pn, "Black", 469, 475, 30);
        pn.setPrefWidth(261);
        pn.setPrefHeight(35);
        pn.setOnAction(e -> {
            name = pn.getText();
            pn.setText("Saved");
            
            // Create new game instance with human player
            game = new gameV2(name);
            
            System.out.println("Player " + name + " has been added to the game with starting cash at $1000.");
            System.out.println("Game created with CPU players: cpu1, cpu2, cpu3, cpu4, cpu5");
        });
        AnchorPane namePane = new AnchorPane();
        namePane.getChildren().add(pn);
        return namePane;
    }
    
    public AnchorPane foldBtn(Button f){
        style(f, "red", Pos.BOTTOM_CENTER, 30);
        f.setOnAction(e -> {
            game.players.get(name).fold();
        });
        AnchorPane foldPane = new AnchorPane();
        foldPane.getChildren().add(f);
        return foldPane;
    }
    
    public AnchorPane checkBtn(Button c){
        absoluteStyle(c, "Blue", 740, 729, 30);
        c.setOnAction(e -> {
            game.players.get(name).check(game.highestBet);
        });
        AnchorPane checkPane = new AnchorPane();
        checkPane.getChildren().add(c);
        return checkPane;
    }
    
    public void chatCommands(String command) {
        System.out.println(command);
        command = command.toLowerCase().trim();
        
        if (game == null) {
            System.out.println("No game in progress. Please start a game first.");
            return;
        }
        
        switch (command) {
            case "pot":
                System.out.println("Current pot amount: $" + game.getPot());
                break;
            case "players":
                System.out.println("Current players: " + game.players.keySet());
                break;
            case "gamestate":
                System.out.println("Current game state: " + game.getGameState());
                break;
            case "turn":
                System.out.println("Current turn: " + game.getCurrentPlayerName());
                if (game.isHumanPlayerTurn()) {
                    System.out.println("It's YOUR turn!");
                }
                break;
            case "cards":
                if (game.players.containsKey(name)) {
                    System.out.println("Your cards: " + game.players.get(name).cards);
                }
                break;
            case "community":
            case "communitycards":
                System.out.println("Community cards: " + game.getCommunityCards());
                break;
            case "highestbet":
                System.out.println("Current highest bet: $" + game.getHighestBet());
                break;
            case "money":
                if (game.players.containsKey(name)) {
                    System.out.println("Your money: $" + game.players.get(name).money);
                }
                break;
            case "help":
                System.out.println("Available commands: pot, players, gamestate, turn, cards, community, highestbet, money, help");
                break;
            default:
                System.out.println("Unknown command. Type 'help' for available commands.");
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

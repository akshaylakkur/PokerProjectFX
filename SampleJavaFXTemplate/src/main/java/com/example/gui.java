package com.example;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label; // Added Label import
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
    private TextField playerNameInput; // Renamed to avoid conflict with 'name' field
    private ImageView menuScreen;
    private ImageView rulesScreen;
    private AnchorPane rulesPage;
    private gameV2 game;
    private String humanPlayerName; // Renamed from 'name' to be more specific
    private TextArea messageBox;
    private TextArea textInput;

    // UI elements for game display
    private List<ImageView> communityCardViews;
    private List<ImageView> humanPlayerCardViews;
    private Label potLabel;
    private Label highestBetLabel;
    private Label humanMoneyLabel;
    private Label humanCurrentBetLabel;
    // Potentially more labels for CPU players if desired, positioned around the table

    @Override
    public void start(Stage primaryStage) {
        this.messageBox = new TextArea();
        this.textInput = new TextArea();
        this.primaryStage = primaryStage;
        this.game = null;
        this.humanPlayerName = ""; // Initialize humanPlayerName

        // Create chatbox / message area
        messageBox.setEditable(false);
        messageBox.setWrapText(true);
        messageBox.setPrefWidth(300);
        messageBox.setPrefHeight(200);
        messageBox.setStyle("-fx-control-inner-background: black; -fx-text-fill: white;");
        // Positioning will be set in quickPlay for the game scene

        textInput.setPromptText("Type your message here...");
        textInput.setPrefWidth(300);
        textInput.setPrefHeight(15);
        textInput.setStyle("-fx-control-inner-background: white; -fx-text-fill: black;");
        // Positioning will be set in quickPlay for the game scene
        textInput.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    String command = textInput.getText();
                    textInput.clear();
                    event.consume();
                    chatCommands(command);
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
                    // Auto-scroll to the bottom
                    messageBox.setScrollTop(Double.MAX_VALUE);
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
        button.setFont(Font.font("Verdana", size)); // Use passed size
        button.setStyle(
            "-fx-background-color:" + color + ";" + // Removed #2E8B57; to use only passed color
            "-fx-text-fill: white;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 10;"
        );
        StackPane.setAlignment(button, position);
    }

    public void absoluteStyle(Button button, String color, double x, double y, int size){
        button.setFont(Font.font("Verdana", size));
        button.setStyle(
            "-fx-background-color:" + color + ";" + // Removed #2E8B57;
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
            "-fx-background-color:" + color + ";" + // Removed #2E8B57;
            "-fx-text-fill: white;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 10;"
        );
        t.setLayoutX(x);
        t.setLayoutY(y);
    }

    private ImageView enterScreen(String fileName){
        Image enter = new Image("file:"+fileName);
        if (enter.isError()) {
            System.err.println("Error loading image: " + fileName + " - " + enter.getException());
            // Provide a fallback or default image if loading fails
            // For now, it will just show a blank ImageView
        }
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

            playerNameInput = new TextField("ENTER NAME"); // Use playerNameInput
            AnchorPane namePage = pName(playerNameInput);
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
            // Corrected path - ensure this path is valid for your project
            Image enter = new Image("file:SampleJavaFXTemplate/src/main/java/com/example/images/poker_rules.jpg");
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

            if (humanPlayerName == null || humanPlayerName.isEmpty()) { // Check humanPlayerName
                playerNameInput.setFont(Font.font("Verdana", 15));
                playerNameInput.setText("Please enter your Name");
                return;
            }

            // Ensure game is initialized with the humanPlayerName and GUI reference
            if (game == null || !game.humanPlayerName.equals(humanPlayerName)) {
                game = new gameV2(humanPlayerName); // Pass 'this' GUI instance
                System.out.println("Game initialized for " + humanPlayerName);
            }

            ImageView gScreen = enterScreen("SampleJavaFXTemplate/src/main/java/com/example/images/poker_table.jpg");
            imHandler(gScreen);

            Button backToMenuButton = new Button("Back to Menu");
            BackHomeButton(backToMenuButton, menuPage);

            Button fold = new Button("Fold");
            absoluteStyle(fold, "red", 540, 729, 30);
            fold.setOnAction(t -> {
                if (game.isHumanPlayerTurn()) {
                    game.processHumanMove("fold", 0);
                    updateGameDisplay(); // Update after human move
                } else {
                    System.out.println("It's not your turn!");
                }
            });

            Button check = new Button("Check");
            absoluteStyle(check, "Blue", 740, 729, 30);
            check.setOnAction(t -> {
                if (game.isHumanPlayerTurn()) {
                    game.processHumanMove("check", 0);
                    updateGameDisplay(); // Update after human move
                } else {
                    System.out.println("It's not your turn!");
                }
            });

            Button call = new Button("Call");
            absoluteStyle(call, "Orange", 340, 729, 30);
            call.setOnAction(t -> {
                if (game.isHumanPlayerTurn()) {
                    game.processHumanMove("call", 0);
                    updateGameDisplay(); // Update after human move
                } else {
                    System.out.println("It's not your turn!");
                }
            });

            TextField raise = new TextField("Raise Amount");
            absoluteText(raise, "Black", 13, 729, 18);
            raise.setOnAction(t -> {
                if (game.isHumanPlayerTurn()) {
                    try {
                        String amt = raise.getText();
                        int raiseAmount = Integer.parseInt(amt);
                        game.processHumanMove("raise", raiseAmount);
                        raise.setText("Raised $" + amt);
                        updateGameDisplay(); // Update after human move
                    } catch (NumberFormatException ex) {
                        raise.setText("Invalid amount");
                    }
                } else {
                    System.out.println("It's not your turn!");
                }
            });
            raise.setPrefWidth(171);
            raise.setPrefHeight(59);

            // Button to start new game
            Button startGameButton = new Button("Start Game");
            absoluteStyle(startGameButton, "Green", 13, 650, 20);
            startGameButton.setOnAction(t -> {
                game.startNewGame();
                updateGameDisplay(); // Update after game starts
            });

            // Initialize and position UI elements for game display
            potLabel = new Label("Pot: $0");
            absoluteStyleLabel(potLabel, "white", 10, 10, 20); // Top-left
            highestBetLabel = new Label("Highest Bet: $0");
            absoluteStyleLabel(highestBetLabel, "white", 10, 40, 20); // Below pot
            humanMoneyLabel = new Label("Your Money: $0");
            absoluteStyleLabel(humanMoneyLabel, "white", 450, 670, 20); // Near human player cards
            humanCurrentBetLabel = new Label("Your Bet: $0");
            absoluteStyleLabel(humanCurrentBetLabel, "white", 450, 700, 20); // Below human money

            communityCardViews = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                ImageView cardView = new ImageView();
                cardView.setFitWidth(80);
                cardView.setFitHeight(110);
                cardView.setLayoutX(350 + i * 90); // Position community cards centrally
                cardView.setLayoutY(280);
                communityCardViews.add(cardView);
            }

            humanPlayerCardViews = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                ImageView cardView = new ImageView();
                cardView.setFitWidth(80);
                cardView.setFitHeight(110);
                cardView.setLayoutX(450 + i * 90); // Position human player cards
                cardView.setLayoutY(550);
                humanPlayerCardViews.add(cardView);
            }

            AnchorPane boxPane = new AnchorPane(gScreen);
            boxPane.getChildren().addAll(messageBox, fold, check, call, raise, textInput, startGameButton,
                                        potLabel, highestBetLabel, humanMoneyLabel, humanCurrentBetLabel);
            boxPane.getChildren().addAll(communityCardViews);
            boxPane.getChildren().addAll(humanPlayerCardViews);
            // Add backToMenuButton to the game screen
            boxPane.getChildren().add(backToMenuButton);
            AnchorPane.setBottomAnchor(backToMenuButton, 10.0); // Position back button
            AnchorPane.setLeftAnchor(backToMenuButton, 10.0);

            AnchorPane.setTopAnchor(messageBox, 10.0);
            AnchorPane.setRightAnchor(messageBox, 10.0);
            AnchorPane.setTopAnchor(textInput, 215.0); // Position below messageBox
            AnchorPane.setRightAnchor(textInput, 10.0);

            primaryStage.getScene().setRoot(boxPane);

            // Welcome message
            System.out.println("Welcome to Texas Hold'em Poker!");
            System.out.println("You are playing against 5 CPU players.");
            System.out.println("Available Chat Commands:");
            System.out.println("1. pot - Current pot amount");
            System.out.println("2. players - Current players");
            System.out.println("3. gamestate - Current game state");
            System.out.println("4. turn - Whose turn it is");
            System.out.println("5. cards - Your current cards");

            updateGameDisplay(); // Initial display update
        });

        AnchorPane gamePane = new AnchorPane();
        gamePane.getChildren().add(qb);
        return gamePane;
    }

    // Helper for styling labels
    public void absoluteStyleLabel(Label label, String color, double x, double y, int size){
        label.setFont(Font.font("Verdana", size));
        label.setStyle(
            "-fx-text-fill: " + color + ";"
        );
        label.setLayoutX(x);
        label.setLayoutY(y);
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
            humanPlayerName = pn.getText(); // Store in humanPlayerName
            pn.setText("Saved: " + humanPlayerName);

            // Initialize game here if not already done, or ensure it's updated
            if (game == null || !game.humanPlayerName.equals(humanPlayerName)) {
                game = new gameV2(humanPlayerName); // Pass 'this' GUI instance
            }

            System.out.println("Player " + humanPlayerName + " has been added to the game with starting cash at $1000.");
            System.out.println("Game created with CPU players: cpu1, cpu2, cpu3, cpu4, cpu5");
        });
        AnchorPane namePane = new AnchorPane();
        namePane.getChildren().add(pn);
        return namePane;
    }

    // Removed foldBtn and checkBtn as their logic is now in quickPlay

    public void chatCommands(String command) {
        System.out.println("> " + command); // Echo command for clarity
        command = command.toLowerCase().trim();

        if (game == null) {
            System.out.println("No game in progress. Please enter your name and click 'Quick Play' to start a game first.");
            return;
        }

        switch (command) {
            case "pot":
                System.out.println("Current pot amount: $" + game.getPot());
                break;
            case "players":
                System.out.println("Current players:");
                for (Player p : game.players.values()) {
                    System.out.println("- " + p.toString()); // Use Player's toString for detailed info
                }
                break;
            case "gamestate":
                System.out.println("Current game state: " + game.getGameState());
                break;
            case "turn":
                System.out.println("Current turn: " + game.getCurrentPlayerName());
                if (game.isHumanPlayerTurn()) {
                    System.out.println("It's YOUR turn! Please use the action buttons (Fold, Check, Call, Raise).");
                }
                break;
            case "cards":
                if (game.players.containsKey(humanPlayerName)) {
                    System.out.println("Your cards: " + game.players.get(humanPlayerName).cards);
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
                if (game.players.containsKey(humanPlayerName)) {
                    System.out.println("Your money: $" + game.players.get(humanPlayerName).money);
                }
                break;
            case "help":
                System.out.println("Available commands: pot, players, gamestate, turn, cards, community, highestbet, money, help");
                break;
            default:
                System.out.println("Unknown command. Type 'help' for available commands.");
        }
        updateGameDisplay(); // Update display after chat command (might reveal new info)
    }

    /**
     * Updates the game display elements (pot, highest bet, player money, cards).
     * This method should be called whenever the game state changes significantly.
     */
    public void updateGameDisplay() {
        if (game == null) return;

        // Update pot and highest bet labels
        potLabel.setText("Pot: $" + game.getPot());
        highestBetLabel.setText("Highest Bet: $" + game.getHighestBet());

        // Update human player info
        Player human = game.getHumanPlayer();
        if (human != null) {
            humanMoneyLabel.setText("Your Money: $" + human.money);
            humanCurrentBetLabel.setText("Your Bet: $" + human.currentBet);
            // Update human player cards
            for (int i = 0; i < humanPlayerCardViews.size(); i++) {
                if (i < human.cards.size()) {
                    // Load card image (needs to map Card object to image file)
                    Card card = human.cards.get(i);
                    String cardImagePath = getCardImagePath(card); // Helper method needed
                    humanPlayerCardViews.get(i).setImage(new Image("file:" + cardImagePath));
                } else {
                    humanPlayerCardViews.get(i).setImage(null); // Clear unused card slots
                }
            }
        }

        // Update community cards
        for (int i = 0; i < communityCardViews.size(); i++) {
            if (i < game.getCommunityCards().size()) {
                Card card = game.getCommunityCards().get(i);
                String cardImagePath = getCardImagePath(card);
                communityCardViews.get(i).setImage(new Image("file:" + cardImagePath));
            } else {
                communityCardViews.get(i).setImage(null); // Clear unused card slots
            }
        }

        // TODO: Implement updates for CPU player money/bet labels and status (folded/all-in)
        // This would involve creating individual labels for each CPU player and updating them here.
    }

    /**
     * Helper method to get the file path for a card image.
     * Assumes card images are named like "suit_value.png" (e.g., "clubs_2.png", "hearts_K.png")
     * and located in "SampleJavaFXTemplate/src/main/java/com/example/images/cards/".
     *
     * @param card The Card object.
     * @return The file path to the card image.
     */
    private String getCardImagePath(Card card) {
        String valueStr;
        Object value = card.getValue();
        if (value instanceof Integer) {
            valueStr = value.toString(); // For 2-10, this is fine
        } else {
            switch ((String) value) {
                case "Jack": valueStr = "J"; break; // Use "J" for Jack
                case "Queen": valueStr = "Q"; break; // Use "Q" for Queen
                case "King": valueStr = "K"; break; // Use "K" for King
                case "Ace": valueStr = "A"; break; // Use "A" for Ace
                default: valueStr = "unknown"; // Fallback, though should not be hit
            }
        }
        String suitStr = card.getSuit().toLowerCase(); // "hearts", "diamonds", "clubs", "spades"
        // Construct the path: e.g., "clubs_2.png", "hearts_K.png"
        return "SampleJavaFXTemplate/src/main/java/com/example/images/cards/" + suitStr + "_" + valueStr + ".png";
    }

    public static void main(String[] args) {
        launch(args);
    }
}

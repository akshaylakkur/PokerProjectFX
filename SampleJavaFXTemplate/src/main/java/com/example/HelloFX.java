package com.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HelloFX extends Application {
    // MediaPlayer plays a short WAV sound when the button is clicked
    private final MediaPlayer c_major = new MediaPlayer(
        new Media(getClass().getResource("/C_major.wav").toString())
    );

    // These fields are used across multiple methods
    private Circle ball;
    private Scene scene;
    private double dx = 150;  // Horizontal speed in pixels/second
    private double dy = 120;  // Vertical speed in pixels/second

    @Override
    public void start(Stage stage) {
        // Label at the top
        Label label = new Label("Hello, World!");
        label.setLayoutX(150);
        label.setLayoutY(20);

        // Create a circle (the "ball") and set its initial position
        ball = new Circle(20, Color.BLUE);  // radius 20
        ball.setLayoutX(50);
        ball.setLayoutY(50);

        // Button to play sound
        Button playSoundButton = new Button("Play Sound");
        playSoundButton.setLayoutX(150);
        playSoundButton.setLayoutY(160);
        playSoundButton.setOnAction(event -> playSound());

        // Create an image
        Image image = new Image(getClass().getResource("/redman.png").toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setPreserveRatio(true);
        imageView.setX(110);
        imageView.setY(10);

        // Pane is used so we can manually position nodes (absolute layout)
        Pane root = new Pane();
        root.getChildren().addAll(ball, label, playSoundButton, imageView);

        // Create the scene and store it in a field for access from timer
        scene = new Scene(root, 400, 200);
        stage.setScene(scene);
        stage.setTitle("JavaFX AnimationTimer Example");
        stage.show();

        // Start the ball animation using a named inner class
        BallAnimationTimer timer = new BallAnimationTimer();
        timer.start();
    }

    // Method to move the ball based on how much time has passed
    private void moveBall(double elapsedSeconds) {
        // Update position based on speed and elapsed time
        double x = ball.getLayoutX() + dx * elapsedSeconds;
        double y = ball.getLayoutY() + dy * elapsedSeconds;

        // Check for wall collisions and reverse direction if needed
        if (x < ball.getRadius() || x >= scene.getWidth() - ball.getRadius()) {
            dx = -dx;
        }
        if (y < ball.getRadius() || y >= scene.getHeight() - ball.getRadius()) {
            dy = -dy;
        }

        // Apply new position
        ball.setLayoutX(x);
        ball.setLayoutY(y);
    }

    // Inner class that handles frame-by-frame animation using system time
    private class BallAnimationTimer extends AnimationTimer {
        private long lastUpdate = 0;  // Timestamp of the last frame

        @Override
        public void handle(long now) {
            if (lastUpdate > 0) {
                // Convert nanoseconds to seconds for speed calculation
                double elapsedSeconds = (now - lastUpdate) / 1_000_000_000.0;
                moveBall(elapsedSeconds);
            }
            lastUpdate = now;  // Update the timestamp for next frame
        }
    }

    // Play the sound from the beginning
    private void playSound() {
        if (c_major.getStatus() == MediaPlayer.Status.PLAYING) {
            c_major.stop();  // Stop if it's already playing
        }
        c_major.seek(Duration.ZERO);  // Reset to beginning
        c_major.play();
    }

    public static void main(String[] args) {
        System.out.println("JavaFX version: " + System.getProperty("javafx.runtime.version"));
        launch(args);
    }
}

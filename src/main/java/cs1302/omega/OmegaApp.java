package cs1302.omega;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Random;
import javafx.scene.shape.Line;

/**
 * Implementation of the arcade game "Pong". A single player will battle against
 * a computer to
 * not allow the ball past his/her own paddle which is controlled by the user's
 * mouse.
 */
public class OmegaApp extends Application {

    private int width = 800;
    private int height = 600;
    private int playerHeight = 100;
    private int playerWidth = 15;
    private double ballSize = 20;
    private double ballYSpeed = 1;
    private double ballXSpeed = 1;
    private double playerOneYPosition = height / 2;
    private double playerTwoYPosition = height / 2;
    private double ballXPosition = width / 2;
    private double ballYPosition = width / 2;
    private int playerOneScore = 0;
    private int playerTwoScore = 0;
    private boolean gameIsStarted;
    private int playerOneXPosition = 0;
    private int playerTwoXPosition = width - playerWidth;

    // main
    public static void main(String[] args) {
        try {
            Application.launch(args);
        } catch (Exception e) {
            System.err.println("Something went wrong I think");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param stage Stage object.
     * @throws Exception
     */
    public void start(Stage stage) throws Exception {

        // stage setup
        stage.setTitle("POnG");
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
        tl.setCycleCount(Timeline.INDEFINITE);

        // mouse code
        canvas.setOnMouseMoved(e -> playerOneYPosition = e.getY());
        canvas.setOnMouseClicked(e -> gameIsStarted = true);
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();
        tl.play();
    }

    /**
     * {@inheritDoc}
     * @param gc Graphical overlay which contains the drawn-in game elements.
     */
    private void run(GraphicsContext gc) {
        gc.setFill(Color.BLACK); // background color
        gc.fillRect(0, 0, width, height);
        gc.setFill(Color.WHITE); // text color
        gc.setFont(Font.font(35));
        if (gameIsStarted) {
            ballXPosition += ballXSpeed;  // ball movement
            ballYPosition += ballYSpeed;
            if (ballXPosition < width - width / 4) { //computer movement
                playerTwoYPosition = ballYPosition - (playerHeight / 2);
            } else {
                playerTwoYPosition = ballYPosition > playerTwoYPosition + playerHeight / 2
                            ? playerTwoYPosition += 1.525
                            : playerTwoYPosition - 1.525;
            }
            gc.fillOval(ballXPosition, ballYPosition, ballSize, ballSize); // drawing the ball
        } else {
            gc.setStroke(Color.WHITE);
            gc.setTextAlign(TextAlignment.CENTER);
            if (playerOneScore == 0 && playerTwoScore == 0) { // text at game launch
                gc.strokeText("Click to Start", width / 2, height / 2);
            }
            if (playerOneScore != 0 || playerTwoScore != 0) { // prompt to continue playing
                gc.strokeText("Click to Start the Next Round", width / 2, height / 2);
            }
            ballXPosition = width / 2; // ball position reset
            ballYPosition = height / 2; // ball position reset
            ballXSpeed = (new Random().nextInt(3) == 0) ? 1 : -1; // reset ball speed & direction
            ballYSpeed = (new Random().nextInt(3) == 0) ? 1 : -1; // reset ball speed & direction
        }
        if (ballYPosition > height || ballYPosition < 0) { // ball stays in canvas
            ballYSpeed *= -1;
        }
        if (ballXPosition > playerTwoXPosition + playerWidth) { // player score increment
            playerOneScore++;
            gameIsStarted = false;
        }
        if (ballXPosition < playerOneXPosition - playerWidth) { // computer score increment
            playerTwoScore++;
            gameIsStarted = false;
        }
        // // ball speed increase after every bounce between sides
        if (((ballXPosition + ballSize > playerTwoXPosition) && ballYPosition >= playerTwoYPosition
                && ballYPosition <= playerTwoYPosition + playerHeight) ||
                ((ballXPosition < playerOneXPosition + playerWidth)
                        && ballYPosition >= playerOneYPosition
                        && ballYPosition <= playerOneYPosition + playerHeight)) {
            Random rand = new Random();
            int redirectionAngler = (rand.nextInt(4) - 5) == 0 ? 4 : -4;
            ballYSpeed += Math.signum(ballYSpeed) * redirectionAngler;
            ballXSpeed += Math.signum(ballXSpeed);
            ballXSpeed *= -1;
            ballYSpeed *= -1;
        }
        gc.fillText(playerOneScore + "\t" + playerTwoScore, width / 2, 100); // show score
        gc.fillRect(playerTwoXPosition, playerTwoYPosition, playerWidth, playerHeight);
        gc.fillRect(playerOneXPosition, playerOneYPosition, playerWidth, playerHeight);
        gc.strokeLine(width / 2, height, width / 2, height * -1);// draw center line and paddles
    }
}

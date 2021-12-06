package pong;

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
public class Pong extends Application {

    private static final int width = 800;
    private static final int height = 600;
    private static final int PLAYER_HEIGHT = 100;
    private static final int PLAYER_WIDTH = 15;
    private static final double BALL_SIZE = 20;
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
    private int playerTwoXPosition = width - PLAYER_WIDTH;

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
     *
     * @param gc Graphical overlay which contains the drawn-in game elements.
     */
    private void run(GraphicsContext gc) {

        // background color
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        // text color
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(35));

        if (gameIsStarted) {

            // ball movement
            ballXPosition += ballXSpeed;
            ballYPosition += ballYSpeed;

            // computer movement
            if (playerOneScore == playerTwoScore) {
                if (ballXPosition < width - width / 4) {
                    playerTwoYPosition = ballYPosition - (PLAYER_HEIGHT / 2);
                } else {
                    playerTwoYPosition = ballYPosition > playerTwoYPosition + PLAYER_HEIGHT / 2
                            ? playerTwoYPosition += 1.525
                            : playerTwoYPosition - 1.525;
                }
            }

            // drawing the ball
            gc.fillOval(ballXPosition, ballYPosition, BALL_SIZE, BALL_SIZE);
        } else {
            gc.setStroke(Color.WHITE);
            gc.setTextAlign(TextAlignment.CENTER);

            // text at game launch
            if (playerOneScore == 0 && playerTwoScore == 0) {
                gc.strokeText("Click to Start", width / 2, height / 2);
            }

            // prompt to continue playing
            if (playerOneScore != 0 || playerTwoScore != 0) {
                gc.strokeText("Click to Start the Next Round", width / 2, height / 2);
            }

            // ball position reset
            ballXPosition = width / 2;
            ballYPosition = height / 2;

            // reset ball speed & direction
            ballXSpeed = (new Random().nextInt(3) == 0) ? 1 : -1;

            ballYSpeed = (new Random().nextInt(3) == 0) ? 1 : -1;
        }

        // ball stays in canvas
        if (ballYPosition > height || ballYPosition < 0)
            ballYSpeed *= -1;

        // player score increment
        if (ballXPosition > playerTwoXPosition + PLAYER_WIDTH) {
            playerOneScore++;
            gameIsStarted = false;
        }

        // computer score increment
        if (ballXPosition < playerOneXPosition - PLAYER_WIDTH) {
            playerTwoScore++;
            gameIsStarted = false;
        }

        // // ball speed increase after every bounce between sides
        if (((ballXPosition + BALL_SIZE > playerTwoXPosition) && ballYPosition >= playerTwoYPosition
                && ballYPosition <= playerTwoYPosition + PLAYER_HEIGHT) ||
                ((ballXPosition < playerOneXPosition + PLAYER_WIDTH)
                        && ballYPosition >= playerOneYPosition
                        && ballYPosition <= playerOneYPosition + PLAYER_HEIGHT)) {
            Random rand = new Random();
            int redirectionAngler = (rand.nextInt(4) - 5) == 0 ? 4 : -4;
            ballYSpeed += Math.signum(ballYSpeed) * redirectionAngler;
            ballXSpeed += Math.signum(ballXSpeed);

            ballXSpeed *= -1;
            ballYSpeed *= -1;
        }

        // show score
        gc.fillText(playerOneScore + "\t" + playerTwoScore, width / 2, 100);

        // draw pong paddles
        gc.fillRect(playerTwoXPosition, playerTwoYPosition, PLAYER_WIDTH, PLAYER_HEIGHT);
        gc.fillRect(playerOneXPosition, playerOneYPosition, PLAYER_WIDTH, PLAYER_HEIGHT);

        // draw center line
        gc.strokeLine(width / 2, height, width / 2, height * -1);

        /**
         * dynamic difficulty adjustment:
         * if player one has more points computer opponent gets faster and if player one has less
         * points computer opponent gets slower. when player one has an equal amount of points to
         * player two then
         * the difficulty will revert back to the original difficulty at game start
         */
        if (playerOneScore > playerTwoScore) {
            if (ballXPosition < width - width / 4) {
                playerTwoYPosition = ballYPosition - (PLAYER_HEIGHT / 2);
            } else {
                playerTwoYPosition = ballYPosition > playerTwoYPosition + PLAYER_HEIGHT / 2
                        ? playerTwoYPosition += 2.2
                        : playerTwoYPosition - 2.2;
            }
        }
        if (playerOneScore < playerTwoScore) {
            if (ballXPosition < width - width / 8) {
                playerTwoYPosition = ballYPosition - PLAYER_HEIGHT;
            } else {
                playerTwoYPosition = ballYPosition > playerTwoYPosition + PLAYER_HEIGHT / 2
                        ? playerTwoYPosition += 1.15
                        : playerTwoYPosition - 1.15;
            }
        }

    }
}

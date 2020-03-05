package demo;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

public class Main extends Application {
    DynamicAnimator animator;
    Pane root;
    OuterBoundaryCollisionBehavior gateCollision;
    InnerBoundaryCollisionBehavior boundaryCollision;
    OuterBoundaryCollisionBehavior gateWallTopCollision;
    OuterBoundaryCollisionBehavior gateWallBottomCollision;
    PushBehavior push;
    ArrayList<Ball> allBalls = new ArrayList<Ball>();

    @Override
    public void start(Stage primaryStage) throws Exception{
        root = new Pane();
        primaryStage.setTitle("Separate Blue From Red");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();

        setUpAnimatorAndBehaviors();
        createInitialBalls();

        // Add the button
        Button button = new Button("Add Particle");
        root.getChildren().add(button);
        button.setLayoutX(460);
        button.setLayoutY(525);
        button.setOnAction((ActionEvent e) -> {
            addBall();
        });

        setUpMouseTracking();

        // Start the animation
        animator.start();

    }

    // Sets up the animator and collision behaviors
    private void setUpAnimatorAndBehaviors() {
        // Setting up the animator and the behaviors
        animator = new DynamicAnimator();
        boundaryCollision = new InnerBoundaryCollisionBehavior(0, 0, 500, 1000);
        gateWallTopCollision = new OuterBoundaryCollisionBehavior(0, 498, 200, 502);
        gateWallBottomCollision = new OuterBoundaryCollisionBehavior(300, 498, 500, 502);
        boundaryCollision.setVisualizeBoundary(true);
        gateWallTopCollision.setVisualizeBoundary(true);
        gateWallBottomCollision.setVisualizeBoundary(true);

        // Add the gate
        gateCollision = new OuterBoundaryCollisionBehavior(200, 498, 300, 502);
        gateCollision.setVisualizeBoundary(true);
        gateCollision.setVisualizationStyle(Color.YELLOW);

        push = new PushBehavior(PushBehavior.PushType.INSTANTANEOUS);
        animator.addBehavior(boundaryCollision);
        animator.addBehavior(gateWallTopCollision);
        animator.addBehavior(gateWallBottomCollision);
        animator.addBehavior(gateCollision);
        animator.addBehavior(push);

        // Visualize the gate and the walls
        root.getChildren().add(boundaryCollision);
        root.getChildren().add(gateWallTopCollision);
        root.getChildren().add(gateWallBottomCollision);
        root.getChildren().add(gateCollision);
    }

    private void createInitialBalls() {
        for (int i = 0; i < 6; i++) {
            addBall();
        }
    }

    // Adds a random blue or red ball
    private void addBall() {
        Random random = new Random();
        boolean isBlue = random.nextBoolean();
        boolean isOnLeft = random.nextBoolean();
        double x = isOnLeft ? 250 : 750;
        if (isBlue) {
            Ball blueBall = new Ball(x, 250, true);
            root.getChildren().add(blueBall);
            boundaryCollision.addItem(blueBall);
            gateWallTopCollision.addItem(blueBall);
            gateWallBottomCollision.addItem(blueBall);
            gateCollision.addItem(blueBall);
            push.addItem(blueBall);
            allBalls.add(blueBall);
        } else {
            Ball redBall = new Ball(x, 250, false);
            root.getChildren().add(redBall);
            boundaryCollision.addItem(redBall);
            gateWallTopCollision.addItem(redBall);
            gateWallBottomCollision.addItem(redBall);
            gateCollision.addItem(redBall);
            push.addItem(redBall);
            allBalls.add(redBall);
        }
    }

    private void mouseMoveToPoint(double x, double y) {
        int gateHeight = 100;
        int originalGateCenterY = 250;
        int gateMinY = Math.max(0, Math.min(500 - gateHeight, (int)y - gateHeight / 2));
        int gateMaxY = Math.max(gateHeight, Math.min(500, (int)y + gateHeight / 2));
        int gateCenterY = (gateMaxY + gateMinY) / 2;
        int deltaY = gateCenterY - originalGateCenterY;

        gateCollision.setPosition(gateMinY, 498, gateMaxY, 502);
        gateWallTopCollision.setPosition(0, 498, gateMinY, 502);
        gateWallBottomCollision.setPosition(gateMaxY, 498, 500, 502);
    }

    private void removeGateCollision() {
        animator.removeBehavior(gateCollision);
        gateCollision.setVisualizeBoundary(false);
    }

    private void addGateCollision() {
        animator.addBehavior(gateCollision);
        gateCollision.setVisualizeBoundary(true);
    }

    private void setUpMouseTracking() {
        // Add the mouse tracking
        root.setOnMouseMoved((MouseEvent e) -> {
            // Move the gate and the collision walls
            mouseMoveToPoint(e.getX(), e.getY());
        });

        root.setOnMousePressed((MouseEvent e) -> {
            removeGateCollision();
        });

        root.setOnMouseReleased((MouseEvent e) -> {
            addGateCollision();
            checkForSuccess();
        });

        root.setOnMouseDragged((MouseEvent e) -> {
            mouseMoveToPoint(e.getX(), e.getY());
            removeGateCollision();
        });
    }

    private void checkForSuccess() {
        boolean isFirstBall = true;
        boolean blueOnLeft = true;

        for (Ball ball: allBalls) {
            if (isFirstBall) {  // If first ball, record location
                if (ball.isBlue) {
                    blueOnLeft = ballOnLeft(ball);
                } else {
                    blueOnLeft = !ballOnLeft(ball);
                }
                isFirstBall = false;
            } else {    // Not first ball. Check same location
                if (ball.isBlue) {
                    if (blueOnLeft != ballOnLeft(ball)) {
                        return;
                    }
                } else {
                    if (blueOnLeft == ballOnLeft(ball)) {
                        return;
                    }
                }
            }


        }

        // Passed on checks. Game Finished.
        gameFinished();
    }

    private void gameFinished() {
        Text finishText = new Text("You Win!");
        finishText.setFont(Font.font(40));
        finishText.setStroke(Color.WHITE);
        root.getChildren().add(finishText);
        finishText.setLayoutX(430);
        finishText.setLayoutY(250);
    }

    boolean ballOnLeft(Ball ball) {
        return ball.getBounds().getMaxX() < 500;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

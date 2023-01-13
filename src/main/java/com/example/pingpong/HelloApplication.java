package com.example.pingpong;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

import static java.lang.Math.*;

public class HelloApplication extends Application {
    Scene scene;
    boolean[] keysPressed = new boolean[4];
    double ballAngle = 1;
    int[] scores = new int[2];


    @Override
    public void start(Stage stage) throws IOException {
        Group group = new Group();
        Rectangle background = new Rectangle(800, 500);
        background.setFill(Paint.valueOf("green"));
        background.setStroke(Paint.valueOf("blue"));
        background.setStrokeWidth(20);
        background.setX(10);
        background.setY(10);
        group.getChildren().add(background);
        Rectangle playerRect = new Rectangle(20, 100);
        playerRect.setFill(Paint.valueOf("yellow"));
        playerRect.setStroke(Paint.valueOf("black"));
        playerRect.setStrokeWidth(5);
        playerRect.setX(770);
        playerRect.setY(100);
        group.getChildren().add(playerRect);

        Rectangle botRect = new Rectangle(20, 100);
        botRect.setFill(Paint.valueOf("red"));
        botRect.setStroke(Paint.valueOf("black"));
        botRect.setStrokeWidth(5);
        botRect.setX(30);
        botRect.setY(200);
        group.getChildren().add(botRect);

        Circle ball = new Circle(20, Paint.valueOf("white"));
        ball.setCenterX(400);
        ball.setCenterY(250);
        group.getChildren().add(ball);

        scene = new Scene(group);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.UP) {
                    keysPressed[0] = true;
                } else if (keyEvent.getCode() == KeyCode.DOWN) {
                    keysPressed[1] = true;
                } else if (keyEvent.getCode() == KeyCode.Q) {
                    Platform.exit();
                } else if (keyEvent.getCode() == KeyCode.R) {
                    ball.setCenterX(400);
                    ball.setCenterY(250);
                }
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.UP) {
                    keysPressed[0] = false;
                } else if (keyEvent.getCode() == KeyCode.DOWN) {
                    keysPressed[1] = false;
                }
            }
        });
        stage.setTitle("Ping Pong!");
        stage.setScene(scene);
        stage.show();


        KeyFrame keyFrame = new KeyFrame(new Duration(50),
                event -> {
                    movePlayer(playerRect);
                    moveBot(botRect, ball);
                    moveBall(ball);
                    ballCollision(ball, playerRect, botRect);
                }
        );

        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    void ballCollision(Circle ball, Rectangle playerRect, Rectangle botRect) {

        if (intersects(ball, playerRect)) {
            double totalHeight = 2*ball.getRadius() + playerRect.getHeight();
            double heightDifference = ball.getCenterY()  + ball.getRadius() - playerRect.getY();
            ballAngle  = 3 * PI / 4 + PI/2 * heightDifference / totalHeight;

        } else if (intersects(ball, botRect)) {
            double totalHeight = 2*ball.getRadius() + botRect.getHeight();
            double heightDifference = ball.getCenterY()  + ball.getRadius() - botRect.getY();
            ballAngle  = 1 * PI / 4 - PI/2 * heightDifference / totalHeight;
        }
    }
    boolean intersects(Circle ball, Rectangle rect)
    {
        double circleDistanceX = abs(ball.getCenterX() - rect.getX()-rect.getWidth()/2);
        double circleDistanceY = abs(ball.getCenterY() - rect.getY()-rect.getHeight()/2);

        if (circleDistanceX > (rect.getWidth()/2 + ball.getRadius())) { return false; }
        if (circleDistanceY > (rect.getHeight()/2 + ball.getRadius())) { return false; }

        if (circleDistanceX <= (rect.getWidth()/2)) { return true; }
        if (circleDistanceY <= (rect.getHeight()/2)) { return true; }

        double cornerDistance_sq = pow((circleDistanceX - rect.getWidth()/2),2) + pow((circleDistanceY - rect.getHeight()/2), 2);

        return (cornerDistance_sq <= pow(ball.getRadius(), 2));
    }

    void movePlayer(Rectangle playerRect) {
        if (keysPressed[0] && playerRect.getY() > 20) {
            playerRect.setY(playerRect.getY() - 4);
        }
        if (keysPressed[1] && playerRect.getY() + playerRect.getHeight() + 20 < scene.getHeight()) {
            playerRect.setY(playerRect.getY() + 4);
        }

    }

    void moveBot(Rectangle botRect, Circle ball) {

    }

    void moveBall(Circle ball) {
        if (ball.getCenterX() < 10 || ball.getCenterX() > 810) {
            return;
        }
        double newPosX = ball.getCenterX() + cos(ballAngle) * 10;
        double newPosY = ball.getCenterY() - sin(ballAngle) * 10;

        if (newPosY - ball.getRadius() < 0) {
            newPosY = -newPosY + 2 * ball.getRadius();
            ballAngle = 2 * PI - ballAngle;
        } else if (newPosY + ball.getRadius() > 520) {
            newPosY = 1040 - 2 * ball.getRadius() - newPosY;
            ballAngle = 2 * PI - ballAngle;
        }
        ball.setCenterX(newPosX);
        ball.setCenterY(newPosY);
    }


    public static void main(String[] args) {
        launch();
    }
}
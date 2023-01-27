package com.example.pingpong;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.security.Key;
import java.util.Random;

import static java.lang.Math.*;

public class HelloApplication extends Application {
    Scene scene;
    boolean[] keysPressed = new boolean[4];
    double ballAngle = 1;
    int[] scores = new int[2];

    Timeline timeline;
    Label score;

    Rectangle playerRect, botRect, background;
    Circle ball;
    boolean pvp;
    double w = 0, h = 0;
    Stage stage;

    int ballSpeed = 10, playerSpeed = 4;

    Rectangle[] obstacles;

    @Override
    public void start(Stage stage) throws IOException {
        pvp = false;
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        this.stage = stage;
        stage.setMinHeight(500);
        stage.setMinWidth(1000);
        w = 800;
        h = 500;
        Group group = new Group();
        background = new Rectangle(w, h);
        background.setFill(Paint.valueOf("green"));
        background.setX(0);
        background.setY(0);
        group.getChildren().add(background);
        playerRect = new Rectangle(20, 100);
        playerRect.setFill(Paint.valueOf("yellow"));
        playerRect.setStroke(Paint.valueOf("black"));
        playerRect.setStrokeWidth(5);
        playerRect.setX(w - 40);
        playerRect.setY(h/2 - 50);
        group.getChildren().add(playerRect);

        botRect = new Rectangle(20, 100);
        botRect.setFill(Paint.valueOf("red"));
        botRect.setStroke(Paint.valueOf("black"));
        botRect.setStrokeWidth(5);
        botRect.setX(10);
        botRect.setY(h/2 - 50);
        group.getChildren().add(botRect);

        ball = new Circle(20, Paint.valueOf("white"));
        ball.setCenterX(w/2);
        ball.setCenterY(h/2);
        group.getChildren().add(ball);

        score = new Label("0:0");
        score.setLayoutX(background.getWidth()/2);
        score.setLayoutY(0);
        score.setFont(new Font("red", 30));
        score.setStyle("-fx-background-color: transparent;");
        group.getChildren().add(score);

        scene = new Scene(group);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.UP) {
                    keysPressed[0] = true;
                } else if (keyEvent.getCode() == KeyCode.DOWN) {
                    keysPressed[1] = true;
                }else if (keyEvent.getCode() == KeyCode.W) {
                    keysPressed[2] = true;
                }else if (keyEvent.getCode() == KeyCode.S) {
                    keysPressed[3] = true;
                } else if (keyEvent.getCode() == KeyCode.Q) {
                    Platform.exit();
                } else if (keyEvent.getCode() == KeyCode.M) {
                    pvp = !pvp;
                }else if (keyEvent.getCode() == KeyCode.R) {
                    ball.setCenterX(400);
                    ball.setCenterY(250);
                    timeline.play();
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
                }else if (keyEvent.getCode() == KeyCode.W) {
                    keysPressed[2] = false;
                }else if (keyEvent.getCode() == KeyCode.S) {
                    keysPressed[3] = false;
                }
            }
        });
        stage.setTitle("Ping Pong!");
        stage.setScene(scene);
        stage.show();


        int amountOfObstacles = 3;
        obstacles = new Rectangle[amountOfObstacles];
        Random rand = new Random();
        for(int i = 0; i < 3; ++i){
            obstacles[i] = new Rectangle(20, 100);
            obstacles[i].setY(rand.nextDouble(0, stage.getHeight() - 100));
            obstacles[i].setX(rand.nextDouble(100, stage.getWidth() - 100));
            obstacles[i].setFill(Paint.valueOf("gray"));
            obstacles[i].setStroke(Paint.valueOf("black"));
            obstacles[i].setStrokeWidth(5);

            group.getChildren().add(obstacles[i]);
        }


        KeyFrame keyFrame = new KeyFrame(new Duration(50), event -> {
                    changeValues();
                    movePlayer();
                    moveBot(ball);
                    moveBall(ball);
                    ballCollision(ball);
                });

        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        Timeline t = new Timeline(new KeyFrame(new Duration(10000), event -> {
            ballSpeed += 2;
            playerSpeed += 1;
        }));
        t.setCycleCount(Timeline.INDEFINITE);
        t.play();



    }
    void changeValues(){
        double newW = stage.getWidth();
        double newH = stage.getHeight();
        if(h == newH && w == newW){
            return;
        }
        ball.setCenterX(ball.getCenterX()/w*newW);
        ball.setCenterY(ball.getCenterY()/h*newH);
        background.setWidth(newW);
        background.setHeight(newH);
        playerRect.setX(playerRect.getX()/w*newW);
        playerRect.setY(playerRect.getY()/h*newH);
        botRect.setX(botRect.getX()/w*newW);
        botRect.setY(botRect.getY()/h*newH);
        score.setLayoutX(newW/2);
        w = newW;
        h = newH;
    }

    void ballCollision(Circle ball) {
        if (intersects(ball, playerRect)) {
            double totalHeight = 2*ball.getRadius() + playerRect.getHeight();
            double heightDifference = ball.getCenterY()  + ball.getRadius() - playerRect.getY();
            ballAngle  = 3 * PI / 4 + PI/2 * heightDifference / totalHeight;

        } else if (intersects(ball, botRect)) {
            double totalHeight = 2*ball.getRadius() + botRect.getHeight();
            double heightDifference = ball.getCenterY()  + ball.getRadius() - botRect.getY();
            ballAngle  = PI / 4 - PI/2 * heightDifference / totalHeight;
        }/*else{
            for(Rectangle i : obstacles){
                if(intersects(ball, i)){
                    double totalHeight = 2*ball.getRadius() + i.getHeight();
                    double heightDifference = ball.getCenterY()  + ball.getRadius() - i.getY();
                    if(ballAngle % 2* PI > PI / 2 && ballAngle % 2* PI < 3 * PI / 2 ) {
                        ballAngle = 3 * PI / 4 - PI / 2 * heightDifference / totalHeight;
                    }else{
                        ballAngle  = PI / 4 + PI/2 * heightDifference / totalHeight;
                    }
                    if(ballAngle < PI/2){
                        if(ball.getCenterX() > i.getX()){
                            ballAngle = 2 * PI - ballAngle;
                        }
                    }
                }
            }
        }//*/
    }

    boolean intersects(Circle ball, Rectangle rect) {
        double circleDistanceX = abs(ball.getCenterX() - rect.getX()-rect.getWidth()/2);
        double circleDistanceY = abs(ball.getCenterY() - rect.getY()-rect.getHeight()/2);

        if (circleDistanceX > (rect.getWidth()/2 + ball.getRadius())) { return false; }
        if (circleDistanceY > (rect.getHeight()/2 + ball.getRadius())) { return false; }

        if (circleDistanceX <= (rect.getWidth()/2)) { return true; }
        if (circleDistanceY <= (rect.getHeight()/2)) { return true; }

        double cornerDistance_sq = pow((circleDistanceX - rect.getWidth()/2),2) + pow((circleDistanceY - rect.getHeight()/2), 2);

        return (cornerDistance_sq <= pow(ball.getRadius(), 2));
    }

    void movePlayer() {
        if (keysPressed[0] && playerRect.getY() > 20) {
            playerRect.setY(playerRect.getY() - playerSpeed);
        }
        if (keysPressed[1] && playerRect.getY() + playerRect.getHeight() + 20 < scene.getHeight()) {
            playerRect.setY(playerRect.getY() + playerSpeed);
        }
    }

    void moveBot(Circle ball) {
        if(pvp){
            if (keysPressed[2] && botRect.getY() > 20) {
                botRect.setY(botRect.getY() - playerSpeed);
            }
            if (keysPressed[3] && botRect.getY() + botRect.getHeight() + 20 < scene.getHeight()) {
                botRect.setY(botRect.getY() + playerSpeed);
            }
        }else{
            if (botRect.getY() + botRect.getHeight()/3 > ball.getCenterY()) {
                botRect.setY(botRect.getY() - playerSpeed);
            } else if (botRect.getY() + botRect.getHeight()*2/3 < ball.getCenterY()) {
                botRect.setY(botRect.getY() + playerSpeed);
            }
        }
    }

    void moveBall(Circle ball) {
        if(ball.getCenterX() < 0 || ball.getCenterX() > w){
            if (ball.getCenterX() < 0) {
                ++scores[1];
                ballAngle = PI;
            }else if(ball.getCenterX() > w) {
                ++scores[0];
                ballAngle = 0;
            }
            playerRect.setY(background.getHeight()/2 - playerRect.getHeight()/2);
            botRect.setY(background.getHeight()/2 - playerRect.getHeight()/2);
            timeline.pause();
            score.setText(scores[0] + ":" + scores[1]);
            ballSpeed = 10;
            playerSpeed = 4;
            return;
        }

        double newPosX = ball.getCenterX() + cos(ballAngle) * ballSpeed;
        double newPosY = ball.getCenterY() - sin(ballAngle) * ballSpeed;

        if (newPosY - ball.getRadius() < 0) {
            newPosY = -newPosY + 2 * ball.getRadius();
            ballAngle = 2 * PI - ballAngle;
        } else if (newPosY + ball.getRadius() > h - 30) {
            newPosY = (h - 30) * 2 - 2 * ball.getRadius() - newPosY;
            ballAngle = 2 * PI - ballAngle;
        }
        ball.setCenterX(newPosX);
        ball.setCenterY(newPosY);
    }


    public static void main(String[] args) {
        launch();
    }
}
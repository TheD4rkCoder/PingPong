package com.example.pingpong;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Group group = new Group();
        Rectangle r = new Rectangle(800, 500);
        r.setFill(Paint.valueOf("green"));
        r.setStroke(Paint.valueOf("blue"));
        r.setStrokeWidth(20);
        r.setX(10);
        r.setY(10);
        group.getChildren().add(r);
        Rectangle r2 = new Rectangle(20, 100);
        r2.setFill(Paint.valueOf("yellow"));
        r2.setStroke(Paint.valueOf("black"));
        r2.setStrokeWidth(5);
        r2.setX(30);
        r2.setY(100);
        group.getChildren().add(r2);
        Rectangle r3 = new Rectangle(20, 100);
        r3.setFill(Paint.valueOf("red"));
        r3.setStroke(Paint.valueOf("black"));
        r3.setStrokeWidth(5);
        r3.setX(770);
        r3.setY(200);
        group.getChildren().add(r3);


        Scene scene = new Scene(group);
        stage.setTitle("Ping Pong!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
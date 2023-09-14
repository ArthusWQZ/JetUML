package org.jetuml.gui;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ToastNotification {

    private final Stage aStage;

    protected ToastNotification(String pMessage, Stage pOwnerStage) {

        Stage stage = new Stage();

        stage.initOwner(pOwnerStage);
        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);

        Text text = new Text(pMessage);

        text.setFont(Font.font(10));
        text.setFill(Color.WHITE);

        StackPane pane = new StackPane(text);

        pane.setStyle("-fx-padding: 10px; -fx-background-color: rgb(200, 50, 50); -fx-background-radius: 10");
        pane.setOpacity(0);

        Scene scene = new Scene(pane);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);

        this.aStage = stage;
    }

}

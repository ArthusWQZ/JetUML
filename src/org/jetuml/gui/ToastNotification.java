package org.jetuml.gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class ToastNotification {

    private static final int FADE_IN_DELAY = 500;
    private static final int FADE_OUT_DELAY = 500;
    private static final int NOTIFICATION_DELAY = 5000;

    private final Stage aStage;

    protected ToastNotification(String pMessage, Stage pOwnerStage) {

        Stage stage = new Stage();

        stage.initOwner(pOwnerStage);
        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);

        Text text = new Text(pMessage);

        text.setFont(Font.font(12));
        text.setFill(Color.WHITE);

        StackPane pane = new StackPane(text);

        pane.setStyle("-fx-padding: 8px; -fx-background-color: rgb(200, 70, 70); -fx-background-radius: 10");
        pane.setOpacity(0);

        Scene scene = new Scene(pane);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);

        this.aStage = stage;
    }

    protected void show() {
        this.aStage.show();

        Timeline fadeInTimeline = new Timeline();
        KeyFrame fadeInKey = new KeyFrame(Duration.millis(FADE_IN_DELAY), new KeyValue(this.aStage.getScene().getRoot().opacityProperty(), 1));

        fadeInTimeline.getKeyFrames().add(fadeInKey);
        fadeInTimeline.setOnFinished(actionEvent -> {
            new Thread(() -> {
                try {
                    Thread.sleep(NOTIFICATION_DELAY);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }

                Timeline fadeOutTimeline = new Timeline();
                KeyFrame fadeOutKey = new KeyFrame(Duration.millis(FADE_OUT_DELAY), new KeyValue(this.aStage.getScene().getRoot().opacityProperty(), 0));

                fadeOutTimeline.getKeyFrames().add(fadeOutKey);
                fadeOutTimeline.setOnFinished(actionEvent1 -> aStage.close()); // AUTO CLOSE ?

                fadeOutTimeline.play();


            }).start();
        });
        fadeInTimeline.play();
    }

}

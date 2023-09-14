package org.jetuml.gui;

import javafx.stage.Stage;

public class ToastNotification {

    private final String aMessage;
    private final Stage aStage;

    protected ToastNotification(String pMessage) {
        this.aMessage = pMessage;
        this.aStage = new Stage();
    }

}

package org.jetuml.gui;

import javafx.stage.Stage;

import java.util.LinkedList;

public class NotificationHandler {

    private static final NotificationHandler INSTANCE = new NotificationHandler();

    private Stage aMainStage;
    private LinkedList<ToastNotification> aNotificationList = new LinkedList<>();

    private NotificationHandler() {}

    public static NotificationHandler getInstance() { return INSTANCE; }

    public void setStage(Stage pMainStage) {
        this.aMainStage = pMainStage;
    }

    public void spawn(String pText) {

        if (this.aMainStage == null) return;

        double x = this.aMainStage.getX()+18;
        double y = this.aMainStage.getY()+this.aMainStage.getHeight()-18;

        ToastNotification toast = new ToastNotification(pText, this.aMainStage);
        toast.show(x, y);

    }
}

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

    private void updateVerticalPosition(int pOffset) {

        double y = this.aMainStage.getY() + this.aMainStage.getHeight() - 18;
        for (int i = 0; i < aNotificationList.size(); i++) {
            ToastNotification toast = aNotificationList.get(i);
            toast.setY( y - 40*( (aNotificationList.size() - 1 - i) + pOffset) );
        }

    }

    public class CleanUpCallback {

        private CleanUpCallback() {}

        public void execute(ToastNotification pToast) {
            if (INSTANCE.aNotificationList.contains(pToast)) {
                INSTANCE.aNotificationList.remove(pToast);
            }
        }

    }

    public void spawn(String pText) {

        if (this.aMainStage == null) return;

        double x = this.aMainStage.getX()+18;
        double y = this.aMainStage.getY()+this.aMainStage.getHeight()-18;

        ToastNotification toast = new ToastNotification(pText, this.aMainStage);

        this.updateVerticalPosition(1);
        aNotificationList.add(toast);

        // System.out.println(aNotificationList.size());

        toast.show(x, y, new CleanUpCallback());

        // TODO: remove toast from aNotificationList

    }
}

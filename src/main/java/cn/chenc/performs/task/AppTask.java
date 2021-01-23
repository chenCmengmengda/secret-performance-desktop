package cn.chenc.performs.task;

import cn.chenc.performs.controller.AppController;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.Parent;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2020/12/29 12:57
 *
 */
public class AppTask extends ScheduledService<Void> {

    private AppController appController;

    private Parent root;

    public AppTask() {
    }

    public AppTask(AppController appController, Parent root) {
        this.appController = appController;
        this.root = root;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() {
                appController.startGetSystemInfo();
                return null;
            }
        };
    }
}

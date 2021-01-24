package cn.chenc.performs.factory;

import javafx.stage.Stage;

/**
 * 　@description: javafx启动的主舞台
 * 　@author secret
 * 　@date 2021/1/5 17:27
 *
 */
public class BaseStage implements StageInterface{

    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        BaseStage.stage = stage;
    }

    @Override
    public void close() {
        if(stage!=null) {
            stage.close();
        }
    }

    @Override
    public void show() {
        stage.show();
    }

    @Override
    public void setFps(double fps){

    }
}

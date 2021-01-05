package cn.chenc.performs.factory;

import javafx.stage.Stage;

/**
 * 　@description: javafx启动的主舞台
 * 　@author secret
 * 　@date 2021/1/5 17:27
 *
 */
public class BaseStage {

    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        BaseStage.stage = stage;
    }
}

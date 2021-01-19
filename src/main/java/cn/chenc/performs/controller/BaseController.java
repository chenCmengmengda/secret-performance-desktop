package cn.chenc.performs.controller;

import cn.chenc.performs.factory.BaseStage;
import javafx.stage.Stage;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2021/1/19 17:53
 *
 */
public class BaseController {

    private Stage stage;

    BaseController(){
        this.stage= BaseStage.getStage();
    }

    public void show(){
        stage.show();
    }

    public void close(){
        stage.close();
    }
}

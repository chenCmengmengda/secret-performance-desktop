package cn.chenc.performs.state;

import cn.chenc.performs.controller.SetupController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2020/12/29 14:49
 *
 */
public class SetupState extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        URL url= SetupState.class.getClassLoader().getResource("setup.fxml");

        String urlStr=java.net.URLDecoder.decode(String.valueOf(url),"utf-8");
        url=new URL(urlStr);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent root = fxmlLoader.load();
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("设置");
        stage.getIcons().add(new Image(
                SetupState.class.getResourceAsStream("/images/icon.png")));
        Scene scene=new Scene(root, 300, 200);
        stage.setScene(scene);
        SetupController setupController=fxmlLoader.getController();
        stage.show();
    }
}

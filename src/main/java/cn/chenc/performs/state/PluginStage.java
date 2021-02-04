package cn.chenc.performs.state;

import cn.chenc.performs.factory.BaseStage;
import cn.chenc.performs.factory.SingletonFactory;
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
 * 　@date 2021/2/2 20:35
 *
 */
public class PluginStage extends BaseStage {
    private static PluginStage instance = null;
    private Stage mainStage;

    public static PluginStage getInstance() {
        if (instance == null) {
            instance = SingletonFactory.getWeakInstace(PluginStage.class);
        }
        return instance;
    }

    private void start() throws Exception {
        Stage stage = new Stage();
        mainStage=stage;
        URL url= PluginStage.class.getResource("/fxml/plugin.fxml");

        String urlStr=java.net.URLDecoder.decode(String.valueOf(url),"utf-8");
        url=new URL(urlStr);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent root = fxmlLoader.load();
        root.getStylesheets().add(getClass().getResource("/css/plugin.css").toExternalForm());
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("插件扩展");
        stage.getIcons().add(new Image(
                PluginStage.class.getResourceAsStream("/images/icon.png")));
        Scene scene=new Scene(root, 350, 450);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void close() {
        if(mainStage!=null) {
            mainStage.close();
        }
    }

    @Override
    public void show(){
        if(mainStage!=null) {
            if(mainStage.isIconified()){ mainStage.setIconified(false);}
            if(!mainStage.isShowing()){ mainStage.show(); }
            mainStage.toFront();
        } else{
            try {
                getInstance().start();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}

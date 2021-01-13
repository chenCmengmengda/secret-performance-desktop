package cn.chenc.performs.state;

import cn.chenc.performs.consts.StageTitleConst;
import cn.chenc.performs.factory.BaseStage;
import cn.chenc.performs.factory.SingletonFactory;
import cn.chenc.performs.util.Win32Util;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.net.URL;


/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2021/1/13 9:51
 *
 */
public class MediaWallpaperStage extends BaseStage {

    private static MediaWallpaperStage instance = null;
    private Dimension screenSize;
    private Stage mainStage;

    public MediaWallpaperStage() {

    }

    //调用单例工厂
    public static MediaWallpaperStage getInstance() {
        if (instance == null) {
            instance = SingletonFactory.getWeakInstace(MediaWallpaperStage.class);
        }
        return instance;
    }

    public void start() throws Exception {
        Stage stage=new Stage();
        mainStage=stage;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();//获取屏幕
        stage.initOwner(BaseStage.getStage());
        stage.initStyle(StageStyle.TRANSPARENT);
        URL url= SetupState.class.getResource("/fxml/mediaWallpaper.fxml");

        String urlStr=java.net.URLDecoder.decode(String.valueOf(url),"utf-8");
        url=new URL(urlStr);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent root = fxmlLoader.load();
        root.getStylesheets().add(getClass().getResource("/css/mediaWallpaper.css").toExternalForm());

        Scene scene=new Scene(root,screenSize.getWidth(),screenSize.getHeight());
        stage.setX(0);
        stage.setY(0);
        stage.setTitle(StageTitleConst.MEDIAWALLPAPERTITLE);
        stage.setScene(scene);
        stage.show();
        Win32Util.setWinIconAfter(StageTitleConst.MEDIAWALLPAPERTITLE);
    }

    @Override
    public void close() {
        mainStage.close();
    }

    @Override
    public void show(){
        if(mainStage!=null) {
            mainStage.show();
            Win32Util.setWinIconAfter(StageTitleConst.MEDIAWALLPAPERTITLE);
        } else{
            try {
                getInstance().start();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }


}

package cn.chenc.performs.controller;

import cn.chenc.performs.enums.ConfigEnum;
import cn.chenc.performs.state.WebWallpaperStage;
import cn.chenc.performs.util.ConfigPropertiesUtil;
import cn.chenc.performs.util.StringUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.awt.*;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2021/1/19 15:43
 *
 */
public class WebWallpaperController extends BaseController{
    private Dimension screenSize;
    private static WebWallpaperController instance;
    private Stage stage;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private WebView webView;

    public static WebWallpaperController getInstance(){
        return instance;
    }

    @FXML
    private void initialize() {
        instance=this;
        Platform.runLater(()->{
            stage= (Stage) rootAnchorPane.getScene().getWindow();
        });
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();//获取屏幕
        String webWallpaperPathConf= ConfigPropertiesUtil.get(ConfigEnum.WEBWALLPAPERPATH.getKey());
        initWebView();
        if(!StringUtil.isEmpty(webWallpaperPathConf)) {
//            setMedia("file:///E:/upload/secretBlog/media/20200520221446179/20200520221446184.mp4");
            setWeb(webWallpaperPathConf);
//            setMedia("http://secretopen.gitee.io/secret-performance-desktop/media/test.mp4");
        }
    }

    private void initWebView(){
        webView.setMinSize(screenSize.getWidth(),screenSize.getHeight());
        webView.setMaxSize(screenSize.getWidth(),screenSize.getHeight());
        //设置最大历史记录数0,不记录任何历史记录
        WebHistory webHistory =webView.getEngine().getHistory();
        webHistory.setMaxSize(0);
    }

    public void setWeb(String path){
        WebEngine webEngine=webView.getEngine();
        //alert调试
//        webEngine.setOnAlert((WebEvent<String> wEvent) -> {
//            System.out.println("JS alert() message: " + wEvent.getData());
//        });
        //先跳转到空页面，立即释放部分内存
        webEngine.load(null);
        webEngine.load(path);
    }

    @Override
    public void show(){
        if(!stage.isShowing()) {//没有显示
            WebWallpaperStage.getInstance().show();
            String webWallpaperPathConf=ConfigPropertiesUtil.get(ConfigEnum.WEBWALLPAPERPATH.getKey());
            if (!StringUtil.isEmpty(webWallpaperPathConf)) {
                setWeb(webWallpaperPathConf);
            }
        }
    }

    @Override
    public void close(){
        WebWallpaperStage.getInstance().close();
        //释放网页资源
        webView.getEngine().load(null);
    }

}

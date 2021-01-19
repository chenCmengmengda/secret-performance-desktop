package cn.chenc.performs.controller;

import cn.chenc.performs.enums.ConfigEnum;
import cn.chenc.performs.state.WebWallpaperStage;
import cn.chenc.performs.util.ConfigPropertiesUtil;
import cn.chenc.performs.util.StringUtil;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.awt.*;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2021/1/19 15:43
 *
 */
public class WebWallpaperController {
    private Dimension screenSize;
    private static WebWallpaperController instance;
    private static String webWallpaperPathConf;
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
    }

    public void setWeb(String path){
        WebEngine webEngine=webView.getEngine();
        webEngine.load(path);
    }

    public void show(){
        WebWallpaperStage.getInstance().show();
        if(!StringUtil.isEmpty(webWallpaperPathConf)){
            setWeb(webWallpaperPathConf);
        }
    }

    public void close(){
        WebWallpaperStage.getInstance().close();
        //释放网页资源
        webView.getEngine().load(null);
    }

}

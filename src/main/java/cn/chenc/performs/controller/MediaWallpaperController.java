package cn.chenc.performs.controller;

import cn.chenc.performs.enums.ConfigEnum;
import cn.chenc.performs.state.MediaWallpaperStage;
import cn.chenc.performs.util.ConfigPropertiesUtil;
import cn.chenc.performs.util.StringUtil;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.awt.*;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2021/1/13 11:21
 *
 */
public class MediaWallpaperController {
    private Dimension screenSize;
    private static MediaWallpaperController instance;
    private static String mediaWallpaperPathConf;
    @FXML
    private FlowPane rootFlowPane;
    @FXML
    private MediaView mediaView;

    public static MediaWallpaperController getInstance(){
        return instance;
    }

    @FXML
    private void initialize() {
        instance=this;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();//获取屏幕
        String mediaWallpaperPathConf=ConfigPropertiesUtil.get(ConfigEnum.MEDIAWALLPAPERPATH.getKey());
        initMediaView();
        if(!StringUtil.isEmpty(mediaWallpaperPathConf)) {
//            setMedia("file:///E:/upload/secretBlog/media/20200520221446179/20200520221446184.mp4");
            setMedia(mediaWallpaperPathConf);
//            setMedia("http://secretopen.gitee.io/secret-performance-desktop/media/test.mp4");
        }
    }

    private void initMediaView(){
        mediaView.setFitWidth(screenSize.getWidth());
        mediaView.setFitHeight(screenSize.getHeight());
        mediaView.setPreserveRatio(false);
    }

    public void setMedia(String path){
        mediaWallpaperPathConf=path;
        Media media=new Media(path);
        //创建媒体播放器
        MediaPlayer mPlayer=new MediaPlayer(media);
        mediaView.setMediaPlayer(mPlayer);
        //设置音量
        mPlayer.setVolume(0);
        //自动循环播放
        mPlayer.setAutoPlay(true);
        mPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public void show(){
        MediaWallpaperStage.getInstance().show();
        if(!StringUtil.isEmpty(mediaWallpaperPathConf)){
            setMedia(mediaWallpaperPathConf);
        }
    }

    public void close(){
        MediaWallpaperStage.getInstance().close();
        //释放视频资源
        mediaView.getMediaPlayer().dispose();
    }
}

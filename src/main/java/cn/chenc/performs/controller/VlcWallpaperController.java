package cn.chenc.performs.controller;

import cn.chenc.performs.enums.ConfigEnum;
import cn.chenc.performs.state.VlcWallpaperStage;
import cn.chenc.performs.util.ConfigPropertiesUtil;
import cn.chenc.performs.util.StringUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurfaceFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.awt.*;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2021/1/21 15:57
 *
 */
public class VlcWallpaperController extends BaseController{
    private Dimension screenSize;
    private static VlcWallpaperController instance;
    private Stage stage;
    private MediaPlayerFactory mediaPlayerFactory;
    private EmbeddedMediaPlayer embeddedMediaPlayer;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private ImageView imageView;

    public static VlcWallpaperController getInstance(){
        return instance;
    }

    public void initialize() {
        instance=this;
        Platform.runLater(()->{
            stage= (Stage) rootAnchorPane.getScene().getWindow();
        });
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();//获取屏幕
        imageView.setFitWidth(screenSize.getWidth());
        imageView.setFitHeight(screenSize.getHeight());

        //创建vlc媒体工厂
        mediaPlayerFactory = new MediaPlayerFactory("--gain","0");
        //获得新媒体
        embeddedMediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
        embeddedMediaPlayer.videoSurface().set(ImageViewVideoSurfaceFactory.videoSurfaceForImageView(imageView));
        this.embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void playing(MediaPlayer mediaPlayer) {
            }

            @Override
            public void paused(MediaPlayer mediaPlayer) {
            }

            @Override
            public void stopped(MediaPlayer mediaPlayer) {
            }

            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
            }
        });
        //读取配置
        String mediaWallpaperPathConf=ConfigPropertiesUtil.get(ConfigEnum.MEDIAWALLPAPERPATH.getKey());
        if(!StringUtil.isEmpty(mediaWallpaperPathConf)) {
            setMedia(mediaWallpaperPathConf);
        }

    }

    public void setMedia(String path){
        embeddedMediaPlayer.media().play(path);
        //循环播放
        embeddedMediaPlayer.controls().setRepeat(true);
    }

    @Override
    public void show(){
        if(!stage.isShowing()) {//没有显示
            VlcWallpaperStage.getInstance().show();
            String mediaWallpaperPathConf= ConfigPropertiesUtil.get(ConfigEnum.MEDIAWALLPAPERPATH.getKey());
            if (!StringUtil.isEmpty(mediaWallpaperPathConf)) {
                setMedia(mediaWallpaperPathConf);
            }
        }
    }

    @Override
    public void close(){
        VlcWallpaperStage.getInstance().close();
        //关闭当前播放的媒体
        embeddedMediaPlayer.controls().stop();
    }

}

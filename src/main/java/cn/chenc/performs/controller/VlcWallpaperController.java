package cn.chenc.performs.controller;

import cn.chenc.performs.enums.ConfigEnum;
import cn.chenc.performs.state.VlcWallpaperStage;
import cn.chenc.performs.util.ConfigPropertiesUtil;
import cn.chenc.performs.util.StringUtil;
import cn.chenc.performs.vlcj.JavaFxVideoSurface;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;
import javafx.util.Duration;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
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

    private WritableImage img;



    /**
     *
     */
    private static final double FPS = 20.0;


    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private ImageView imageView;
    @FXML
    private Canvas canvas;

    public static VlcWallpaperController getInstance(){
        return instance;
    }

    /**
     *
     */
    private Timeline timeline;

    /**
     *
     */
    private final EventHandler<ActionEvent> nextFrameHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent t) {
            renderFrame();
        }
    };

    public void initialize() {
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        double duration = 1000.0 / FPS;
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(duration), nextFrameHandler));

        instance=this;
        Platform.runLater(()->{
            stage= (Stage) rootAnchorPane.getScene().getWindow();
        });
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();//获取屏幕
//        imageView.setFitWidth(screenSize.getWidth());
//        imageView.setFitHeight(screenSize.getHeight());
        canvas.setWidth(screenSize.getWidth());
        canvas.setHeight(screenSize.getHeight());

        //创建vlc媒体工厂
        mediaPlayerFactory = new MediaPlayerFactory("--gain","0");
        //获得新媒体
        embeddedMediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
//        embeddedMediaPlayer.videoSurface().set(ImageViewVideoSurfaceFactory.videoSurfaceForImageView(imageView));
        embeddedMediaPlayer.videoSurface().set(JavaFxVideoSurface.videoSurfaceForImageView());

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
        //开始计时器
        startTimer();
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

    protected void startTimer() {
        if (timeline.getStatus() != Animation.Status.RUNNING) {
            timeline.play();
        }
    }

    @Override
    public void close(){
        VlcWallpaperStage.getInstance().close();
        //关闭当前播放的媒体
        embeddedMediaPlayer.controls().stop();
    }

    protected final void renderFrame() {

        GraphicsContext g = canvas.getGraphicsContext2D();

        double width = canvas.getWidth();
        double height = canvas.getHeight();

        //颜色填充画布
        g.fillRect(0, 0, width, height);
        if(img==null){
            img=JavaFxVideoSurface.getImg();
        }
        if (img != null) {
            double imageWidth = img.getWidth();
            double imageHeight = img.getHeight();

            double sx = width / imageWidth;
            double sy = height / imageHeight;

            double sf = Math.min(sx, sy);

            double scaledW = imageWidth * sf;
            double scaledH = imageHeight * sf;

            Affine ax = g.getTransform();

            g.translate(
                    (width - scaledW) / 2,
                    (height - scaledH) / 2
            );

            if (sf != 1.0) {
                g.scale(sf, sf);
            }
            g.drawImage(img, 0, 0);
            g.setTransform(ax);
        }

    }
}

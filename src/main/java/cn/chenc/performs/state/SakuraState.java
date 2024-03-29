package cn.chenc.performs.state;

import cn.chenc.performs.consts.CommonConst;
import cn.chenc.performs.consts.StageTitleConst;
import cn.chenc.performs.enums.ConfigEnum;
import cn.chenc.performs.factory.BaseStage;
import cn.chenc.performs.factory.SingletonFactory;
import cn.chenc.performs.util.ConfigPropertiesUtil;
import cn.chenc.performs.util.OsUtil;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2021/1/6 8:52
 *
 */
public class SakuraState extends BaseStage{
    private static SakuraState instance = null;
    private static Stage mainStage;
    private static double FPS= CommonConst.ANIMATIONFPS;
    private Timeline timeLine;
    GraphicsContext gc;
    int[] xx = new int[100];//x轴
    int[] yy = new int[100];//yz轴
    double[] size = new double[100];//大小
    int[] r=new int[100];//初始化角度
    int[] rv=new int[100];//初始化角度增量
    int[] vx=new int[100];//x轴移动速度
    int[] vy=new int[100];//y轴下落速度
    int[] imageIndex=new int[100];//每个樱花图片下标
    List<Image> imageList=new ArrayList<Image>();
    private Dimension screenSize;

    //动画事件
    private final EventHandler<ActionEvent> eventHandler = e->{
        // 刷新操作
        gc.clearRect(0,0,screenSize.getWidth(),screenSize.getHeight());
        sakura(gc);
    };

    static{
        //初始化fps
        Double fpsConfig= ConfigPropertiesUtil.getDouble(ConfigEnum.ANIMATIONFPS.getKey());
        if(fpsConfig!=null){
            FPS=fpsConfig;
        }
    }

    //调用单例工厂
    public static SakuraState getInstance() {
        if (instance == null) {
            instance = SingletonFactory.getWeakInstace(SakuraState.class);
        }
        return instance;
    }

    public void start() {
        Stage stage= BaseStage.getStage();
        mainStage = new Stage();
        mainStage.initOwner(stage);
        mainStage.setTitle(StageTitleConst.SAKURATITLE);
        //透明窗口
        mainStage.initStyle(StageStyle.TRANSPARENT);
        mainStage.setX(0);
        mainStage.setY(0);
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();//获取屏幕
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-fill: null;-fx-background-color: rgba(0,0,0,0)");
        javafx.scene.canvas.Canvas canvas = new Canvas(screenSize.getWidth(),screenSize.getHeight());
        root.getChildren().add(canvas);
        Scene scene  = new Scene(root);
        scene.setFill(null);
        mainStage.setScene(scene);
        //关闭自由调整大小
        mainStage.setResizable(false);
        stage.show();
        mainStage.show();
        //置于图标下层
        OsUtil.setWinIconAfter(StageTitleConst.SAKURATITLE);

        //初始化樱花坐标，大小
        for(int i = 0; i < 100; ++i) {
            this.xx[i] = (int)(Math.random() * screenSize.getWidth());
            this.yy[i] = (int)(Math.random() * screenSize.getHeight());
            this.size[i] = (int)(Math.random() * 10);
            this.vx[i]= 5-(int)(Math.random() * 10);
            this.vy[i] = 1+(int)(Math.random() * 10);//下落速度 [1,11)
            this.imageIndex[i]=(int)(Math.random()*3);
            this.r[i]=360-(int)(Math.random() * 720);//初始化角度 [-360 ,360)
            this.rv[i]=5-(int)(Math.random() * 10);//角度改变 [-5,5)
        }
        //初始化樱花图片
        for(int i=1;i<4;i++){
            try {
                URL sakuraUrl=getClass().getResource("/images/sakura"+i+".png");
                String  sakuraUrlStr = java.net.URLDecoder.decode(String.valueOf(sakuraUrl),"utf-8");
                Image image=new Image(sakuraUrlStr);
                imageList.add(image);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        // 获取画板对象
        gc = canvas.getGraphicsContext2D();
        // 创建时间轴
        timeLine = new Timeline();
        // 获取时间轴的帧列表
        ObservableList<KeyFrame> keyFrames = timeLine.getKeyFrames();
        // 添加关键帧
        keyFrames.add(new KeyFrame(Duration.millis(1000/FPS), eventHandler));
        // 设置时间轴播放次数为无限
        timeLine.setCycleCount(Timeline.INDEFINITE);
        // 播放时间轴
        timeLine.play();
    }

    public void sakura(GraphicsContext gc){
        // 保存现场
        gc.save();
        for(int i = 0; i < 100; ++i) {
            //x轴随机左右移动
            this.xx[i]=xx[i]+vx[i];
            //y轴下落
            this.yy[i]+=vy[i];
            if (this.yy[i] > screenSize.height) {//超过屏幕重新计算部分初始值
                this.xx[i]=(int)(Math.random() * screenSize.getWidth());
                this.yy[i] = 0;
                this.vy[i]=1+(int)(Math.random()*10);
                this.r[i]=360-(int)(Math.random() * 720);
                this.size[i]=(int)(Math.random()*10);
            }
            //旋转角度
            r[i]+=rv[i];
            //大小
            size[i]+=0.02;

            Rotate rotate=new Rotate(r[i],xx[i]+size[i]/2.0,yy[i]+size[i]/2.0);
            gc.setTransform(rotate.getMxx(),rotate.getMyx(),rotate.getMxy(),rotate.getMyy(),
                    rotate.getTx(),rotate.getTy());
            gc.drawImage(imageList.get(imageIndex[i]),(double)xx[i],(double)yy[i],size[i],size[i]);
        }
        // 恢复现场
        gc.restore();
    }

    @Override
    public void close() {
        if(mainStage!=null) {
            mainStage.close();
            //隐藏就停止动画，节省性能
            timeLine.stop();
        }
    }

    public static Stage getStage(){
        return mainStage;
    }

    @Override
    public void setFps(double fps){
        FPS=fps;
        timeLine.getKeyFrames().set(0,new KeyFrame(Duration.millis(1000/FPS), eventHandler));
        stopTimer();
        startTimer();
    }

    protected void startTimer() {
        if (timeLine.getStatus() != Animation.Status.RUNNING) {
            timeLine.play();
        }
    }

    protected void stopTimer() {
        if (timeLine.getStatus() != Animation.Status.STOPPED) {
            timeLine.stop();
        }
    }

    @Override
    public void show(){
        if(mainStage==null){
            getInstance().start();
            return ;
        }
        if(!mainStage.isShowing()){
            mainStage.show();
            timeLine.play();
            OsUtil.setWinIconAfter(StageTitleConst.SAKURATITLE);
        }
    }

}

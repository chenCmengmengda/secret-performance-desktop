package cn.chenc.performs.state;

import cn.chenc.performs.factory.BaseStage;
import cn.chenc.performs.factory.SingletonFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
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
    private Stage mainStage;
    private Timeline timeLine;
    int[] xx = new int[100];
    int[] yy = new int[100];
    int[] size = new int[100];
    int[] r=new int[100];
    List<Image> imageList=new ArrayList<Image>();
    private Dimension screenSize;


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
        //透明窗口
        mainStage.initStyle(StageStyle.TRANSPARENT);
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

        //初始化樱花坐标，大小
        for(int i = 0; i < 100; ++i) {
            this.xx[i] = (int)(Math.random() * screenSize.getWidth());
            this.yy[i] = (int)(Math.random() * screenSize.getHeight());
            this.size[i] = 10;
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
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // 创建时间轴
        timeLine = new Timeline();
        // 获取时间轴的帧列表
        ObservableList<KeyFrame> keyFrames = timeLine.getKeyFrames();
        // 添加关键帧
        keyFrames.add(new KeyFrame(Duration.seconds(0.1), e->{
            // 刷新操作
            gc.clearRect(0,0,screenSize.getWidth(),screenSize.getHeight());
            sakura(gc);
        }));
        // 设置时间轴播放次数为无限
        timeLine.setCycleCount(-1);
        // 播放时间轴
        timeLine.play();
    }

    public void sakura(GraphicsContext gc){
        // 保存现场
        gc.save();
        for(int i = 0; i < 100; ++i) {
            //x轴随机左右移动
            this.xx[i]=xx[i]+1-(int)(Math.random() * 2);
            //超过屏幕宽度，随机生成一个x位置
            if(xx[i]>screenSize.getWidth() || xx[i]<0){
                xx[i]=(int)(Math.random() * screenSize.getWidth());
            }
            //y轴下落
            this.yy[i]++;
            if (this.yy[i] > screenSize.height) {
                this.yy[i] = 0;
            }
            //旋转角度
            r[i]+=5;
            //获取图片下标
            int imgIndex=0;
            if (this.yy[i] > 0 && this.yy[i] < 150) {
                imgIndex=0;
            } else if (this.yy[i] > 150 && this.yy[i] < 500) {
                imgIndex=1;
            } else {
                imgIndex=2;
            }
            Rotate rotate=new Rotate(r[i],xx[i]+size[i]/2.0,yy[i]+size[i]/2.0);
            gc.setTransform(rotate.getMxx(),rotate.getMyx(),rotate.getMxy(),rotate.getMyy(),
                    rotate.getTx(),rotate.getTy());
            gc.drawImage(imageList.get(imgIndex),(double)xx[i],(double)yy[i],size[i],size[i]);
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

    @Override
    public void show(){
        if(mainStage!=null) {
            mainStage.show();
            timeLine.play();
        } else{
            getInstance().start();
        }
    }

}

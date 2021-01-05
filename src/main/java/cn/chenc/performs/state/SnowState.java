package cn.chenc.performs.state;

import cn.chenc.performs.factory.BaseStage;
import cn.chenc.performs.factory.SingletonFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2021/1/5 21:37
 *
 */
public class SnowState {
    private static SnowState instance = null;
    private Stage mainStage;
    private Timeline timeLine;
    int[] xx = new int[100];
    int[] yy = new int[100];
    int[] fonts = new int[100];
    private Dimension screenSize;

    //调用单例工厂
    public static SnowState getInstance() {
        if (instance == null) {
            instance = SingletonFactory.getWeakInstace(SnowState.class);
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

        //初始化雪花坐标
        for(int i = 0; i < 100; ++i) {
            this.xx[i] = (int)(Math.random() * screenSize.getWidth());
            this.yy[i] = (int)(Math.random() * screenSize.getHeight());
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
            snow(gc);
        }));
        // 设置时间轴播放次数为无限
        timeLine.setCycleCount(-1);
        // 播放时间轴
        timeLine.play();

    }

    public void snow(GraphicsContext gc){
        // 保存现场
        gc.save();
        gc.setFill(Color.WHITE);
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

            if (this.yy[i] > 0 && this.yy[i] < 150) {
                this.fonts[i] = 18;
            } else if (this.yy[i] > 150 && this.yy[i] < 500) {
                this.fonts[i] = 22;
            } else {
                this.fonts[i] = 32;
            }
            gc.setFont(Font.font("微软雅黑", FontWeight.findByWeight(1),fonts[i]));
            gc.fillText("*", this.xx[i], this.yy[i]);
        }
        // 恢复现场
        gc.restore();
    }

    public void close() {
        mainStage.close();
        //隐藏就停止动画，节省性能
        timeLine.stop();
    }

    public void show(){
        if(mainStage!=null) {
            mainStage.show();
            timeLine.play();
        } else{
            getInstance().start();
        }
    }

}

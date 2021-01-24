package cn.chenc.performs.state;

import cn.chenc.performs.consts.CommonConst;
import cn.chenc.performs.consts.StageTitleConst;
import cn.chenc.performs.enums.ConfigEnum;
import cn.chenc.performs.factory.BaseStage;
import cn.chenc.performs.factory.SingletonFactory;
import cn.chenc.performs.util.ConfigPropertiesUtil;
import cn.chenc.performs.util.StringUtil;
import cn.chenc.performs.util.Win32Util;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.util.Random;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2021/1/5 8:38
 *
 */
public class CodeRainState extends BaseStage{

    private static CodeRainState instance = null;
    private static Stage mainStage;
    private static double FPS= CommonConst.ANIMATIONFPS;
    Timeline timeLine;
    GraphicsContext gc;
    private Random random = new Random();
    //行高,列宽
    private final static int gap = 10;
    private Dimension screenSize;
    //存放雨点顶部的位置信息(marginTop)
    private int[] posArr;
    //行数
    private int lines;
    //列数
    private int columns;
    //文字颜色
    private static String textColor=CommonConst.CODERAINTEXTCOLOR;

    //动画事件
    private final EventHandler<ActionEvent> eventHandler = e->{
        // 刷新操作
        gc.clearRect(0,0,screenSize.getWidth(),screenSize.getHeight());

        codeRain(gc);
    };

    static{
        //获取配置
        //文字颜色
        String textColorConf= ConfigPropertiesUtil.get(ConfigEnum.CODERAINTEXTCOLOR.getKey());
        if(!StringUtil.isEmpty(textColorConf)){
            textColor=textColorConf;
        }
        //fps
        Double fpsConfig=ConfigPropertiesUtil.getDouble(ConfigEnum.ANIMATIONFPS.getKey());
        if(fpsConfig!=null){
            FPS=fpsConfig;
        }
    }

    //调用单例工厂
    public static CodeRainState getInstance() {
        if (instance == null) {
            instance = SingletonFactory.getWeakInstace(CodeRainState.class);
        }
        return instance;
    }

    public void start() {
        Stage stage=BaseStage.getStage();
        // 设置风格为 UTILITY
//        stage.initStyle(StageStyle.DECORATED);
        // 设置父级透明度为0
//        stage.setOpacity(0);
        mainStage = new Stage();
        mainStage.setTitle(StageTitleConst.CODERAINTITLE);
        mainStage.initOwner(stage);
        //透明窗口
        mainStage.initStyle(StageStyle.TRANSPARENT);
        mainStage.setX(0);
        mainStage.setY(0);
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();//获取屏幕
        //最大化
//        mainStage.setMaximized(true);
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-fill: null;-fx-background-color: rgba(0,0,0,0)");
        Canvas canvas = new Canvas(screenSize.getWidth(),screenSize.getHeight());
        root.getChildren().add(canvas);
        Scene scene  = new Scene(root);
        scene.setFill(null);
        mainStage.setScene(scene);
        //关闭自由调整大小
        mainStage.setResizable(false);
        stage.show();
        mainStage.show();
        Win32Util.setWinIconAfter(StageTitleConst.CODERAINTITLE);

        lines = screenSize.height / gap;
        columns = screenSize.width / gap;
        posArr = new int[columns + 1];
        random = new Random();
        for (int i = 0; i < posArr.length; i++) {
            posArr[i] = random.nextInt(lines);
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

    /**
     * @return 随机字符
     */
    private char getChr() {
        return (char) (random.nextInt(94) + 33);
    }



    /**
     * 代码雨
     */
    public void codeRain(GraphicsContext gc){
        // 保存现场
        gc.save();
        //当前列
        int currentColumn = 0;
        for (int x = 0; x < screenSize.width; x += 5*gap) {
            int endPos = posArr[currentColumn];
            Color color=Color.valueOf(textColor);
            gc.setFill(color);
            gc.setFont(Font.font(null, FontWeight.BOLD,9));
            gc.fillText(String.valueOf(getChr()), x, endPos * gap);
            int cr=0;
            int cg=0;
            int cb=0;
            for (int j = endPos - 15; j < endPos; j++) {
                //颜色渐变
                cr += 20;
                if (cr > (int)(color.getRed()*255)) {
                    cr = (int)(color.getRed()*255);
                }
                cg += 20;
                if (cg > (int)(color.getGreen()*255)) {
                    cg = (int)(color.getGreen()*255);
                }
                cb += 20;
                if (cb > (int)(color.getBlue()*255)) {
                    cb = (int)(color.getBlue()*255);
                }
                gc.setFill(Color.rgb(cr,cg, cb));
                gc.fillText(String.valueOf(getChr()), x, j * gap);
            }
            //每放完一帧，当前列上雨点的位置随机下移1~5行
//            posArr[currentColumn] += random.nextInt(5);
            posArr[currentColumn] += 1;
            //当雨点位置超过屏幕高度时，重新产生一个随机位置
            if (posArr[currentColumn] * gap > screenSize.height) {
                posArr[currentColumn] = random.nextInt(lines);
//                posArr[currentColumn] = 0;
            }
            currentColumn++;
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
        if(mainStage==null){
            getInstance().start();
            return ;
        }
        if(!mainStage.isShowing()){
            mainStage.show();
            timeLine.play();
            Win32Util.setWinIconAfter(StageTitleConst.CODERAINTITLE);
        }
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

    public static Stage getStage(){
        return mainStage;
    }

    public static String getTextColor() {
        return textColor;
    }

    public static void setTextColor(String textColor) {
        CodeRainState.textColor = textColor;
    }
}

package cn.chenc.performs.state;

import cn.chenc.performs.consts.CommonConst;
import cn.chenc.performs.enums.ConfigEnum;
import cn.chenc.performs.factory.BaseStage;
import cn.chenc.performs.factory.SingletonFactory;
import cn.chenc.performs.util.ColorUtil;
import cn.chenc.performs.util.ConfigPropertiesUtil;
import cn.chenc.performs.util.StringUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2021/1/4 21:39
 *
 */
public class ClockState {

    private static ClockState instance = null;
    private Stage mainStage;
    private Timeline timeLine;
    //时钟边框颜色
    private static String clockBorderColor ="#ffffff";
    //时钟背景颜色
    private static String clockBackground ="#000000";
    //时钟背景透明度
    private static double clockBackgroundOpacity =0.3;
    //时钟其他部分透明度
    private static double clockOtherOopcity =0.8;
    //秒针颜色
    private static String secondColor ="#ff0000";
    //分针颜色
    private static String minuteColor ="#0000ff";
    //时针颜色
    private static String hourColor ="#808080";
    //时间颜色
    private static String timeColor ="#F5F5F5";


    static {
        //获取配置
        //边框色
        String borderColorConf=ConfigPropertiesUtil.get(ConfigEnum.CLOCKBORDERCOLOR.getKey());
        if(!StringUtil.isEmpty(borderColorConf)){
            clockBorderColor=borderColorConf;
        }
        //背景色
        String backgroundColorConf=ConfigPropertiesUtil.get(ConfigEnum.CLOCKBACKGROUND.getKey());
        if(!StringUtil.isEmpty(backgroundColorConf)){
            clockBackground=backgroundColorConf;
        }
        //背景透明度
        String backgroundOpacityConf=ConfigPropertiesUtil.get(ConfigEnum.CLOCKBACKGROUNDOPACITY.getKey());
        if(!StringUtil.isEmpty(backgroundOpacityConf)){
            clockBackgroundOpacity=Double.parseDouble(backgroundOpacityConf);
        }
        //秒针颜色
        String secondColorConf=ConfigPropertiesUtil.get(ConfigEnum.SECONDCOLOR.getKey());
        if(!StringUtil.isEmpty(secondColorConf)){
            secondColor=secondColorConf;
        }
        //分针颜色
        String minuteColorConf=ConfigPropertiesUtil.get(ConfigEnum.MINUTECOLOR.getKey());
        if(!StringUtil.isEmpty(minuteColorConf)){
            minuteColor=minuteColorConf;
        }
        //时针颜色
        String hourColorConf=ConfigPropertiesUtil.get(ConfigEnum.HOURCOLOR.getKey());
        if(!StringUtil.isEmpty(hourColorConf)){
            hourColor=hourColorConf;
        }
        //时间颜色
        String timeColorConf=ConfigPropertiesUtil.get(ConfigEnum.TIMECOLOR.getKey());
        if(!StringUtil.isEmpty(timeColorConf)){
            timeColor=timeColorConf;
        }
    }

    public ClockState() {

    }

    //调用单例工厂
    public static ClockState getInstance() {
        if (instance == null) {
            instance = SingletonFactory.getWeakInstace(ClockState.class);
        }
        return instance;
    }

    /**
     * 重置
     */
    public void reset(){
        //边框色
        clockBorderColor = CommonConst.CLOCKBORDERCOLOR;
        //背景色
        clockBackground = CommonConst.CLOCKBACKGROUND;
        //背景透明度
        clockBackgroundOpacity = CommonConst.CLOCKBACKGROUNDOPACITY;
        //秒针颜色
        secondColor = CommonConst.SECONDCOLOR;
        //分针颜色
        minuteColor = CommonConst.MINUTECOLOR;
        //时针颜色
        hourColor = CommonConst.HOURCOLOR;
        //时间颜色
        timeColor = CommonConst.TIMECOLOR;
    }

    public void start() {
        Stage stage=BaseStage.getStage();
        // 设置风格为 UTILITY
//        stage.initStyle(StageStyle.UTILITY);
        // 设置父级透明度为0
//        stage.setOpacity(0);
        mainStage = new Stage();
        mainStage.initOwner(stage);
        mainStage.initStyle(StageStyle.TRANSPARENT);
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-fill: null;-fx-background-color: rgba(0,0,0,0)");
        Canvas canvas = new Canvas(600,450);
        root.getChildren().add(canvas);
        Scene scene  = new Scene(root,600,450);
        scene.setFill(null);
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        stage.show();
        mainStage.show();
        // 获取画板对象
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // 创建时间轴
        timeLine = new Timeline();
        // 获取时间轴的帧列表
        ObservableList<KeyFrame> keyFrames = timeLine.getKeyFrames();
        // 添加关键帧
        keyFrames.add(new KeyFrame(Duration.seconds(0.1), e->{
            // 刷新操作
            gc.clearRect(0,0,600,450);
            // 绘制表盘
            dials(gc);
            // 绘制刻度
            scale(gc);
            // 绘制指针
            point(gc);
        }));
        // 设置时间轴播放次数为无限
        timeLine.setCycleCount(-1);
        // 播放时间轴
        timeLine.play();
    }
    /**
     * 绘制表盘
     * @param gc
     */
    public void dials(GraphicsContext gc) {
        // 保存现场
        gc.save();
        // 变换坐标到外切圆矩形左上角坐标
        gc.translate(100,25);
        gc.setLineWidth(4);
        //时钟边框颜色
        gc.setStroke(ColorUtil.setOpacity(clockBorderColor, clockOtherOopcity));
        gc.strokeOval(0, 0, 400, 400);
        //时钟填充颜色
        gc.setFill(ColorUtil.setOpacity(clockBackground, clockBackgroundOpacity));
        gc.fillOval(0,0,400,400);
        gc.restore();
        gc.getFill();
    }
    /**
     * 绘制刻度
     * @param gc
     */

    public void scale(GraphicsContext gc) {
        // 保存现场
        gc.save();
        // 变换坐标系原点到表盘中心
        gc.translate(300,225);
        // 坐标逆时针旋转角度-90
        gc.rotate(-90);
        // 设置字体大小
        gc.setFont(Font.font(16));
        gc.setFill(ColorUtil.setOpacity(clockBorderColor, clockOtherOopcity));
        for(int i = 1 ; i < 61 ; i++) {
            // 每一个刻度角度为6度
            gc.rotate(6);
            if(i % 5 == 0) {
                gc.save();
                // 当前坐标切换到 (250,0) 即刻度左边界位置
                gc.translate(165,0);
                // 设置表格数字位置 相对于桌面应该是竖直
                gc.rotate(90-i/5*30);
                gc.fillText(i/5+"",0,0);
                gc.restore();
                gc.fillRect(175,0,22,10);
            }
            else{
                gc.fillRect(185,0,12,5);
            }
        }
        // 恢复现场
        gc.restore();
    }
    /**
     * 绘制指针
     * @param gc
     */
    public void point(GraphicsContext gc) {
        LocalDateTime time = LocalDateTime.now();
        int seconds =  time.getSecond();
        int minutes = time.getMinute();
        int hours = time.getHour();
        //秒针
        double[] pointX1 = new double[]{0,50,180,50};
        double[] pointY1 = new double[]{0,5,0,-5};
        //分针
        double[] pointX2 = new double[]{0,30,150,30};
        double[] pointY2 = new double[]{0,10,0,-10};
        //时针
        double[] pointX3 = new double[]{0,20,120,20};
        double[] pointY3 = new double[]{0,12,0,-12};
        gc.save();
        // 坐标移动至圆心
        gc.translate(300, 225);
        // 时间数字
        {
            String timeText1 = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            gc.setFill(Color.valueOf(timeColor));
            gc.setFont(Font.font(30));
            gc.fillText(timeText1,-70,-80);
            String timeText2 = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            gc.setFill(Color.valueOf(timeColor));
            gc.setFont(Font.font(60));
            gc.fillText(timeText2,-120,20);
        }
        // 秒钟
        {
            gc.save();
            gc.rotate(-90);
            gc.setFill(ColorUtil.setOpacity(secondColor, clockOtherOopcity));
            gc.rotate(seconds*6);
            // 四边形秒钟
            gc.fillPolygon(pointX1,pointY1, 4);
            gc.restore();
        }
        // 分钟
        {
            gc.save();
            gc.rotate(-90);
            gc.setFill(ColorUtil.setOpacity(minuteColor, clockOtherOopcity));
            gc.rotate(minutes*6+0.1*seconds);
            // 四边形分钟
            gc.fillPolygon(pointX2,pointY2, 4);
            gc.restore();
        }
        // 时钟
        {
            gc.save();
            gc.rotate(-90);
            gc.setFill(ColorUtil.setOpacity(hourColor, clockOtherOopcity));
            gc.rotate(hours*30+minutes*0.5+seconds*(0.5/60));
            // 四边形时钟
            gc.fillPolygon(pointX3,pointY3, 4);
            gc.restore();
        }
        // 恢复现场
        gc.restore();

    }

    public void close() {
        mainStage.close();
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

    public static String getClockBorderColor() {
        return clockBorderColor;
    }

    public static void setClockBorderColor(String clockBorderColor) {
        ClockState.clockBorderColor = clockBorderColor;
    }

    public static String getClockBackground() {
        return clockBackground;
    }

    public static void setClockBackground(String clockBackground) {
        ClockState.clockBackground = clockBackground;
    }

    public static double getClockBackgroundOpacity() {
        return clockBackgroundOpacity;
    }

    public static void setClockBackgroundOpacity(double clockBackgroundOpacity) {
        ClockState.clockBackgroundOpacity = clockBackgroundOpacity;
    }

    public static double getClockOtherOopcity() {
        return clockOtherOopcity;
    }

    public static void setClockOtherOopcity(double clockOtherOopcity) {
        ClockState.clockOtherOopcity = clockOtherOopcity;
    }

    public static String getSecondColor() {
        return secondColor;
    }

    public static void setSecondColor(String secondColor) {
        ClockState.secondColor = secondColor;
    }

    public static String getMinuteColor() {
        return minuteColor;
    }

    public static void setMinuteColor(String minuteColor) {
        ClockState.minuteColor = minuteColor;
    }

    public static String getHourColor() {
        return hourColor;
    }

    public static void setHourColor(String hourColor) {
        ClockState.hourColor = hourColor;
    }

    public static String getTimeColor() {
        return timeColor;
    }

    public static void setTimeColor(String timeColor) {
        ClockState.timeColor = timeColor;
    }
}


package cn.chenc.performs.state;

import cn.chenc.performs.consts.CommonConst;
import cn.chenc.performs.consts.StageTitleConst;
import cn.chenc.performs.enums.ConfigEnum;
import cn.chenc.performs.factory.BaseStage;
import cn.chenc.performs.factory.SingletonFactory;
import cn.chenc.performs.listener.DragListener;
import cn.chenc.performs.util.ColorUtil;
import cn.chenc.performs.util.ConfigPropertiesUtil;
import cn.chenc.performs.util.Win32Util;
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

import java.awt.*;
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
    private Canvas canvas;
    private DragListener dragListener;
    //时钟位置
    private static Double clockX;
    private static Double clockY;
    //时钟宽高
    private static int clockSize=450;
    //直径
    private static int clockd=400;
    //圆心
    private static int clockCenter=225;
    //圆边框粗细
    private static int borderLine=4;
    //粗刻度线与圆心距离
    private static int clockBRange=175;
    //粗刻度线宽高
    private static int clockBWidth=25;
    private static int clockBHeight=10;
    //细刻度线与圆心距离
    private static int clockLRange=185;
    //细刻度线宽高
    private static int clockLWidth=15;
    private static int clockLHeight=5;
    //刻度数字距离
    private static int clockTextRange=165;
    //刻度数字大小
    private static int clockTextSize=16;
    //秒针
    private static double[] clockPointX1 = new double[]{0,50,180,50};
    private static double[] clockPointY1 = new double[]{0,5,0,-5};
    //分针
    private static double[] clockPointX2 = new double[]{0,30,150,30};
    private static double[] clockPointY2 = new double[]{0,10,0,-10};
    //时针
    private static double[] clockPointX3 = new double[]{0,20,120,20};
    private static double[] clockPointY3 = new double[]{0,12,0,-12};
    //日期
    private static int dateSize=30;
    private static int dateX=-60;
    private static int dateY=80;
    //时间
    private static int timeSize=60;
    private static int timeX=-120;
    private static int timeY=20;

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
        //位置
        clockX= ConfigPropertiesUtil.getDouble(ConfigEnum.CLOCKX.getKey());
        clockY= ConfigPropertiesUtil.getDouble(ConfigEnum.CLOCKY.getKey());
        //大小
        String sizeConf=ConfigPropertiesUtil.get(ConfigEnum.CLOCKSIZE.getKey());
        if(!StringUtil.isEmpty(sizeConf)){
            setClockSize(Integer.parseInt(sizeConf));
        }
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
        //大小
        clockSize = CommonConst.CLOCKSIZE;
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
        //设置窗口位置
        if(clockX != null){
            mainStage.setX(clockX);
        }
        if(clockY != null){
            mainStage.setY(clockY);
        }
        mainStage.initOwner(stage);
        mainStage.initStyle(StageStyle.TRANSPARENT);
        mainStage.setTitle(StageTitleConst.CLOCKTITLE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();//获取屏幕
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-fill: null;-fx-background-color: rgba(0,0,0,0)");
        canvas = new Canvas(clockSize,clockSize);
        root.getChildren().add(canvas);
        Scene scene  = new Scene(root,clockSize,clockSize);
        scene.setFill(null);
        mainStage.setScene(scene);
//        mainStage.setResizable(false);
        stage.show();
        mainStage.show();
        //设置窗口位置
        Win32Util.setWinIconAfter(StageTitleConst.CLOCKTITLE);
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
        gc.translate(25,25);
        gc.setLineWidth(borderLine);
        //时钟边框颜色
        gc.setStroke(ColorUtil.setOpacity(clockBorderColor, clockOtherOopcity));
        gc.strokeOval(0, 0, clockd, clockd);
        //时钟填充颜色
        gc.setFill(ColorUtil.setOpacity(clockBackground, clockBackgroundOpacity));
        gc.fillOval(0,0,clockd, clockd);
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
        gc.translate(clockCenter,clockCenter);
        // 坐标逆时针旋转角度-90
        gc.rotate(-90);
        // 设置字体大小
        gc.setFont(Font.font(clockTextSize));
        gc.setFill(ColorUtil.setOpacity(clockBorderColor, clockOtherOopcity));
        for(int i = 1 ; i < 61 ; i++) {
            // 每一个刻度角度为6度
            gc.rotate(6);
            if(i % 5 == 0) {
                gc.save();
                // 当前坐标切换到 刻度左边界位置,相对圆心的坐标
                gc.translate(clockTextRange,0);
                // 设置表格数字位置 相对于桌面应该是竖直
                gc.rotate(90-i/5*30);
                gc.fillText(i/5+"",0,0);
                gc.restore();
                gc.fillRect(clockBRange,0,clockBWidth,clockBHeight);
            }
            else{
                gc.fillRect(clockLRange,0,clockLWidth,clockLHeight);
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
        double[] pointX1 = clockPointX1;
        double[] pointY1 = clockPointY1;
        //分针
        double[] pointX2 = clockPointX2;
        double[] pointY2 = clockPointY2;
        //时针
        double[] pointX3 = clockPointX3;
        double[] pointY3 = clockPointY3;
        gc.save();
        // 坐标移动至圆心
        gc.translate(clockCenter, clockCenter);
        // 时间数字
        {
            String timeText1 = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            gc.setFill(Color.valueOf(timeColor));
            gc.setFont(Font.font(dateSize));
            gc.fillText(timeText1,dateX,-dateY);
            String timeText2 = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            gc.setFill(Color.valueOf(timeColor));
            gc.setFont(Font.font(timeSize));
            gc.fillText(timeText2,timeX,timeY);
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
            Win32Util.setWinIconAfter(StageTitleConst.CLOCKTITLE);
        } else{
            getInstance().start();
        }
    }

    public void openDrag(){
        if(mainStage != null && mainStage.isShowing()) {
            if (dragListener == null) {
                dragListener = new DragListener(mainStage, ConfigEnum.CLOCKX, ConfigEnum.CLOCKY);
                dragListener.enableDrag(mainStage.getScene().getRoot());
                Win32Util.setWinIconTop(StageTitleConst.CLOCKTITLE);
            } else {
                dragListener.enableDrag(mainStage.getScene().getRoot());
                Win32Util.setWinIconTop(StageTitleConst.CLOCKTITLE);
            }
        }
    }

    public void closeDrag(){
        if(dragListener!=null && mainStage!=null && mainStage.isShowing()){
            dragListener.closeDrag(mainStage.getScene().getRoot());
            mainStage.close();
            mainStage.show();
            Win32Util.setWinIconAfter(StageTitleConst.CLOCKTITLE);
        }
    }

    public static void setClockSize(int size){
        if(size<150){
            return;
        }
        //调整窗口大小
        if(getInstance().mainStage!=null) {
            getInstance().mainStage.setWidth(size);
            getInstance().mainStage.setHeight(size);
            getInstance().canvas.setWidth(size);
            getInstance().canvas.setHeight(size);
        }
        //大小
        clockSize=size;
        //直径
        clockd=size-50;
        //半径
        int clockr=clockd/2;
        //圆心
        clockCenter=clockSize/2;
        //圆边框粗细
        if(size>=400){
            borderLine=4;
        } else if(size>=300){
            borderLine=3;
        } else {
            borderLine=2;
        }
        //粗刻度线宽高
        if(size>=400) {
            clockBWidth = 25;
            clockBHeight=10;
        } else if(size>=300) {
            clockBWidth = 15;
            clockBHeight=7;
        } else {
            clockBWidth = 8;
            clockBHeight=4;
        }
        //粗刻度线与圆心距离
        clockBRange=clockr-clockBWidth;
        //细刻度线宽高
        if(size>=400) {
            clockLWidth = 15;
            clockLHeight = 5;
        } else if(size>=300) {
            clockLWidth = 7;
            clockLHeight = 3;
        } else {
            clockLWidth = 4;
            clockLHeight = 2;
        }
        //细刻度线与圆心距离
        clockLRange=clockr-clockLWidth;
        //刻度数字大小与圆心距离
        if(size>=400){
            clockTextRange=clockBRange-10;
            clockTextSize=16;
        } else if(size>=300){
            clockTextRange=clockBRange-10;
            clockTextSize=12;
        } else {
            clockTextRange=clockBRange-5;
            clockTextSize=8;
        }
        //秒针
        if(size>=400) {
            clockPointX1 = new double[]{0, 50, clockr - clockBWidth + 5, 50};
            clockPointY1 = new double[]{0, 5, 0, -5};
        } else if(size >= 300) {
            clockPointX1 = new double[]{0, 30, clockr - clockBWidth + 5, 30};
            clockPointY1 = new double[]{0, 3, 0, -3};
        } else {
            clockPointX1 = new double[]{0, 15, clockr - clockBWidth + 5, 15};
            clockPointY1 = new double[]{0, 1, 0, -1};
        }
        //分针
        if(size>=400) {
            clockPointX2 = new double[]{0, 30, clockr-(int)(clockBWidth*2.5), 30};
            clockPointY2 = new double[]{0, 10, 0, -10};
        } else if(size>=300){
            clockPointX2 = new double[]{0, 20, clockr-(int)(clockBWidth*2.5), 20};
            clockPointY2 = new double[]{0, 5, 0, -5};
        } else{
            clockPointX2 = new double[]{0, 10, clockr-(int)(clockBWidth*2.5), 10};
            clockPointY2 = new double[]{0, 2, 0, -2};
        }
        //时针
        if(size>=400) {
            clockPointX3 = new double[]{0, 20, clockr - (int) (clockBWidth * 3), 20};
            clockPointY3 = new double[]{0, 12, 0, -12};
        } else if(size>=300) {
            clockPointX3 = new double[]{0, 10, clockr - (int) (clockBWidth * 3), 10};
            clockPointY3 = new double[]{0, 6, 0, -6};
        } else {
            clockPointX3 = new double[]{0, 5, clockr - (int) (clockBWidth * 3), 5};
            clockPointY3 = new double[]{0, 3, 0, -3};
        }
        //数字时间
        if(size>=400){
            dateSize=30;
            dateX=-(dateSize*2);
            dateY=clockr/2-20;
            timeSize=60;
            timeX=-(timeSize*2);
        } else if(size>=300) {
            dateSize=15;
            dateX=-(dateSize*2);
            dateY=clockr/2-10;
            timeSize=30;
            timeX=-(timeSize*2);
            timeY=10;
        } else {
            dateSize=9;
            dateX=-(dateSize*2);
            dateY=clockr/2-5;
            timeSize=15;
            timeX=-(timeSize*2);
            timeY=5;
        }

    }

    public static int getClockSize(){
        return clockSize;
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


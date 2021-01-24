package cn.chenc.performs.controller;

import cn.chenc.performs.consts.CommonConst;
import cn.chenc.performs.consts.LayoutConst;
import cn.chenc.performs.consts.StageTitleConst;
import cn.chenc.performs.enums.AnimationEnum;
import cn.chenc.performs.enums.ConfigEnum;
import cn.chenc.performs.enums.WallpaperEnum;
import cn.chenc.performs.factory.StageInterface;
import cn.chenc.performs.listener.DragListener;
import cn.chenc.performs.model.AppModel;
import cn.chenc.performs.state.*;
import cn.chenc.performs.util.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.Sensors;
import oshi.software.os.OperatingSystem;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.DecimalFormat;

public class AppController {

    private static AppController instance;
    public  static AppModel model = new AppModel();
    //鼠标拖动监听
    public static DragListener dragListener;

    //主窗口
    private static Stage stage;
    @FXML
    private FlowPane rootFlowPane;
    @FXML
    private HBox systemInfoHbox;
    @FXML
    private ImageView SystemLogo;
    @FXML
    private Label SystemInfo;
    @FXML
    private VBox cpuVBox;
    @FXML
    private Label cpuName;
    @FXML
    private Label CPU;
    @FXML
    private VBox ramVBox;
    @FXML
    private Label RAM;
    @FXML
    private AreaChart cpuChart;
    @FXML
    private NumberAxis cpuXAxis;
    @FXML
    private NumberAxis cpuYAxis;
    @FXML
    private AreaChart ramChart;
    @FXML
    private NumberAxis ramXAxis;
    @FXML
    private NumberAxis ramYAxis;

    XYChart.Series<Number, Number> series1 = new XYChart.Series<>();//cpu图表数据
    XYChart.Series<Number, Number> series2 = new XYChart.Series<>();//内存图表数据


    //oshi相关
    private static  SystemInfo systemInfo;
    private static CentralProcessor processor;
    private static Sensors sensors;

    private static String winLogoFile="win-logo.txt";

    private long[] prevTicks;//保存之前统计的cpu信息


    static{
        systemInfo = new SystemInfo();
        processor = systemInfo.getHardware().getProcessor();
        sensors = systemInfo.getHardware().getSensors();
    }

    public static AppController getInstance(){
        return instance;
    }

    public static Stage getStage() {
        return stage;
    }

    /**
     * @description: 开始获取系统信息
     * @param
     * @return void
     * @throws
     * @author secret
     * @date 2020/12/25 secret
     */
    @FXML
    private void initialize() {
        instance=this;
        //窗口初始化
        Platform.runLater(()->{
            //鼠标拖拽
            stage= (Stage) rootFlowPane.getScene().getWindow();
            dragListener=new DragListener(stage,ConfigEnum.SCENEX,ConfigEnum.SCENEY);
        });
        //初始化cpu,内存信息
        initCpuChart();
        initRamChart();
        startGetSystemInfo();
        //监听绑定的数据模型
        //logo
        SystemLogo.imageProperty().bindBidirectional(model.getImageObjProperty());
        //theme
        model.getThemeColorProperty().addListener((obs, oldText, newText) -> {
            Platform.runLater(()->setThemeColor(newText));
        });
        //drag
        model.getDragProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal){
                dragListener.enableDrag(rootFlowPane);//开启监听
                Win32Util.setWinIconTop(StageTitleConst.APPTITLE);
            } else {//显示状态才重启
                dragListener.closeDrag(rootFlowPane);//关闭监听
                if(stage.isShowing()) {
                    stage.close();
                    stage.show();
                    Win32Util.setWinIconAfter(StageTitleConst.APPTITLE);
                }
            }
        });
        //layout-type
        model.getLayoutProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(()->setLayout(newVal));
        });
        //从配置文件读取配置
        //动态壁纸
        String wallpaperType=ConfigPropertiesUtil.get(ConfigEnum.WALLPAPERTYPE.getKey());
        String mediaWallpaperConf=ConfigPropertiesUtil.get(ConfigEnum.MEDIAWALLPAPERPATH.getKey());
        String webWallpaperConf=ConfigPropertiesUtil.get(ConfigEnum.WEBWALLPAPERPATH.getKey());
        if((StringUtil.isEmpty(wallpaperType) || wallpaperType.equals(WallpaperEnum.MEDIA.getKey()))
                &&!StringUtil.isEmpty(mediaWallpaperConf)) {
//            MediaWallpaperStage.getInstance().show();
//            MediaWallpaperController.getInstance().setMedia(mediaWallpaperConf);
            VlcWallpaperStage.getInstance().show();
//            Platform.runLater(()->VlcWallpaperController.getInstance().setMedia(mediaWallpaperConf));
//            VlcWallpaperController.getInstance().setMedia(mediaWallpaperConf);
        } else if(!StringUtil.isEmpty(wallpaperType) && wallpaperType.equals(WallpaperEnum.WEB.getKey())
                &&!StringUtil.isEmpty(webWallpaperConf)) {
            WebWallpaperStage.getInstance().show();
            WebWallpaperController.getInstance().setWeb(webWallpaperConf);
        }
        //主面板显示
        initMainPaneDisplay();
        //logo-url
        String logoUrlConfig=null;
        if(!StringUtil.isEmpty(ConfigPropertiesUtil.get(ConfigEnum.LOGOURL.getKey()))) {
            logoUrlConfig=ConfigPropertiesUtil.get(ConfigEnum.LOGOURL.getKey());
        }
        //logo-opacity
        double logoOpacityConf;
        if(!StringUtil.isEmpty(ConfigPropertiesUtil.get(ConfigEnum.LOGOOPACITY.getKey()))) {
            logoOpacityConf = ConfigPropertiesUtil.getDouble(ConfigEnum.LOGOOPACITY.getKey());
        } else {
            logoOpacityConf=CommonConst.LOGOOPACITY;
        }
        //渲染logo
        String finalLogoUrlConfig = logoUrlConfig;
        Platform.runLater(() -> setSystemLogo(finalLogoUrlConfig,logoOpacityConf));
        //system-info
        printlnSystemInfo();
        //theme-color
        if(!StringUtil.isEmpty(ConfigPropertiesUtil.get(ConfigEnum.THEMECOLOR.getKey()))) {
            model.setThemeColor(ConfigPropertiesUtil.get(ConfigEnum.THEMECOLOR.getKey()));
        } else {
            Platform.runLater(()->setThemeColor(null));
        }
        //layout-type
        if(!StringUtil.isEmpty(ConfigPropertiesUtil.get(ConfigEnum.LAYOUTTYPE.getKey()))) {
            model.setLayout(ConfigPropertiesUtil.get(ConfigEnum.LAYOUTTYPE.getKey()));
        } else {
            Platform.runLater(()-> setLayout(null));
        }
        //clock
        Boolean clockopen=ConfigPropertiesUtil.getBoolean(ConfigEnum.CLOCKOPEN.getKey());
        if(clockopen!=null && clockopen) {
            ClockState clockState = ClockState.getInstance();
            clockState.show();
        }
        //animation
        Boolean animationopen=ConfigPropertiesUtil.getBoolean(ConfigEnum.ANIMATIONOPEN.getKey());
        if(animationopen!=null && animationopen) {
            String animationType=ConfigPropertiesUtil.get(ConfigEnum.ANIMATIONTYPE.getKey());
            if(StringUtil.isEmpty(animationType)){
                StageInterface stageInterface= CodeRainState.getInstance();
                stageInterface.show();
            } else if(animationType.equals(AnimationEnum.CODERAIN.getKey())){
                StageInterface stageInterface= CodeRainState.getInstance();
                stageInterface.show();
            } else if(animationType.equals(AnimationEnum.SNOW.getKey())){
                StageInterface stageInterface= SnowState.getInstance();
                stageInterface.show();
            } else if(animationType.equals(AnimationEnum.SAKURA.getKey())){
                StageInterface stageInterface= SakuraState.getInstance();
                stageInterface.show();
            }
        }
    }

    public void initMainPaneDisplay(){
        Boolean logoDisplayConfig=ConfigPropertiesUtil.getBoolean(ConfigEnum.LOGODISPLAY.getKey());
        if(logoDisplayConfig!=null && logoDisplayConfig.equals(false)){
            setSystemLogoVisible(false);
        }
        Boolean systemInfoDisplayConfig=ConfigPropertiesUtil.getBoolean(ConfigEnum.SYSTEMINFODISPLAY.getKey());
        if(systemInfoDisplayConfig!=null && systemInfoDisplayConfig.equals(false)){
            setSystemInfoVisible(false);
        }
        Boolean cpuInfoDisplayConfig=ConfigPropertiesUtil.getBoolean(ConfigEnum.CPUINFODISPLAY.getKey());
        if(cpuInfoDisplayConfig!=null && cpuInfoDisplayConfig.equals(false)){
            setCpuVBoxVisible(false);
        }
        Boolean ramInfoDisplayConfig=ConfigPropertiesUtil.getBoolean(ConfigEnum.RAMINFODISPLAY.getKey());
        if(ramInfoDisplayConfig!=null && ramInfoDisplayConfig.equals(false)){
            setRamVBoxVisible(false);
        }

    }

    /**
     * 重置默认
     */
    public void defaultMainPaneDisplay(){
        setSystemLogoVisible(true);
        setSystemInfoVisible(true);
        setCpuVBoxVisible(true);
        setRamVBoxVisible(true);
    }

    public void setSystemLogoVisible(boolean b){
        SystemLogo.setVisible(b);
        SystemLogo.setManaged(b);
    }

    public void setSystemInfoVisible(boolean b){
        SystemInfo.setVisible(b);
        SystemInfo.setManaged(b);
    }

    public void setCpuVBoxVisible(boolean b){
        cpuVBox.setVisible(b);
        cpuVBox.setManaged(b);
    }

    public void setRamVBoxVisible(boolean b){
        ramVBox.setVisible(b);
        ramVBox.setManaged(b);
    }

    public void setThemeColor(String color){
        if(StringUtil.isEmpty(color)) {//传入颜色为空，直接取默认配置
            String themeColor = CommonConst.THEMECOLOR;
            SystemInfo.setTextFill(Color.valueOf(themeColor));
            cpuName.setTextFill(Color.valueOf(themeColor));
            CPU.setTextFill(Color.valueOf(themeColor));
            RAM.setTextFill(Color.valueOf(themeColor));
            cpuChart.lookup(".chart-series-area-line").setStyle("-fx-stroke:"+themeColor+";");
            cpuChart.lookup(".chart-series-area-fill").setStyle("-fx-fill:"+themeColor+"33;");
            ramChart.lookup(".chart-series-area-line").setStyle("-fx-stroke:"+themeColor+";");
            ramChart.lookup(".chart-series-area-fill").setStyle("-fx-fill:"+themeColor+"33;");
        } else{ //监听颜色设置
            SystemInfo.setTextFill(Color.valueOf(color));
            cpuName.setTextFill(Color.valueOf(color));
            CPU.setTextFill(Color.valueOf(color));
            RAM.setTextFill(Color.valueOf(color));
            cpuChart.lookup(".chart-series-area-line").setStyle("-fx-stroke:"+color+";");
            cpuChart.lookup(".chart-series-area-fill").setStyle("-fx-fill:"+color+"33;");
            ramChart.lookup(".chart-series-area-line").setStyle("-fx-stroke:"+color+";");
            ramChart.lookup(".chart-series-area-fill").setStyle("-fx-fill:"+color+"33;");
        }
    }



    public void startGetSystemInfo() {
        Platform.runLater(()->{
            //更新JavaFX的主线程的代码放在此处
            try {
                printlnCpuInfo();
                printlnRamInfo();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void printlnCpuInfo() throws InterruptedException{
        if(prevTicks==null) {
            prevTicks = processor.getSystemCpuLoadTicks();
        }
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        prevTicks=ticks;//把当前cpu信息保存下来
//        System.out.println("----------------cpu信息----------------");

        long currentTimeMillis=System.currentTimeMillis();
        //当前cpu利用率:1.0-空闲/总量
        double cpuDouble = 1.0 - (idle * 1.0 / totalCpu);
        String totalCpuStr = new DecimalFormat("#.##%").format(cpuDouble);
//        if(totalCpu==0){
//            totalCpuStr="100%";
//        }
//        System.out.println("cpu核数:" + processor.getLogicalProcessorCount());
//        System.out.println("cpu系统使用率:" + new DecimalFormat("#.##%").format(cSys * 1.0 / totalCpu));
//        System.out.println("cpu用户使用率:" + new DecimalFormat("#.##%").format(user * 1.0 / totalCpu));
//        System.out.println("cpu当前等待率:" + new DecimalFormat("#.##%").format(iowait * 1.0 / totalCpu));
//        System.out.println("cpu当前使用率:" + totalCpuStr);
        //无法监测cpu温度
//        String cpuTemperature=String.format("%.1f°C",sensors.getCpuTemperature());
//        System.out.println("cpu当前温度:"+cpuTemperature);
        //获取cpu产商信息
        String cpuVendor=processor.getProcessorIdentifier().getVendor();
        //获取cpu名称
        String cpuName=processor.getProcessorIdentifier().getName();
        //获取cpu频率
//        String cpu=processor.getProcessorIdentifier().getVendorFreq();
//        System.out.println(processor.getProcessorIdentifier());
        //获取当前频率
        this.cpuName.setText(cpuName);
        CPU.setText("CPU("+totalCpuStr+")");

        //调用cpuchar图
        this.printlnCpuChart(currentTimeMillis,cpuDouble);
    }

    /**
     * 初始化cpu图表
     */
    private void initCpuChart(){
        //声明两条轴
        cpuXAxis = (NumberAxis) cpuChart.getXAxis();
        cpuYAxis = (NumberAxis) cpuChart.getYAxis();
        //设置透明
        cpuXAxis.setTickLabelFill(null);
        //取消自动坐标上限
        cpuXAxis.setAutoRanging(false);
        //设置x轴间隔单位
        cpuXAxis.setTickUnit(1000);
        //x轴动画
        cpuXAxis.setAnimated(true);
        //关闭x轴刻度和刻度线
        cpuXAxis.setTickLabelsVisible(false);
        cpuXAxis.setTickMarkVisible(false);

        cpuYAxis.setTickLabelFill(null);
        cpuYAxis.setLowerBound(0);
        cpuYAxis.setUpperBound(100);
        cpuYAxis.setTickUnit(20);
        cpuYAxis.setAutoRanging(false);
        cpuYAxis.setAnimated(true);
        cpuYAxis.setTickLabelsVisible(false);
        cpuYAxis.setTickMarkVisible(false);

    }

    private void printlnCpuChart(long currentTimeMillis,double cpu){

        //60秒前时间戳
        long beforeTimeInMilli =currentTimeMillis-(1000*60);

        //设置x轴上下限
        cpuXAxis.setLowerBound((double) beforeTimeInMilli);
        cpuXAxis.setUpperBound((double)currentTimeMillis);

        long cputoLong= (long) (cpu*100);
        //添加当前的数据
        series1.getData().add(new XYChart.Data<>(currentTimeMillis, cputoLong));
        //如果数据超过60个，则移除第一个
        if(series1.getData().size()>60){
            series1.getData().remove(0);
        }

        //显示图表
        if(this.cpuChart.getData().size()==0) {
            this.cpuChart.getData().add(series1);
        }
    }

    public void printlnRamInfo(){
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        long currentTimeMillis=System.currentTimeMillis();
        //总内存
        long totalByte = memory.getTotal();
        String totalByteStr=formatByte(totalByte);
        //剩余
        long acaliableByte = memory.getAvailable();
        String acaliableByteStr = formatByte(acaliableByte);
        //使用
        String useRamTotal = formatByte(totalByte-acaliableByte);
        //使用率
        double ramDouble=(totalByte-acaliableByte)*1.0/totalByte;
        String useRamDecimal = new DecimalFormat("#.##%").format((totalByte-acaliableByte)*1.0/totalByte);
//        System.out.println("总内存 = " + totalByteStr);
//        System.out.println("使用" + formatByte(totalByte-acaliableByte));
//        System.out.println("剩余内存 = " + useRamTotal);
//        System.out.println("使用率：" + useRamDecimal);

        RAM.setText("内存("+useRamDecimal+")");

        //内存图表
        printlnRamChart(currentTimeMillis,ramDouble);

    }

    private void initRamChart(){
        //声明两条轴
        ramXAxis = (NumberAxis) ramChart.getXAxis();
        ramYAxis = (NumberAxis) ramChart.getYAxis();

        ramXAxis.setTickLabelFill(null);
        ramXAxis.setAutoRanging(false);
        ramXAxis.setTickUnit(1000);
        ramXAxis.setAnimated(true);
        ramXAxis.setTickLabelsVisible(false);
        ramXAxis.setTickMarkVisible(false);
        ramYAxis.setTickLabelFill(null);
        ramYAxis.setLowerBound(0);
        ramYAxis.setUpperBound(100);
        ramYAxis.setTickUnit(20);
        ramYAxis.setAutoRanging(false);
        ramYAxis.setAnimated(true);
        ramYAxis.setTickLabelsVisible(false);
        ramYAxis.setTickMarkVisible(false);
    }

    //打印内存图表
    private void printlnRamChart(long currentTimeMillis,double ram){

        //60秒前时间戳
        long beforeTimeInMilli =currentTimeMillis-(1000*60);

        ramXAxis.setLowerBound((double) beforeTimeInMilli);
        ramXAxis.setUpperBound((double)currentTimeMillis);

        long ramtoLong= (long) (ram*100);
        series2.getData().add(new XYChart.Data<>(currentTimeMillis, ramtoLong));
        if(series2.getData().size()>60){
            series2.getData().remove(0);
        }
        //显示图表
        if(this.ramChart.getData().size()==0) {
            this.ramChart.getData().add(series2);
        }

    }

    private static String formatByte(long byteNumber){
        //换算单位
        double FORMAT = 1024.0;
        double kbNumber = byteNumber/FORMAT;
        if(kbNumber<FORMAT){
            return new DecimalFormat("#.##KB").format(kbNumber);
        }
        double mbNumber = kbNumber/FORMAT;
        if(mbNumber<FORMAT){
            return new DecimalFormat("#.##MB").format(mbNumber);
        }
        double gbNumber = mbNumber/FORMAT;
        if(gbNumber<FORMAT){
            return new DecimalFormat("#.##GB").format(gbNumber);
        }
        double tbNumber = gbNumber/FORMAT;
        return new DecimalFormat("#.##TB").format(tbNumber);
    }

    public void printlnSystemInfo(){
        //获取系统信息
        OperatingSystem operatingSystem=systemInfo.getOperatingSystem();
        String manufacturer=operatingSystem.getManufacturer();//供应商
        String family=System.getProperty("os.name");//操作系统名称
//        String version = operatingSystem.getVersionInfo().getVersion();//系统版本
//        version = String.format("%.0f",Double.parseDouble(version));
        String codeName=operatingSystem.getVersionInfo().getCodeName();//代码名称  eg:Home/Pro
//        String buildNumber=operatingSystem.getVersionInfo().getBuildNumber();//内部版本号

        String osArch = System.getProperty("os.arch"); //系统架构

        String info=family+" "+codeName+"\n"
                +"系统架构:"+osArch+"\n"
                +"分辨率:"+ HardwareUtil.getScreenToString();
        SystemInfo.setText(info);
    }

    public void setSystemLogo(String url,double opacity) {
        if(StringUtil.isEmpty(url)){//空则使用默认配置
            try {
                URL logoUrl=getClass().getResource(CommonConst.WINLOGOPATH);
                String urlStr = java.net.URLDecoder.decode(String.valueOf(logoUrl),"utf-8");
                Image image = new Image(urlStr, SystemLogo.getFitWidth(), SystemLogo.getFitHeight(), true, true);
                //改变图片透明度
                WritableImage wImage = ImageUtil.imgOpacity(image, opacity);
                model.setImageUrl(urlStr);
                model.setLogoOpacity(opacity);
                Platform.runLater(() -> model.setImageObj(wImage));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else { //使用用户配置
            Image image = new Image(url, SystemLogo.getFitWidth(), SystemLogo.getFitHeight(), true, true);
            //改变图片透明度
            WritableImage wImage = ImageUtil.imgOpacity(image, opacity);
            model.setImageUrl(url);
            model.setLogoOpacity(opacity);
            Platform.runLater(() -> model.setImageObj(wImage));
        }
    }

    //设置布局款式
    public void setLayout(String type){
        if(StringUtil.isEmpty(type) || type.equals(LayoutConst.DEFAULTTYPE)){//默认款式
            stage.setWidth(LayoutConst.MAINSCENEWIDTH1);
            stage.setHeight(LayoutConst.MAINSCENEHEIGHT1);
        } else {//款式2
            stage.setWidth(LayoutConst.MAINSCENEWIDTH2);
            stage.setHeight(LayoutConst.MAINSCENEHEIGHT2);
        }
    }

    public void show(){
        if(!stage.isShowing()) {
            stage.show();
            Win32Util.setWinIconAfter(StageTitleConst.APPTITLE);
        }
    }

    public void close(){
        stage.close();
    }

}

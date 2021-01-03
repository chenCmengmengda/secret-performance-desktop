package cn.chenc.performs.controller;

import cn.chenc.performs.consts.CommonConst;
import cn.chenc.performs.consts.LayoutConst;
import cn.chenc.performs.enums.ConfigEnum;
import cn.chenc.performs.listener.DragListener;
import cn.chenc.performs.model.AppModel;
import cn.chenc.performs.util.ConfigPropertiesUtil;
import cn.chenc.performs.util.ImageUtil;
import cn.chenc.performs.util.StringUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
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
import java.util.Calendar;

public class AppController {

    public  static AppModel model = new AppModel();
    //鼠标拖动监听
    public static DragListener dragListener;
    //主窗口
    private static Stage stage;
    @FXML
    private FlowPane rootFlowPane;
    @FXML
    private ImageView SystemLogo;
    @FXML
    private Label SystemInfo;
    @FXML
    private Label CPU;
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
        //窗口初始化
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //鼠标拖拽
                stage= (Stage) rootFlowPane.getScene().getWindow();
                dragListener=new DragListener(stage);
            }
        });
        //初始化cpu信息
        startGetSystemInfo(null);
        //监听绑定的数据模型
        //logo
        model.getImageProperty().addListener((obs, oldText, newText) -> {
//            setSystemLogo(newText);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    setSystemLogo(newText);
                }
            });
        });
        //logo透明度
        model.getLogoOpacityProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    setSystemLogoOpacity(Double.parseDouble(String.valueOf(newVal)));
                }
            });
        });
        //theme
        model.getThemeColorProperty().addListener((obs, oldText, newText) -> {
//            setThemeColor(newText);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    setThemeColor(newText);
                }
            });
        });
        //drag
        model.getDragProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal){
                dragListener.enableDrag(rootFlowPane);//开启监听
            } else{
                dragListener.closeDrag(rootFlowPane);//关闭监听
            }
        });
        //layout-type
        model.getLayoutProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    setLayout(newVal);
                }
            });
        });
        //从配置文件读取配置
        //logo-url
        if(!StringUtil.isEmpty(ConfigPropertiesUtil.get(ConfigEnum.LOGOURL.getKey()))) {
            model.setImageUrl(ConfigPropertiesUtil.get(ConfigEnum.LOGOURL.getKey()));
        } else {
            printlnSystemInfo();
        }
        //logo-opacity
        if(!StringUtil.isEmpty(ConfigPropertiesUtil.get(ConfigEnum.LOGOOPACITY.getKey()))) {
            model.setLogoOpacity(ConfigPropertiesUtil.getDouble(ConfigEnum.LOGOOPACITY.getKey()));
        } else {
            setSystemLogoOpacity(0);
        }
        //theme-color
        if(!StringUtil.isEmpty(ConfigPropertiesUtil.get(ConfigEnum.THEMECOLOR.getKey()))) {
            model.setThemeColor(ConfigPropertiesUtil.get(ConfigEnum.THEMECOLOR.getKey()));
        } else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    setThemeColor(null);
                }
            });
        }
        //layout-type
        if(!StringUtil.isEmpty(ConfigPropertiesUtil.get(ConfigEnum.LAYOUTTYPE.getKey()))) {
            model.setLayout(ConfigPropertiesUtil.get(ConfigEnum.LAYOUTTYPE.getKey()));
        } else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    setLayout(null);
                }
            });
        }


    }


    public void setThemeColor(String color){
        if(StringUtil.isEmpty(color)) {//传入颜色为空，直接取默认配置
            String themeColor = CommonConst.THEMECOLOR;
            SystemInfo.setTextFill(Color.valueOf(themeColor));
            CPU.setTextFill(Color.valueOf(themeColor));
            RAM.setTextFill(Color.valueOf(themeColor));
            cpuChart.lookup(".chart-series-area-line").setStyle("-fx-stroke:"+themeColor+";");
            cpuChart.lookup(".chart-series-area-fill").setStyle("-fx-fill:"+themeColor+"33;");
            ramChart.lookup(".chart-series-area-line").setStyle("-fx-stroke:"+themeColor+";");
            ramChart.lookup(".chart-series-area-fill").setStyle("-fx-fill:"+themeColor+"33;");
        } else{ //监听颜色设置
            SystemInfo.setTextFill(Color.valueOf(color));
            CPU.setTextFill(Color.valueOf(color));
            RAM.setTextFill(Color.valueOf(color));
            cpuChart.lookup(".chart-series-area-line").setStyle("-fx-stroke:"+color+";");
            cpuChart.lookup(".chart-series-area-fill").setStyle("-fx-fill:"+color+"33;");
            ramChart.lookup(".chart-series-area-line").setStyle("-fx-stroke:"+color+";");
            ramChart.lookup(".chart-series-area-fill").setStyle("-fx-fill:"+color+"33;");
        }
    }



    public void startGetSystemInfo(Parent root) {
//        try {
//            this.printlnCpuInfo(root);
//            this.printlnRamInfo(root);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //更新JavaFX的主线程的代码放在此处
                try {
                    printlnCpuInfo(root);
                    printlnRamInfo(root);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void printlnCpuInfo(Parent root) throws InterruptedException{
//        CentralProcessor processor = systemInfo.getHardware().getProcessor();
//        Sensors sensors = systemInfo.getHardware().getSensors();
        if(prevTicks==null) {
            prevTicks = processor.getSystemCpuLoadTicks();
        }
        // 睡眠1s
//        TimeUnit.SECONDS.sleep(1);
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
        System.out.println("----------------cpu信息----------------");

        long currentTimeMillis=System.currentTimeMillis();
        double cpuDouble = 1.0 - (idle * 1.0 / totalCpu);
        String totalCpuStr = new DecimalFormat("#.##%").format(cpuDouble);
        System.out.println("cpu当前使用率:" + totalCpuStr);
        //无法监测cpu温度
//        String cpuTemperature=String.format("%.1f°C",sensors.getCpuTemperature());
//        System.out.println("cpu当前温度:"+cpuTemperature);

        CPU.setText("CPU("+totalCpuStr+")");

        //调用cpuchar图
        this.printlnCpuChart(root,currentTimeMillis,cpuDouble);
    }

    private void printlnCpuChart(Parent root,long currentTimeMillis,double cpu){

        //60秒前时间戳
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, -1);// 1分钟之前的时间
        long beforeTimeInMilli = beforeTime.getTimeInMillis();


//        this.cpuChart=(AreaChart) root.lookup("#cpuChart");
//        cpuChart.setStyle("-fx-background:transparent;");
        //声明两条轴
        cpuXAxis = (NumberAxis) cpuChart.getXAxis();
        cpuYAxis = (NumberAxis) cpuChart.getYAxis();
        //设置透明
        cpuXAxis.setTickLabelFill(null);
        //取消自动坐标上限
        cpuXAxis.setAutoRanging(false);
        //设置x轴上下限
        cpuXAxis.setLowerBound((double) beforeTimeInMilli);
        cpuXAxis.setUpperBound((double)currentTimeMillis);
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

//        yAxis.setAnimated(true);


        long cputoLong= (long) (cpu*100);
        series1.getData().add(new XYChart.Data<>(currentTimeMillis, cputoLong));
        if(series1.getData().size()>60){
            series1.getData().remove(0);
        }

        //显示图表
        if(this.cpuChart.getData().size()==0) {
            this.cpuChart.getData().add(series1);
        }
    }

    public void printlnRamInfo(Parent root){
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
        System.out.println("总内存 = " + totalByteStr);
        System.out.println("使用" + formatByte(totalByte-acaliableByte));
        System.out.println("剩余内存 = " + useRamTotal);
        System.out.println("使用率：" + useRamDecimal);

        RAM.setText("内存("+useRamDecimal+")");

        //内存图表
        printlnRamChart(root,currentTimeMillis,ramDouble);

    }

    //打印内存图表
    private void printlnRamChart(Parent root,long currentTimeMillis,double ram){

        //60秒前时间戳
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, -1);// 1分钟之前的时间
        long beforeTimeInMilli = beforeTime.getTimeInMillis();


//        this.ramChart=(AreaChart) root.lookup("#ramChart");
//        cpuChart.setStyle("-fx-background:transparent;");
        //声明两条轴
        ramXAxis = (NumberAxis) ramChart.getXAxis();
        ramYAxis = (NumberAxis) ramChart.getYAxis();
        ramXAxis.setTickLabelFill(null);
        ramXAxis.setAutoRanging(false);
        ramXAxis.setLowerBound((double) beforeTimeInMilli);
        ramXAxis.setUpperBound((double)currentTimeMillis);
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

//        yAxis.setAnimated(true);


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
        //ascii文字logo
//        String result = "";
//        try {
//            InputStream inputStream=AppController.class.getClassLoader().getResourceAsStream(winLogoFile);
//
////            File file = new File(inputStream);
//            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
//            String s = null;
//            while((s = br.readLine())!=null) {//使用readLine方法，一次读一行
//                result = result + "\n" +s;
//            }
//            br.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        SystemLogo.setText(result);
        setSystemLogo(null);
        //获取系统信息
        OperatingSystem operatingSystem=systemInfo.getOperatingSystem();
        String manufacturer=operatingSystem.getManufacturer();//供应商
        String family=operatingSystem.getFamily();//操作系统名称
        String version = operatingSystem.getVersionInfo().getVersion();
        String codeName="";
        if(operatingSystem.getVersionInfo().getCodeName().equals("Home")){
            codeName="家庭版";
        } else if(operatingSystem.getVersionInfo().getCodeName().contains("Pro")){
            codeName="专业版";
        } else if(operatingSystem.getVersionInfo().getCodeName().equals("Enterprise")){
            codeName="企业版";
        }
        String buildNumber=operatingSystem.getVersionInfo().getBuildNumber();//内部版本号
        String osArch = System.getProperty("os.arch"); //系统架构
        String systemInfo=family+version+" "+codeName+"\n"
                +"版本号:"+buildNumber+"\n"
                +"系统架构:"+osArch;
        SystemInfo.setText(systemInfo);
    }

    public void setSystemLogo(String url) {
        if(StringUtil.isEmpty(url)){//空则使用默认配置
            try {
                URL logoUrl=getClass().getResource(CommonConst.WINLOGOPATH);
                String urlStr = java.net.URLDecoder.decode(String.valueOf(logoUrl),"utf-8");
                Image image = new Image(urlStr, SystemLogo.getFitWidth(), SystemLogo.getFitHeight(), true, true);
                //改变图片透明度
                WritableImage wImage = new ImageUtil().imgOpacity(image, CommonConst.LOGOOPACITY);
                SystemLogo.setImage(wImage);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else { //使用用户配置
            Image image = new Image(url, SystemLogo.getFitWidth(), SystemLogo.getFitHeight(), true, true);
            //改变图片透明度
            WritableImage wImage = new ImageUtil().imgOpacity(image, model.getLogoOpacity());
            SystemLogo.setImage(wImage);
        }
    }

    public void setSystemLogoOpacity(double opacity) {
        Image image = SystemLogo.getImage();
        if(opacity==0){//使用默认配置
            WritableImage wImage = new ImageUtil().imgOpacity(image, CommonConst.LOGOOPACITY);
            SystemLogo.setImage(wImage);
        } else {
            //改变图片透明度
            WritableImage wImage = new ImageUtil().imgOpacity(image, opacity);
            SystemLogo.setImage(wImage);
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

}

package cn.chenc.performs;

import cn.chenc.performs.consts.CommonConst;
import cn.chenc.performs.consts.LayoutConst;
import cn.chenc.performs.consts.StageTitleConst;
import cn.chenc.performs.controller.AppController;
import cn.chenc.performs.enums.ConfigEnum;
import cn.chenc.performs.enums.OsEnum;
import cn.chenc.performs.factory.BaseStage;
import cn.chenc.performs.util.ConfigPropertiesUtil;
import cn.chenc.performs.util.OsUtil;
import com.gitee.secretopen.plugin.SecretPluginFactory;
import com.gitee.secretopen.plugin.config.SecretPluginConfig;
import com.gitee.secretopen.plugin.model.PluginModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2020/12/25 13:57
 *
 */
public class App extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        //存储单例主stage
        BaseStage.setStage(primaryStage);
        // 设置风格为 UTILITY
        primaryStage.initStyle(StageStyle.UTILITY);
        // 设置父级透明度为0
        primaryStage.setOpacity(0);
        //让该窗口移出屏幕(经测试ubuntu20.04/GNOME上，该窗口会显示)
        primaryStage.setX(-1000);
        primaryStage.setY(-1000);

        Stage mainStage = new Stage();
        // 将 primaryStage 设置为归属对象，即父级窗口
        mainStage.initOwner(primaryStage);
        URL url=App.class.getResource("/App.fxml");
//        InputStream inputStream=App.class.getResourceAsStream("/");

        String urlStr=java.net.URLDecoder.decode(String.valueOf(url),"utf-8");
        url=new URL(urlStr);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent root = fxmlLoader.load();

        Scene scene=new Scene(root, LayoutConst.MAINSCENEWIDTH1, LayoutConst.MAINSCENEHEIGHT1);
        scene.setFill(null);//scene要透明
        VBox box=new VBox();
        box.setStyle("-fx-background:transparent;");//vbox透明
        mainStage.initStyle(StageStyle.TRANSPARENT);//取消窗口装饰
//        mainStage.initStyle(StageStyle.DECORATED);
        mainStage.setTitle(StageTitleConst.APPTITLE);
        //设置窗口横纵坐标，默认自适应屏幕宽度,且支持从设置获取
        Double sceneX = ConfigPropertiesUtil.getDouble(ConfigEnum.SCENEX.getKey());
        Double sceneY = ConfigPropertiesUtil.getDouble(ConfigEnum.SCENEY.getKey());
        if(sceneX==null) {
            mainStage.setX(LayoutConst.SCENEX1);
        } else {
            mainStage.setX(sceneX);
        }
        if(sceneY==null) {
            mainStage.setY(LayoutConst.SCENEY1);
        } else {
            mainStage.setY(sceneY);
        }
        mainStage.setScene(scene);
        AppController controller=fxmlLoader.getController();
//        controller.startGetSystemInfo();
        //创建定时任务
        EventHandler<ActionEvent> eventHandler= event -> {controller.startGetSystemInfo();};
        Timeline animation=new Timeline(new KeyFrame(Duration.millis(1000),eventHandler));
        //循环计数
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
/*
        AppTask appTask = new AppTask(controller,root);
        //获取当前系统cpu核心数
        HardwareAbstractionLayer hal = new SystemInfo().getHardware();
        CentralProcessor processor =hal.getProcessor();
        int cpucore=processor.getPhysicalProcessorCount();
        // 设置线程池，restart会尝试重用线程
        appTask.setExecutor(Executors.newFixedThreadPool(cpucore+1));
        // 延时0s开始
        appTask.setDelay(Duration.millis(0));
        // 间隔1s执行
        appTask.setPeriod(Duration.millis(1000));
        appTask.start();
*/
//        LineChart cpuChart = (LineChart) root.lookup("#cpuChart");
//        controller.printlnCpuChart(cpuChart);
        if(OsUtil.decideOs(OsEnum.WINDOWS.getKey())) {
            MySystemTray.getInstance(mainStage);
        }
        //显示窗口
        primaryStage.show();
        Boolean mainPaneConfig=ConfigPropertiesUtil.getBoolean(ConfigEnum.MAINPANEDISPLAY.getKey());
        if(mainPaneConfig==null || mainPaneConfig.equals(true)) {
            mainStage.show();
        }
        //设置窗口位置
        OsUtil.setWinIconAfter(StageTitleConst.APPTITLE);
        //加载插件
        SecretPluginConfig config = SecretPluginConfig.getInstance(true);
        SecretPluginFactory factory=new SecretPluginFactory(CommonConst.PLUGINPACKAGE,new File(CommonConst.PLUGINPATH));
        PluginModel pluginModel=SecretPluginFactory.getPluginModelList().get(0);
        Logger logger = LogManager.getLogger(App.class.getName());;
        logger.info("{插件启动成功}"+pluginModel.getClassName());

    }

    public static void main(String[] args) {
        launch(args);
    }


}

package cn.chenc.performs;

import cn.chenc.performs.controller.AppController;
import cn.chenc.performs.task.AppTask;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;
import java.net.URL;
import java.util.concurrent.Executors;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2020/12/25 13:57
 *
 */
public class App extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        // 设置风格为 UTILITY
        primaryStage.initStyle(StageStyle.UTILITY);
        // 设置父级透明度为0
        primaryStage.setOpacity(0);

        Stage mainStage = new Stage();
        // 将 primaryStage 设置为归属对象，即父级窗口
        mainStage.initOwner(primaryStage);
        URL url=App.class.getResource("/App.fxml");
//        InputStream inputStream=App.class.getResourceAsStream("/");

        String urlStr=java.net.URLDecoder.decode(String.valueOf(url),"utf-8");
        url=new URL(urlStr);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent root = fxmlLoader.load();
        int sceneWidth=350;
        int sceneHeight=650;
        Scene scene=new Scene(root, 350, 650);
        scene.setFill(null);//scene要透明
        VBox box=new VBox();
        box.setStyle("-fx-background:transparent;");//vbox透明
        mainStage.initStyle(StageStyle.TRANSPARENT);//取消窗口装饰
        mainStage.setTitle("performance-desktop");
        //获取屏幕宽度
        Dimension screensize   =   Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)screensize.getWidth();
        mainStage.setX(width-sceneWidth);
        mainStage.setY(0);
        mainStage.setScene(scene);

        AppController controller=fxmlLoader.getController();
//        controller.startGetSystemInfo(root);
        //创建定时任务
//        EventHandler<ActionEvent> eventHandler= event -> {controller.startGetSystemInfo(root);};
//        Timeline animation=new Timeline(new KeyFrame(Duration.millis(1000),eventHandler));

        AppTask appTask = new AppTask(controller,root);
        // 设置线程池，restart会尝试重用线程
        appTask.setExecutor(Executors.newFixedThreadPool(5));
        // 延时0s开始
        appTask.setDelay(Duration.millis(0));
        // 间隔1s执行
        appTask.setPeriod(Duration.millis(1000));
        appTask.start();
        //循环计数
//        animation.setCycleCount(Timeline.INDEFINITE);
//        animation.play();

//        LineChart cpuChart = (LineChart) root.lookup("#cpuChart");
//        controller.printlnCpuChart(cpuChart);
        MySystemTray.getInstance(mainStage);
        primaryStage.show();
        mainStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}

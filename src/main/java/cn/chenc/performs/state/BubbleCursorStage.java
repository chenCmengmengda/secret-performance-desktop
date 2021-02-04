package cn.chenc.performs.state;

import cn.chenc.performs.factory.BaseStage;
import cn.chenc.performs.factory.SingletonFactory;
import cn.chenc.performs.util.HardwareUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2021/2/2 20:42
 *
 */
public class BubbleCursorStage extends BaseStage {
    private static BubbleCursorStage instance = null;
    private Stage mainStage;

    public static BubbleCursorStage getInstance() {
        if (instance == null) {
            instance = SingletonFactory.getWeakInstace(BubbleCursorStage.class);
        }
        return instance;
    }

    private void start() throws Exception {
        Stage stage=new Stage();
        mainStage=stage;
        stage.initOwner(BaseStage.getStage());
        stage.initStyle(StageStyle.TRANSPARENT);
        URL url= BubbleCursorStage.class.getResource("/fxml/bubbleCursor.fxml");

        String urlStr=java.net.URLDecoder.decode(String.valueOf(url),"utf-8");
        url=new URL(urlStr);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent root = fxmlLoader.load();

        Scene scene=new Scene(root, HardwareUtil.getScreenWeight(),HardwareUtil.getScreenHeight());
//        Scene scene=new Scene(root, 500,500);
        scene.setFill(null);
        stage.setX(0);
        stage.setY(0);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void close() {
        if(mainStage!=null) {
            mainStage.close();
        }
    }

    @Override
    public void show(){
        if(mainStage==null){
            try {
                getInstance().start();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return ;
        }
        if(!mainStage.isShowing()){
            mainStage.show();
        }
    }
}

package cn.chenc.performs.controller;

import com.gitee.secretopen.plugin.SecretPluginFactory;
import com.gitee.secretopen.plugin.SecretPluginInterface;
import com.gitee.secretopen.plugin.model.PluginModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2021/2/3 16:14
 *
 */
public class PluginController extends BaseController {

    @FXML
    private ScrollPane root;
    @FXML
    private VBox vBox;

    private List<PluginModel> pluginModelList;

    private Map<String, Object> pluginMap;

    //插件开关事件
    private EventHandler<ActionEvent> toggleButtonAction = event -> {
        JFXToggleButton button=(JFXToggleButton) (event.getSource());
        //获取列表下标
        int index=vBox.getChildren().indexOf(button.getParent());

        if(button.isSelected()){
            //启用插件
            PluginModel pluginModel=pluginModelList.get(index);
            pluginModel.setEnabled(true);
            SecretPluginInterface secretPluginInterface=(SecretPluginInterface)pluginMap.get(pluginModel.getClassName());
            secretPluginInterface.initialize();
        } else {
            //关闭插件
            PluginModel pluginModel=pluginModelList.get(index);
            pluginModel.setEnabled(false);
            SecretPluginInterface secretPluginInterface=(SecretPluginInterface)pluginMap.get(pluginModel.getClassName());
            secretPluginInterface.stop();
        }
    };

    //打开插件设置界面
    private EventHandler<ActionEvent> setupButtonAction = event -> {
        JFXButton button=(JFXButton) (event.getSource());
        //获取列表下标
        int index=vBox.getChildren().indexOf(button.getParent());
        //从列表中找到该插件并打开设置界面
        PluginModel pluginModel=pluginModelList.get(index);
        pluginModel.setEnabled(false);
        SecretPluginInterface secretPluginInterface=(SecretPluginInterface)pluginMap.get(pluginModel.getClassName());
        secretPluginInterface.openSetup();
    };

    public void initialize() {
        vBox.prefWidthProperty().bind(root.widthProperty());
        vBox.prefHeightProperty().bind(root.heightProperty());
        root.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                for(int i=0;i<vBox.getChildren().size();i++){
                    Label label= (Label) vBox.lookup("#pluginName"+i);
                    label.setMinWidth(t1.doubleValue()-150);
                }
            }
        });
        //获取插件列表
        pluginModelList = SecretPluginFactory.getPluginModelList();
        //获取插件
        pluginMap = SecretPluginFactory.getPluginMap();
        Stream.iterate(0, i -> i + 1).limit(pluginModelList.size()).forEach(i -> {
            PluginModel item= pluginModelList.get(i);
            HBox hBox=new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            Label label=new Label(item.getName());
            label.setAlignment(Pos.CENTER_LEFT);
            label.setId("pluginName"+i);//设置id
            hBox.getChildren().add(label);
            if(item.isHasSetup()) {
                JFXButton jfoenixButton = new JFXButton("设置");
                hBox.getChildren().add(jfoenixButton);
                jfoenixButton.setOnAction(setupButtonAction);
            }
            JFXToggleButton jfxToggleButton = new JFXToggleButton();
            jfxToggleButton.setText("开启");
            if(item.isEnabled()){
                jfxToggleButton.setSelected(true);
            }
            jfxToggleButton.setOnAction(toggleButtonAction);
            hBox.getChildren().add(jfxToggleButton);
            vBox.getChildren().add(hBox);
        });


    }





}

package cn.chenc.performs.controller;

import cn.chenc.performs.enums.ConfigEnum;
import cn.chenc.performs.util.ColorUtil;
import cn.chenc.performs.util.ConfigPropertiesUtil;
import cn.chenc.performs.util.StringUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2020/12/29 15:02
 *
 */
public class SetupController {

    @FXML
    private GridPane rootGridPane;
    @FXML
    private Button checkLogo;
    @FXML
    private TextField logoPath;
    @FXML
    private Label themeLabel;
    @FXML
    private ColorPicker themeChoose;

    @FXML
    private void initialize(){
        setLogoPath();
        setThemeChoose();
    }

    public void setLogoPath() {
        if(!StringUtil.isEmpty(ConfigPropertiesUtil.get(ConfigEnum.LOGOURL.getKey()))) {
            logoPath.appendText(ConfigPropertiesUtil.get(ConfigEnum.LOGOURL.getKey()));
        } else {
            logoPath.setText("");
        }
    }

    public void setThemeChoose(){
        String color=ConfigPropertiesUtil.get(ConfigEnum.THEMECOLOR.getKey());
        if(!StringUtil.isEmpty(color)) {
            themeChoose.setValue(Color.valueOf(color));
        } else {
            themeChoose.setValue(Color.web("#00ffff"));
        }
    }

    @FXML
    public void checkLogoAction(ActionEvent event){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        Stage stage= (Stage) rootGridPane.getScene().getWindow();
        File file=fileChooser.showOpenDialog(rootGridPane.getScene().getWindow());
        if(file!=null) {//如果文件不为空，设置文本框内容
            String filePath = "file:\\"+file.getPath();
            logoPath.appendText(filePath);
            AppController.model.setImageUrl(filePath);
            ConfigPropertiesUtil.set(ConfigEnum.LOGOURL.getKey(),filePath);
        }
    }

    //清空文件选择
    @FXML
    public void resetLogoAction(){
        logoPath.setText("");
        AppController.model.setImageUrl(null);
    }

    @FXML
    public void themeChooseAction(){
        Color color=themeChoose.getValue();
        String colorStr= ColorUtil.parse(color.toString());
        AppController.model.setThemeColor(colorStr);
        ConfigPropertiesUtil.set(ConfigEnum.THEMECOLOR.getKey(),colorStr);
    }


    /**
     * @description: 重置
     * @param
     * @return void
     * @throws
     * @author secret
     * @date 2020/12/30 secret
     */
    @FXML
    public void resetConfigAction(){
        ConfigPropertiesUtil.clean();
        logoPath.setText("");
        themeChoose.setValue(Color.web("#00ffff"));
        AppController.model.setImageUrl(null);
        AppController.model.setThemeColor(null);
    }


}

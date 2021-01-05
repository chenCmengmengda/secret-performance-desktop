package cn.chenc.performs.controller;

import cn.chenc.performs.consts.CommonConst;
import cn.chenc.performs.consts.LayoutConst;
import cn.chenc.performs.enums.ConfigEnum;
import cn.chenc.performs.state.ClockState;
import cn.chenc.performs.state.CodeRainState;
import cn.chenc.performs.util.ColorUtil;
import cn.chenc.performs.util.ConfigPropertiesUtil;
import cn.chenc.performs.util.StringUtil;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;


/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2020/12/29 15:02
 *
 */
public class SetupController {

    //stage对象
    private static  Stage stage;
    @FXML
    private GridPane rootGridPane;
    @FXML
    private Button checkLogo;
    @FXML
    private TextField logoPath;
    @FXML
    private Label logoOpacityLabel;
    @FXML
    private Slider logoOpacitySlider;
    @FXML
    private Label themeLabel;
    @FXML
    private ColorPicker themeChoose;
    @FXML
    private CheckBox dragCheckBox;
    @FXML
    private ToggleGroup layoutToggleGroup;
    @FXML
    private RadioButton layoutRadio1;
    @FXML
    private RadioButton layoutRadio2;
    @FXML
    private CheckBox clockOpenCheckBox;
    @FXML
    private CheckBox animationOpenCheckBox;

    @FXML
    private void initialize(){
        //监听窗口关闭
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage = (Stage) rootGridPane.getScene().getWindow();
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        System.out.print("监听到窗口关闭");
                        AppController.model.setDrag(false);
                    }
                });
            }
        });

        setLogoPath();
        setThemeChoose();
        setLogoOpacitySlider();
        setdragCheckBox();
        setLayoutToggleGroup();
        setClockOpenCheckBox();
        setAnimationOpenCheckBox();
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
        } else {//默认配置
            themeChoose.setValue(Color.web(CommonConst.THEMECOLOR));
        }
    }

    public void setLogoOpacitySlider(){
        String logoOpacity=ConfigPropertiesUtil.get(ConfigEnum.LOGOOPACITY.getKey());
        if(!StringUtil.isEmpty(logoOpacity)){
            logoOpacitySlider.setValue(Double.parseDouble(logoOpacity));
        } else{//默认配置
            logoOpacitySlider.setValue(CommonConst.LOGOOPACITY);
        }
        //注册监听事件
        logoOpacitySlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                String oldValStr=String.format("%.2f",old_val);
                String newValStr=String.format("%.2f",new_val);
                //防止新旧值频繁更新以及有色图片完全透明和原图片本身透明部分混合导致最后不渲染
                if(!oldValStr.equals(newValStr) && !newValStr.equals("0.00")) {
                    AppController.model.setLogoOpacity(Double.parseDouble(newValStr));
                    ConfigPropertiesUtil.set(ConfigEnum.LOGOOPACITY.getKey(),newValStr);
                }
            }
        });
    }

    public void setdragCheckBox(){
        dragCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                AppController.model.setDrag(new_val);//设置是否开启拖拽监听
            }
        });
    }



    //初始化样式单选
    public void setLayoutToggleGroup(){
        layoutRadio1.setUserData(0);
        layoutRadio2.setUserData(1);
        if(StringUtil.isEmpty(ConfigPropertiesUtil.get(ConfigEnum.LAYOUTTYPE.getKey())) ||
                ConfigPropertiesUtil.get(ConfigEnum.LAYOUTTYPE.getKey()).equals("0")){
            layoutRadio1.setSelected(true);
        } else if(ConfigPropertiesUtil.get(ConfigEnum.LAYOUTTYPE.getKey()).equals("1")){
            layoutRadio2.setSelected(true);
        }

        //注册监听
        layoutToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (layoutToggleGroup.getSelectedToggle() != null) {
                    String newVal=layoutToggleGroup.getSelectedToggle().getUserData().toString();
                    AppController.model.setLayout(newVal);
                    ConfigPropertiesUtil.set(ConfigEnum.LAYOUTTYPE.getKey(),newVal);
                }
            }
        });

    }

    //是否开启时钟
    public void setClockOpenCheckBox(){
        Boolean b=ConfigPropertiesUtil.getBoolean(ConfigEnum.CLOCKOPEN.getKey());
        if(b!=null && b){
            clockOpenCheckBox.setSelected(true);
        }
        clockOpenCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                ClockState clockState=ClockState.getInstance();
                if(new_val) {//设置是否开启时钟监听
//                    clockState.start(BaseStage.getStage());
                    clockState.show();
                } else {
                    clockState.close();
                }
                ConfigPropertiesUtil.set(ConfigEnum.CLOCKOPEN.getKey(),new_val.toString());
            }
        });
    }

    //是否开启动画
    public void setAnimationOpenCheckBox(){
        Boolean b=ConfigPropertiesUtil.getBoolean(ConfigEnum.ANIMATIONOPEN.getKey());
        if(b!=null && b){
            animationOpenCheckBox.setSelected(true);
        }
        animationOpenCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                CodeRainState codeRainState = CodeRainState.getInstance();
                if(new_val) {//设置是否开启动画监听
                    codeRainState.show();
                } else {
                    codeRainState.close();
                }
                ConfigPropertiesUtil.set(ConfigEnum.ANIMATIONOPEN.getKey(),new_val.toString());
            }
        });
    }

    @FXML
    public void checkLogoAction(ActionEvent event){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file=fileChooser.showOpenDialog(stage);
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
        ConfigPropertiesUtil.set(ConfigEnum.LOGOURL.getKey(),"");
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
     * 监听默认位置按钮
     */
    @FXML
    private void resetDragAction(){
        ConfigPropertiesUtil.set(ConfigEnum.SCENEX.getKey(),"");
        ConfigPropertiesUtil.set(ConfigEnum.SCENEY.getKey(),"");
        AppController.dragListener.setXY(LayoutConst.SCENEX1,LayoutConst.SCENEY1);
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
        //清空配置文件
        ConfigPropertiesUtil.clean();
        //重置图片设置
        logoPath.setText("");
        AppController.model.setImageUrl(null);
        logoOpacitySlider.setValue(CommonConst.LOGOOPACITY);
        AppController.model.setLogoOpacity(CommonConst.LOGOOPACITY);
        //重置主题色
        themeChoose.setValue(Color.web(CommonConst.THEMECOLOR));
        AppController.model.setThemeColor(null);
        //重置主窗口位置
        AppController.dragListener.setXY(LayoutConst.SCENEX1,LayoutConst.SCENEY1);
        //重置布局
        layoutRadio1.setSelected(true);
        AppController.model.setLayout("0");
        //重置时钟和动画开启
        clockOpenCheckBox.setSelected(true);
        animationOpenCheckBox.setSelected(true);
    }


}

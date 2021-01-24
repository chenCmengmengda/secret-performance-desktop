package cn.chenc.performs.controller;

import cn.chenc.performs.consts.CommonConst;
import cn.chenc.performs.consts.LayoutConst;
import cn.chenc.performs.consts.StageTitleConst;
import cn.chenc.performs.enums.AnimationEnum;
import cn.chenc.performs.enums.ConfigEnum;
import cn.chenc.performs.enums.WallpaperEnum;
import cn.chenc.performs.factory.StageInterface;
import cn.chenc.performs.state.*;
import cn.chenc.performs.util.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;


/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2020/12/29 15:02
 *
 */
public class SetupController {

    //stage对象
    private static  Stage stage;
    //动画stage
    private static StageInterface animationStage;
    //wallpaperController
    private BaseController wallpaperController;
    @FXML
    private GridPane rootGridPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private CheckBox mainPane;
    @FXML
    private CheckBox logoDisplay;
    @FXML
    private CheckBox systemInfoDisplay;
    @FXML
    private CheckBox cpuInfoDisplay;
    @FXML
    private CheckBox ramInfoDisplay;
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
    private CheckBox clockDragCheckBox;
    @FXML
    private Slider clockSizeSlider;
    @FXML
    private ColorPicker clockBorderColor;
    @FXML
    private ColorPicker clockBackgroundColor;
    @FXML
    private Slider clockBackgroundOpacitySlider;
    @FXML
    private ColorPicker clockHourColor;
    @FXML
    private ColorPicker clockMinuteColor;
    @FXML
    private ColorPicker clockSecondColor;
    @FXML
    private ColorPicker clockTimeColor;
    @FXML
    private CheckBox animationOpenCheckBox;
    @FXML
    private ChoiceBox animationTypeChoiceBox;
    @FXML
    private ColorPicker codeRainTextColor;
    @FXML
    private Slider animationFps;
    @FXML
    private ChoiceBox wallpaperTypeChoiceBox;
    @FXML
    private VBox mediaBox;
    @FXML
    private TextField mediaPath;
    @FXML
    private Slider mediaFps;
    @FXML
    private HBox webHBox;
    @FXML
    private TextField webPath;

    @FXML
    private void initialize(){
        //绑定父级宽高
        scrollPane.prefWidthProperty().bind(rootGridPane.widthProperty());
        scrollPane.prefViewportHeightProperty().bind(rootGridPane.heightProperty());
        //监听窗口关闭
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage = (Stage) rootGridPane.getScene().getWindow();
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
//                        System.out.print("监听到窗口关闭");
                        //关闭拖拽
                        AppController.model.setDrag(false);
                        dragCheckBox.setSelected(false);
                        ClockState.getInstance().closeDrag();
                        clockDragCheckBox.setSelected(false);
                    }
                });
            }
        });

        initMainPaneDisplay();
        setLogoPath();
        setThemeChoose();
        setLogoOpacitySlider();
        setdragCheckBox();
        setLayoutToggleGroup();
        setClockOpenCheckBox();
        initClockConfig();
        setAnimationOpenCheckBox();
        initAnimationConfig();
        initWallpaperType();
        initMediaPath();
        initMediaWallpaperFps();
        initWebPath();
    }

    /**
     * 初始化主面板显示设置
     */
    public void initMainPaneDisplay(){
        //默认都为打开状态
        //主面板
        if(!StringUtil.isEmpty(ConfigPropertiesUtil.get(ConfigEnum.MAINPANEDISPLAY.getKey()))) {
            mainPane.setSelected(ConfigPropertiesUtil.getBoolean(ConfigEnum.MAINPANEDISPLAY.getKey()));
        } else{
            mainPane.setSelected(true);
        }
        //logo
        if(!StringUtil.isEmpty(ConfigPropertiesUtil.get(ConfigEnum.LOGODISPLAY.getKey()))) {
            logoDisplay.setSelected(ConfigPropertiesUtil.getBoolean(ConfigEnum.LOGODISPLAY.getKey()));
        } else{
            logoDisplay.setSelected(true);
        }
        //系统信息
        if(!StringUtil.isEmpty(ConfigPropertiesUtil.get(ConfigEnum.SYSTEMINFODISPLAY.getKey()))) {
            systemInfoDisplay.setSelected(ConfigPropertiesUtil.getBoolean(ConfigEnum.SYSTEMINFODISPLAY.getKey()));
        } else{
            systemInfoDisplay.setSelected(true);
        }
        //cpu信息
        if(!StringUtil.isEmpty(ConfigPropertiesUtil.get(ConfigEnum.CPUINFODISPLAY.getKey()))) {
            cpuInfoDisplay.setSelected(ConfigPropertiesUtil.getBoolean(ConfigEnum.CPUINFODISPLAY.getKey()));
        } else{
            cpuInfoDisplay.setSelected(true);
        }
        //ram信息
        if(!StringUtil.isEmpty(ConfigPropertiesUtil.get(ConfigEnum.RAMINFODISPLAY.getKey()))) {
            ramInfoDisplay.setSelected(ConfigPropertiesUtil.getBoolean(ConfigEnum.RAMINFODISPLAY.getKey()));
        } else{
            ramInfoDisplay.setSelected(true);
        }
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
                    AppController.getInstance().setSystemLogo(AppController.model.getImageUrl(),Double.parseDouble(newValStr));
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

    /**
     * 初始化时钟配置
     */
    public void initClockConfig(){
        initClockColor();
        //监听
        clockDragCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                ClockState clockState=ClockState.getInstance();
                if(new_val) {//设置是否开启时钟监听
                    ClockState clockState1=ClockState.getInstance();
                    clockState.openDrag();
                } else {
                    clockState.closeDrag();
                }
            }
        });
        clockSizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                int newValInt=new_val.intValue();
                ClockState.setClockSize(newValInt);
                ConfigPropertiesUtil.set(ConfigEnum.CLOCKSIZE.getKey(),String.valueOf(newValInt));
            }
        });
        clockBorderColor.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                String color=ColorUtil.parse(newValue.toString());
                ClockState.setClockBorderColor(color);
                ConfigPropertiesUtil.set(ConfigEnum.CLOCKBORDERCOLOR.getKey(), color);
            }
        });
        clockBackgroundColor.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                String color=ColorUtil.parse(newValue.toString());
                ClockState.setClockBackground(color);
                ConfigPropertiesUtil.set(ConfigEnum.CLOCKBACKGROUND.getKey(), color);
            }
        });
        clockBackgroundOpacitySlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                String oldValStr=String.format("%.2f",old_val);
                String newValStr=String.format("%.2f",new_val);
                //防止新旧值频繁更新
                if(!oldValStr.equals(newValStr) && !newValStr.equals("1.00")) {
                    ClockState.setClockBackgroundOpacity(Double.valueOf(newValStr));
                    ConfigPropertiesUtil.set(ConfigEnum.CLOCKBACKGROUNDOPACITY.getKey(),newValStr);
                }
            }
        });
        clockSecondColor.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                String color=ColorUtil.parse(newValue.toString());
                ClockState.setSecondColor(color);
                ConfigPropertiesUtil.set(ConfigEnum.SECONDCOLOR.getKey(), color);
            }
        });
        clockMinuteColor.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                String color=ColorUtil.parse(newValue.toString());
                ClockState.setMinuteColor(color);
                ConfigPropertiesUtil.set(ConfigEnum.MINUTECOLOR.getKey(), color);
            }
        });
        clockHourColor.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                String color=ColorUtil.parse(newValue.toString());
                ClockState.setHourColor(color);
                ConfigPropertiesUtil.set(ConfigEnum.HOURCOLOR.getKey(), color);
            }
        });
        clockTimeColor.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                String color=ColorUtil.parse(newValue.toString());
                ClockState.setTimeColor(color);
                ConfigPropertiesUtil.set(ConfigEnum.TIMECOLOR.getKey(), color);
            }
        });
    }

    private void initClockColor(){
        clockSizeSlider.setValue(ClockState.getClockSize());
        clockBorderColor.setValue(Color.valueOf(ClockState.getClockBorderColor()));
        clockBackgroundColor.setValue(Color.valueOf(ClockState.getClockBackground()));
        clockBackgroundOpacitySlider.setValue(ClockState.getClockBackgroundOpacity());
        clockSecondColor.setValue(Color.valueOf(ClockState.getSecondColor()));
        clockMinuteColor.setValue(Color.valueOf(ClockState.getMinuteColor()));
        clockHourColor.setValue(Color.valueOf(ClockState.getHourColor()));
        clockTimeColor.setValue(Color.valueOf(ClockState.getTimeColor()));
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
                animationStage.close();
                if(animationTypeChoiceBox.getValue().equals(AnimationEnum.CODERAIN.getValue())){
                    animationStage = CodeRainState.getInstance();
                } else if(animationTypeChoiceBox.getValue().equals(AnimationEnum.SNOW.getValue())){
                    animationStage = SnowState.getInstance();
                } else if(animationTypeChoiceBox.getValue().equals(AnimationEnum.SAKURA.getValue())){
                    animationStage = SakuraState.getInstance();
                }
                if(new_val) {//设置是否开启动画监听
                    animationStage.show();
                } else {
                    animationStage.close();
                }
                ConfigPropertiesUtil.set(ConfigEnum.ANIMATIONOPEN.getKey(),new_val.toString());
            }
        });
        codeRainTextColor.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                String color=ColorUtil.parse(newValue.toString());
                CodeRainState.setTextColor(color);
                ConfigPropertiesUtil.set(ConfigEnum.CODERAINTEXTCOLOR.getKey(), color);
            }
        });
    }

    //初始化动画设置
    public void initAnimationConfig(){
        //动画类型初始化
        initAnimationType();
        //初始化动画fps
        initAnimationFps();
        //代码雨初始化配置
        initCodeRain();
        //雪花飘落初始化
        //樱花初始化

        //监听select
        animationTypeChoiceBox.getSelectionModel().selectedIndexProperty()
            .addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue ov, Number value, Number new_value) {
                    animationStage.close();
                    if(new_value.intValue()==0){//代码雨
                        animationStage=CodeRainState.getInstance();
                    } else if(new_value.intValue()==1){//雪花
                        animationStage= SnowState.getInstance();
                    } else if(new_value.intValue()==2){//樱花
                        animationStage= SakuraState.getInstance();
                    }
                    animationStage.show();
                    ConfigPropertiesUtil.set(ConfigEnum.ANIMATIONTYPE.getKey(),AnimationEnum.getKey(new_value.intValue()));
                }
            });
    }


    private void initAnimationType(){
        if(animationTypeChoiceBox.getItems().size()==0) {
            animationTypeChoiceBox.getItems().addAll(AnimationEnum.getValues());
        }
        String animationType=ConfigPropertiesUtil.get(ConfigEnum.ANIMATIONTYPE.getKey());
        if(!StringUtil.isEmpty(animationType)) {
            animationTypeChoiceBox.setValue(AnimationEnum.getEnumByKey(animationType).getValue());
            if(animationType.equals(AnimationEnum.CODERAIN.getKey())){
                animationStage = CodeRainState.getInstance();
            } else if(animationType.equals(AnimationEnum.SNOW.getKey())){
                animationStage = SnowState.getInstance();
            } else if(animationType.equals(AnimationEnum.SAKURA.getKey())){
                animationStage = SakuraState.getInstance();
            }
        } else{//默认代码雨
            animationTypeChoiceBox.setValue(AnimationEnum.CODERAIN.getValue());
            animationStage = CodeRainState.getInstance();
        }
    }

    //初始化帧数
    private void initAnimationFps(){
        Double fpsConfig=ConfigPropertiesUtil.getDouble(ConfigEnum.ANIMATIONFPS.getKey());
        if(fpsConfig!=null){
            animationFps.setValue(fpsConfig);
        } else {
            animationFps.setValue(CommonConst.ANIMATIONFPS);
        }
    }

    private void initCodeRain(){
        String codeRainTextConf=ConfigPropertiesUtil.get(ConfigEnum.CODERAINTEXTCOLOR.getKey());
        if(!StringUtil.isEmpty(codeRainTextConf)) {
            codeRainTextColor.setValue(Color.valueOf(codeRainTextConf));
        } else {
            codeRainTextColor.setValue(Color.valueOf(CommonConst.CODERAINTEXTCOLOR));
        }
    }

    /**
     * 初始化壁纸类型
     */
    private void initWallpaperType(){
        if(wallpaperTypeChoiceBox.getItems().size()==0) {
            wallpaperTypeChoiceBox.getItems().addAll(WallpaperEnum.getValues());
        }
        String wallpaperType=ConfigPropertiesUtil.get(ConfigEnum.WALLPAPERTYPE.getKey());
        if(StringUtil.isEmpty(wallpaperType) || wallpaperType.equals(WallpaperEnum.MEDIA.getKey())){//media
            wallpaperTypeChoiceBox.setValue(WallpaperEnum.MEDIA.getValue());
            setWebHBoxVisible(false);
            wallpaperController=VlcWallpaperController.getInstance();
        } else if(wallpaperType.equals(WallpaperEnum.WEB.getKey())){//web
            wallpaperTypeChoiceBox.setValue(WallpaperEnum.WEB.getValue());
            setMediaHBoxVisible(false);
            wallpaperController=WebWallpaperController.getInstance();
        }
    }

    //初始化视频壁纸
    private void initMediaPath(){
        String mediaWallpapaerPathConf=ConfigPropertiesUtil.get(ConfigEnum.MEDIAWALLPAPERPATH.getKey());
        if(!StringUtil.isEmpty(mediaWallpapaerPathConf)){
            mediaPath.setText(mediaWallpapaerPathConf);
//            wallpaperController=MediaWallpaperController.getInstance();
//            wallpaperController=VlcWallpaperController.getInstance();
        }
    }

    private void initMediaWallpaperFps(){
        Double mediaWallpaperFpsConfig=ConfigPropertiesUtil.getDouble(ConfigEnum.MEDIAWALLPAPERFPS.getKey());
        if(mediaWallpaperFpsConfig!=null){
            mediaFps.setValue(mediaWallpaperFpsConfig);
        } else{
            mediaFps.setValue(CommonConst.MEDIAWALLPAPERFPS);
        }
    }


    private void initWebPath(){
        String webWallpapaerPathConf=ConfigPropertiesUtil.get(ConfigEnum.WEBWALLPAPERPATH.getKey());
        if(!StringUtil.isEmpty(webWallpapaerPathConf)){
            webPath.setText(webWallpapaerPathConf);
//            wallpaperController=WebWallpaperController.getInstance();
        }
    }

    @FXML
    public void openMainPane(ActionEvent event){
        boolean b=((CheckBox)event.getSource()).isSelected();
        if(b){
            AppController.getInstance().show();
        } else {
            AppController.getInstance().close();
        }
        ConfigPropertiesUtil.set(ConfigEnum.MAINPANEDISPLAY.getKey(), String.valueOf(b));
    }

    @FXML
    public void openLogoDisplay(ActionEvent event){
        boolean b=((CheckBox)event.getSource()).isSelected();
        AppController.getInstance().setSystemLogoVisible(b);
        ConfigPropertiesUtil.set(ConfigEnum.LOGODISPLAY.getKey(), String.valueOf(b));
    }

    @FXML
    public void openSystemInfoDisplay(ActionEvent event){
        boolean b=((CheckBox)event.getSource()).isSelected();
        AppController.getInstance().setSystemInfoVisible(b);
        ConfigPropertiesUtil.set(ConfigEnum.SYSTEMINFODISPLAY.getKey(), String.valueOf(b));
    }

    @FXML
    public void openCpuInfoDisplay(ActionEvent event){
        boolean b=((CheckBox)event.getSource()).isSelected();
        AppController.getInstance().setCpuVBoxVisible(b);
        ConfigPropertiesUtil.set(ConfigEnum.CPUINFODISPLAY.getKey(), String.valueOf(b));
    }

    @FXML
    public void openRamInfoDisplay(ActionEvent event){
        boolean b=((CheckBox)event.getSource()).isSelected();
        AppController.getInstance().setRamVBoxVisible(b);
        ConfigPropertiesUtil.set(ConfigEnum.RAMINFODISPLAY.getKey(), String.valueOf(b));
    }

    @FXML
    public void checkLogoAction(ActionEvent event){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file=fileChooser.showOpenDialog(stage);
        if(file!=null) {//如果文件不为空，设置文本框内容
            String filePath = "file:\\"+file.getPath();
            logoPath.appendText(filePath);
            AppController.getInstance().setSystemLogo(filePath,AppController.model.getLogoOpacity());
            ConfigPropertiesUtil.set(ConfigEnum.LOGOURL.getKey(),filePath);
        }
    }

    //清空文件选择
    @FXML
    public void resetLogoAction(){
        ConfigPropertiesUtil.set(ConfigEnum.LOGOURL.getKey(),"");
        logoPath.setText("");
        AppController.getInstance().setSystemLogo(null,AppController.model.getLogoOpacity());
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

    @FXML
    private void animationFpsAction(MouseEvent event){
        Slider slider=(Slider) (event.getSource());
        animationStage.setFps(slider.getValue());
        ConfigPropertiesUtil.set(ConfigEnum.ANIMATIONFPS.getKey(),String.valueOf(slider.getValue()));
    }

    @FXML
    private void wallpaperTypeAction(ActionEvent event){
        String wallpaperType=((ChoiceBox)event.getSource()).getValue().toString();
        if(wallpaperType.equals(WallpaperEnum.MEDIA.getValue())){//media类型
            setMediaHBoxVisible(true);
            setWebHBoxVisible(false);
            if(!StringUtil.isEmpty(mediaPath.getText())){
                //关闭当前壁纸
                if(wallpaperController!=null) {
                    wallpaperController.close();
                }
                //打开新壁纸
                openMediaWallpaperStage();
                //打开其他特效窗口
                openOtherStage();
            }
            ConfigPropertiesUtil.set(ConfigEnum.WALLPAPERTYPE.getKey(),WallpaperEnum.MEDIA.getKey());
        } else{//web类型
            setMediaHBoxVisible(false);
            setWebHBoxVisible(true);
            if(!StringUtil.isEmpty(webPath.getText())){
                //关闭当前壁纸
                if(wallpaperController!=null) {
                    wallpaperController.close();
                }
                //打开新壁纸
                openWebWallpaperStage();
                //打开其他特效窗口
                openOtherStage();
            }
            ConfigPropertiesUtil.set(ConfigEnum.WALLPAPERTYPE.getKey(),WallpaperEnum.WEB.getKey());
        }
    }

    /**
     * 打开视频壁纸
     */
    private void openMediaWallpaperStage(){
//        if(MediaWallpaperController.getInstance()==null){
        if(VlcWallpaperController.getInstance()==null){
//            MediaWallpaperStage.getInstance().show();
            VlcWallpaperStage.getInstance().show();
        } else {
//            MediaWallpaperController.getInstance().show();
            VlcWallpaperController.getInstance().show();
        }
//        wallpaperController=MediaWallpaperController.getInstance();
        wallpaperController=VlcWallpaperController.getInstance();
    }

    /**
     * 打开web壁纸
     */
    private void openWebWallpaperStage(){
        if(WebWallpaperController.getInstance()==null){
            WebWallpaperStage.getInstance().show();
        } else {
            WebWallpaperController.getInstance().show();
        }
        wallpaperController=WebWallpaperController.getInstance();
    }

    /**
     * 是否显示视频壁纸设置
     * @param b
     */
    public void setMediaHBoxVisible(boolean b){
        mediaBox.setVisible(b);
        mediaBox.setManaged(b);
    }

    /**
     * 是否显示web壁纸设置
     * @param b
     */
    public void setWebHBoxVisible(boolean b){
        webHBox.setVisible(b);
        webHBox.setManaged(b);
    }

    /**
     * 重启其他窗口
     */
    private void openOtherStage(){
        //其他面板为显示状态才需要重启
        //重启主面板
        if(AppController.getStage().isShowing()) {
            AppController.getInstance().close();
            AppController.getInstance().show();
            Win32Util.setWinIconAfter(StageTitleConst.APPTITLE);
        }
        //重启时钟
        if(ClockState.getStage()!=null && ClockState.getStage().isShowing()) {
            ClockState.getInstance().close();
            ClockState.getInstance().show();
            Win32Util.setWinIconAfter(StageTitleConst.CLOCKTITLE);
        }
        if (CodeRainState.getStage()!=null && CodeRainState.getStage().isShowing() && animationStage instanceof CodeRainState) {
            //代码雨重启
            CodeRainState.getInstance().close();
            CodeRainState.getInstance().show();
        } else if (SnowState.getStage() !=null && SnowState.getStage().isShowing() && animationStage instanceof SnowState) {
            //雪花重启
            SnowState.getInstance().close();
            SnowState.getInstance().show();
        } else if (SakuraState.getStage() != null && SakuraState.getStage().isShowing() && animationStage instanceof SakuraState) {
            //樱花重启
            SakuraState.getInstance().close();
            SakuraState.getInstance().show();
        }
    }

    @FXML
    private void checkMediaAction(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择视频壁纸");
        FileUtil.mkdir("wallpaper/media");
        fileChooser.setInitialDirectory(new File("wallpaper/media"));
        File file=fileChooser.showOpenDialog(stage);
        if(file!=null) {
//            String path = file.toURI().toString();
            String path=file.getPath();
            mediaPath.setText(path);
            openMediaWallpaperStage();
//            ((MediaWallpaperController)wallpaperController).setMedia(path);
            ((VlcWallpaperController)wallpaperController).setMedia(path);
            openOtherStage();
            ConfigPropertiesUtil.set(ConfigEnum.MEDIAWALLPAPERPATH.getKey(), path);
        }
    }

    @FXML
    private void resetMediaAction(){
        ConfigPropertiesUtil.set(ConfigEnum.MEDIAWALLPAPERPATH.getKey(),"");
        mediaPath.setText("");
        //关闭动态背景窗口
//        MediaWallpaperController.getInstance().close();
        VlcWallpaperController.getInstance().close();
    }

    /**
     * 视频fps
     * @param event
     */
    @FXML
    private void mediaFpsAction(MouseEvent event){
        Slider slider=(Slider)(event.getSource());
        VlcWallpaperController.getInstance().setFps(slider.getValue());
        ConfigPropertiesUtil.set(ConfigEnum.MEDIAWALLPAPERFPS.getKey(), String.valueOf(slider.getValue()));
    }


    @FXML
    private void checkWebAction(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择web壁纸");
        fileChooser.setInitialDirectory(new File("wallpaper/web"));
        File file=fileChooser.showOpenDialog(stage);
        if(file!=null) {
            String path=file.toURI().toString();
//            String path = "file:/"+file.getPath().replaceAll("\\\\","/");
            webPath.setText(path);
            openWebWallpaperStage();
            ((WebWallpaperController)wallpaperController).setWeb(path);
            openOtherStage();
            ConfigPropertiesUtil.set(ConfigEnum.WEBWALLPAPERPATH.getKey(), path);
        }
    }

    @FXML
    private void resetWebAction(ActionEvent event){
        ConfigPropertiesUtil.set(ConfigEnum.WEBWALLPAPERPATH.getKey(),"");
        webPath.setText("");
        //关闭动态背景窗口
        WebWallpaperController.getInstance().close();
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
        //重置面板显示
        initMainPaneDisplay();
        AppController.getInstance().show();
        AppController.getInstance().defaultMainPaneDisplay();
        //重置图片设置
        logoPath.setText("");
        AppController.getInstance().setSystemLogo(null,CommonConst.LOGOOPACITY);
        logoOpacitySlider.setValue(CommonConst.LOGOOPACITY);
        //重置主题色
        themeChoose.setValue(Color.web(CommonConst.THEMECOLOR));
        AppController.model.setThemeColor(null);
        //重置主窗口位置
        AppController.dragListener.setXY(LayoutConst.SCENEX1,LayoutConst.SCENEY1);
        //重置布局
        layoutRadio1.setSelected(true);
        AppController.model.setLayout("0");
        //重置时钟
        clockOpenCheckBox.setSelected(false);
        ClockState clockState=ClockState.getInstance();
        clockState.reset();
        initClockColor();
        //重置动画
        initAnimationType();
        initCodeRain();
        animationOpenCheckBox.setSelected(false);
        //重置壁纸
        if(wallpaperController!=null){
            wallpaperController.close();
        }
        initWallpaperType();
        mediaPath.setText("");
        webPath.setText("");
    }


}

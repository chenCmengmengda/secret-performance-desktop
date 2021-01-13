package cn.chenc.performs.controller;

import cn.chenc.performs.consts.CommonConst;
import cn.chenc.performs.consts.LayoutConst;
import cn.chenc.performs.consts.StageTitleConst;
import cn.chenc.performs.enums.AnimationEnum;
import cn.chenc.performs.enums.ConfigEnum;
import cn.chenc.performs.factory.StageInterface;
import cn.chenc.performs.state.*;
import cn.chenc.performs.util.ColorUtil;
import cn.chenc.performs.util.ConfigPropertiesUtil;
import cn.chenc.performs.util.StringUtil;
import cn.chenc.performs.util.Win32Util;
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
    //动画stage
    private static StageInterface animationStage;
    @FXML
    private GridPane rootGridPane;
    @FXML
    private ScrollPane scrollPane;
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
    private TextField mediaPath;

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
        initClockConfig();
        setAnimationOpenCheckBox();
        initAnimationConfig();
        setMediaPath();
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
                    animationStage = SnowState.getInstance();
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

    private void setMediaPath(){
        String mediaWallpapaerPathConf=ConfigPropertiesUtil.get(ConfigEnum.MEDIAWALLPAPERPATH.getKey());
        if(!StringUtil.isEmpty(mediaWallpapaerPathConf)){
            mediaPath.setText(mediaWallpapaerPathConf);
        }
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

    private void initCodeRain(){
        codeRainTextColor.setValue(Color.valueOf(CodeRainState.getTextColor()));
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


    @FXML
    private void checkMediaAction(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file=fileChooser.showOpenDialog(stage);
        if(file!=null) {
            String path = "file:/"+file.getPath().replaceAll("\\\\","/");
            mediaPath.setText(path);
            MediaWallpaperController mediaWallpaperController = MediaWallpaperController.getInstance();
            if (mediaWallpaperController == null) {
                MediaWallpaperStage.getInstance().show();
                mediaWallpaperController=MediaWallpaperController.getInstance();
            } else{
                MediaWallpaperStage.getInstance().show();
            }
            mediaWallpaperController.setMedia(path);
            //重启主面板
            AppController.getStage().close();
            AppController.getStage().show();
            Win32Util.setWinIconAfter(StageTitleConst.APPTITLE);
            //重启时钟
            ClockState.getInstance().close();
            ClockState.getInstance().show();
            Win32Util.setWinIconAfter(StageTitleConst.CLOCKTITLE);
            if (animationStage instanceof CodeRainState) {
                //代码雨重启
                CodeRainState.getInstance().close();
                CodeRainState.getInstance().show();
            } else if (animationStage instanceof SnowState) {
                //雪花重启
                SnowState.getInstance().close();
                SnowState.getInstance().show();
            } else if (animationStage instanceof SakuraState) {
                //樱花重启
                SakuraState.getInstance().close();
                SakuraState.getInstance().show();
            }
            ConfigPropertiesUtil.set(ConfigEnum.MEDIAWALLPAPERPATH.getKey(), path);
        }
    }

    @FXML
    private void resetMediaAction(){
        ConfigPropertiesUtil.set(ConfigEnum.MEDIAWALLPAPERPATH.getKey(),"");
        mediaPath.setText("");
        //关闭动态背景窗口
        MediaWallpaperStage.getInstance().close();
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
        //重置时钟
        clockOpenCheckBox.setSelected(true);
        ClockState clockState=ClockState.getInstance();
        clockState.reset();
        initClockColor();
        //重置动画
        initAnimationType();
        initCodeRain();
        animationOpenCheckBox.setSelected(true);
    }


}

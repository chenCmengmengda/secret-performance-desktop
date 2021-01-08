package cn.chenc.performs.enums;

/**
 * 　@description: 默认配置枚举
 * 　@author secret
 * 　@date 2020/12/30 9:38
 *
 */
public enum ConfigEnum {
    LOGOURL("logo-url"),
    LOGOOPACITY("logo-opacity"),
    THEMECOLOR("theme-color"),
    SCENEX("scene-x"),
    SCENEY("scene-y"),
    LAYOUTTYPE("layout-type"),
    CLOCKOPEN("clock-open"),
    CLOCKX("clock-x"),
    CLOCKY("clock-y"),
    CLOCKSIZE("clock-size"),
    CLOCKBORDERCOLOR("clock-border-color"),
    CLOCKBACKGROUND("clock-background"),
    CLOCKBACKGROUNDOPACITY("clock-background-opacity"),
    SECONDCOLOR("second-color"),
    MINUTECOLOR("minute-color"),
    HOURCOLOR("hour-color"),
    TIMECOLOR("time-color"),
    ANIMATIONOPEN("animation-open"),
    ANIMATIONTYPE("animation-type"),
    CODERAINTEXTCOLOR("coderain-text-color"),
    ;

    private String key;

    private String value;

    ConfigEnum(String key){
        this.key= key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

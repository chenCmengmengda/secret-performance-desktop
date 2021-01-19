package cn.chenc.performs.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2021/1/19 14:35
 *
 */
public enum WallpaperEnum {
    MEDIA("media","视频类型"),
    WEB("web","web类型"),
    ;
    private String key;

    private String value;

    private static List<String> keys=new ArrayList<>();

    private static List<String> values=new ArrayList<>();

    static {
        for (WallpaperEnum e : WallpaperEnum.values()) {
            keys.add(e.getKey());
            values.add(e.getValue());
        }
    }

    WallpaperEnum(String key,String value){
        this.key= key;
        this.value=value;
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

    public static WallpaperEnum getEnumByKey(String key){
        for (WallpaperEnum e : WallpaperEnum.values()) {
            if(e.getKey().equals(key)){
                return e;
            }
        }
        return null;
    }

    public static WallpaperEnum getEnumByValue(String value){
        for (WallpaperEnum e : WallpaperEnum.values()) {
            if(e.getValue().equals(value)){
                return e;
            }
        }
        return null;
    }

    public static List<String> getKeys(){
        return keys;
    }

    public static String getKey(int i){
        return keys.get(i);
    }

    public static List<String> getValues(){
        return values;
    }

    public static String getValue(int i){
        return values.get(i);
    }

}

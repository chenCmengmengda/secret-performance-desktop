package cn.chenc.performs.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 　@description: 动画枚举
 * 　@author secret
 * 　@date 2021/1/6 14:23
 *
 */
public enum AnimationEnum {

    CODERAIN("coderain","黑客帝国"),
    SNOW("snow","雪花飘落"),
    SAKURA("sakura","樱花飘落"),
    ;

    private String key;

    private String value;

    private static List<String> keys=new ArrayList<>();

    private static List<String> values=new ArrayList<>();

    static {
        for (AnimationEnum ae : AnimationEnum.values()) {
            keys.add(ae.getKey());
            values.add(ae.getValue());
        }
    }

    AnimationEnum(String key,String value){
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

    public static AnimationEnum getEnumByKey(String key){
        for (AnimationEnum ae : AnimationEnum.values()) {
            if(ae.getKey().equals(key)){
                return ae;
            }
        }
        return null;
    }

    public static AnimationEnum getEnumByValue(String value){
        for (AnimationEnum ae : AnimationEnum.values()) {
            if(ae.getValue().equals(value)){
                return ae;
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

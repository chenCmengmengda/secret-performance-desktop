package cn.chenc.performs.enums;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2021/1/27 14:39
 *
 */
public enum OsEnum {
    WINDOWS("Windows"),
    MAC("Mac"),
    LINUX("Linux"),
    ;
    private String key;

    OsEnum(String key){
        this.key=key;
    }

    public String getKey() {
        return key;
    }
}

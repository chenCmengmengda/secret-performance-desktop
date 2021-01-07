package cn.chenc.performs.util;

import javafx.scene.paint.Color;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2020/12/30 12:53
 *
 */
public class ColorUtil {

    /**
     * 16进制色号转化为 #开头色号，只取6位
     * @param color
     * @return
     */
    public static String parse(String color){
        return "#"+color.substring(2,8);
    }


    public static Color setOpacity(String s,double opacity){
        return Color.web(s,opacity);
    }

}

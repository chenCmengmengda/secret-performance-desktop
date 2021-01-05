package cn.chenc.performs.util;

import javafx.scene.paint.Color;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2020/12/30 12:53
 *
 */
public class ColorUtil {

    public static String parse(String color){
        return "#"+color.substring(2,8);
    }

    public static String setOpacity(Color color,int opacity){
        return parse(color.toString())+opacity;
    }

    public static Color setOpacity(String s,double opacity){
        return Color.web(s,opacity);
    }

}

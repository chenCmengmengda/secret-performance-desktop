package cn.chenc.performs.util;

import java.awt.*;

/**
 * 　@description: 获取本机硬件信息工具
 * 　@author secret
 * 　@date 2021/1/14 19:56
 *
 */
public class HardwareUtil {


    /**
     * 获取屏幕宽度
     * @return
     */
    public static double getScreenWeight(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();//获取屏幕
        return screenSize.getWidth();
    }

    /**
     * 获取屏幕高度
     * @return
     */
    public static double getScreenHeight(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();//获取屏幕
        return screenSize.getHeight();
    }

    /**
     * 屏幕分辨率字符串 w x h
     * @return
     */
    public static String getScreenToString(){
        return String.format("%.0f",getScreenWeight())+" x "+String.format("%.0f",getScreenHeight());
    }

}

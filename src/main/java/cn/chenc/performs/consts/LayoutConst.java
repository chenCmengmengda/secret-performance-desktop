package cn.chenc.performs.consts;

import java.awt.*;

/**
 * 　@description: 布局常量
 * 　@author secret
 * 　@date 2021/1/2 15:13
 *
 */
public class LayoutConst {

    //默认布局类型
    public static String DEFAULTTYPE="0";

    /**
     * 主窗口默认宽度-款式1
     */
    public static double MAINSCENEWIDTH1=350;

    /**
     * 主窗口默认高度-款式1
     */
    public static double MAINSCENEHEIGHT1=650;

    /**
     * 主窗口默认横坐标位置，窗口自适应-款式1
     */
    public static final double SCENEX1= Toolkit.getDefaultToolkit().getScreenSize().getWidth()-MAINSCENEWIDTH1;

    /**
     * 主窗口默认纵坐标位置-款式1
     */
    public static final double SCENEY1=0;

    /**
     * 主窗口默认宽度-款式2
     */
    public static double MAINSCENEWIDTH2=1100;

    /**
     * 主窗口默认高度-款式2
     */
    public static double MAINSCENEHEIGHT2=350;

    /**
     * 主窗口默认横坐标位置，窗口自适应-款式2
     */
    public static final double SCENEX2= (Toolkit.getDefaultToolkit().getScreenSize().getWidth()-MAINSCENEWIDTH2)/2.0;

    /**
     * 主窗口默认纵坐标位置-款式2
     */
    public static final double SCENEY2=Toolkit.getDefaultToolkit().getScreenSize().getHeight()-MAINSCENEHEIGHT2-100;



}

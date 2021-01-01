package cn.chenc.performs.consts;

import java.awt.*;

/**
 * 　@description: 通用常量类
 * 　@author secret
 * 　@date 2020/12/31 19:28
 *
 */
public class CommonConst {

    /**
     * 主窗口默认宽度
     */
    public static double MAINSCENEWIDTH=350;

    /**
     * 主窗口默认高度
     */
    public static double MAINSCENEHEIGHT=650;

    /**
     * 开机启动目录
     */
    public static final String WINSTARTUPPATH=System.getProperty("user.home")+
            "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\";

    /**
     * 打包exe程序名
     */
    public static final String EXENAME="secret-performance-desktop";

    /**
     * 创建WINDOWS系统开机自启脚本相对路径
     */
    public static final String WINSTARTUPSCRIPT="/script/myshortcut.vbs";

    /**
     * logo默认配置
     */
    public static final String WINLOGOPATH="/images/win-logo.png";

    /**
     * logo默认透明度
     */
    public static final double LOGOOPACITY=0.3;

    /**
     * 主题色默认配置
     */
    public static final String THEMECOLOR="#00ffff";

    /**
     * 主窗口默认横坐标位置，窗口自适应
     */
    public static final double SCENEX=Toolkit.getDefaultToolkit().getScreenSize().getWidth()-MAINSCENEWIDTH;

    /**
     * 主窗口默认纵坐标位置
     */
    public static final double SCENEY=0;


}

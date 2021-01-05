package cn.chenc.performs.consts;

/**
 * 　@description: 通用常量类
 * 　@author secret
 * 　@date 2020/12/31 19:28
 *
 */
public class CommonConst {

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
     * 时钟边框颜色
     */
    public static final String CLOCKBORDERCOLOR="#FFFFFF";

    //时钟背景颜色
    public static final String CLOCKBACKGROUND="#000000";

    /**
     * 时钟背景透明度
     */
    public static final double CLOCKBACKGROUNDOPACITY=0.7;

    //时钟其他部分透明度
    public static final double CLOCKOTHEROPACITY=0.7;



}

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
     * 内置插件所在包
     */
    public static String PLUGINPACKAGE="cn.chenc.performs.plugins";

    /**
     * 外部插件所在路径
     */
    public static String PLUGINPATH ="plugins";

    /**
     * logo默认透明度
     */
    public static final double LOGOOPACITY=0.3;

    /**
     * 主题色默认配置
     */
    public static final String THEMECOLOR="#00ffff";


    /**
     * 时钟大小
     */
    public static final int CLOCKSIZE=450;
    /**
     * 时钟边框颜色
     */
    public static final String CLOCKBORDERCOLOR="#FFFFFF";

    //时钟背景颜色
    public static final String CLOCKBACKGROUND="#000000";

    /**
     * 时钟背景透明度
     */
    public static final double CLOCKBACKGROUNDOPACITY=0.3;

    //时钟其他部分透明度
    public static final double CLOCKOTHEROPACITY=0.7;

    //秒针颜色
    public static final String SECONDCOLOR="#ff0000";
    //分针颜色
    public static final String MINUTECOLOR="#0000ff";
    //时针颜色
    public static final String HOURCOLOR="#808080";
    //时间颜色
    public static final String TIMECOLOR="#F5F5F5";
    //代码雨文字颜色
    public static final String CODERAINTEXTCOLOR="#00ff00";
    //动画帧数
    public static final double ANIMATIONFPS=10.0;
    //视频壁纸帧数
    public static final double MEDIAWALLPAPERFPS=60.0;


}

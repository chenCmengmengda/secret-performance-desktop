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



}

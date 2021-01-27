package cn.chenc.performs.util;

import cn.chenc.performs.enums.OsEnum;

/**
 * 　@description: 系统通用工具
 * 　@author secret
 * 　@date 2021/1/27 14:32
 *
 */
public class OsUtil {

    /**
     * 判断当前操作系统
     * @param type
     * @return
     */
    public static boolean decideOs(String type){
        if(osName().toLowerCase().contains(type.toLowerCase())){//不区分大小写
            return true;
        }
        return false;
    }

    /**
     * 操作系统名称
     * @return
     */
    public static String osName(){
        return System.getProperty("os.name");
    }

    public static void setWinIconAfter(String title){
        if(decideOs(OsEnum.WINDOWS.getKey())){
            Win32Util.setWinIconAfter(title);
        } else if(decideOs(OsEnum.LINUX.getKey())){

        } else if(decideOs(OsEnum.MAC.getKey())){

        }
    }

    public static void setWinIconTop(String title){
        if(decideOs(OsEnum.WINDOWS.getKey())){
            Win32Util.setWinIconTop(title);
        } else if(decideOs(OsEnum.LINUX.getKey())){

        } else if(decideOs(OsEnum.MAC.getKey())){

        }
    }

}

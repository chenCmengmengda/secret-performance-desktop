package cn.chenc.performs.util;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2020/12/30 10:20
 *
 */
public class StringUtil {

    public static boolean isEmpty(String str){
        if(str == null || str.equals("")){
            return true;
        }
        return false;
    }

}

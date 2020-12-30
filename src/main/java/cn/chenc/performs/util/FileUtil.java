package cn.chenc.performs.util;

import java.io.File;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2020/12/28 17:01
 *
 */
public class FileUtil {

    public static boolean exists(String filePath){
        File file=new File(filePath);
        if(!file.exists()){
            return false;
        }
        return true;
    }

}

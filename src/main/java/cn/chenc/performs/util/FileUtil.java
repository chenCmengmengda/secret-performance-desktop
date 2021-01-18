package cn.chenc.performs.util;

import java.io.File;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2020/12/28 17:01
 *
 */
public class FileUtil {

	/**
	 * 判断该文件路径  文件或目录是否存在
	 * @param filePath  文件路径
	 * @return  存在返回true  不存在返回false
	 */
	public static boolean exists(String filePath){
		if(new File(filePath).exists()) return true;
		return false;
	}

}

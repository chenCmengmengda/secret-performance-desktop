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

	/**
	 * @description: 创建文件夹
	 * @param filePath
	 * @return boolean 创建成功 true 否则 false
	 * @throws
	 * @author secret
	 * @date 2021/1/19 secret
	 */
	public static boolean mkdir(String filePath){
		File file=new File(filePath);
		if(!file.isDirectory()){
			return file.mkdir();
		}
		return false;
	}

}

package cn.chenc.performs.util;



import java.io.*;
import java.util.Properties;
import java.util.Set;

/**
 * 　@description: TODO
 * 　@author 陈_C
 * 　@date 2020/10/26 10:50
 *
 */
public class ConfigPropertiesUtil {

    //创建Properties对象
    private static Properties property = new Properties();
    private static String propertyPath="config"+ File.separator+"config.properties";
    private static String filePath;


    static{
        String rootPath= null;
        try {
            rootPath = new File("").getCanonicalPath();
            filePath=rootPath+File.separator+propertyPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (
//                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(filePath),"utf-8");
                BufferedReader reader = new BufferedReader(inputStreamReader);
        ) {
            property.load(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取字符串类型的值
     * @param key
     * @return
     */
    public static String get(String key) {
        return property.getProperty(key);
    }

    /**
     * 获取Integer类型的值
     * @param key
     * @return
     */
    public static Integer getInteger(String key) {
        String value = get(key);
        return null == value ? null : Integer.valueOf(value);
    }

    public static Double getDouble(String key){
        String value = get(key);
        return StringUtil.isEmpty(value) ? null : Double.valueOf(value);
    }

    /**
     * 获取Boolean类型的值
     * @param key
     * @return
     */
    public static Boolean getBoolean(String key) {
        String value = get(key);
        return StringUtil.isEmpty(value) ? null : Boolean.valueOf(value);
    }

    public static Set<String> getKeys(){
        return property.stringPropertyNames();
    }

    /**
     * 设置一个键值对
     * @param key
     * @param value
     */
    public static void set(String key,String value){
        try {
            property.setProperty(key, value);
            OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(new File(filePath)), "utf-8");
            property.store(oStreamWriter, "");
            oStreamWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 添加一个键值对
     * @param key
     * @param value
     */
    public static void add(String key,Object value){
        property.put(key,value);
    }

    public static void clean(){
        Set<String> set=getKeys();
        for(String key:set){
            set(key,"");
        }
    }

}

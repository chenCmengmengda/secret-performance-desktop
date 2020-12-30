package cn.chenc.performs.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2020/12/30 8:34
 *
 */
public class AppModel {
    private StringProperty imageProperty = new SimpleStringProperty();
    private StringProperty themeColorProperty = new SimpleStringProperty();

    public StringProperty getImageProperty(){
        return imageProperty;
    }

    public StringProperty getThemeColorProperty(){
        return themeColorProperty;
    }

    public final void setImageUrl(String value){
        getImageProperty().set(value);
    }

    public final String getImageUrl(String value){
        return getImageProperty().get();
    }

    public final void setThemeColor(String value){
        getThemeColorProperty().set(value);
    }

    public final String getThemeColor(String value){
        return getThemeColorProperty().get();
    }

}

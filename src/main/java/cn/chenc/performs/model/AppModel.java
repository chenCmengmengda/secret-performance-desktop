package cn.chenc.performs.model;

import javafx.beans.property.*;
import javafx.scene.image.Image;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2020/12/30 8:34
 *
 */
public class AppModel {
    private StringProperty imageProperty = new SimpleStringProperty();
    private ObjectProperty<Image> imageObjProperty= new SimpleObjectProperty<Image>();
    private DoubleProperty logoOpacityProperty = new SimpleDoubleProperty();
    private StringProperty themeColorProperty = new SimpleStringProperty();
    private BooleanProperty dragProperty = new SimpleBooleanProperty();
    private StringProperty layoutProperty = new SimpleStringProperty();

    public StringProperty getImageProperty(){
        return imageProperty;
    }

    public ObjectProperty<Image> getImageObjProperty(){
        return imageObjProperty;
    }

    public StringProperty getThemeColorProperty(){
        return themeColorProperty;
    }

    public DoubleProperty getLogoOpacityProperty(){
        return logoOpacityProperty;
    }

    public BooleanProperty getDragProperty(){
        return dragProperty;
    }

    public StringProperty getLayoutProperty(){
        return layoutProperty;
    }

    public final void setImageUrl(String value){
        getImageProperty().set(value);
    }

    public final String getImageUrl(){
        return getImageProperty().get();
    }

    public final void setImageObj(Image image){
        getImageObjProperty().set(image);
    }

    public final Image getImageObj(){
        return getImageObjProperty().get();
    }

    public final void setLogoOpacity(double value){
        getLogoOpacityProperty().set(value);
    }

    public final double getLogoOpacity(){
        return getLogoOpacityProperty().get();
    }

    public final void setThemeColor(String value){
        getThemeColorProperty().set(value);
    }

    public final String getThemeColor(){
        return getThemeColorProperty().get();
    }

    public final void setDrag(boolean value){
        getDragProperty().set(value);
    }

    public final boolean getDrag(){
        return getDragProperty().get();
    }

    public final void setLayout(String value){
        getLayoutProperty().set(value);
    }

    public final String getLayout(){
        return getLayoutProperty().get();
    }

}

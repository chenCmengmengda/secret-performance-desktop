package cn.chenc.performs.listener;

import cn.chenc.performs.enums.ConfigEnum;
import cn.chenc.performs.util.ConfigPropertiesUtil;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * 　@description: 鼠标拖拽监听
 * 　@author secret
 * 　@date 2021/1/1 21:21
 *
 */
public class DragListener implements EventHandler<MouseEvent> {
    private double xOffset = 0;
    private double yOffset = 0;
    private final Stage stage;
    private ConfigEnum configEnumX;
    private ConfigEnum configEnumY;

    public DragListener(Stage stage,ConfigEnum configEnumX,ConfigEnum configEnumY) {
        this.stage = stage;
        this.configEnumX = configEnumX;
        this.configEnumY = configEnumY;
    }

    @Override
    public void handle(MouseEvent event) {
        event.consume();
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {//鼠标按下
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {//鼠标拖动
            stage.setX(event.getScreenX() - xOffset);
            if(event.getScreenY() - yOffset < 0) {
                stage.setY(0);
            }else {
                stage.setY(event.getScreenY() - yOffset);
            }
            ConfigPropertiesUtil.set(configEnumX.getKey(),String.valueOf(stage.getX()));
            ConfigPropertiesUtil.set(configEnumY.getKey(),String.valueOf(stage.getY()));
        }
    }

    //开启拖拽
    public void enableDrag(Node node) {
        node.setOnMousePressed(this);
        node.setOnMouseDragged(this);
    }

    //关闭拖拽
    public void closeDrag(Node node){
        node.setOnMousePressed(null);
        node.setOnMouseDragged(null);
    }

    public void setXY(double x,double y){
        stage.setX(x);
        stage.setY(y);
    }
}

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2020/12/26 11:42
 */
module cn.chenc.performs {
    requires com.github.oshi;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
    requires com.sun.jna;
    requires com.sun.jna.platform;

    requires java.desktop;

    opens cn.chenc.performs to javafx.fxml;
    opens cn.chenc.performs.controller to javafx.fxml;
    opens cn.chenc.performs.state;
    exports cn.chenc.performs;
    exports cn.chenc.performs.controller;
    exports cn.chenc.performs.state;
    exports cn.chenc.performs.task;
    exports cn.chenc.performs.util;
    exports cn.chenc.performs.factory;

}

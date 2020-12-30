/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2020/12/26 11:42
 */
module cn.chenc.performs {
    requires com.github.oshi;
    requires javafx.controls;
    requires javafx.fxml;

    requires java.desktop;

    opens cn.chenc.performs to javafx.fxml;
    opens cn.chenc.performs.controller to javafx.fxml;
    exports cn.chenc.performs;
    exports cn.chenc.performs.controller;
    exports cn.chenc.performs.state;
    exports cn.chenc.performs.task;
    exports cn.chenc.performs.util;

}

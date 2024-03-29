/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2020/12/26 11:42
 */
module cn.chenc.performs {
    requires com.github.oshi;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.web;
    requires com.sun.jna;
    requires com.sun.jna.platform;
    requires uk.co.caprica.vlcj;
    requires uk.co.caprica.vlcj.javafx;
    requires java.desktop;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires org.slf4j;
    requires org.slf4j.simple;
    requires hutool.core;
    requires secret.plugin;
    requires com.jfoenix;

    opens cn.chenc.performs to javafx.fxml;
    opens cn.chenc.performs.controller to javafx.fxml;
    opens cn.chenc.performs.state;
    opens cn.chenc.performs.factory;
    opens cn.chenc.performs.plugins;
    exports cn.chenc.performs;
    exports cn.chenc.performs.controller;
    exports cn.chenc.performs.state;
    exports cn.chenc.performs.task;
    exports cn.chenc.performs.util;
    exports cn.chenc.performs.factory;
    exports cn.chenc.performs.vlcj;
    exports cn.chenc.performs.plugins;
    exports cn.chenc.performs.model;

}

package cn.chenc.performs.plugins;

import cn.chenc.performs.controller.BubbleCorsorController;
import cn.chenc.performs.state.BubbleCursorStage;
import com.gitee.secretopen.plugin.SecretPlugin;
import com.gitee.secretopen.plugin.SecretPluginInterface;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2021/2/3 12:58
 *
 */
@SecretPlugin
public class BubbleCorsorPlugin implements SecretPluginInterface {
    //开启插件
    @Override
    public void initialize() {
        if(BubbleCorsorController.getInstance()==null){
            BubbleCursorStage.getInstance().show();
        } else {
            BubbleCorsorController.getInstance().show();
        }

    }

    //关闭插件
    @Override
    public void stop() {
        BubbleCorsorController.getInstance().close();
    }

    @Override
    public void openSetup() {

    }

    @Override
    public boolean hasSetup() {
        return false;
    }

    @Override
    public String getPluginName() {
        return "鼠标跟随-彩色粒子";
    }
}

package cn.chenc.performs.plugins;

import com.gitee.secretopen.plugin.SecretPlugin;
import com.gitee.secretopen.plugin.SecretPluginInterface;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2021/1/30 18:16
 *
 */
@SecretPlugin
public class TestPlugin2 implements SecretPluginInterface {
    @Override
    public void initialize() {

    }

    @Override
    public void stop() {

    }

    @Override
    public String getPluginName() {
        return "test2";
    }
}

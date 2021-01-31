package cn.chenc.performs.plugins;

import com.gitee.secretopen.plugin.SecretPlugin;
import com.gitee.secretopen.plugin.SecretPluginInterface;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2021/1/28 15:02
 */
@SecretPlugin
public final class TestPlugin implements SecretPluginInterface {
    @Override
    public void initialize() {

    }

    @Override
    public void stop() {

    }

    @Override
    public String getPluginName() {
        return "test";
    }
}

package com.jroossien.cmdsigns.config;

public class PluginCfg extends EasyConfig {

    public PluginCfg(String fileName) {
        this.setFile(fileName);
        load();
    }
}

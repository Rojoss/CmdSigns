package com.jroossien.cmdsigns;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class CmdSigns extends JavaPlugin {

    private static CmdSigns instance;

    private final Logger log = Logger.getLogger("Essence");

    @Override
    public void onDisable() {
        instance = null;
        log("disabled");
    }

    @Override
    public void onEnable() {
        instance = this;
        log.setParent(this.getLogger());

        log("loaded successfully");
    }

    public void log(Object msg) {
        log.info("[CmdSigns " + getDescription().getVersion() + "] " + msg.toString());
    }
    public void warn(Object msg) {
        log.warning("[CmdSigns " + getDescription().getVersion() + "] " + msg.toString());
    }
    public void logError(Object msg) {
        log.severe("[CmdSigns " + getDescription().getVersion() + "] " + msg.toString());
    }

    public static CmdSigns inst() {
        return instance;
    }
}

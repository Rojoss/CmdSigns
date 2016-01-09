package com.jroossien.cmdsigns;

import com.jroossien.cmdsigns.commands.Commands;
import com.jroossien.cmdsigns.menu.Menu;
import com.jroossien.cmdsigns.menu.TemplateMenu;
import com.jroossien.cmdsigns.signs.TemplateManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class CmdSigns extends JavaPlugin {

    private static CmdSigns instance;

    private Commands cmds;

    private TemplateManager tm;
    private TemplateMenu tMenu;

    private final Logger log = Logger.getLogger("CmdSigns");

    @Override
    public void onDisable() {
        instance = null;
        log("disabled");
    }

    @Override
    public void onEnable() {
        instance = this;
        log.setParent(this.getLogger());

        cmds = new Commands(this);

        tm = new TemplateManager(this);
        tMenu = new TemplateMenu(this);

        registerListeners();

        log("loaded successfully");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return cmds.onCommand(sender, cmd, label, args);
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new Menu.Events(), this);
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

    public TemplateManager getTM() {
        return tm;
    }

    public TemplateMenu getTMenu() {
        return tMenu;
    }
}

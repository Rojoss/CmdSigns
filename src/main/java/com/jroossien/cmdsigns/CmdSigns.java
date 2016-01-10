package com.jroossien.cmdsigns;

import com.jroossien.cmdsigns.commands.Commands;
import com.jroossien.cmdsigns.config.DelayCfg;
import com.jroossien.cmdsigns.config.PluginCfg;
import com.jroossien.cmdsigns.config.messages.MessageCfg;
import com.jroossien.cmdsigns.listeners.MainListener;
import com.jroossien.cmdsigns.menu.Menu;
import com.jroossien.cmdsigns.menu.TemplateMenu;
import com.jroossien.cmdsigns.signs.TemplateManager;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class CmdSigns extends JavaPlugin {

    private static CmdSigns instance;
    private Vault vault;
    private Economy economy;

    private PluginCfg cfg;
    private DelayCfg delays;
    private MessageCfg msgCfg;

    private Commands cmds;

    private TemplateManager tm;
    private TemplateMenu tMenu;

    private final Logger log = Logger.getLogger("CmdSigns");

    @Override
    public void onDisable() {
        delays.save();
        instance = null;
        log("disabled");
    }

    @Override
    public void onEnable() {
        instance = this;
        log.setParent(this.getLogger());

        Plugin vaultPlugin = getServer().getPluginManager().getPlugin("Vault");
        if (vaultPlugin != null) {
            vault = (Vault)vaultPlugin;
            RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            if (economyProvider != null) {
                economy = economyProvider.getProvider();
            }
        }
        if (economy == null) {
            log("Failed to load Economy from Vault. The plugin will still work fine but economy costs for signs won't work!");
        }

        cfg = new PluginCfg("plugins/CmdSigns/CmdSigns.yml");
        delays = new DelayCfg("plugins/CmdSigns/data/Delays.yml");
        msgCfg = new MessageCfg("plugins/CmdSigns/Messages.yml");

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
        pm.registerEvents(new MainListener(this), this);
    }

    public void log(Object msg) {
        log.info("[CmdSigns " + getDescription().getVersion() + "] " + msg.toString());
    }

    public static CmdSigns inst() {
        return instance;
    }

    public Vault getVault() {
        return vault;
    }

    public Economy getEco() {
        return economy;
    }

    public PluginCfg getCfg() {
        return cfg;
    }

    public DelayCfg getDelays() {
        return delays;
    }

    public MessageCfg getMsgCfg() {
        return msgCfg;
    }

    public TemplateManager getTM() {
        return tm;
    }

    public TemplateMenu getTMenu() {
        return tMenu;
    }
}

package com.jroossien.cmdsigns.commands;

import com.jroossien.cmdsigns.CmdSigns;
import com.jroossien.cmdsigns.config.messages.MessageCfg;
import com.jroossien.cmdsigns.config.messages.Msg;
import com.jroossien.cmdsigns.config.messages.Param;
import com.jroossien.cmdsigns.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands {
    private CmdSigns cs;

    public Commands(CmdSigns cs) {
        this.cs = cs;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (label.equalsIgnoreCase("cmdsigns") || label.equalsIgnoreCase("cmdsign")) {
            if (args.length < 1) {
                Msg.INVALID_USAGE.send(sender, Param.P("{usage}", "/" + label + " [reload|loadtemplates|info]"));
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (!Util.hasPermission(sender, "cmdsigns.cmd.cmdsigns.reload")) {
                    Msg.NO_PERMISSION.send(sender);
                    return true;
                }
                MessageCfg.inst().load();
                Msg.RELOADED.send(sender);
                return true;
            }

            if (args[0].equalsIgnoreCase("loadtemplates")) {
                if (!Util.hasPermission(sender, "cmdsigns.cmd.cmdsigns.loadtemplates")) {
                    Msg.NO_PERMISSION.send(sender);
                    return true;
                }
                int count = cs.getTM().reloadTemplates();
                Msg.TEMPLATES_LOADED.send(sender, Param.P("{count}", count));
                return true;
            }

            if (args[0].equalsIgnoreCase("info")) {
                sender.sendMessage(Util.color("&8===== &4&lCmdSigns Plugin &8=====\n" +
                        "&6&lAuthor&8&l: &aWorstboy(Jos)\n" +
                        "&6&lVersion&8&l: &a" + cs.getDescription().getVersion() + "\n" +
                        "&6&lSpigot URL&8&l: &9https://www.spigotmc.org/resources/cmdsigns.16904/"));
                return true;
            }

            Msg.INVALID_USAGE.send(sender, Param.P("{usage}", "/" + label + " [reload|loadtemplates|info]"));
            return true;
        }


        if (label.equalsIgnoreCase("signs") || label.equalsIgnoreCase("templates")) {
            if (!(sender instanceof Player)) {
                Msg.PLAYER_COMMAND.send(sender);
                return true;
            }

            if (!Util.hasPermission(sender, "cmdsigns.cmd.signs")) {
                Msg.NO_PERMISSION.send(sender);
                return true;
            }

            Player player = (Player)sender;
            cs.getTMenu().show(player);
            return true;
        }

        return false;
    }
}
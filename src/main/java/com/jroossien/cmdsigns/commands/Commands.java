package com.jroossien.cmdsigns.commands;

import com.jroossien.cmdsigns.CmdSigns;
import com.jroossien.cmdsigns.config.messages.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands {
    private CmdSigns cs;

    public Commands(CmdSigns cs) {
        this.cs = cs;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("signs") || label.equalsIgnoreCase("templates")) {
            if (!(sender instanceof Player)) {
                Msg.PLAYER_COMMAND.send(sender);
                return true;
            }
            Player player = (Player)sender;
            cs.getTMenu().show(player);
            return true;
        }
        return false;
    }
}
package com.jroossien.cmdsigns.commands;

import com.jroossien.cmdsigns.CmdSigns;
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
                sender.sendMessage("Player command only.");
                return true;
            }
            Player player = (Player)sender;
            cs.getTMenu().show(player);
            return true;
        }
        return false;
    }
}
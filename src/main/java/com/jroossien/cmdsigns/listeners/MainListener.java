package com.jroossien.cmdsigns.listeners;

import com.jroossien.cmdsigns.CmdSigns;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MainListener implements Listener {

    private CmdSigns cs;

    public MainListener(CmdSigns cs) {
        this.cs = cs;
    }

    @EventHandler
    private void textInput(AsyncPlayerChatEvent event) {
        if (!cs.getTMenu().hasInput(event.getPlayer())) {
            return;
        }
        cs.getTMenu().setInputResult(event.getPlayer(), event.getMessage());
        event.setCancelled(true);
    }

}

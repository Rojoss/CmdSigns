package com.jroossien.cmdsigns.config;

import com.jroossien.cmdsigns.CmdSigns;
import com.jroossien.cmdsigns.signs.SignTemplate;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DelayCfg extends EasyConfig {

    public Map<String, Map<String, Long>> delays = new HashMap<String, Map<String, Long>>();

    public DelayCfg(String fileName) {
        this.setFile(fileName);
        load();

        //Save every 10 seconds.
        new BukkitRunnable() {
            @Override
            public void run() {
                save();
            }
        }.runTaskTimer(CmdSigns.inst(), 100, 200);
    }

    public void setDelay(UUID player, SignTemplate template) {
        Map<String, Long> playerDelays = new HashMap<String, Long>();
        if (delays.containsKey(player.toString())) {
            playerDelays = delays.get(player.toString());
        }
        playerDelays.put(template.getName(), System.currentTimeMillis() + template.getDelay() * 1000);
        delays.put(player.toString(), playerDelays);
    }
    public Long getDelay(UUID player, String template) {
        if (!delays.containsKey(player.toString())) {
            return 0l;
        }
        Map<String, Long> playerDelays = delays.get(player.toString());
        if (!playerDelays.containsKey(template)) {
            return 0l;
        }
        Long endTime = playerDelays.get(template);
        if (System.currentTimeMillis() >= endTime) {
            playerDelays.remove(template);
            delays.put(player.toString(), playerDelays);
            return 0l;
        }
        return endTime - System.currentTimeMillis();
    }
}

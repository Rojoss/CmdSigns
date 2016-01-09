package com.jroossien.cmdsigns.signs;

import com.jroossien.cmdsigns.CmdSigns;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SignTemplate {

    private YamlConfiguration config;

    private String name;

    private boolean enabled;
    private int uniqueLine;
    private boolean playerCmd;
    private boolean ignoreColors;
    private int delay;
    private String cost;

    private String[] syntax;
    private String[] commands;

    public SignTemplate(YamlConfiguration config, String name, String[] syntax, String[] commands, int uniqueLine, boolean enabled, boolean playerCmd, boolean ignoreColors, int delay, String cost) {
        this.config = config;
        this.name = name;
        this.syntax = syntax;
        this.commands = commands;
        this.uniqueLine = uniqueLine;
        this.enabled = enabled;
        this.playerCmd = playerCmd;
        this.ignoreColors = ignoreColors;
        this.delay = delay;
        this.cost = cost;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public String getName() {
        return name;
    }


    public String[] getSyntax() {
        return syntax;
    }

    public String getSyntax(int line) {
        if (line < 0 || line >= syntax.length) {
            return "";
        }
        return syntax[line];
    }

    public void setSyntax(int line, String syntax) {
        this.syntax[line] = syntax;
        config.set("syntax." + (line+1), syntax);
    }


    public String getCommand(CmdTrigger trigger) {
        if (trigger == null || trigger.ordinal() >= commands.length) {
            return "";
        }
        return commands[trigger.ordinal()];
    }

    public void setCommand(CmdTrigger trigger, String cmd) {
        this.commands[trigger.ordinal()] = cmd;
        config.set("commands." + trigger.toString().toLowerCase().replace("_", "-"), cmd);
    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        config.set("enabled", enabled);
    }


    public int getUniqueLine() {
        return uniqueLine;
    }

    public void setUniqueLine(int uniqueLine) {
        this.uniqueLine = uniqueLine;
        config.set("uniqueLine", uniqueLine+1);
    }


    public boolean isPlayerCmd() {
        return playerCmd;
    }

    public void setPlayerCmd(boolean playerCmd) {
        this.playerCmd = playerCmd;
        config.set("playerCmd", playerCmd);
    }


    public boolean isIgnoreColors() {
        return ignoreColors;
    }

    public void setIgnoreColors(boolean ignoreColors) {
        this.ignoreColors = ignoreColors;
        config.set("ignoreColors", ignoreColors);
    }


    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
        config.set("delay", delay);
    }


    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
        config.set("cost", cost);
    }


    public void save() {
        CmdSigns.inst().getTM().setTemplate(this);
        File file = new File(CmdSigns.inst().getTM().getTemplateDir(), getName() + ".yml");
        try {
            getConfig().save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

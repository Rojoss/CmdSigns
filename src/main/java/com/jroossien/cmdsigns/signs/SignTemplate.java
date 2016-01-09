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
    private String[] syntax;
    private String[] commands;

    public SignTemplate(YamlConfiguration config, String name, String[] syntax, String[] commands, int uniqueLine, boolean enabled) {
        this.config = config;
        this.name = name;
        this.syntax = syntax;
        this.commands = commands;
        this.uniqueLine = uniqueLine;
        this.enabled = enabled;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public String getName() {
        return name;
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

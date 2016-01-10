package com.jroossien.cmdsigns.signs;

import com.jroossien.cmdsigns.CmdSigns;
import com.jroossien.cmdsigns.util.Util;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TemplateManager {

    private CmdSigns cs;
    private Map<String, SignTemplate> templates = new HashMap<String, SignTemplate>();

    private File templateDir;

    public TemplateManager(CmdSigns cs) {
        this.cs = cs;

        templateDir = new File(cs.getDataFolder(), "\\templates");
        templateDir.mkdirs();

        reloadTemplates();
    }

    public int reloadTemplates() {
        Map<String, SignTemplate> templateMap = new HashMap<String, SignTemplate>();

        Map<String, File> configFiles = Util.getFiles(templateDir, "yml");
        for (Map.Entry<String, File> entry : configFiles.entrySet()) {
            YamlConfiguration templateCfg = YamlConfiguration.loadConfiguration(entry.getValue());

            String[] syntax = new String[] {templateCfg.getString("syntax.1"), templateCfg.getString("syntax.2"), templateCfg.getString("syntax.3"), templateCfg.getString("syntax.4")};
            String[] cmds = new String[] {templateCfg.getString("commands.left"), templateCfg.getString("commands.right"), templateCfg.getString("commands.shift-left"), templateCfg.getString("commands.shift-right")};
            int uniqueLine = templateCfg.getInt("uniqueLine");
            boolean enabled = templateCfg.getBoolean("enabled");
            boolean playerCmd = templateCfg.getBoolean("playerCmd");
            boolean ignoreColors = templateCfg.getBoolean("ignoreColors");
            int delay = templateCfg.getInt("delay");
            String cost = templateCfg.getString("cost");

            templateMap.put(entry.getKey(), new SignTemplate(templateCfg, entry.getKey(), syntax, cmds, uniqueLine-1, enabled, playerCmd, ignoreColors, delay, cost));
        }
        templates = templateMap;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (templates.size() < 1) {
                    //Load default [cmd] template
                    SignTemplate template = createTemplate("cmd");
                    template.setSyntax(0, "[cmd]");
                    template.setSyntax(1, "{command}");
                    template.setSyntax(2, "{arg-1}");
                    template.setSyntax(3, "{arg-2}");
                    template.setCommand(CmdTrigger.LEFT, "{command} {arg-1} {arg-2}");
                    template.setCommand(CmdTrigger.RIGHT, "{command} {arg-1} {arg-2}");
                    template.setCommand(CmdTrigger.SHIFT_LEFT, "{command} {arg-1} {arg-2}");
                    template.setCommand(CmdTrigger.SHIFT_RIGHT, "{command} {arg-1} {arg-2}");
                    template.save();
                }
            }
        }.runTaskLater(cs, 10);

        return templates.size();
    }

    public SignTemplate createTemplate(String name) {
        if (hasTemplate(name)) {
            return getTemplate(name);
        }

        File file = new File(templateDir, name + ".yml");
        YamlConfiguration templateCfg = YamlConfiguration.loadConfiguration(file);
        templateCfg.set("enabled", true);
        templateCfg.set("uniqueLine", 1);
        templateCfg.set("playerCmd", true);
        templateCfg.set("ignoreColors", true);
        templateCfg.set("delay", 0);
        templateCfg.set("cost", "");
        templateCfg.set("syntax.1", "");
        templateCfg.set("syntax.2", "");
        templateCfg.set("syntax.3", "");
        templateCfg.set("syntax.4", "");
        templateCfg.set("commands.left", "");
        templateCfg.set("commands.right", "");
        templateCfg.set("commands.shift-left", "");
        templateCfg.set("commands.shift-right", "");
        try {
            templateCfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SignTemplate template = new SignTemplate(templateCfg, name, new String[] {"", "", "", ""}, new String[] {"", "", "", ""}, 0, true, true, true, 0, "");
        templates.put(name, template);
        return template;
    }

    public void setTemplate(SignTemplate template) {
        templates.put(template.getName(), template);
    }

    public SignTemplate getTemplate(String name) {
        if (!hasTemplate(name)) {
            return null;
        }
        return templates.get(name);
    }

    public boolean hasTemplate(String name) {
        return templates.containsKey(name);
    }

    public Map<String, SignTemplate> getTemplates() {
        return templates;
    }

    public List<SignTemplate> getList() {
        return new ArrayList<SignTemplate>(templates.values());
    }

    public File getTemplateDir() {
        return templateDir;
    }
}

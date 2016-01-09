package com.jroossien.cmdsigns.signs;

import com.jroossien.cmdsigns.CmdSigns;
import com.jroossien.cmdsigns.util.Util;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void reloadTemplates() {
        Map<String, SignTemplate> templateMap = new HashMap<String, SignTemplate>();

        Map<String, File> configFiles = Util.getFiles(templateDir, "yml");
        for (Map.Entry<String, File> entry : configFiles.entrySet()) {
            YamlConfiguration templateCfg = YamlConfiguration.loadConfiguration(entry.getValue());

            boolean enabled = templateCfg.getBoolean("enabled");
            int uniqueLine = templateCfg.getInt("uniqueLine");
            String[] syntax = new String[] {templateCfg.getString("syntax.1"), templateCfg.getString("syntax.2"), templateCfg.getString("syntax.3"), templateCfg.getString("syntax.4")};
            String[] cmds = new String[] {templateCfg.getString("commands.left"), templateCfg.getString("commands.right"), templateCfg.getString("commands.shift-left"), templateCfg.getString("commands.shift-right")};

            templateMap.put(entry.getKey(), new SignTemplate(entry.getKey(), syntax, cmds, uniqueLine, enabled));
        }

        templates = templateMap;
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
}

package com.jroossien.cmdsigns.signs;

import com.jroossien.cmdsigns.CmdSigns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateManager {

    private CmdSigns cs;
    private Map<String, SignTemplate> templates = new HashMap<String, SignTemplate>();

    public TemplateManager(CmdSigns cs) {
        this.cs = cs;
        reloadTemplates();
    }

    public void reloadTemplates() {
        Map<String, SignTemplate> templateMap = new HashMap<String, SignTemplate>();

        //TODO: Load in all templates from config.

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

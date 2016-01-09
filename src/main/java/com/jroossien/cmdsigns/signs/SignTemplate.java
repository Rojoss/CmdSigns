package com.jroossien.cmdsigns.signs;

public class SignTemplate {

    private String name;
    private boolean enabled;
    private int uniqueLine;
    private String[] syntax;
    private String[] commands;

    public SignTemplate(String name, String[] syntax, String[] commands, int uniqueLine, boolean enabled) {
        this.name = name;
        this.syntax = syntax;
        this.commands = commands;
        this.uniqueLine = uniqueLine;
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getUniqueLine() {
        return uniqueLine;
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

    public String getCommand(CmdTrigger trigger) {
        if (trigger == null || trigger.ordinal() >= commands.length) {
            return "";
        }
        return commands[trigger.ordinal()];
    }
}

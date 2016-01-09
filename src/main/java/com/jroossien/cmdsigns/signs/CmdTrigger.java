package com.jroossien.cmdsigns.signs;

public enum CmdTrigger {
    LEFT("Left click"),
    RIGHT("Right click"),
    SHIFT_LEFT("Shift + Left click"),
    SHIFT_RIGHT("Shift + Right click");

    private String name;

    CmdTrigger(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

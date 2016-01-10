package com.jroossien.cmdsigns.cost;

import org.bukkit.entity.Player;

public abstract class Cost {
    public boolean success;
    public String error;

    abstract void parse(String input);
    abstract boolean has(Player player);
    abstract void take(Player player);

    public static Cost get(String input) {
        String[] split = input.split(":", 2);
        if (split.length <= 1) {
            return null;
        }
        String type = split[0];
        String value = split[1];
        if (type.equalsIgnoreCase("eco") || type.equalsIgnoreCase("economy") || type.equalsIgnoreCase("money") || type.equalsIgnoreCase("coins")) {
            return new EconomyCost(value);
        } else if (type.equalsIgnoreCase("item") || type.equalsIgnoreCase("i")) {
            return new ItemCost(value);
        } else if (type.equalsIgnoreCase("exp") || type.equalsIgnoreCase("xp") || type.equalsIgnoreCase("experience")) {
            return new ExpCost(value);
        }
        return null;
    }
}

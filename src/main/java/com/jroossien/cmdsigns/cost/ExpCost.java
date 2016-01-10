package com.jroossien.cmdsigns.cost;

import com.jroossien.cmdsigns.util.ExpUtil;
import com.jroossien.cmdsigns.util.Util;
import org.bukkit.entity.Player;

public class ExpCost extends Cost {

    private Integer amount = null;

    public ExpCost(String input) {
        parse(input);
    }

    public boolean has(Player player) {
        if (amount == null) {
            return false;
        }
        ExpUtil expUtil = new ExpUtil(player);
        return expUtil.hasExp(amount);
    }

    public void take(Player player) {
        ExpUtil expUtil = new ExpUtil(player);
        expUtil.changeExp(expUtil.getCurrentExp() - amount);
    }

    public void parse(String input) {
        amount = Util.getInt(input);
        success = false;
        if (amount == null) {
            return;
        }
        if (amount <= 0) {
            return;
        }
        success = true;
    }
}

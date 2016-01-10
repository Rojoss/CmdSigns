package com.jroossien.cmdsigns.cost;

import com.jroossien.cmdsigns.config.messages.Msg;
import com.jroossien.cmdsigns.config.messages.Param;
import com.jroossien.cmdsigns.util.ExpUtil;
import com.jroossien.cmdsigns.util.Util;
import org.bukkit.Bukkit;
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
        expUtil.changeExp(-amount);
    }

    public String format() {
        return Msg.COST_EXP_SYNTAX.getMsg(Param.P("{amount}", amount));
    }

    public void parse(String input) {
        amount = Util.getInt(input);
        success = false;
        if (amount == null || amount <= 0) {
            error = Msg.INVALID_XP_AMOUNT.getMsg(true, true);
            return;
        }
        success = true;
    }
}

package com.jroossien.cmdsigns.cost;

import com.jroossien.cmdsigns.CmdSigns;
import com.jroossien.cmdsigns.config.messages.Msg;
import com.jroossien.cmdsigns.config.messages.Param;
import com.jroossien.cmdsigns.util.Util;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public class EconomyCost extends Cost {

    private Economy eco;
    private Double amount = null;

    public EconomyCost(String input) {
        eco = CmdSigns.inst().getEco();
        parse(input);
    }

    public boolean has(Player player) {
        if (eco == null || amount == null) {
            return false;
        }
        return eco.has(player, amount);
    }

    public void take(Player player) {
        eco.withdrawPlayer(player, amount);
    }

    public String format() {
        return Msg.COST_MONEY_SYNTAX.getMsg(Param.P("{amount}", amount));
    }

    public void parse(String input) {
        amount = Util.getDouble(input);
        success = false;
        if (amount == null || amount < 0) {
            error = Msg.INVALID_ECO_AMOUNT.getMsg(true, true);
            return;
        }
        if (eco == null) {
            error = Msg.NO_VAULT_ECONOMY.getMsg(true, true);
            return;
        }
        success = true;
    }
}

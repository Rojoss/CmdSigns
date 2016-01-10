package com.jroossien.cmdsigns.cost;

import com.jroossien.cmdsigns.CmdSigns;
import com.jroossien.cmdsigns.config.messages.Msg;
import com.jroossien.cmdsigns.config.messages.Param;
import com.jroossien.cmdsigns.util.EItem;
import com.jroossien.cmdsigns.util.Util;
import net.milkbowl.vault.item.ItemInfo;
import net.milkbowl.vault.item.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemCost extends Cost {

    private EItem item = null;

    public ItemCost(String input) {
        parse(input);
    }

    public boolean has(Player player) {
        return Util.hasItems(player.getInventory(), item, item.getAmount(), true, true);
    }

    public void take(Player player) {
        Util.removeItems(player.getInventory(), item, item.getAmount(), true, true);
        player.updateInventory();
    }

    public String format() {
        return item.getAmount() + " " + item.getType().toString().toLowerCase().replace("_", " ") + (item.getDurability() > 0 ? ":" + item.getDurability() : "") +
                (item.getItemMeta().hasDisplayName() ? " name:" + item.getName() : "");
    }

    public void parse(String input) {
        success = false;

        String[] words = input.split(" ");
        if (input == null || input.trim().length() < 1) {
            error = Msg.INVALID_ITEM_EMPTY.getMsg(true, true);
            return;
        }

        EItem item = new EItem(Material.AIR);
        List<String> sections = Util.splitQuotedString(input.trim());

        //Item
        String[] split = sections.get(0).split(":");
        Material material = Material.matchMaterial(split[0]);
        Short data = 0;
        if (split.length > 1) {
            data = Util.getShort(split[1]);
            if (data == null) {
                 data = 0;
            }
        }

        //Item aliases through Vault.
        if (material == null && CmdSigns.inst().getVault() != null) {
            ItemInfo itemInfo = Items.itemByString(sections.get(0));
            if (itemInfo != null) {
                material = itemInfo.getType();
                data = itemInfo.getSubTypeId();
            }
        }

        if (material == null || material == Material.AIR) {
            error = Msg.INVALID_ITEM_UNKNOWN.getMsg(true, true);
            return;
        }
        item.setType(material);
        item.setDurability(data);
        item.setAmount(1);

        //If there is no meta specified we're done parsing...
        if (sections.size() < 2) {
            this.item = item;
            success = true;
            return;
        }
        ItemMeta defaultMeta = Bukkit.getServer().getItemFactory().getItemMeta(item.getType());

        //Amount and meta
        for (int i = 1; i < sections.size(); i++) {
            String section = sections.get(i);
            //Amount
            if (Util.getInt(section) != null) {
                item.setAmount(Util.getInt(section));
                continue;
            }

            split = section.split(":", 2);
            if (split.length < 2) {
                error = Msg.INVALID_ITEM_META.getMsg(true, true, Param.P("{type}", split[0]));
                return;
            }

            //Name
            if (split[0].equalsIgnoreCase("name")) {
                item.setName(split[1].replace("_", " "));
            }
            //TODO: Maybe add more meta tags but don't think it's needed.
        }

        this.item = item;
        success = true;
    }
}

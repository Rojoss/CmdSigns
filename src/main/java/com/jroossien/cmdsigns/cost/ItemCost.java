package com.jroossien.cmdsigns.cost;

import com.jroossien.cmdsigns.CmdSigns;
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
        int amount = item.getAmount();
        EItem clone = item.clone();
        clone.setAmount(1);
        return player.getInventory().contains(clone, amount);
    }

    public void take(Player player) {
        player.getInventory().remove(item);
    }

    public void parse(String input) {
        success = false;

        String[] words = input.split(" ");
        if (input == null || input.trim().length() < 1) {
            //error - No item specified
            return;
        }

        EItem item = new EItem(Material.AIR);
        List<String> sections = Util.splitQuotedString(input.trim());

        //Item
        String[] split = sections.get(0).split(":");
        Material material = Material.matchMaterial(split[0]);
        Short data = 0;
        if (split.length > 0) {
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
            //error - Unknown item
            return;
        }
        item.setType(material);
        item.setDurability(data);
        item.setAmount(1);

        //If there is no meta specified we're done parsing...
        if (sections.size() < 2) {
            this.item = item;
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
                //error - Invalid meta tag (empty)
                return;
            }

            //Name
            if (split[0].equalsIgnoreCase("name")) {
                item.setName(split[1].replace("_", " "));
            }

            //Lore
            if (split[0].equalsIgnoreCase("lore")) {
                item.setLore(split[1].replace("_", " ").replace("|", "\n"));
            }

            //TODO: Maybe add more meta tags but don't think it's needed.
        }

        this.item = item;
        success = true;
    }
}

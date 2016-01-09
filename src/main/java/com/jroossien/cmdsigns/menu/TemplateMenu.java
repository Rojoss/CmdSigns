package com.jroossien.cmdsigns.menu;

import com.jroossien.cmdsigns.CmdSigns;
import com.jroossien.cmdsigns.signs.CmdTrigger;
import com.jroossien.cmdsigns.signs.SignTemplate;
import com.jroossien.cmdsigns.signs.TemplateManager;
import com.jroossien.cmdsigns.util.EItem;
import com.jroossien.cmdsigns.util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TemplateMenu extends Menu {

    private CmdSigns cs;
    private TemplateManager tm;
    private Map<UUID, String> playerMenu = new HashMap<UUID, String>();
    private Map<UUID, String> input = new HashMap<UUID, String>();

    public TemplateMenu(CmdSigns cs) {
        super(cs, "template-edit", 6, "&9&lTemplate Editing");
        this.cs = cs;
        this.tm = cs.getTM();
    }

    @Override
    protected void onDestroy() {}

    @Override
    protected void onShow(InventoryOpenEvent event) {
        updateContent((Player)event.getPlayer());
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {}

    @Override
    protected void onClick(InventoryClickEvent event) {
        event.setCancelled(true);

        Player player = (Player)event.getWhoClicked();
        UUID uuid = player.getUniqueId();

        String menu = playerMenu.get(uuid);

        if (menu.startsWith("main")) {
            //Main menu
            if (event.getRawSlot() == 0) {
                //Previous page
                int page = Util.getInt(menu.split("-")[1]);
                if (page > 1) {
                    playerMenu.put(uuid, "main-" + (page-1));
                    updateContent(player);
                }
            } else if (event.getRawSlot() == 8) {
                //Next page
                int page = Util.getInt(menu.split("-")[1]);
                if (tm.getTemplates().size() > page * 45) {
                    playerMenu.put(uuid, "main-" + (page+1));
                    updateContent(player);
                }
            } else if (event.getRawSlot() == 4) {
                //New template
                input.put(uuid, "name");
                player.closeInventory();
                //TODO: Send message that user can type name of template in chat.
            } else if (event.getRawSlot() == 7) {
                //Close menu
                playerMenu.remove(uuid);
                player.closeInventory();
            } else if (event.getRawSlot() > 8 && event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.SIGN) {
                //Click on sign.
                playerMenu.put(uuid, "template-" + Util.stripAllColor(new EItem(event.getCurrentItem()).getName()));
                updateContent(player);
            }
        } else {
            String name = menu.split("-")[1];
            SignTemplate template = tm.getTemplate(name);
            if (template == null && (event.getRawSlot() != 0 || event.getRawSlot() != 8)) {
                return;
            }

            //Template edit menu
            if (event.getRawSlot() == 0) {
                //Back to list
                playerMenu.put(uuid, "main-0");
                updateContent(player);
            } else if (event.getRawSlot() == 8) {
                //Close menu
                playerMenu.remove(uuid);
                player.closeInventory();
            } else if (event.getRawSlot() >= 18 && event.getRawSlot() <= 21) {
                //Edit syntax
                if (event.getClick() == ClickType.SHIFT_RIGHT) {
                    template.setSyntax(event.getRawSlot() - 18, "");
                    template.save();
                    updateContent(player);
                } else if (event.getClick() == ClickType.SHIFT_LEFT) {
                    template.setUniqueLine(event.getRawSlot() - 18);
                    template.save();
                    updateContent(player);
                } else {
                    input.put(uuid, "syntax-" + (event.getRawSlot()-18));
                    player.closeInventory();
                    //TODO: Send message that user can type the syntax in chat.
                }
            } else if (event.getRawSlot() >= 23 && event.getRawSlot() <= 26) {
                //Edit commands
                if (event.getClick() == ClickType.SHIFT_RIGHT) {
                    template.setCommand(CmdTrigger.fromID(event.getRawSlot() - 23), "");
                    template.save();
                    updateContent(player);
                } else {
                    input.put(uuid, "cmd-" + (event.getRawSlot()-23));
                    player.closeInventory();
                    //TODO: Send message that user can type the command in chat.
                }
            } else if (event.getRawSlot() == 37) {
                //Player/Console command
                template.setPlayerCmd(!template.isPlayerCmd());
                template.save();
                updateContent(player);
            } else if (event.getRawSlot() == 38) {
                //Ignore/Match colors
                template.setIgnoreColors(!template.isIgnoreColors());
                template.save();
                updateContent(player);
            } else if (event.getRawSlot() == 40) {
                //Enable/disable
                template.setEnabled(!template.isEnabled());
                template.save();
                updateContent(player);
            } else if (event.getRawSlot() == 42) {
                //Set Delay
                if (event.isLeftClick()) {
                    //TODO: Start input session instead (placeholder)
                    template.setDelay(60);
                } else if (event.isRightClick()) {
                    template.setDelay(0);
                }
                template.save();
                updateContent(player);
            } else if (event.getRawSlot() == 43) {
                //Set Cost
                if (event.isLeftClick()) {
                    //TODO: Start input session instead (placeholder)
                    template.setCost("$100");
                } else if (event.isRightClick()) {
                    template.setCost("");
                }
                template.save();
                updateContent(player);
            }
        }
    }

    private void updateContent(Player player) {
        UUID uuid = player.getUniqueId();
        if (!playerMenu.containsKey(uuid)) {
            playerMenu.put(uuid, "main-0");
        }
        String menu = playerMenu.get(uuid);

        clearMenu(player);
        if (menu.startsWith("main")) {
            //Main menu
            setSlot(0, new EItem(Material.SKULL_ITEM).setName("&6&lPrevious Page").setSkull("MHF_ArrowLeft").addAllFlags(true), player);
            setSlot(1, new EItem(Material.PAPER).setName("&6&lInformation").setLore("&7Click on any of the &asigns &7to &aedit &7that template.", "&7Click on the &elog &7to create a &enew &7template."), player);
            setSlot(4, new EItem(Material.LOG).setName("&a&lNew Template").setLore("&7Adds a new template to the list.", "&7The menu will close so you type a name in chat.", "&7When it's added click on the sign", "&7to set it up and enable it."), player);
            setSlot(7, new EItem(Material.STAINED_CLAY, 1, (short) 14).setName("&c&lClose").setLore("&7Close the template editing menu."), player);
            setSlot(8, new EItem(Material.SKULL_ITEM).setName("&6&lNext Page").setSkull("MHF_ArrowRight").addAllFlags(true), player);

            int page = Util.getInt(menu.split("-")[1]);

            List<SignTemplate> templates = tm.getList();
            int slot = 9;
            for (int i = page * 45; i < page * 45 + 45 && i < templates.size(); i++) {
                SignTemplate template = templates.get(i);
                String[] lore = new String[] {"&7" + template.getSyntax(0), "&7" + template.getSyntax(1), "&7" + template.getSyntax(2), "&7" + template.getSyntax(3)};
                setSlot(slot, new EItem(Material.SIGN).setName("&e&l" + template.getName()).setLore(lore), player);
                slot++;
            }
        } else {
            //Template edit menu
            String name = menu.split("-")[1];
            setSlot(0, new EItem(Material.SKULL_ITEM).setName("&6&lBack").setSkull("MHF_ArrowLeft").addAllFlags(true), player);
            setSlot(1, new EItem(Material.PAPER).setName("&6&lInformation").setLore("&7Click on the &anametags &7to edit the &asyntax &7of each line.",
                    "&7Click on the &ecommand blocks &7to set the  &ecommands&7.", "&7On the &bbottom &7you can modify extra &bsettings&7.", "&7For example you can set a cost and/or delay etc."), player);
            setSlot(8, new EItem(Material.STAINED_CLAY, 1, (short) 14).setName("&c&lClose").setLore("&7Close the template editing menu."), player);

            int[] emptySlots = new int[] {9,10,11,12,13,14,15,16,17, 22, 27,28,29,30,31,32,33,34,35, 36,39,41,44, 45,46,47,48,49,50,51,52,53};
            for (int slot : emptySlots) {
                setSlot(slot, new EItem(Material.STAINED_GLASS_PANE,1, (short)15).setName("&8#").addAllFlags(true));
            }

            SignTemplate template = tm.getTemplate(name);
            if (template == null) {
                setSlot(4, new EItem(Material.SIGN).setName("&e&l" + name).setLore("&c&lNo template data found!", "&7Try closing and reopening the menu."), player);
                return;
            }
            setSlot(4, new EItem(Material.SIGN).setName("&e&l" + name).setLore("&7" + template.getSyntax(0), "&7" + template.getSyntax(1), "&7" + template.getSyntax(2), "&7" + template.getSyntax(3)), player);

            for (int i = 0; i < 4; i++) {
                EItem item = new EItem(Material.NAME_TAG, i+1).setName("&6Line " + (i+1)).setLore(template.getSyntax(i) == null || template.getSyntax(i).isEmpty() ? "&c&oempty" : "&e" + template.getSyntax(i),
                        "&7Click to edit the syntax of this line.", "&7Shift right click to clear it.", "&7Shift left click to make this the unique line.");
                if (template.getUniqueLine() == i) {
                    item.makeGlowing(true);
                    item.addLore("&dThis is the &d&lunique line&d!", "&dThis means any sign matching this line", "&dwill be considered a &5&l" + template.getName() + " &dsign!");
                }
                setSlot(i+18, item);
            }

            for (CmdTrigger trigger : CmdTrigger.values()) {
                setSlot(trigger.ordinal() + 23, new EItem(Material.COMMAND, trigger.ordinal() + 1).setName("&6" + trigger.getName()).setLore(
                        template.getCommand(trigger) == null || template.getCommand(trigger).isEmpty() ? "&c&onone" : "&e" + template.getCommand(trigger), "&7Click to edit this command.", "&7Shift right click to clear it."));
            }

            if (template.isPlayerCmd()) {
                setSlot(37, new EItem(Material.SKULL_ITEM, 1, (short)3).setName("&6&lPlayer Command").setLore("&7The commands will be executed by the &6player&7.", "&8Click to change to console."), player);
            } else {
                setSlot(37, new EItem(Material.COMMAND).setName("&e&lConsole Command").setLore("&7The commands will be executed by the &econsole&7.",
                        "&7You can use the &f&l{player} &7placeholder in any command.", "&8Click to change to player."), player);
            }

            if (template.isIgnoreColors()) {
                setSlot(38, new EItem(Material.BUCKET).setName("&6&lIgnoring colors").setLore("&7Colors on the unique line will be ignored.",
                        "&cDon't put any color codes in the syntax of the unique line!", "&aYou can put any colors you want on the actual sign.", "&8Click to change to matching colors."), player);
            } else {
                setSlot(38, new EItem(Material.WATER_BUCKET).setName("&e&lMatching colors").setLore("&7Colors on the unique line have to match exactly.",
                        "&7For example if you put &9[heal] &7as syntax", "&7On the sign you need to put &9[heal] &7too.", "&8Click to change to ignoring colors."), player);
            }

            if (template.isEnabled()) {
                setSlot(40, new EItem(Material.INK_SACK, 1, (short)10).setName("&a&lEnabled").setLore("&7This template is currently &aenabled&7.", "&8Click to disable the template."), player);
            } else {
                setSlot(40, new EItem(Material.INK_SACK, 1, (short)8).setName("&c&lDisabled").setLore("&7this template is currently &cdisabled&7.",
                        "&7&oUsers will still be blocked", "&7&ofrom creating/breaking this sign.", "&8Click to enable the template."), player);
            }

            if (template.getDelay() > 0) {
                setSlot(42, new EItem(Material.WATCH).setName("&6&l" + template.getDelay() + " Seconds Delay").setLore("&7Players have to wait &e" + template.getDelay() + " &7seconds",
                        "&7after using the sign to use it again.", "&aLeft &7click to &aedit &7the delay.", "&eRight &7click to &eremove &7the delay."), player);
            } else {
                setSlot(42, new EItem(Material.WATCH).setName("&c&lNo Delay").setLore("&aClick to set a delay.",
                        "&7Players will have to wait xxx seconds", "&7after using the sign to use it again", "&7when a delay is set."), player);
            }

            if (template.getCost() != null && !template.getCost().isEmpty()) {
                setSlot(43, new EItem(Material.GOLD_INGOT).setName("&6&lCost&8&l: &e&l" + template.getCost()).setLore("&7Players have to pay &e" + template.getCost() + " &7to use the sign.",
                        "&aLeft &7click to &aedit &7the cost.", "&eRight &7click to &eremove &7the cost."), player);
            } else {
                setSlot(43, new EItem(Material.GOLD_INGOT).setName("&c&lNo Cost").setLore("&aClick to set a cost.",
                        "&7Players will have to pay money or items", "&7to use the sign when a cost is set."), player);
            }
        }
    }

    public boolean hasInput(Player player) {
        return input.containsKey(player.getUniqueId());
    }

    public void setInputResult(Player player, String string) {
        UUID uuid = player.getUniqueId();
        String type = input.get(player.getUniqueId());
        string = string.trim();

        //Disable input when q is typed.
        if (string.equalsIgnoreCase("q")) {
            input.remove(player.getUniqueId());
            //TODO: Send message that input is disabled.
            return;
        }

        if (type.equalsIgnoreCase("name")) {
            //New template name input
            if (!string.matches("[a-zA-Z]+")) {
                //TODO: Send message that the name is invalid.
                return;
            }
            if (tm.getTemplate(string) != null) {
                //TODO: Send message that the name is already used.
                return;
            }

            //Create the template and open that menu.
            input.remove(player.getUniqueId());
            tm.createTemplate(string);
            playerMenu.put(player.getUniqueId(), "template-" + string);

            show(player);
            updateContent(player);
            return;
        }


        String menu = playerMenu.get(uuid);
        String name = menu.split("-")[1];
        SignTemplate template = tm.getTemplate(name);
        if (template == null) {
            //This should never happen but just in case. (Don't want users getting stuck in input mode)
            input.remove(player.getUniqueId());
            //TODO: Send message that input is disabled.
            return;
        }

        int index = Util.getInt(type.split("-")[1]);
        if (type.startsWith("syntax")) {
            if (template.getUniqueLine() == index) {
                for (SignTemplate t : tm.getList()) {
                    if (t.getSyntax(t.getUniqueLine()).equalsIgnoreCase(string)) {
                        //TODO: Send message that the line is not unique and it must be.
                        return;
                    }
                }
            }

            input.remove(player.getUniqueId());
            template.setSyntax(index, string);
            template.save();
            show(player);
            updateContent(player);
            return;
        }

        if (type.startsWith("cmd")) {
            input.remove(player.getUniqueId());
            template.setCommand(CmdTrigger.fromID(index), string);
            template.save();
            show(player);
            updateContent(player);
            return;
        }
    }
}

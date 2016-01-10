package com.jroossien.cmdsigns.menu;

import com.jroossien.cmdsigns.CmdSigns;
import com.jroossien.cmdsigns.config.messages.Msg;
import com.jroossien.cmdsigns.config.messages.Param;
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
        super(cs, "template-edit", 6, Msg.TITLE.getMsg());
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
                if (page >= 1) {
                    playerMenu.put(uuid, "main-" + (page-1));
                    updateContent(player);
                }
            } else if (event.getRawSlot() == 8) {
                //Next page
                int page = Util.getInt(menu.split("-")[1]);
                if (tm.getTemplates().size() > ((page+1) * 45)) {
                    playerMenu.put(uuid, "main-" + (page+1));
                    updateContent(player);
                }
            } else if (event.getRawSlot() == 4) {
                //New template
                input.put(uuid, "name");
                player.closeInventory();
                Msg.NEW_TEMPLATE.send(player);
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
                    //TODO: Check if line syntax is unique before setting it.
                    template.setUniqueLine(event.getRawSlot() - 18);
                    template.save();
                    updateContent(player);
                } else {
                    input.put(uuid, "syntax-" + (event.getRawSlot()-18));
                    player.closeInventory();
                    Msg.EDIT_SYNTAX.send(player, Param.P("{line}", (event.getRawSlot()-18)));
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
                    Msg.EDIT_SYNTAX.send(player, Param.P("{type}", CmdTrigger.fromID((event.getRawSlot()-23)).getName()));
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
            setSlot(0, new EItem(Material.SKULL_ITEM).setName(Msg.PREV_PAGE.getMsg()).setSkull("MHF_ArrowLeft").addAllFlags(true), player);
            setSlot(1, new EItem(Material.PAPER).setName(Msg.INFO.getMsg()).setLore(Msg.INFO_MAIN.getMsg()), player);
            setSlot(4, new EItem(Material.LOG).setName(Msg.ADD_TEMPLATE.getMsg()).setLore(Msg.ADD_TEMPLATE_DESC.getMsg()), player);
            setSlot(7, new EItem(Material.STAINED_CLAY, 1, (short) 14).setName(Msg.CLOSE.getMsg()).setLore(Msg.CLOSE_DESC.getMsg()), player);
            setSlot(8, new EItem(Material.SKULL_ITEM).setName(Msg.NEXT_PAGE.getMsg()).setSkull("MHF_ArrowRight").addAllFlags(true), player);

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
            setSlot(0, new EItem(Material.SKULL_ITEM).setName(Msg.BACK.getMsg()).setSkull("MHF_ArrowLeft").addAllFlags(true), player);
            setSlot(1, new EItem(Material.PAPER).setName(Msg.INFO.getMsg()).setLore(Msg.INFO_TEMPLATE.getMsg()), player);
            setSlot(8, new EItem(Material.STAINED_CLAY, 1, (short) 14).setName(Msg.CLOSE.getMsg()).setLore(Msg.CLOSE_DESC.getMsg()), player);

            int[] emptySlots = new int[] {9,10,11,12,13,14,15,16,17, 22, 27,28,29,30,31,32,33,34,35, 36,39,41,44, 45,46,47,48,49,50,51,52,53};
            for (int slot : emptySlots) {
                setSlot(slot, new EItem(Material.STAINED_GLASS_PANE,1, (short)15).setName("&8#").addAllFlags(true));
            }

            SignTemplate template = tm.getTemplate(name);
            if (template == null) {
                setSlot(4, new EItem(Material.SIGN).setName("&e&l" + name).setLore(Msg.NO_DATA.getMsg()), player);
                return;
            }
            setSlot(4, new EItem(Material.SIGN).setName("&e&l" + name).setLore("&7" + template.getSyntax(0), "&7" + template.getSyntax(1), "&7" + template.getSyntax(2), "&7" + template.getSyntax(3)), player);

            for (int i = 0; i < 4; i++) {
                EItem item = new EItem(Material.NAME_TAG, i+1).setName(Msg.LINE.getMsg() + " " + (i+1)).setLore(
                        template.getSyntax(i) == null || template.getSyntax(i).isEmpty() ? Msg.EMPTY.getMsg() : "&e" + template.getSyntax(i), Msg.SYNTAX_DESC.getMsg());
                if (template.getUniqueLine() == i) {
                    item.makeGlowing(true);
                    item.addLore(Msg.UNIQUE_LINE.getMsg(Param.P("{template}", template.getName())));
                }
                setSlot(i+18, item);
            }

            for (CmdTrigger trigger : CmdTrigger.values()) {
                setSlot(trigger.ordinal() + 23, new EItem(Material.COMMAND, trigger.ordinal() + 1).setName("&6" + trigger.getName()).setLore(
                        template.getCommand(trigger) == null || template.getCommand(trigger).isEmpty() ? Msg.NONE.getMsg() : "&e" + template.getCommand(trigger), Msg.CMD_DESC.getMsg()));
            }

            if (template.isPlayerCmd()) {
                setSlot(37, new EItem(Material.SKULL_ITEM, 1, (short)3).setName(Msg.PLAYER_CMD.getMsg()).setLore(Msg.PLAYER_CMD_DESC.getMsg()), player);
            } else {
                setSlot(37, new EItem(Material.COMMAND).setName(Msg.CONSOLE_CMD.getMsg()).setLore(Msg.CONSOLE_CMD_DESC.getMsg()), player);
            }

            if (template.isIgnoreColors()) {
                setSlot(38, new EItem(Material.BUCKET).setName(Msg.IGNORE_COLOR.getMsg()).setLore(Msg.IGNORE_COLOR_DESC.getMsg()), player);
            } else {
                setSlot(38, new EItem(Material.WATER_BUCKET).setName(Msg.MATCH_COLOR.getMsg()).setLore(Msg.MATCH_COLOR_DESC.getMsg()), player);
            }

            if (template.isEnabled()) {
                setSlot(40, new EItem(Material.INK_SACK, 1, (short)10).setName(Msg.ENABLED.getMsg()).setLore(Msg.ENABLED_DESC.getMsg()), player);
            } else {
                setSlot(40, new EItem(Material.INK_SACK, 1, (short)8).setName(Msg.DISABLED.getMsg()).setLore(Msg.DISABLED_DESC.getMsg()), player);
            }

            if (template.getDelay() > 0) {
                setSlot(42, new EItem(Material.WATCH).setName(Msg.DELAY_SET.getMsg(Param.P("{seconds}", template.getDelay()))).setLore(Msg.DELAY_SET_DESC.getMsg(Param.P("{seconds}", template.getDelay()))), player);
            } else {
                setSlot(42, new EItem(Material.WATCH).setName(Msg.DELAY.getMsg()).setLore(Msg.DELAY_DESC.getMsg()), player);
            }

            if (template.getCost() != null && !template.getCost().isEmpty()) {
                setSlot(43, new EItem(Material.GOLD_INGOT).setName(Msg.COST_SET.getMsg(Param.P("{cost}", template.getCost()))).setLore(Msg.COST_SET_DESC.getMsg(Param.P("{cost}", template.getCost()))), player);
            } else {
                setSlot(43, new EItem(Material.GOLD_INGOT).setName(Msg.COST.getMsg()).setLore(Msg.COST_DESC.getMsg()), player);
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
            Msg.INPUT_DISABLED.send(player);
            return;
        }

        if (type.equalsIgnoreCase("name")) {
            //New template name input
            if (!string.matches("[a-zA-Z]+")) {
                Msg.INVALID_NAME.send(player);
                return;
            }
            if (tm.getTemplate(string) != null) {
                Msg.NAME_EXISTS.send(player);
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
            Msg.INPUT_DISABLED.send(player);
            return;
        }

        int index = Util.getInt(type.split("-")[1]);
        if (type.startsWith("syntax")) {
            if (template.getUniqueLine() == index) {
                for (SignTemplate t : tm.getList()) {
                    if (t.getSyntax(t.getUniqueLine()).equalsIgnoreCase(string)) {
                        Msg.INPUT_NOT_UNIQUE.send(player, Param.P("{line}", index));
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

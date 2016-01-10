package com.jroossien.cmdsigns.config.messages;

import com.jroossien.cmdsigns.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public enum Msg {
    NO_PERMISSION(Cat.GENERAL, "&cInsufficient permissions."),
    PLAYER_COMMAND(Cat.GENERAL, "&cThis is a player command only."),
    INVALID_USAGE(Cat.GENERAL, "&cInvalid usage! &7{usage}"),

    RELOADED(Cat.COMMAND, "&6All configurations reloaded!"),
    TEMPLATES_LOADED(Cat.COMMAND, "&6Loaded in &a&l{count} &6templates."),

    CREATED(Cat.SIGN, "&6You've created a &a{type} &6sign!"),
    DESTROYED(Cat.SIGN, "&6You've destroyed a &a{type} &6sign!"),
    INVALID_SYNTAX(Cat.SIGN, "&cSyntax mismatch at &8'&c{found-char}&8' &con line &4{line} &cexpected &8'&c{expected-char}&8'&c. &4&lSyntax&8: &c&l{syntax}"),

    NEW_TEMPLATE(Cat.TEMPLATE, "&6To create a new template &atype the name &6of the template &ain chat&6! &8(&7Type &f&lq &7to quit this input mode&8)"),
    EDIT_SYNTAX(Cat.TEMPLATE, "&6Type the syntax for line &a{line} &6in chat! &8(&7Type &f&lq &7to quit this input mode&8)"),
    EDIT_COMMAND(Cat.TEMPLATE, "&6Type the syntax for the &7'&a{type}&7' &6command in chat! &8(&7Type &f&lq &7to quit this input mode&8)"),
    INPUT_DISABLED(Cat.TEMPLATE, "&6Input mode is now disabled."),
    INVALID_NAME(Cat.TEMPLATE, "&cInvalid template name! &8(&7The name can only have regular characters&8)"),
    NAME_EXISTS(Cat.TEMPLATE, "&cInvalid template name! &8(&7A template with this name already exists&8)"),
    INPUT_NOT_UNIQUE(Cat.TEMPLATE, "&cThe specified input for line &4{line} &cis not unique! &8(&7You're editing the unique line which means the syntax must be unique&8)"),

    TITLE(Cat.TEMPLATE_MENU, "&9&lTemplate Editing"),
    PREV_PAGE(Cat.TEMPLATE_MENU, "&6&lPrevious Page"),
    NEXT_PAGE(Cat.TEMPLATE_MENU, "&6&lNext Page"),
    INFO(Cat.TEMPLATE_MENU, "&6&lInformation"),
    ADD_TEMPLATE(Cat.TEMPLATE_MENU, "&a&lAdd New Template"),
    ADD_TEMPLATE_DESC(Cat.TEMPLATE_MENU, "&7Adds a new template to the list.\n&7The menu will close so you type a name in chat.\n&7When it's added click on the sign\n&7to set it up and enable it."),
    BACK(Cat.TEMPLATE_MENU, "&6&lBack"),
    CLOSE(Cat.TEMPLATE_MENU, "&c&lClose"),
    CLOSE_DESC(Cat.TEMPLATE_MENU, "&7Close the template editing menu."),
    INFO_MAIN(Cat.TEMPLATE_MENU, "&7Click on any of the &asigns &7to &aedit &7that template.\n&7Click on the &elog &7to create a &enew &7template."),
    INFO_TEMPLATE(Cat.TEMPLATE_MENU, "&7Click on the &anametags &7to edit the &asyntax &7of each line.\n&7Click on the &ecommand blocks &7to set the  &ecommands&7.\n" +
            "&7On the &bbottom &7you can modify extra &bsettings&7.\n&7For example you can set a cost and/or delay etc."),
    NO_DATA(Cat.TEMPLATE_MENU, "&c&lNo template data found!\n&7Try closing and reopening the menu."),
    LINE(Cat.TEMPLATE_MENU, "&6Line"),
    EMPTY(Cat.TEMPLATE_MENU, "&c&oempty"),
    NONE(Cat.TEMPLATE_MENU, "&c&onone"),
    SYNTAX_DESC(Cat.TEMPLATE_MENU, "&7Click to edit the syntax of this line.\n&7Shift right click to clear it.\n&7Shift left click to make this the unique line."),
    UNIQUE_LINE(Cat.TEMPLATE_MENU, "&dThis is the &d&lunique line&d!\n&dThis means any sign matching this line\n&dwill be considered a &5&l{template} &dsign!"),
    CMD_DESC(Cat.TEMPLATE_MENU, "&7Click to edit this command.\n&7Shift right click to clear it."),
    PLAYER_CMD(Cat.TEMPLATE_MENU, "&6&lPlayer Command"),
    PLAYER_CMD_DESC(Cat.TEMPLATE_MENU, "&7The commands will be executed by the &6player&7.\n&8Click to change to console."),
    CONSOLE_CMD(Cat.TEMPLATE_MENU, "&e&lConsole Command"),
    CONSOLE_CMD_DESC(Cat.TEMPLATE_MENU, "&7The commands will be executed by the &econsole&7.\n&7You can use the &f&l{player} &7placeholder in any command.\n&8Click to change to player."),
    IGNORE_COLOR(Cat.TEMPLATE_MENU, "&6&lIgnoring colors"),
    IGNORE_COLOR_DESC(Cat.TEMPLATE_MENU, "&7Colors on the unique line will be ignored.\n&cDon't put any color codes in the syntax of the unique line!\n&aYou can put any colors you want on the actual sign.\n&8Click to change to matching colors."),
    MATCH_COLOR(Cat.TEMPLATE_MENU, "&e&lMatching colors"),
    MATCH_COLOR_DESC(Cat.TEMPLATE_MENU, "&7Colors on the unique line have to match exactly.\n&7For example if you put &9[heal] &7as syntax\n&7On the sign you need to put &9[heal] &7too.\n&8Click to change to ignoring colors."),
    ENABLED(Cat.TEMPLATE_MENU, "&a&lEnabled"),
    ENABLED_DESC(Cat.TEMPLATE_MENU, "&7This template is currently &aenabled&7.\n&8Click to disable the template."),
    DISABLED(Cat.TEMPLATE_MENU, "&c&lDisabled"),
    DISABLED_DESC(Cat.TEMPLATE_MENU, "&7this template is currently &cdisabled&7.\n&7&oUsers will still be blocked\n&7&ofrom creating/breaking this sign.\n&8Click to enable the template."),
    DELAY(Cat.TEMPLATE_MENU, "&c&lNo Delay"),
    DELAY_DESC(Cat.TEMPLATE_MENU, "&aClick to set a delay.\n&7Players will have to wait xxx seconds\n&7after using the sign to use it again\n&7when a delay is set."),
    DELAY_SET(Cat.TEMPLATE_MENU, "&6&l{seconds} Seconds Delay"),
    DELAY_SET_DESC(Cat.TEMPLATE_MENU, "&7Players have to wait &e{seconds} &7seconds\n&7after using the sign to use it again.\n&aLeft &7click to &aedit &7the delay.\n&eRight &7click to &eremove &7the delay."),
    COST(Cat.TEMPLATE_MENU, "&c&lNo Cost"),
    COST_DESC(Cat.TEMPLATE_MENU, "&aClick to set a cost.\n&7Players will have to pay money or items\n&7to use the sign when a cost is set."),
    COST_SET(Cat.TEMPLATE_MENU, "&6&lCost&8&l: &e&l{cost}"),
    COST_SET_DESC(Cat.TEMPLATE_MENU, "&7Players have to pay &e{cost} &7to use the sign.\n&aLeft &7click to &aedit &7the cost.\n&eRight &7click to &eremove &7the cost."),

    ;


    private Cat cat;
    private String msg;

    Msg(Cat cat, String msg) {
        this.cat = cat;
        this.msg = msg;
    }


    public String getDefault() {
        return msg;
    }

    public String getName() {
        return toString().toLowerCase().replace("_", "-");
    }

    public String getCategory() {
        return cat.toString().toLowerCase().replace("_", "-");
    }

    public String getMsg() {
        return MessageCfg.inst().getMessage(getCategory(), getName());
    }

    public String getMsg(Param... params) {
        return getMsg(false, false, params);
    }

    public String getMsg(boolean prefix, boolean color, Param... params) {
        String message = (prefix ? MessageCfg.inst().prefix : "") + getMsg();
        for (Param p : params) {
            message = message.replace(p.getParam(), p.toString());
        }
        if (color) {
            message = Util.color(message);
        }
        return message;
    }


    public void broadcast(Param... params) {
        broadcast(true, true, params);
    }

    public void broadcast(boolean prefix, boolean color, Param... params) {
        Bukkit.broadcastMessage(getMsg(prefix, color, params));
    }

    public void send(CommandSender sender, Param... params) {
        send(sender, true, true, params);
    }

    public void send(CommandSender sender, boolean prefix, boolean color, Param... params) {
        if (sender != null) {
            sender.sendMessage(getMsg(prefix, color, params));
        }
    }


    private enum Cat {
        GENERAL,
        COMMAND,
        SIGN,
        TEMPLATE,
        TEMPLATE_MENU,
        ;
    }
}

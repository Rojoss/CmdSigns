package com.jroossien.cmdsigns.listeners;

import com.jroossien.cmdsigns.CmdSigns;
import com.jroossien.cmdsigns.config.messages.Msg;
import com.jroossien.cmdsigns.signs.CmdTrigger;
import com.jroossien.cmdsigns.signs.SignTemplate;
import com.jroossien.cmdsigns.util.Argument;
import com.jroossien.cmdsigns.util.SignParser;
import com.jroossien.cmdsigns.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class MainListener implements Listener {

    private CmdSigns cs;

    public MainListener(CmdSigns cs) {
        this.cs = cs;
    }

    @EventHandler
    private void textInput(AsyncPlayerChatEvent event) {
        if (!cs.getTMenu().hasInput(event.getPlayer())) {
            return;
        }
        cs.getTMenu().setInputResult(event.getPlayer(), event.getMessage());
        event.setCancelled(true);
    }


    private SignTemplate getMatchingSign(String[] lines) {
        for (int i = 0; i < 4; i++) {
            lines[i] = Util.stripAllColor(lines[i]);
        }
        for (SignTemplate template : cs.getTM().getTemplates().values()) {
            if (template.getUniqueLine() >= 0 && template.getUniqueLine() < 4 && template.getSyntax(template.getUniqueLine()) != null && template.getSyntax(template.getUniqueLine()).equalsIgnoreCase(lines[template.getUniqueLine()])) {
                return template;
            }
        }
        return null;
    }

    public Sign getSign(Block block, boolean mustAttach) {
        if (block.getState() instanceof Sign) {
            return (Sign)block.getState();
        }
        /*
        //Check for attached blocks.
        BlockFace[] dirs = new BlockFace[] {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
        for (BlockFace dir : dirs) {
            Block relative = block.getRelative(dir);
            if (relative.getState() instanceof Sign) {
                if (!mustAttach) {
                    return (Sign)relative.getState();
                }
                org.bukkit.material.Sign signMat = (org.bukkit.material.Sign)relative.getState().getData();
                if (relative.getRelative(signMat.getAttachedFace()).equals(block)) {
                    return (Sign)relative.getState();
                }
            }
        }
        */
        return null;
    }

    public boolean matchLine(String syntax, String line) {
        if (syntax == null || line == null) {
            return false;
        }
        syntax = Util.stripAllColor(syntax);
        if (syntax.isEmpty()) {
            return true;
        }
        if (syntax.equals(line)) {
            return true;
        }
        String str = syntax.replaceAll("\\{\\w+\\}", "");

        return false;
    }

    @EventHandler
    private void signCreate(SignChangeEvent event) {
        SignTemplate template = getMatchingSign(event.getLines().clone());
        if (template == null) {
            return;
        }
        String perm = "cmdsigns.signs.create." + template.getName();
        if (!Util.hasPermission(event.getPlayer(), perm)) {
            Msg.NO_PERMISSION.send(event.getPlayer());
            event.setCancelled(true);
        }

        SignParser parser = new SignParser(template, event.getLines());
        if (parser.isValid()) {
            for (int i = 0; i < 4; i++) {
                event.setLine(i, Util.color(event.getLine(i)));
            }
            //TODO: Send message sign created
        } else {
            event.setCancelled(true);
            event.getPlayer().sendMessage(parser.getError());
            //TODO: Send message invalid sign syntax.
        }
    }

    @EventHandler
    private void signBreak(BlockBreakEvent event) {
        Sign signBlock = getSign(event.getBlock(), true);
        if (signBlock == null) {
            return;
        }

        SignTemplate template = getMatchingSign(signBlock.getLines().clone());
        if (template == null) {
            return;
        }

        /*
        if (!inSignBreakList(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            return;
        }
        */

        String perm = "cmdsigns.signs.break." + template.getName();
        if (!Util.hasPermission(event.getPlayer(), perm)) {
            Msg.NO_PERMISSION.send(event.getPlayer());
            event.setCancelled(true);
        }

        //TODO: Send sign broken message.
    }

    @EventHandler
    private void signUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (event.getAction() == Action.PHYSICAL) {
            block = block.getRelative(BlockFace.DOWN);
        }
        if (block == null || block.getType() == Material.AIR) {
            return;
        }
        Sign signBlock = getSign(block, true);
        if (signBlock == null) {
            return;
        }

        SignTemplate template = getMatchingSign(signBlock.getLines().clone());
        if (template == null) {
            return;
        }
        if (!template.isEnabled()) {
            return;
        }
        if (!signBlock.getBlock().equals(event.getClickedBlock())) {
            return;
        }

        String perm = "essence.signs.use." + template.getName();
        if (!Util.hasPermission(player, perm)) {
            Msg.NO_PERMISSION.send(player);
            event.setCancelled(true);
        }

        String cmd = "";
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (player.isSneaking()) {
                cmd = template.getCommand(CmdTrigger.SHIFT_LEFT);
            } else {
                cmd = template.getCommand(CmdTrigger.LEFT);
            }
        } else {
            if (player.isSneaking()) {
                cmd = template.getCommand(CmdTrigger.SHIFT_RIGHT);
            } else {
                cmd = template.getCommand(CmdTrigger.RIGHT);
            }
        }

        if (cmd != null && !cmd.isEmpty()) {
            SignParser signParser = new SignParser(template, signBlock.getLines());
            if (signParser.isValid()) {
                cmd = cmd.replace("{player}", player.getName());
                for (Argument arg : signParser.getArguments()) {
                    cmd = cmd.replace("{" + arg.getName() + "}", arg.getValue());
                }
                if (template.isPlayerCmd()) {
                    player.performCommand(cmd.trim());
                } else {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd.trim());
                }
            } else {
                player.sendMessage(signParser.getError());
                //TODO: Send invalid sign syntax message.
            }
        }
    }

}

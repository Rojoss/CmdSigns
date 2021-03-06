package com.jroossien.cmdsigns.util;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    /**
     * Integrate ChatColor in a string based on color codes.
     * @param str The string to apply color to.
     * @return formatted string
     */
    public static String color(String str) {
        for (ChatColor c : ChatColor.values()) {
            str = str.replaceAll("&" + c.getChar() + "|&" + Character.toUpperCase(c.getChar()), "§" + c.getChar());
        }
        return str;
    }

    /**
     * Integrate ChatColor in a string based on color codes.
     * @param str The string to apply color to.
     * @return formatted string
     */
    public static String colorChatColors(String str) {
        for (ChatColor c : ChatColor.values()) {
            str = str.replaceAll("&" + c.getChar() + "|&" + Character.toUpperCase(c.getChar()), c.toString());
        }
        return str;
    }

    /**
     * Remove all color and put colors as the formatting codes like &amp;1.
     * @param str The string to remove color from.
     * @return formatted string
     */
    public static String removeColor(String str) {
        for (ChatColor c : ChatColor.values()) {
            str = str.replace(c.toString(), "&" + c.getChar());
        }
        return str;
    }

    /**
     * Strips all coloring from the specified string.
     * @param str The string to remove color from.
     * @return String without any colors and without any color symbols.
     */
    public static String stripAllColor(String str) {
        return ChatColor.stripColor(colorChatColors(str));
    }



    public static String implode(Object[] arr, String glue, String lastGlue, int start, int end) {
        String ret = "";

        if (arr == null || arr.length <= 0)
            return ret;

        for (int i = start; i <= end && i < arr.length; i++) {
            if (i >= end-1 || i >= arr.length-2) {
                ret += arr[i].toString() + lastGlue;
            } else {
                ret += arr[i].toString() + glue;
            }
        }

        if (ret.trim().isEmpty()) {
            return ret;
        }
        return ret.substring(0, ret.length() - lastGlue.length());
    }

    public static String implode(Object[] arr, String glue, int start) {
        return implode(arr, glue, glue, start, arr.length - 1);
    }

    public static String implode(Object[] arr, String glue, String lastGlue) {
        return implode(arr, glue, lastGlue, 0, arr.length - 1);
    }

    public static String implode(Object[] arr, String glue) {
        return implode(arr, glue, 0);
    }

    public static String implode(Collection<?> args, String glue) {
        if (args.isEmpty())
            return "";
        return implode(args.toArray(new Object[args.size()]), glue);
    }

    public static String implode(Collection<?> args, String glue, String lastGlue) {
        if (args.isEmpty())
            return "";
        return implode(args.toArray(new Object[args.size()]), glue, lastGlue);
    }



    public static Map<String, File> getFiles(File dir, final String extension) {
        Map<String, File> names = new HashMap<String, File>();
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return names;
        }

        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith("." + extension);
            }
        });

        for (File file : files) {
            names.put(file.getName().substring(0, file.getName().length() - extension.length() - 1), file);
        }

        return names;
    }


    public static List<String> splitNewLinesList(List<String> list) {
        if (list != null && list.size() > 0) {
            List<String> loreList = new ArrayList<String>();
            List<String> listClone = list;
            for (String string : listClone) {
                loreList.addAll(Arrays.asList(string.split("\n")));
            }
            return loreList;
        }
        return list;
    }


    /**
     * Splits the specified string in sections.
     * Strings inside quotes will be placed together in sections.
     * For example 'Essence is "super awesome"' will return {"essence", "is", "super awesome"}
     * @author sk89q, desht
     * @param string The string that needs to be split.
     * @return List of strings split from the input string.
     */
    public static List<String> splitQuotedString(String string) {
        List<String> sections = new ArrayList<String>();

        Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
        Matcher regexMatcher = regex.matcher(string);

        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null) {
                sections.add(regexMatcher.group(1));
            } else if (regexMatcher.group(2) != null) {
                sections.add(regexMatcher.group(2));
            } else {
                sections.add(regexMatcher.group());
            }
        }
        return sections;
    }


    /**
     * Get a Color from a string.
     * The string can be either rrr,ggg,bbb or #hexhex or without the hashtag.
     * It can also be a name of a color preset.
     */
    public static Color getColor(String string) {
        if (string.isEmpty()) {
            return Color.WHITE;
        }
        if (string.contains("#")) {
            string = string.replace("#", "");
        }

        if (string.split(",").length > 2) {
            return getColorFromRGB(string);
        } else if (string.matches("[0-9A-Fa-f]+")) {
            return getColorFromHex(string);
        } else {
            //TODO: Return color from preset.
            return null;
        }
    }

    public static Color getColorFromHex(String string) {
        int c = 0;
        if (string.contains("#")) {
            string = string.replace("#", "");
        }
        if (string.matches("[0-9A-Fa-f]+")) {
            return Color.fromRGB(Integer.parseInt(string, 16));
        }
        return null;
    }

    public static Color getColorFromRGB(String string) {
        String[] split = string.split(",");
        if (split.length < 3) {
            return null;
        }
        Integer red = Util.getInt(split[0]);
        Integer green = Util.getInt(split[1]);
        Integer blue = Util.getInt(split[2]);
        if (red == null || green == null || blue == null) {
            return null;
        }
        return Color.fromRGB(Math.min(Math.max(red, 0), 255), Math.min(Math.max(green, 0), 255), Math.min(Math.max(blue, 0), 255));
    }


    /**
     * Convert a string like '1' to a int. Returns null if it's invalid.
     * @param str
     * @return int
     */
    public static Integer getInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
        }
        return null;
    }

    /**
     * Convert a string like '1' to a short. Returns null if it's invalid.
     * @param str
     * @return short
     */
    public static Short getShort(String str) {
        try {
            return Short.parseShort(str);
        } catch (NumberFormatException e) {
        }
        return null;
    }

    /**
     * Convert a string like '1' to a long. Returns null if it's invalid.
     * @param str
     * @return int
     */
    public static Long getLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
        }
        return null;
    }

    /**
     * Convert a string like '1.5' to a double. Returns null if it's invalid.
     * @param str
     * @return double
     */
    public static Double getDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
        }
        return null;
    }

    /**
     * Convert a string like '1.12' to a float. Returns null if it's invalid.
     * @param str
     * @return float
     */
    public static Float getFloat(String str) {
        if (str != null && str != "") {
            try {
                return Float.parseFloat(str);
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }


    /**
     * Checks if the sender has the specified permission.
     * It will check recursively at starting at the bottom component of the permission.
     * So if the input permission is 'cmdsigns.signs.create.cmd' the player needs to have any of the following permissions.
     * cmdsigns.*
     * cmdsigns.signs.*
     * cmdsigns.signs.create.*
     * cmdsigns.signs.create.cmd
     * @param sender The sender to do the permission check on. (Remember that Player is a CommandSender!)
     * @param permission The permission to check. This should be the FULL permission string not a sub permission.
     * @return true if the sender has any of the permissions and false if not.
     */
    public static boolean hasPermission(CommandSender sender, String permission) {
        permission = permission.toLowerCase().trim();
        if (sender.hasPermission(permission)) {
            return true;
        }
        String[] components = permission.split("\\.");
        String perm = "";
        for (String component : components) {
            perm += component + ".";
            if (sender.hasPermission(perm + "*")) {
                return true;
            }
        }
        return false;
    }


    public static boolean hasItems(Inventory inv, ItemStack item, int amt, boolean checkName, boolean checkDurability) {
        for (int i = 0; i <= inv.getSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack == null) {
                continue;
            }
            //Check if items match
            if (!compareItems(stack, item, checkName, checkDurability)) {
                continue;
            }

            //Remove the items
            int stackAmt = stack.getAmount();
            if (stackAmt >= amt) {
                return true;
            }
            amt -= stackAmt;
        }
        return amt <= 0;
    }


    /**
     * Remove items from a Inventory.
     * It'll check for items of the ItemStack you specified and it will try and remove the specified amt.
     * If you set checkName to true it will only remove items with the exact same name and the same goes for durability.
     * If there is a remainder what didn't get removed it will be returned.
     * @param inv The inventory to remove the items from.
     * @param item The item to remove (used to check type, durability, name etc)
     * @param amt The amount of items to try and remove.
     * @param checkName If set to true only items matching the name will be removed.
     * @param checkDurability If set to true only items matching durability will be removed.
     * @return Amount of items that didn't get removed. (remainder)
     */
    public static int removeItems(Inventory inv, ItemStack item, int amt, boolean checkName, boolean checkDurability) {
        int stackAmt;
        for (int i = 0; i <= inv.getSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack == null) {
                continue;
            }
            //Check if items match
            if (!compareItems(stack, item, checkName, checkDurability)) {
                continue;
            }

            //Remove the items
            stackAmt = stack.getAmount();
            if (stackAmt > amt) {
                stack.setAmount(stackAmt - amt);
                return 0;
            }
            inv.setItem(i, new ItemStack(Material.AIR));
            if (stackAmt == amt) {
                return 0;
            }
            amt -= stackAmt;
        }
        return amt;
    }

    /**
     * Compare 2 ItemStack's with each other.
     * If you set checkName to true it will only be called equal when both items have the same name and the same goes for durability.
     * @param stack1 ItemStack 1 to compare with stack 2.
     * @param stack2 ItemStack 2 to compare with stack 1.
     * @param checkName If set to true only items matching the name will be removed.
     * @param checkDurability If set to true only items matching durability will be removed.
     * @return
     */
    public static boolean compareItems(ItemStack stack1, ItemStack stack2, boolean checkName, boolean checkDurability) {
        if (stack1.getType() != stack2.getType()) {
            return false;
        }
        if (checkDurability && stack1.getDurability() != stack2.getDurability()) {
            return false;
        }
        if (checkName && stack2.hasItemMeta()) {
            if (!stack1.hasItemMeta() || !stack1.getItemMeta().getDisplayName().equalsIgnoreCase(stack2.getItemMeta().getDisplayName())) {
                return false;
            }
        }
        return true;
    }
}

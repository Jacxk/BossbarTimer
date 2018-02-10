package com.minestom.BarMenuCreator;

import com.minestom.BossbarTimer;
import com.minestom.DataHandler.BarsData;
import com.minestom.DataHandler.PlayerEditingData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BossbarMenuMaker {


    private static void itemBuilder(Inventory inventory, String displayName, Material material, int data, int slot, List<String> lore) {
        ItemStack itemStack = new ItemStack(material, 1, (short) data);
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> displayLore = new ArrayList<>();
        for (String addToLore : lore) {
            displayLore.add(ChatColor.translateAlternateColorCodes('&', addToLore));
        }
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7" + displayName));
        itemMeta.setLore(displayLore);
        itemMeta.addItemFlags(ItemFlag.values());
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(slot, itemStack);
    }

    public static void createMainMenu(Player player) {
        Inventory inv = Bukkit.createInventory(player, InventoryType.HOPPER, "BossbarTimer");

        itemBuilder(inv, "&a&lCreate new Bar", Material.BOOK, 0, 1, Arrays.asList("", "&7Click here to create", "&7and edit a new bar timer."));
        itemBuilder(inv, "&a&lEdit Bar", Material.BOOK, 0, 3, Arrays.asList("", "&7Click here to edit see", "&7and edit available bars."));

        player.openInventory(inv);
    }

    public static void createEditBarsMenu(Player player, BossbarTimer plugin) {
        FileConfiguration configuration = plugin.getConfig();
        Inventory inv = Bukkit.createInventory(player, 54, "Select a bar to edit");
        int slot = -1;

        for (String barKeyNames : configuration.getConfigurationSection("Bars").getKeys(false)) {
            slot++;
            if (slot == 49) {
                slot += 1;
            }
            itemBuilder(inv, "&a&l" + barKeyNames, Material.WRITTEN_BOOK, 0, slot, Arrays.asList("",
                    "&bColor: &7" + configuration.getString("Bars." + barKeyNames + ".Color"),
                    "&dStyle: &7" + configuration.getString("Bars." + barKeyNames + ".Style"),
                    "&eTime: &7" + configuration.getString("Bars." + barKeyNames + ".Time"), "",
                    "&aLeft-Click &7to edit this bar",
                    "&aShift-Left-Click &7to remove this bar"));
        }
        itemBuilder(inv, "&6&lBack", Material.BARRIER, 0, 49, Arrays.asList("", "&7Click here to go back"));
        player.openInventory(inv);
    }

    public static void createEditMenu(Player player, BossbarTimer plugin) {
        PlayerEditingData editingData = plugin.getUtilities().getEditingData(player);
        BarsData barsData = editingData.getBarsData();
        Inventory inv = Bukkit.createInventory(player, InventoryType.HOPPER, "Edit Mode");

        List<String> lore = new ArrayList<>();
        lore.add("&7Current Frames:");
        for (String cmds : barsData.getNameFrames()) {
            if (barsData.getNameFrames().isEmpty() || barsData.getNameFrames() == null) break;
            lore.add("&c- &f" + cmds.replaceAll("[\\[\\]]", ""));
        }
        lore.addAll(Arrays.asList("&ePeriod: &7" + barsData.getNamePeriod(), "", "&eLeft-Click &7to add a new frame", "&eRight-Click &7to delete the last frame", "&eShift-Left-Click &7to remove all frames", "&eShift-Right-Click &7to edit the period time"));

        itemBuilder(inv, "&a&lChange Color", Material.INK_SACK, 0, 0, Arrays.asList("&7Click here to enter the", "&7edit color mode.", "", "&eCurrent Color: &a" + barsData.getColor()));
        itemBuilder(inv, "&a&lChange Style", Material.EMPTY_MAP, 0, 1, Arrays.asList("&7Click here to enter the", "&7edit style mode.", "", "&eCurrent Style: &a" + barsData.getStyle()));
        itemBuilder(inv, "&a&lChange Display Name", Material.BOOK, 0, 2, lore);
        itemBuilder(inv, "&a&lAdvanced Settings", Material.REDSTONE_COMPARATOR, 0, 3, Arrays.asList("&7Click here to see", "&7more advanced settings.", "&7Such as time and commands"));
        itemBuilder(inv, "&a&lSave &7| &c&lCancel", Material.BARRIER, 0, 4, Arrays.asList("", "&eLeft-Click &7to save the changes.", "&eShift-Left-Click &7to cancel the changes.", "", "&7BarName: &c" + editingData.getBarKeyName()));

        player.openInventory(inv);
    }

    public static void createColorMenu(Player player) {
        Inventory inv = Bukkit.createInventory(player, 9, "Choose a color");

        itemBuilder(inv, "&a&lGreen", Material.INK_SACK, 10, 0, Arrays.asList("&7Click here to change", "&7the bar color."));
        itemBuilder(inv, "&b&lBlue", Material.INK_SACK, 12, 1, Arrays.asList("&7Click here to change", "&7the bar color."));
        itemBuilder(inv, "&d&lPink", Material.INK_SACK, 9, 2, Arrays.asList("&7Click here to change", "&7the bar color."));
        itemBuilder(inv, "&5&lPurple", Material.INK_SACK, 13, 3, Arrays.asList("&7Click here to change", "&7the bar color."));
        itemBuilder(inv, "&e&lYellow", Material.INK_SACK, 11, 4, Arrays.asList("&7Click here to change", "&7the bar color."));
        itemBuilder(inv, "&f&lWhite", Material.INK_SACK, 15, 5, Arrays.asList("&7Click here to change", "&7the bar color."));
        itemBuilder(inv, "&4&lRed", Material.INK_SACK, 1, 6, Arrays.asList("&7Click here to change", "&7the bar color."));
        itemBuilder(inv, "&6&lBack", Material.ARROW, 0, 8, Arrays.asList("&7Click here", "&7to go back."));

        player.openInventory(inv);
    }

    public static void createStyleMenu(Player player) {
        Inventory inv = Bukkit.createInventory(player, 9, "Choose a style");

        itemBuilder(inv, "&a&lSolid", Material.EMPTY_MAP, 0, 2, Arrays.asList("&7Click here to change", "&7the bar style."));
        itemBuilder(inv, "&a&lSegmented 6", Material.EMPTY_MAP, 0, 3, Arrays.asList("&7Click here to change", "&7the bar style."));
        itemBuilder(inv, "&a&lSegmented 10", Material.EMPTY_MAP, 0, 4, Arrays.asList("&7Click here to change", "&7the bar style."));
        itemBuilder(inv, "&a&lSegmented 12", Material.EMPTY_MAP, 0, 5, Arrays.asList("&7Click here to change", "&7the bar style."));
        itemBuilder(inv, "&a&lSegmented 20", Material.EMPTY_MAP, 0, 6, Arrays.asList("&7Click here to change", "&7the bar style."));
        itemBuilder(inv, "&6&lBack", Material.ARROW, 0, 8, Arrays.asList("&7Click here", "&7to go back."));

        player.openInventory(inv);
    }

    public static void createConfimMenu(Player player) {
        Inventory inv = Bukkit.createInventory(player, InventoryType.HOPPER, "Confirm...");

        itemBuilder(inv, "&a&lConfirm", Material.EMERALD_BLOCK, 0, 1, Arrays.asList("&7Click here to confirm", "&7and execute the action."));
        itemBuilder(inv, "&c&lDeny", Material.REDSTONE_BLOCK, 0, 3, Arrays.asList("&7Click here to confirm", "&7and execute the action."));

        player.openInventory(inv);
    }

    public static void createAvancedMenu(Player player, BossbarTimer plugin) {
        PlayerEditingData editingData = plugin.getUtilities().getEditingData(player);
        BarsData barsData = editingData.getBarsData();
        Inventory inv = Bukkit.createInventory(player, InventoryType.HOPPER, "Advanced Settings");

        List<String> lore = new ArrayList<>();
        lore.add("&7Current Commands:");
        for (String cmds : barsData.getCommands()) {
            if (barsData.getCommands().isEmpty() || barsData.getCommands() == null) break;
            lore.add("&c- &f" + cmds.replaceAll("[\\[\\]]", ""));
        }
        lore.addAll(Arrays.asList("", "&eLeft-Click &7to add a command.", "&eRight-Click &7to delete the last command.", "&eShift-Left-Click &7to remove all commands."));

        itemBuilder(inv, "&a&lChange Bar Timer", Material.NAME_TAG, 0, 0, Arrays.asList("&7Click here to enter the", "&7edit timer mode.", "", "&eCurrent Time: &a" + barsData.getCountdownTime()));
        itemBuilder(inv, "&a&lEdit Commands", Material.MAP, 0, 1, lore);
        itemBuilder(inv, "&a&lAnnouncerMode", Material.BLAZE_ROD, 0, 2, Arrays.asList("&7Enabled: &c" + barsData.isAnnouncerEnabled(), "&7Show Every: &c" + barsData.getAnnouncerTime(), "", "&eLeft-Click &7to toggle the", "&7Announcer mode.", "&eRight-Click &7to change the time."));
        itemBuilder(inv, "&6&lBack", Material.ARROW, 0, 4, Arrays.asList("&7Click here", "&7to go back."));

        player.openInventory(inv);
    }
}

package com.minestom.BarInterface;

import com.minestom.BossbarTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BossbarInterface {


    private static void itemBuilder(Inventory inventory, String displayName, Material material, int data, int slot, List<String> lore) {
        ItemStack itemStack = new ItemStack(material, 1, (short) data);
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> displayLore = new ArrayList<>();
        for (String addToLore : lore) {
            displayLore.add(ChatColor.translateAlternateColorCodes('&', addToLore));
        }
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        itemMeta.setLore(displayLore);
        itemMeta.addItemFlags(ItemFlag.values());
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(slot, itemStack);
    }

    public static void createMainMenu(Player player) {
        Inventory inv = Bukkit.createInventory(player, InventoryType.HOPPER, "BossbarTimer");

        itemBuilder(inv, "&a&lCreate new Bar", Material.BOOK, 0, 1, Arrays.asList("&7Click here to create", "&7and edit a new bar timer."));
        itemBuilder(inv, "&a&lEdit Bar", Material.BOOK, 0, 3, Arrays.asList("&7Click here to edit", "&7a current bar."));
        player.openInventory(inv);
    }

    public static void createEditMenu(Player player, BossbarTimer plugin) {
        Map<String, String> values = plugin.getCreateBarValues().get(plugin.getBarKeyName().get(player));
        Inventory inv = Bukkit.createInventory(player, InventoryType.HOPPER, "Edit Mode");

        itemBuilder(inv, "&a&lChange Color", Material.INK_SACK, 0, 0, Arrays.asList("&7Click here to enter the", "&7edit color mode.", "", "&eCurrent Color: &a" + values.get("Color")));
        itemBuilder(inv, "&a&lChange Style", Material.EMPTY_MAP, 0, 1, Arrays.asList("&7Click here to enter the", "&7edit style mode.", "", "&eCurrent Style: &a" + values.get("Style")));
        itemBuilder(inv, "&a&lChange Display Name", Material.BOOK, 0, 2, Arrays.asList("&7Click here to enter the", "&7edit display name mode."));
        itemBuilder(inv, "&a&lChange Bar Timer", Material.NAME_TAG, 0, 3, Arrays.asList("&7Click here to enter the", "&7edit timer mode.", "", "&eCurrent Time: " + values.get("Time")));
        itemBuilder(inv, "&e&lSave", Material.BARRIER, 0, 4, Arrays.asList("&7Click here save", "&7the current bar.", "", "&aBarName: &c" + plugin.getBarKeyName().get(player)));

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

}

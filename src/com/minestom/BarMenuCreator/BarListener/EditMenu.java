package com.minestom.BarMenuCreator.BarListener;

import com.minestom.BarMenuCreator.BossbarMenuMaker;
import com.minestom.BossbarTimer;
import com.minestom.Utils.MessageUtil;
import com.minestom.Utils.Utilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EditMenu implements Listener {

    private BossbarTimer plugin;

    public EditMenu(BossbarTimer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        Utilities utilities = plugin.getUtilities();

        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        ItemStack item = event.getCurrentItem();
        Inventory inventory = event.getClickedInventory();
        String inventoryName = event.getView().getTopInventory().getTitle();
        InventoryType.SlotType slotType = event.getSlotType();

        if (inventoryName.equals("Edit Mode") && slotType != InventoryType.SlotType.OUTSIDE
                && inventory.getType() == InventoryType.HOPPER) {
            if (item == null) {
                return;
            }
            event.setCancelled(true);
            if (slot == 0 && item.hasItemMeta()) {
                BossbarMenuMaker.createColorMenu(player);
            }
            if (slot == 1 && item.hasItemMeta()) {
                BossbarMenuMaker.createStyleMenu(player);
            }
            if (slot == 2 && item.hasItemMeta()) {
                Map<String, String> values = plugin.getCreateBarValues().get(plugin.getBarKeyName().get(player));
                List<String> lore = new ArrayList<>();

                for (String cmds : values.get("DisplayName").split(", ")) {
                    if (values.get("DisplayName").isEmpty()) continue;
                    lore.add(cmds.replaceAll("[\\[\\]]", ""));
                }
                if (event.getClick() == ClickType.LEFT) {
                    player.closeInventory();
                    plugin.removeEditing(player);
                    plugin.setEditingName(player);
                    MessageUtil.sendMessage(player, "&aEnter the next frame for the display name of the bar in the chat. Use &eCancel &ato cancel.");
                    MessageUtil.sendMessage(player, "&7TIP: You can use '{time}' to show the time left!");
                }
                if (event.getClick() == ClickType.RIGHT) {

                    if (!lore.isEmpty()) {
                        lore.remove(lore.size() - 1);
                        values.put("DisplayName", lore.toString());
                    }
                    if (lore.size() == 0) {
                        lore.clear();
                        values.put("DisplayName", "");
                    }
                    plugin.getCreateBarValues().put(plugin.getBarKeyName().get(player), values);
                    BossbarMenuMaker.createEditMenu(player, plugin);

                    utilities.setFrames(lore);
                }
                if (event.getClick() == ClickType.SHIFT_LEFT) {

                    lore.clear();
                    values.put("DisplayName", "");
                    plugin.getCreateBarValues().put(plugin.getBarKeyName().get(player), values);
                    BossbarMenuMaker.createEditMenu(player, plugin);

                    utilities.setFrames(Arrays.asList("&cExample &fText", "&fExample &cText"));

                }
                if (event.getClick() == ClickType.SHIFT_RIGHT) {
                    plugin.setEditPeriod(player);
                    MessageUtil.sendMessage(player, "&aEnter the period time of the name bar in the chat &7(time in ticks)&a. Use &eCancel &ato cancel.");
                    player.closeInventory();
                }
            }
            if (slot == 3 && item.hasItemMeta()) {
                BossbarMenuMaker.createAvancedMenu(player, plugin);
            }
            if (slot == 4 && item.hasItemMeta()) {

                if (event.getClick() == ClickType.SHIFT_LEFT) {

                    plugin.removeEditing(player);
                    plugin.setCanceling(player);
                    plugin.setConfirm(player);
                    BossbarMenuMaker.createConfimMenu(player);

                }
                if (event.getClick() == ClickType.LEFT) {

                    plugin.removeEditing(player);
                    plugin.setSaving(player);
                    plugin.setConfirm(player);
                    BossbarMenuMaker.createConfimMenu(player);

                }
            }
        }
    }
}

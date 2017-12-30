package com.minestom.BarInterface.BarListener;

import com.minestom.BarInterface.BossbarInterface;
import com.minestom.BossBarManager;
import com.minestom.BossbarTimer;
import com.minestom.Utils.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

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
                BossbarInterface.createColorMenu(player);
            }
            if (slot == 1 && item.hasItemMeta()) {
                BossbarInterface.createStyleMenu(player);
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
                    BossBarManager barManager = plugin.getBarManagerMap().get(plugin.getBarKeyName().get(player));

                    if (!lore.isEmpty()) {
                        lore.remove(lore.size() - 1);
                        values.put("DisplayName", lore.toString());
                    }
                    if (lore.size() == 0) {
                        lore.clear();
                        values.put("DisplayName", "");
                    }
                    plugin.getCreateBarValues().put(plugin.getBarKeyName().get(player), values);
                    BossbarInterface.createEditMenu(player, plugin);

                    BukkitTask task = plugin.getUtilities().animateText(lore, Long.parseLong(values.get("Period")), barManager);
                    task.cancel();
                    BukkitTask task2 = plugin.getUtilities().animateText(lore, Long.parseLong(values.get("Period")), barManager);

                }
                if (event.getClick() == ClickType.SHIFT_LEFT) {
                    BossBarManager barManager = plugin.getBarManagerMap().get(plugin.getBarKeyName().get(player));

                    lore.clear();
                    values.put("DisplayName", "");
                    plugin.getCreateBarValues().put(plugin.getBarKeyName().get(player), values);
                    BossbarInterface.createEditMenu(player, plugin);

                    BukkitTask task = plugin.getUtilities().animateText(Arrays.asList("&cExample &fText", "&fExample &cText"), Long.parseLong(values.get("Period")), barManager);
                    task.cancel();
                    BukkitTask task2 = plugin.getUtilities().animateText(Arrays.asList("&cExample &fText", "&fExample &cText"), Long.parseLong(values.get("Period")), barManager);

                }
                if (event.getClick() == ClickType.SHIFT_RIGHT) {
                    plugin.setEditPeriod(player);
                    MessageUtil.sendMessage(player, "&aEnter the period time of the name bar in the chat &7(time in ticks)&a. Use &eCancel &ato cancel.");
                    player.closeInventory();
                }
            }
            if (slot == 3 && item.hasItemMeta()) {
                BossbarInterface.createAvancedMenu(player, plugin);
            }
            if (slot == 4 && item.hasItemMeta()) {

                if (event.getClick() == ClickType.SHIFT_LEFT) {

                    plugin.removeEditing(player);
                    plugin.setCanceling(player);
                    plugin.setConfirm(player);
                    BossbarInterface.createConfimMenu(player);

                }
                if (event.getClick() == ClickType.LEFT) {

                    plugin.removeEditing(player);
                    plugin.setSaving(player);
                    plugin.setConfirm(player);
                    BossbarInterface.createConfimMenu(player);

                }
            }
        }
    }
}

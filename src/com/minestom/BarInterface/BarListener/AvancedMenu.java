package com.minestom.BarInterface.BarListener;

import com.minestom.BarInterface.BossbarInterface;
import com.minestom.BossbarTimer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AvancedMenu implements Listener {

    private BossbarTimer plugin;

    public AvancedMenu(BossbarTimer plugin) {
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
        if (inventoryName.equals("Advanced Settings") && slotType != InventoryType.SlotType.OUTSIDE
                && inventory.getType() == InventoryType.HOPPER) {
            if (item == null || !item.hasItemMeta()) {
                return;
            }
            event.setCancelled(true);
            if (slot == 0) {
                player.closeInventory();
                plugin.removeEditing(player);
                plugin.setEditTimer(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&aEnter the time of the bar in the chat. Use &eCancel &ato cancel."));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7TIP: You can use 's' for seconds, 'm' for minutes and 'h' for ours."));
            }
            if (slot == 1) {
                Map<String, String> values = plugin.getCreateBarValues().get(plugin.getBarKeyName().get(player));
                List<String> lore = new ArrayList<>();

                for (String cmds : values.get("Commands").split(", ")) {
                    if (values.get("Commands").isEmpty()) continue;
                    lore.add(cmds.replaceAll("[\\[\\]]", ""));
                }

                if (event.getClick() == ClickType.LEFT) {
                    plugin.removeEditing(player);
                    plugin.setAddingCmd(player);
                    player.closeInventory();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&aEnter the command you wish to add in the chat. Use &eCancel &ato cancel."));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7TIP: Do not use slash '/'. If the commands are 'none' delete them."));
                }
                if (event.getClick() == ClickType.RIGHT) {
                    if (!lore.isEmpty()) {
                        lore.remove(lore.size() - 1);
                        values.put("Commands", lore.toString());
                    }
                    if (lore.size() == 0) {
                        lore.clear();
                        values.put("Commands", "");
                    }
                    plugin.getCreateBarValues().put(plugin.getBarKeyName().get(player), values);
                    BossbarInterface.createAvancedMenu(player, plugin);
                }
                if (event.getClick() == ClickType.SHIFT_LEFT) {
                    lore.clear();
                    values.put("Commands", "");
                    plugin.getCreateBarValues().put(plugin.getBarKeyName().get(player), values);
                    BossbarInterface.createAvancedMenu(player, plugin);
                }

            }
            if (slot == 2) {
                if (event.getClick() == ClickType.LEFT) {
                    Map<String, String> values = plugin.getCreateBarValues().get(plugin.getBarKeyName().get(player));
                    boolean enabled = false;

                    if (values.get("AnnouncerModeEnabled") == null) {
                        enabled = false;
                    } else if (values.get("AnnouncerModeEnabled").equalsIgnoreCase("true")) {
                        enabled = true;
                    } else if (values.get("AnnouncerModeEnabled").equalsIgnoreCase("false")) {
                        enabled = false;
                    }

                    if (!enabled) {
                        values.put("AnnouncerModeEnabled", "True");
                    } else values.put("AnnouncerModeEnabled", "False");

                    BossbarInterface.createAvancedMenu(player, plugin);
                }
                if (event.getClick() == ClickType.RIGHT) {
                    plugin.setAnnouncerTime(player);
                    player.closeInventory();
                    plugin.removeEditing(player);
                }
            }
            if (slot == 4) {
                BossbarInterface.createEditMenu(player, plugin);
            }
        }
    }
}

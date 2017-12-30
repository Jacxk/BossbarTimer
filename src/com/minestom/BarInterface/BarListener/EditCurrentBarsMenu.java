package com.minestom.BarInterface.BarListener;

import com.minestom.BarInterface.BossbarInterface;
import com.minestom.BossBarManager;
import com.minestom.BossbarTimer;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EditCurrentBarsMenu implements Listener {

    private BossbarTimer plugin;

    public EditCurrentBarsMenu(BossbarTimer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {

        FileConfiguration configuration = plugin.getConfig();
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        ItemStack item = event.getCurrentItem();
        String inventoryName = event.getView().getTopInventory().getTitle();
        InventoryType.SlotType slotType = event.getSlotType();
        if (inventoryName.equals("Select a bar to edit") && slotType != InventoryType.SlotType.OUTSIDE) {
            if (item == null || !item.hasItemMeta()) {
                return;
            }
            event.setCancelled(true);
            String barKeyName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
            if (slot != 49) {
                plugin.getCreateBarValues().put(barKeyName, plugin.getBarValues());
                plugin.getBarKeyName().put(player, barKeyName);

                Map<String, String> values = plugin.getCreateBarValues().get(plugin.getBarKeyName().get(player));

                if (configuration.getString("Bars." + barKeyName + ".DisplayName") != null) {
                    values.put("DisplayName", configuration.getStringList("Bars." + barKeyName + ".DisplayName.Frames").toString());
                } else values.put("DisplayName", "[&cAnimated Text, &fAnimated Text]");
                values.put("Time", configuration.getString("Bars." + barKeyName + ".Time"));
                values.put("Color", configuration.getString("Bars." + barKeyName + ".Color"));
                values.put("Style", configuration.getString("Bars." + barKeyName + ".Style"));
                values.put("Period", configuration.getString("Bars." + barKeyName + ".DisplayName.Period"));
                values.put("AnnouncerModeEnabled", configuration.getString("Bars." + barKeyName + ".AnnouncerMode.Enabled"));
                values.put("AnnouncerModeTime", configuration.getString("Bars." + barKeyName + ".AnnouncerMode.Time"));
                if (configuration.getString("Bars." + barKeyName + ".Commands") != null) {
                    values.put("Commands", configuration.getStringList("Bars." + barKeyName + ".Commands").toString());
                } else values.put("Commands", "[say test, say testing]");

                plugin.getCreateBarValues().put(plugin.getBarKeyName().get(player), values);

                if (event.getClick() == ClickType.LEFT) {
                    BossBarManager barManager = plugin.getBarManagerMap().get(plugin.getBarKeyName().get(player));

                    barManager.createBar(configuration.getString("Bars." + barKeyName + ".DisplayName"),
                            configuration.getString("Bars." + barKeyName + ".Color"),
                            configuration.getString("Bars." + barKeyName + ".Style"));
                    plugin.getUtilities().animateText(plugin.getConfig().getStringList("Bars." + barKeyName + ".DisplayName.Frames"),
                            plugin.getConfig().getLong("Bars." + barKeyName + ".DisplayName.Period"), barManager);
                    barManager.setFinished(false);
                    barManager.addPlayer(player);

                    plugin.setEditing(player);
                    BossbarInterface.createEditMenu(player, plugin);
                }
                if (event.getClick() == ClickType.SHIFT_LEFT) {
                    plugin.setDeleting(player);
                    plugin.setConfirm(player);
                    BossbarInterface.createConfimMenu(player);
                }
            }

            if (slot == 49) {
                BossbarInterface.createMainMenu(player);
            }
        }
    }
}

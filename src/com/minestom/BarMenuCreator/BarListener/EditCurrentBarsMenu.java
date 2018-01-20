package com.minestom.BarMenuCreator.BarListener;

import com.minestom.BarMenuCreator.BossbarMenuMaker;
import com.minestom.BossBarManager;
import com.minestom.BossbarTimer;
import com.minestom.Utils.PlayerEditingData;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class EditCurrentBarsMenu implements Listener {

    private BossbarTimer plugin;

    public EditCurrentBarsMenu(BossbarTimer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        String inventoryName = event.getView().getTopInventory().getTitle();
        InventoryType.SlotType slotType = event.getSlotType();
        if (inventoryName.equals("Select a bar to edit") && slotType != InventoryType.SlotType.OUTSIDE) {

            ItemStack item = event.getCurrentItem();
            if (item == null || !item.hasItemMeta()) return;
            event.setCancelled(true);

            int slot = event.getRawSlot();
            FileConfiguration configuration = plugin.getConfig();

            Player player = (Player) event.getWhoClicked();
            String barKeyName = ChatColor.stripColor(item.getItemMeta().getDisplayName());

            if (slot != 49) {

                plugin.getUtilities().addPlayerEditing(player);

                PlayerEditingData editingData = plugin.getUtilities().getEditingData(player);
                editingData.setBarKeyName(barKeyName);

                if (configuration.getString("Bars." + barKeyName + ".DisplayName") != null) {
                    editingData.addBarValue("DisplayName", configuration.getStringList("Bars." + barKeyName + ".DisplayName.Frames").toString());
                } else editingData.addBarValue("DisplayName", "[&cAnimated Text, &fAnimated Text]");
                editingData.addBarValue("Time", configuration.getString("Bars." + barKeyName + ".Time"));
                editingData.addBarValue("Color", configuration.getString("Bars." + barKeyName + ".Color"));
                editingData.addBarValue("Style", configuration.getString("Bars." + barKeyName + ".Style"));
                editingData.addBarValue("Period", configuration.getString("Bars." + barKeyName + ".DisplayName.Period"));
                editingData.addBarValue("AnnouncerModeEnabled", configuration.getString("Bars." + barKeyName + ".AnnouncerMode.Enabled"));
                editingData.addBarValue("AnnouncerModeTime", configuration.getString("Bars." + barKeyName + ".AnnouncerMode.Time"));
                if (configuration.getString("Bars." + barKeyName + ".Commands") != null) {
                    editingData.addBarValue("Commands", configuration.getStringList("Bars." + barKeyName + ".Commands").toString());
                } else editingData.addBarValue("Commands", "[say test, say testing]");

                if (event.getClick() == ClickType.LEFT) {
                    BossBarManager barManager = plugin.getBarManagerMap().get(editingData.getBarKeyName());

                    barManager.createBar(configuration.getString("Bars." + barKeyName + ".DisplayName"),
                            configuration.getString("Bars." + barKeyName + ".Color"),
                            configuration.getString("Bars." + barKeyName + ".Style"));
                    plugin.getUtilities().setFrames(plugin.getConfig().getStringList("Bars." + barKeyName + ".DisplayName.Frames"));
                    plugin.getUtilities().setPeriod(plugin.getConfig().getLong("Bars." + barKeyName + ".DisplayName.Period"));
                    plugin.getUtilities().animateText(barManager);
                    barManager.setFinished(false);
                    barManager.addPlayer(player);

                    editingData.setEditing(true);
                    BossbarMenuMaker.createEditMenu(player, plugin);
                }
                if (event.getClick() == ClickType.SHIFT_LEFT) {
                    editingData.setDeleting(true);
                    editingData.setCanceling(true);
                    BossbarMenuMaker.createConfimMenu(player);
                }
            }

            if (slot == 49) {
                BossbarMenuMaker.createMainMenu(player);
            }
        }
    }
}
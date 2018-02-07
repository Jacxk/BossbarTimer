package com.minestom.BarMenuCreator.BarListener;

import com.minestom.BarMenuCreator.BossbarMenuMaker;
import com.minestom.BossBarManager;
import com.minestom.BossbarTimer;
import com.minestom.Utils.BarsData;
import com.minestom.Utils.PlayerEditingData;
import org.bukkit.ChatColor;
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

            Player player = (Player) event.getWhoClicked();
            String barKeyName = ChatColor.stripColor(item.getItemMeta().getDisplayName());

            if (slot != 49) {

                plugin.getUtilities().addPlayerEditing(player);

                PlayerEditingData editingData = plugin.getUtilities().getEditingData(player);
                BarsData barsData = plugin.getBarDataMap().get(barKeyName);

                if (event.getClick() == ClickType.LEFT) {
                    BossBarManager barManager = barsData.getBossBarManager();

                    barManager.createBar(barsData.getNameFrames().get(0), barsData.getColor(), barsData.getStyle());
                    plugin.getUtilities().setFrames(barsData.getNameFrames());
                    plugin.getUtilities().setPeriod(barsData.getNamePeriod());
                    plugin.getUtilities().animateText(barsData);
                    barManager.addPlayer(player);

                    editingData.setBarKeyName(barKeyName);
                    editingData.setBarsData(barsData);
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
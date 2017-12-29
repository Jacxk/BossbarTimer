package com.minestom;

import com.minestom.BarInterface.BarListener.*;
import com.minestom.Commands.BbtCommand;
import com.minestom.Commands.BbtCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BossbarTimer extends JavaPlugin {

    private Map<String, Double> timer = new HashMap<>();
    private Map<String, Double> initialTime = new HashMap<>();
    private Map<String, BossBarManager> barManagerMap = new HashMap<>();
    private Map<String, Map<String, String>> createBarValues = new HashMap<>();
    private Map<String, String> barValues = new HashMap<>();
    private Map<Player, String> barKeyName = new HashMap<>();
    public List<Player> editing = new ArrayList<>();
    private List<Player> editTimer = new ArrayList<>();
    private List<Player> editingName = new ArrayList<>();
    private List<Player> createBar = new ArrayList<>();
    private List<Player> confirm = new ArrayList<>();
    private List<Player> saving = new ArrayList<>();
    private List<Player> canceling = new ArrayList<>();
    private List<Player> deleting = new ArrayList<>();
    private List<Player> addingCmd = new ArrayList<>();
    private List<Player> announcerTime = new ArrayList<>();
    private BossBarManager barManager;
    private Utilities utilities;

    @Override
    public void onEnable() {
        if (!new File(this.getDataFolder(), "config.yml").exists()) {
            this.saveDefaultConfig();
        }

        barManager = new BossBarManager(this);
        utilities = new Utilities(this);

        BossBarManager barManager = new BossBarManager(this);

        registerListener();
        getCommand("bossbartimer").setExecutor(new BbtCommand(this));
        getCommand("bossbartimer").setTabCompleter(new BbtCompleter(this));

        BukkitTask task = new CountDown(this).runTaskTimer(this, 0L, 20L);
        for (String barName : getConfig().getConfigurationSection("Bars").getKeys(false)) {
            barManagerMap.put(barName, barManager);
            String enabled = getConfig().getString("Bars." + barName + ".AnnouncerMode.Enabled");
            if (enabled != null && enabled.equalsIgnoreCase("true")) {
                String timeFormat = getConfig().getString("Bars." + barName + ".AnnouncerMode.Time");
                utilities.formatTime(barName + "-Announcer", timeFormat);
            }
        }
    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
        if (!timer.isEmpty()) {
            for (Map.Entry<String, Double> entry : timer.entrySet()) {
                String barName = entry.getKey();
                double timeLeft = entry.getValue();
                if (!barName.contains("-Announcer")) {
                    getConfig().set("Data." + barName, timeLeft);
                    saveConfig();
                }
            }
        }
    }

    private void registerListener() {
        getServer().getPluginManager().registerEvents(new MainMenu(this), this);
        getServer().getPluginManager().registerEvents(new EditMenu(this), this);
        getServer().getPluginManager().registerEvents(new ColorsMenu(this), this);
        getServer().getPluginManager().registerEvents(new StylesMenu(this), this);
        getServer().getPluginManager().registerEvents(new ConfirmMenu(this), this);
        getServer().getPluginManager().registerEvents(new AvancedMenu(this), this);
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new NameTimeEdit(this), this);
        getServer().getPluginManager().registerEvents(new EditCurrentBarsMenu(this), this);
    }

    private void loadData() {
        ConfigurationSection data = getConfig().getConfigurationSection("Data");
        if (data.getKeys(true) != null) {
            for (String barName : data.getKeys(false)) {
                String timeFormat = getConfig().getString("Bars." + barName + ".Time");
                utilities.formatTime(barName, timeFormat);

                timer.put(barName, getConfig().getDouble("Data." + barName));
                BossBarManager bossBar = barManagerMap.get(barName);

                bossBar.setBarColor(getConfig().getString("Bars." + barName + ".Color").toUpperCase());
                bossBar.setBarStyle(getConfig().getString("Bars." + barName + ".Style").toUpperCase());
            }
        }
    }

    public Map<String, Map<String, String>> getCreateBarValues() {
        return createBarValues;
    }

    public Map<Player, String> getBarKeyName() {
        return barKeyName;
    }

    public Map<String, String> getBarValues() {
        return barValues;
    }

    public void setAddingCmd(Player player) {
        this.addingCmd.add(player);
    }

    public void setAnnouncerTime(Player player) {
        this.announcerTime.add(player);
    }

    public void setDeleting(Player player) {
        this.deleting.add(player);
    }

    public void setCanceling(Player player) {
        this.canceling.add(player);
    }

    public void setSaving(Player player) {
        this.saving.add(player);
    }

    public void setConfirm(Player player) {
        this.confirm.add(player);
    }

    public void setEditTimer(Player player) {
        this.editTimer.add(player);
    }

    public void setCreatingBar(Player player) {
        this.createBar.add(player);
    }

    public void setEditingName(Player player) {
        this.editingName.add(player);
    }

    public void setEditing(Player player) {
        this.editing.add(player);
    }

    public void removeConfirm(Player player) {
        this.confirm.remove(player);
    }

    public void removeCreatingBar(Player player) {
        this.createBar.remove(player);
    }

    public void removeEditTimer(Player player) {
        this.editTimer.remove(player);
    }

    public void removeEditingName(Player player) {
        this.editingName.remove(player);
    }

    public boolean containsConfirm(Player player) {
        return this.confirm.contains(player);
    }

    public void removeAddingCmd(Player player) {
        this.addingCmd.remove(player);
    }

    public boolean containsAddingCmd(Player player) {
        return this.addingCmd.contains(player);
    }

    public void removeAnnouncerTime(Player player) {
        this.announcerTime.remove(player);
    }

    public boolean containsAnnouncerTime(Player player) {
        return this.announcerTime.contains(player);
    }

    public void removeCanceling(Player player) {
        this.canceling.remove(player);
    }

    public boolean containsCanceling(Player player) {
        return this.canceling.contains(player);
    }

    public void removeDeleting(Player player) {
        this.deleting.remove(player);
    }

    public boolean containsDeleting(Player player) {
        return this.deleting.contains(player);
    }

    public void removeSaving(Player player) {
        this.saving.remove(player);
    }

    public boolean containsSaving(Player player) {
        return this.saving.contains(player);
    }

    public boolean containsEditingName(Player player) {
        return this.editingName.contains(player);
    }

    public boolean containsCreatingBar(Player player) {
        return this.createBar.contains(player);
    }

    public boolean containsEditTimer(Player player) {
        return this.editTimer.contains(player);
    }

    public void removeEditing(Player player) {
        this.editing.remove(player);
    }

    public BossBarManager getBarManager() {
        return barManager;
    }

    public Map<String, BossBarManager> getBarManagerMap() {
        return barManagerMap;
    }

    public Utilities getUtilities() {
        return utilities;
    }

    public Map<String, Double> getInitialTime() {
        return initialTime;
    }

    public Map<String, Double> getTimer() {
        return timer;
    }

}

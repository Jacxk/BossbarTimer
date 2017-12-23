package com.minestom;

import com.cloutteam.samjakob.gui.types.PaginatedGUI;
import com.minestom.BarInterface.BarListener.*;
import com.minestom.BarInterface.BossbarInterface;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
    private List<Player> editing = new ArrayList<>();
    private List<Player> colors = new ArrayList<>();
    private List<Player> styles = new ArrayList<>();
    private List<Player> editTimer = new ArrayList<>();
    private List<Player> editingName = new ArrayList<>();
    private List<Player> createBar = new ArrayList<>();
    private List<Player> confirm = new ArrayList<>();
    private List<Player> saving = new ArrayList<>();
    private List<Player> canceling = new ArrayList<>();
    private List<Player> deleting = new ArrayList<>();
    private List<Player> addingCmd = new ArrayList<>();
    private BossBarManager barManager;
    private Utilities utilities;

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

    public void setStyles(Player player) {
        this.styles.add(player);
    }

    public void setEditing(Player player) {
        this.editing.add(player);
    }

    public void setColors(Player player) {
        this.colors.add(player);
    }

    public void removeConfirm(Player player) {
        this.confirm.remove(player);
    }

    public void removeStyles(Player player) {
        this.styles.remove(player);
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

    public void removeColors(Player player) {
        this.colors.remove(player);
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

    private String colorMessage(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    @Override
    public void onEnable() {
        barManager = new BossBarManager(this);
        PaginatedGUI.prepare(this);
        utilities = new Utilities(this);
        BossBarManager barManager = new BossBarManager(this);
        getServer().getPluginManager().registerEvents(new MainMenu(this), this);
        getServer().getPluginManager().registerEvents(new EditMenu(this), this);
        getServer().getPluginManager().registerEvents(new ColorsMenu(this), this);
        getServer().getPluginManager().registerEvents(new StylesMenu(this), this);
        getServer().getPluginManager().registerEvents(new ConfirmMenu(this), this);
        getServer().getPluginManager().registerEvents(new AvancedMenu(this), this);
        getServer().getPluginManager().registerEvents(new NameTimeEdit(this), this);
        getServer().getPluginManager().registerEvents(new EditCurrentBarsMenu(this), this);
        BukkitTask task = new CountDown(this).runTaskTimer(this, 0L, 20L);
        if (!new File(this.getDataFolder(), "config.yml").exists()) {
            this.saveDefaultConfig();
        }
        for (String name : getConfig().getConfigurationSection("Bars").getKeys(false)) {
            barManagerMap.put(name, barManager);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (barManager.containsBar(player)) {
                barManager.removeBar(player);
            }
        }
    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("bossbartimer")) {
            if (args.length == 0) {
                Player player = (Player) sender;
                if (editing.contains(player)) {
                    BossbarInterface.createEditMenu(player, this);
                } else if (colors.contains(player)) {
                    BossbarInterface.createColorMenu(player);
                } else if (styles.contains(player)) {
                    BossbarInterface.createStyleMenu(player);
                } else BossbarInterface.createMainMenu(player);
                return true;
            }
            if (args[0].equalsIgnoreCase("help")) {
                if (args.length == 1) {
                    sender.sendMessage(colorMessage("&8----------------------------"));
                    sender.sendMessage(colorMessage("&c&l        BossBarTimer"));
                    sender.sendMessage(colorMessage(""));
                    sender.sendMessage(colorMessage("&8 - &a/bbt start <name>"));
                    sender.sendMessage(colorMessage("&8 - &a/bbt stop <name>"));
                    sender.sendMessage(colorMessage("&8 - &a/bbt create <name> <time> <color> <style> <DisplayName>"));
                    sender.sendMessage(colorMessage("&8 - &a/bbt manual <name> <time> <DisplayName>"));
                    sender.sendMessage(colorMessage("&8 - &a/bbt reload | Sort of not working"));
                    sender.sendMessage(colorMessage(""));
                    sender.sendMessage(colorMessage("&8----------------------------"));
                }
            }
            if (args[0].equalsIgnoreCase("reload")) {
                if (args.length == 1) {
                    reloadConfig();
                    sender.sendMessage(colorMessage("&cBossBarTimer > &7The configuration file has been reloaded!"));
                }
            }
            if (args[0].equalsIgnoreCase("stop")) {
                if (args.length == 1) {
                    sender.sendMessage(colorMessage("&cBossBarTimer > &7You need to specify a bar!"));
                    return true;
                }
                String barName = args[1];
                BossBarManager bossBar = barManagerMap.get(barName);
                if (!timer.containsKey(barName)) {
                    sender.sendMessage(colorMessage("&cBossBarTimer > &7That bar is not active!"));
                    return true;
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    bossBar.removeBar(player);
                }
                timer.remove(barName);
                sender.sendMessage(colorMessage("&cBossBarTimer > &7The bar timer has been stopped!"));
            }
            if (args[0].equalsIgnoreCase("create")) {
                if (args.length == 1 || args.length == 2 || args.length == 3 || args.length == 4) {
                    sender.sendMessage(colorMessage("&cBossBarTimer > &7Wrong usage! Use: &a/bbt create <name> <time> <color> <style> <DisplayName>"));
                    return true;
                }
                String barName = args[1];
                String time = args[2];
                String color = args[3];
                String style = args[4];
                StringBuilder displayName = new StringBuilder();
                for (int i = 5; i < args.length; i++) {
                    displayName.append(args[i] + " ");
                }
                getConfig().set("Bars." + barName + ".DisplayName", displayName.toString().trim());
                getConfig().set("Bars." + barName + ".Time", time);
                getConfig().set("Bars." + barName + ".Color", color.toUpperCase());
                getConfig().set("Bars." + barName + ".Style", style.toUpperCase());
                saveConfig();
                sender.sendMessage(colorMessage("&cBossBarTimer > &7A new bar timer has been created!"));
            }
            if (args[0].equalsIgnoreCase("start")) {
                if (args.length == 1) {
                    sender.sendMessage(colorMessage("&cBossBarTimer > &7You need to specify a bar!"));
                    return true;
                }
                String barName = args[1];
                if (!getConfig().isConfigurationSection("Bars." + barName)) {
                    sender.sendMessage(colorMessage("&cBossBarTimer > &7That bar is not available try another one!"));
                    return true;
                }
                if (timer.containsKey(barName)) {
                    sender.sendMessage(colorMessage("&cBossBarTimer > &7That bar is already active!"));
                    return true;
                }
                String timeFormat = getConfig().getString("Bars." + barName + ".Time");
                double time = Double.parseDouble(timeFormat.replaceAll("[a-zA-Z]", ""));
                if (timeFormat.contains("s")) {
                    timer.put(barName, time);
                } else if (timeFormat.contains("m")) {
                    time = time * 60;
                    timer.put(barName, time);
                } else if (timeFormat.contains("h")) {
                    time = time * 3600;
                    timer.put(barName, time);
                } else
                    timer.put(barName, time);
                initialTime.put(barName, time);
                BossBarManager bossBar = barManagerMap.get(barName);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    bossBar.addPlayer(player);
                }
                bossBar.setBarColor(getConfig().getString("Bars." + barName + ".Color").toUpperCase());
                bossBar.setBarStyle(getConfig().getString("Bars." + barName + ".Style").toUpperCase());
                sender.sendMessage(colorMessage("&cBossBarTimer > &7The bar &e" + barName + " &7has been started!"));
            }
        }
        return true;
    }
}

package com.minestom;

import com.minestom.BarMenuCreator.BarListener.*;
import com.minestom.Commands.BbtCommand;
import com.minestom.Commands.BbtCompleter;
import com.minestom.Updater.SpigotUpdater;
import com.minestom.Utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BossbarTimer extends JavaPlugin {

    private Map<String, Double> timer = new HashMap<>();
    private Map<String, Double> initialTime = new HashMap<>();
    private Map<String, BossBarManager> barManagerMap = new HashMap<>();
    private BossBarManager barManager;
    private Utilities utilities;

    @Override
    public void onEnable() {
        if (getServer().getVersion().contains("1.8")) {
            getLogger().info("Your server version is not compatible with the plugin!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        setupConfig();
        init();
        registerListeners();
        registerCommands();
        loadBars();
        getLogger().info("Plugin made by By_Jack with help of False!");
        getLogger().info("The plugin is now ready to use!");
        try {
            new SpigotUpdater(this, 48668, true, false);
        } catch (IOException e) {
            getLogger().info("An Error has occurred: " + e.getMessage());
        }

    }

    @Override
    public void onDisable() {
        getLogger().info("Thanks for using the plugin!");
        getLogger().info("Plugin successfully disabled");
        this.getServer().getScheduler().cancelTasks(this);
        saveBarData();
    }

    private void saveBarData() {
        if (timer.isEmpty()) return;

        for (Map.Entry<String, Double> entry : timer.entrySet()) {
            String barName = entry.getKey();
            double timeLeft = entry.getValue();
            if (!barName.contains("-Announcer")) {
                getConfig().set("Data." + barName, timeLeft);
                saveConfig();
            }
        }
    }

    private void loadBars() {
        BossBarManager barManager = new BossBarManager(this);
        BukkitTask task = new CountDown(this).runTaskTimer(this, 0L, 20L);
        for (String barName : getConfig().getConfigurationSection("Bars").getKeys(false)) {
            barManagerMap.put(barName, barManager);
            String enabled = getConfig().getString("Bars." + barName + ".AnnouncerMode.Enabled");
            if (enabled != null && enabled.equalsIgnoreCase("true")) {
                String timeFormat = getConfig().getString("Bars." + barName + ".AnnouncerMode.Time");
                utilities.formatTime(barName + "-Announcer", timeFormat);
            }
        }

        getConfig().set("Data", null);
        loadBarsData();
    }

    private void loadBarsData() {
        ConfigurationSection data = getConfig().getConfigurationSection("Data");
        if (data != null) {
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

    private void init() {
        barManager = new BossBarManager(this);
        utilities = new Utilities(this);
    }

    private void setupConfig() {
        if (!new File(this.getDataFolder(), "config.yml").exists()) {
            this.saveDefaultConfig();
        }
    }

    private void registerCommands() {
        getCommand("bossbartimer").setExecutor(new BbtCommand(this));
        getCommand("bossbartimer").setTabCompleter(new BbtCompleter(this));
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new MainMenu(this), this);
        pluginManager.registerEvents(new EditMenu(this), this);
        pluginManager.registerEvents(new ColorsMenu(this), this);
        pluginManager.registerEvents(new StylesMenu(this), this);
        pluginManager.registerEvents(new ConfirmMenu(this), this);
        pluginManager.registerEvents(new AvancedMenu(this), this);
        pluginManager.registerEvents(new JoinListener(this), this);
        pluginManager.registerEvents(new InChatEdition(this), this);
        pluginManager.registerEvents(new EditCurrentBarsMenu(this), this);
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

package com.minestom;

import com.minestom.BarMenuCreator.BarListener.*;
import com.minestom.Commands.BbtCommand;
import com.minestom.Commands.BbtCompleter;
import com.minestom.Runnables.Announcer;
import com.minestom.Runnables.CountDown;
import com.minestom.Updater.Update;
import com.minestom.DataHandler.BarsData;
import com.minestom.Utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BossbarTimer extends JavaPlugin implements Listener {

    private Map<String, Long> timer = new HashMap<>();
    private Map<String, Long> announcer = new HashMap<>();
    private Map<String, BarsData> barDataMap = new HashMap<>();
    public boolean debug = false;
    private Utilities utilities;
    private Update update;

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
        getLogger().info("Plugin made by " + this.getDescription().getAuthors().toString());
        update.sendUpdateMessage();
        getLogger().info("The plugin is now ready to use!");

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

        for (Map.Entry<String, Long> entry : timer.entrySet()) {
            String barName = entry.getKey();
            double timeLeft = entry.getValue();
            if (timeLeft != 0) {
                getConfig().set("Data." + barName, timeLeft);
                saveConfig();
            }
        }
    }

    public void loadBars() {
        this.getServer().getScheduler().cancelTasks(this);
        new CountDown(this).runTaskTimer(this, 0L, 20L);
        new Announcer(this).runTaskTimer(this, 0L, 20L);

        for (String barName : getConfig().getConfigurationSection("Bars").getKeys(false)) {
            if (barDataMap.containsKey(barName)) continue;
            barDataMap.put(barName, new BarsData(barName,
                    getConfig().getStringList("Bars." + barName + ".DisplayName.Frames"),
                    getConfig().getStringList("Bars." + barName + ".Commands"),
                    getConfig().getInt("Bars." + barName + ".DisplayName.Period"),
                    getConfig().getString("Bars." + barName + ".Time"),
                    getConfig().getString("Bars." + barName + ".Color"),
                    getConfig().getString("Bars." + barName + ".Style"),
                    getConfig().getBoolean("Bars." + barName + ".AnnouncerMode.Enabled"),
                    getConfig().getString("Bars." + barName + ".AnnouncerMode.Time"),
                    utilities.timeToSeconds(getConfig().getString("Bars." + barName + ".Time"))));

            if (barDataMap.get(barName).isAnnouncerEnabled()) {
                String timeFormat = getConfig().getString("Bars." + barName + ".AnnouncerMode.Time");
                announcer.put(barName, utilities.timeToSeconds(timeFormat));
            }
        }
        loadBarsData();
    }

    private void loadBarsData() {
        ConfigurationSection data = getConfig().getConfigurationSection("Data");
        if (data != null) {
            for (String barName : data.getKeys(false)) {
                timer.put(barName, getConfig().getLong("Data." + barName));
                BarsData barsData = barDataMap.get(barName);
                utilities.start(barsData);
            }
            getConfig().set("Data", null);
            saveConfig();
        }
    }

    private void init() {
        utilities = new Utilities(this);
        update = new Update(this, 51577); // Just a place holder
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

    public Utilities getUtilities() {
        return utilities;
    }

    public Map<String, Long> getTimer() {
        return timer;
    }

    public Map<String, Long> getAnnouncerTimer() {
        return announcer;
    }

    public Update getUpdate() {
        return update;
    }

    public Map<String, BarsData> getBarDataMap() {
        return barDataMap;
    }
}

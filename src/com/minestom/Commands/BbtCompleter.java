package com.minestom.Commands;

import com.minestom.BossbarTimer;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.*;

public class BbtCompleter implements TabCompleter {

    private static final String[] COMMANDS = {"start", "stop", "reload", "debug", "update"};
    private BossbarTimer plugin;

    public BbtCompleter(BossbarTimer plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List completions = new ArrayList();

        if (args.length == 1) {
            String partialCommand = args[0];
            List<String> commands = new ArrayList(Arrays.asList(COMMANDS));
            StringUtil.copyPartialMatches(partialCommand, commands, completions);
        }

        if (args.length == 2) {
            String bars = args[1];
            List<String> barsName = new ArrayList<>();
            if (args[0].equalsIgnoreCase("start")) {
                for (String names : plugin.getConfig().getConfigurationSection("Bars").getKeys(false)) {
                    barsName.add(names);
                }
            }
            if (args[0].equalsIgnoreCase("stop")) {
                for (Map.Entry<String, Long> entry : plugin.getTimer().entrySet()) {
                    barsName.add(entry.getKey());
                }
            }
            StringUtil.copyPartialMatches(bars, barsName, completions);
        }

        Collections.sort(completions);

        return completions;
    }
}

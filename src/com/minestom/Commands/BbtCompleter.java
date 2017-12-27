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

    private static final String[] COMMANDS = {"create", "start", "stop", "reload"};
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
                for (Map.Entry<String, Double> entry : plugin.getTimer().entrySet()) {
                    barsName.add(entry.getKey());
                }
            }
            StringUtil.copyPartialMatches(bars, barsName, completions);
        }

        if (args.length == 4) {
            String color = args[3];
            if (args[0].equalsIgnoreCase("create")) {
                List<String> colors = new ArrayList<>();
                colors.add(BarColor.RED.toString());
                colors.add(BarColor.BLUE.toString());
                colors.add(BarColor.GREEN.toString());
                colors.add(BarColor.PINK.toString());
                colors.add(BarColor.PURPLE.toString());
                colors.add(BarColor.WHITE.toString());
                colors.add(BarColor.YELLOW.toString());
                StringUtil.copyPartialMatches(color, colors, completions);
            }
        }

        if (args.length == 5) {
            String style = args[4];
            if (args[0].equalsIgnoreCase("create")) {
                List<String> styles = new ArrayList<>();
                styles.add(BarStyle.SOLID.toString());
                styles.add(BarStyle.SEGMENTED_20.toString());
                styles.add(BarStyle.SEGMENTED_6.toString());
                styles.add(BarStyle.SEGMENTED_10.toString());
                styles.add(BarStyle.SEGMENTED_12.toString());
                StringUtil.copyPartialMatches(style, styles, completions);
            }
        }

        Collections.sort(completions);

        return completions;
    }
}

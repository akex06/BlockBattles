package dev.akex.blockbattles.utils;

import dev.akex.blockbattles.BlockBattles;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class Color {
    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getPrefix(String message) {
        FileConfiguration config = BlockBattles.getInstance().getConfig();

        return translate(config.getString("prefix") + message);
    }
}

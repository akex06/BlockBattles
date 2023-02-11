package dev.akex.blockbattles.utils;

import dev.akex.blockbattles.BlockBattles;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Config {
    public static FileConfiguration getConfig(String fileName) {
        BlockBattles instance = BlockBattles.getInstance();
        File file = new File(instance.getDataFolder(), fileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            instance.saveResource(fileName, false);
        }

        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        return config;
    }

    public static ConfigurationSection getCounters() {
        return BlockBattles.getInstance().getCounters().getConfigurationSection("normal_items");
    }

    public static Location getLocation(String path) {
        FileConfiguration config = BlockBattles.getInstance().getConfig();
        BlockBattles instance = BlockBattles.getInstance();

        return new Location(
                instance.getServer().getWorld(Objects.requireNonNull(config.getString(path + ".world"))),
                config.getDouble(path + ".x"),
                config.getDouble(path + ".y"),
                config.getDouble(path + ".z"),
                (float) config.getDouble(path + ".yaw"),
                (float) config.getDouble(path + ".pitch")
        );
    }

    public static ArrayList<String> getOwnedCounters(Player player, String name) {
        ArrayList<String> possesedCounters = new ArrayList<>();
        for (Object counterName : BlockBattles.getInstance().getCounters().getList("normal_items." + name)) {
            if (player.getInventory().contains(Material.getMaterial((String) counterName))) {
                possesedCounters.add((String) counterName);
            }
        }
        return possesedCounters;
    }
}

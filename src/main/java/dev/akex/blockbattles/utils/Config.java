package dev.akex.blockbattles.utils;

import dev.akex.blockbattles.BlockBattles;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

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
}

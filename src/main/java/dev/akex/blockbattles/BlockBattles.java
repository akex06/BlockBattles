package dev.akex.blockbattles;

import dev.akex.blockbattles.commands.*;
import dev.akex.blockbattles.listeners.*;
import dev.akex.blockbattles.utils.Color;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

public final class BlockBattles extends JavaPlugin {
    public static BlockBattles instance;
    public static BlockBattles getInstance() {
        return instance;
    }

    private File customConfigFile;
    private FileConfiguration counters;

    public HashMap<Player, String> playersInBattle = new HashMap<>();
    public HashMap<String, Integer> battlePlayerAmount = new HashMap<>();
    public HashMap<String, Player> battleTurns = new HashMap<>();
    public HashMap<Player, Integer> extraTurns = new HashMap<>();
    public HashMap<Player, Integer> playerLuck = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        for (String battleName : getConfig().getConfigurationSection("battles").getKeys(false)) {
            battlePlayerAmount.put(battleName, 0);
        }

        loadEvents();
        loadCommands();

        createCountersConfig();

        System.out.println(Color.getPrefix("Plugin enabled"));
    }
    @Override
    public void onDisable() {
        saveDefaultConfig();
    }

    public FileConfiguration getCounters() {
        return this.counters;
    }

    public void createCountersConfig() {
        customConfigFile = new File(getDataFolder(), "counters.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("counters.yml", false);
        }

        counters = new YamlConfiguration();
        try {
            counters.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void loadEvents() {
        getServer().getPluginManager().registerEvents(new OnInventoryClick(), this);
        getServer().getPluginManager().registerEvents(new OnDisconnect(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new OnBlockPlace(), this);
        getServer().getPluginManager().registerEvents(new OnHangingPlace(), this);
    }

    public void loadCommands() {
        this.getCommand("battles").setExecutor(new Battles());
        this.getCommand("setspawn").setExecutor(new SetSpawn());
        this.getCommand("spawn").setExecutor(new Spawn());
        this.getCommand("leave").setExecutor(new Leave());
        this.getCommand("setbattlespawn").setExecutor(new SetBattleSpawn());
    }
}

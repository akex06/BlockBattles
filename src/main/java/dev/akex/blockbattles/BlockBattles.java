package dev.akex.blockbattles;

import dev.akex.blockbattles.commands.*;
import dev.akex.blockbattles.listeners.*;
import dev.akex.blockbattles.utils.Battle;
import dev.akex.blockbattles.utils.Color;
import dev.akex.blockbattles.utils.Config;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public final class BlockBattles extends JavaPlugin {
    public static BlockBattles instance;
    private Connection connection;
    private FileConfiguration counters;
    private FileConfiguration kits;
    public HashMap<Player, Battle> battles = new HashMap<>();
    public HashMap<Player, String> playersWaiting = new HashMap<>();
    public HashMap<Player, Integer> playerLuck = new HashMap<>();
    @Override
    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        Connection con = null;

        try {
            String jbdc = getConfig().getString("jbdc");
            con = DriverManager.getConnection(jbdc);
            Statement statement = con.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS kits (player VARCHAR(36), kit VARCHAR(32))");

            statement.close();

        } catch (SQLException e) {
            getServer().getPluginManager().disablePlugin(this);
            e.printStackTrace();
        }

        connection = con;

        loadEvents();
        loadCommands();

        counters = Config.getConfig("counters.yml");
        kits = Config.getConfig("kits.yml");
        System.out.println(Color.getPrefix("Plugin enabled"));
    }

    @Override
    public void onDisable() {
        saveDefaultConfig();

        for (Map.Entry<Player, Battle> entry : battles.entrySet()) {
            entry.getValue().removePlayers(null);
        }

        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static BlockBattles getInstance() { return instance; }
    public FileConfiguration getCounters() { return this.counters; }
    public FileConfiguration getKits() { return this.kits; }
    public Connection getConnection() { return connection; }
    public void loadEvents() {
        getServer().getPluginManager().registerEvents(new OnInventoryClick(), this);
        getServer().getPluginManager().registerEvents(new OnDisconnect(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new OnBlockPlace(), this);
        getServer().getPluginManager().registerEvents(new OnBlockBreak(), this);
        getServer().getPluginManager().registerEvents(new OnHangingPlace(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerMove(), this);
    }

    public void loadCommands() {
        this.getCommand("battles").setExecutor(new Battles());
        this.getCommand("setspawn").setExecutor(new SetSpawn());
        this.getCommand("spawn").setExecutor(new Spawn());
        this.getCommand("leave").setExecutor(new Leave());
        this.getCommand("setbattlespawn").setExecutor(new SetBattleSpawn());
        this.getCommand("counters").setExecutor(new Counters());
        this.getCommand("kits").setExecutor(new Kits());
    }
}

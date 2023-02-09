package dev.akex.blockbattles.utils;

import dev.akex.blockbattles.BlockBattles;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Data {
    public static Integer[] slots = {
            10, 11, 12, 13, 14,
            15, 16, 19, 20, 21,
            22, 23, 24, 25, 28, 29, 30, 31,
            32, 33, 34, 37, 38,
            39, 40, 41, 42, 43
    };
    public static ItemStack createItem(Material material, String name, ArrayList<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Color.translate(name));

        if (lore != null) {
            meta.setLore(lore);
        }

        item.setItemMeta(meta);

        return item;
    }

    public static BlockBattles getInstance() {
        return BlockBattles.getInstance();
    }

    public static FileConfiguration getConfig() {
        return getInstance().getConfig();
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



    public static void sendMessages(String message1, String message2, Player player) {

        Battle battle = BlockBattles.getInstance().battles.get(player);
        if (battle != null) {
            Player player2 = battle.player2;

            player.sendMessage(Color.getPrefix(message1));
            player2.sendMessage(Color.getPrefix(message2.replace("{player2}", player2.getName())));
        }
    }

    public static void changeTurns(Player player) {
        Battle battle = Data.getInstance().battles.get(player);

        if (battle == null) {
            return;
        }

        Player player2 = battle.player2;

        if (battle.extraTurns >= 1) {
            battle.extraTurns -= 1;
            sendMessages("&aYou have an extra turn", "&f" + player.getName() + " has an extra turn", player);
        } else {
            battle.turn = battle.turn == player ? player2 : player;
            sendMessages("&cIt's &f" + player2.getName() + " &cturn", "&aIt's your turn", player);
        }
    }

    public static void setSkullTexture(ItemStack item, String url) {
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        PlayerProfile profile = Bukkit.getServer().createPlayerProfile(UUID.randomUUID());

        try {
            profile.getTextures().setSkin(new URL(url));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        meta.setOwnerProfile(profile);
        item.setItemMeta(meta);
    }

    public static ConfigurationSection getCounters() {
        return BlockBattles.getInstance().getCounters().getConfigurationSection("normal_items");
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

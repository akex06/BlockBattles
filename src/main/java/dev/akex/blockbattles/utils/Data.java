package dev.akex.blockbattles.utils;

import dev.akex.blockbattles.BlockBattles;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.*;

public class Data {
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

    public static void removePlayers(Player player) {
        HashMap<Player, String> playersInBattle = Data.getInstance().playersInBattle;

        String battleName = playersInBattle.get(player);
        if (battleName != null) {
            Data.getInstance().battleTurns.remove(battleName);
            Data.getInstance().battlePlayerAmount.put(battleName, 0);
            Data.getInstance().battleTurns.remove(battleName);
            Player player2 = null;
            playersInBattle.remove(player);
            for (Map.Entry<Player, String> entry : playersInBattle.entrySet()) {
                if (entry.getValue().equals(battleName) && entry.getKey() != player) {
                    player2 = entry.getKey();
                    break;
                }
            }

            playersInBattle.remove(player2);
            player2.sendMessage(Color.getPrefix("&fYou've won the game"));
            player2.teleport(getLocation("spawn."));
        }
    }

    public static void sendMessages(String message1, String message2, Player player, String battleName) {
        for (Map.Entry<Player, String> entry : Data.getInstance().playersInBattle.entrySet()) {
            if (entry.getKey() != player && entry.getValue().equals(battleName)) {
                Player player2 = entry.getKey();

                player.sendMessage(Color.getPrefix(message1));
                player2.sendMessage(Color.getPrefix(message2.replace("{player2}", player2.getName())));
            }
        }
    }

    public static void changeTurns(Player player, String battleName) {
        HashMap<Player, String> playersInBattle = Data.getInstance().playersInBattle;
        HashMap<String, Player> battleTurns = Data.getInstance().battleTurns;
        HashMap<Player, Integer> extraTurns = Data.getInstance().extraTurns;
        Player player2 = null;
        for (Map.Entry<Player, String> entry : playersInBattle.entrySet()) {
            if (entry.getKey() != player && entry.getValue().equals(battleName)) {
                player2 = entry.getKey();
                break;
            }
        }

        if (player2 == null) {
            return;
        }

        int playerTurns = extraTurns.getOrDefault(player, 0);
        if (playerTurns >= 1) {
            extraTurns.put(player, playerTurns-1);
            sendMessages("&aYou have an extra turn", "&f" + player.getName() + " has an extra turn", player, battleName);
        } else {
            battleTurns.put(battleName, player2);
            sendMessages("&cIt's &f" + player2.getName() + " &cturn", "&aIt's your turn", player, battleName);
        }
    }
}

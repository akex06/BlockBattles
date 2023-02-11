package dev.akex.blockbattles.utils;

import dev.akex.blockbattles.BlockBattles;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Battle {
    public String battleID;
    public Player player1;
    public Player player2;
    public Location player1Location;
    public Location player2Location;
    public Player turn;
    public int extraTurns = 0;
    public BoundingBox box;
    public Material lastPlaced = null;

    public Battle(String battleName, Player firstPlayer, Player secondPlayer) {
        battleID = battleName;
        player1 = firstPlayer;
        player2 = secondPlayer;
        turn = Math.round(Math.random()) == 0 ? player1 : player2;
    }
    public static Player getOtherPlayer(Player player) {
        HashMap<Player, String> playersWaiting = BlockBattles.getInstance().playersWaiting;
        String battleName = playersWaiting.get(player);

        Player otherPlayer = null;
        for (Map.Entry<Player, String> entry : playersWaiting.entrySet()) {
            if (entry.getKey() != player && entry.getValue().equals(battleName)) {
                otherPlayer = entry.getKey();
            }
        }

        return otherPlayer;
    }

    public static String getBattleID(String battleName) {
        String[] splittedName = ChatColor.stripColor(battleName).split(" ");
        return splittedName[splittedName.length - 1];
    }

    public static int getPlayerWaitingAmount(String battleName) {
        return Collections.frequency(
                BlockBattles.getInstance().playersWaiting.values(),
                battleName
        );
    }

    public void start() {
        HashMap<Player, Battle> battles = BlockBattles.getInstance().battles;
        this.box = getBattleBox(battleID);
        battles.put(player1, this);
        battles.put(player2, this);

        this.player1Location = Config.getLocation("battles." + battleID + ".p1_spawn");
        this.player2Location = Config.getLocation("battles." + battleID + ".p2_spawn");

        if (turn == player1) {
            player1.sendMessage(Color.getPrefix("&aYou start with the game"));
            player2.sendMessage(Color.getPrefix("&f" + player1.getName() + "&a starts with the game"));
        } else {
            player2.sendMessage(Color.getPrefix("&aYou start with the game"));
            player1.sendMessage(Color.getPrefix("&f" + player2.getName() + "&a starts with the game"));
        }
    }

    public void removePlayers(Player winner) {
        HashMap<Player, Battle> battles = BlockBattles.getInstance().battles;
        if (winner == null) {
            player1.sendMessage(Color.getPrefix("&aA winner could not be determined"));
            player2.sendMessage(Color.getPrefix("&aA winner could not be determined"));
        } else {
            Player looser = winner == player1 ? player2 : player1;
            winner.sendMessage(Color.getPrefix("&aYou've won the game"));
            looser.sendMessage(Color.getPrefix("&f" + winner.getName() + " &cwon the game"));
        }


        battles.remove(player1);
        battles.remove(player2);

        removeEffects(player1);
        removeEffects(player2);

        player1.getInventory().clear();
        player2.getInventory().clear();

        Location spawnLocation = Config.getLocation("spawn.");
        player1.teleport(spawnLocation);
        player2.teleport(spawnLocation);

        FileConfiguration config = BlockBattles.getInstance().getConfig();

        BoundingBox box = getBattleBox(battleID);
        Vector max = box.getMax();
        Vector min = box.getMin();
        World world = Bukkit.getWorld(Objects.requireNonNull(config.getString("battles." + battleID + ".box.world")));
        assert world != null;
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    world.setType(x, y, z, Material.AIR);
                }
            }
        }

        for (Entity entity : world.getNearbyEntities(box)) {
            entity.remove();
        }

        System.out.println(battles);
        System.out.println(BlockBattles.getInstance().playersWaiting);
    }

    public static void changeTurns(Player player) {
        Battle battle = BlockBattles.getInstance().battles.get(player);

        if (battle == null) {
            return;
        }
        Player player2 = player == battle.player1 ?  battle.player2 : battle.player1;

        if (battle.extraTurns >= 1) {
            battle.extraTurns -= 1;
            player.sendMessage(Color.getPrefix("&aYou have an extra turn"));
            player2.sendMessage(Color.getPrefix("&f" + player.getName() + " &chas an extra turn"));

        } else {
            player.sendMessage(Color.getPrefix("&cIt's &f" + player2.getName() + " &cturn"));
            player2.sendMessage(Color.getPrefix("&aIt's your turn"));
            battle.turn = battle.turn == player ? player2 : player;
        }
    }

    public static void removeEffects(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    public static BoundingBox getBattleBox(String battleID) {
        FileConfiguration config = BlockBattles.getInstance().getConfig();
        String battlePath = "battles." + battleID;
        World world = Bukkit.getWorld(Objects.requireNonNull(config.getString(battlePath + ".box.world")));
        Location pos1 = new Location(
                world,
                config.getInt(battlePath + ".box.pos_1.x"),
                config.getInt(battlePath + ".box.pos_1.y"),
                config.getInt(battlePath + ".box.pos_1.z")
        );

        Location pos2 = new Location(
                world,
                config.getInt(battlePath + ".box.pos_2.x"),
                config.getInt(battlePath + ".box.pos_2.y"),
                config.getInt(battlePath + ".box.pos_2.z")
        );
        return BoundingBox.of(pos1, pos2);
    }
}

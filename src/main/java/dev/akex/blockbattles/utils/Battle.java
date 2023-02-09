package dev.akex.blockbattles.utils;

import dev.akex.blockbattles.BlockBattles;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Battle {
    public String battleID;
    public Player player1;
    public Player player2;
    public Player turn;
    public int extraTurns = 0;
    public Material lastPlaced = null;

    public Battle(String battleName, Player firstPlayer, Player secondPlayer) {
        battleID = battleName;
        player1 = firstPlayer;
        player2 = secondPlayer;
        turn = Math.round(Math.random()) == 0 ? player1 : player2;
    }

    private void startBattle() {
        player1.sendMessage(Color.getPrefix("&aYou start with the game"));
        player2.sendMessage(Color.getPrefix("&f" + player1.getName() + "&a starts with the game"));
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
        if (turn == player1) {
            player1.sendMessage(Color.getPrefix("&aYou start with the game"));
            player2.sendMessage(Color.getPrefix("&f" + player1.getName() + "&a starts with the game"));
        } else {
            player2.sendMessage(Color.getPrefix("&aYou start with the game"));
            player1.sendMessage(Color.getPrefix("&f" + player2.getName() + "&a starts with the game"));
        }
    }

    public static void removePlayers(Player player) {
        HashMap<Player, Battle> battles = BlockBattles.getInstance().battles;
        Battle battle = battles.get(player);

        if (battle != null) {
            Player player2 = battle.player2;
            battles.remove(player);
            battles.remove(player2);

            player.sendMessage(Color.getPrefix("&f" + player.getName() + " &cwon the game"));
            player2.sendMessage(Color.getPrefix("&aYou've won the game"));

            Location spawnLocation = Data.getLocation("spawn.");
            player.teleport(spawnLocation);
            player2.teleport(spawnLocation);
        }
    }
}

package dev.akex.blockbattles.utils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BattleManager {
    public static void startBattle(String battleName, Player player) {
        HashMap<String, Player> battleTurns = Data.getInstance().battleTurns;
        Player player2 = null;
        for (Map.Entry<Player, String> entry : Data.getInstance().playersInBattle.entrySet()) {
            if (entry.getKey() != player && entry.getValue().equals(battleName)) {
                player2 = entry.getKey();
            }
        }

        int starts = (int) Math.round(Math.random());
        if (starts == 0) {
            battleTurns.put(battleName, player);
            player.sendMessage(Color.getPrefix("&aYou start with the game"));
            player2.sendMessage(Color.getPrefix("&f" + player.getName() + "&a starts with the game"));
        } else {
            battleTurns.put(battleName, player2);
            player2.sendMessage(Color.getPrefix("&aYou start with the game"));
            player.sendMessage(Color.getPrefix("&f" + player2.getName() + "&a starts with the game"));
        }
    }
}

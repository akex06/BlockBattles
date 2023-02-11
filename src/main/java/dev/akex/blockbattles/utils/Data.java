package dev.akex.blockbattles.utils;

import dev.akex.blockbattles.BlockBattles;
import org.bukkit.entity.Player;

public class Data {
    public static void sendMessages(String message1, String message2, Player player) {

        Battle battle = BlockBattles.getInstance().battles.get(player);
        if (battle != null) {
            Player player2 = battle.player2;

            player.sendMessage(Color.getPrefix(message1));
            player2.sendMessage(Color.getPrefix(message2.replace("{player2}", player2.getName())));
        }
    }
}

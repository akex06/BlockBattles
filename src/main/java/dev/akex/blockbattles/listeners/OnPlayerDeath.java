package dev.akex.blockbattles.listeners;

import dev.akex.blockbattles.BlockBattles;
import dev.akex.blockbattles.utils.Battle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;

public class OnPlayerDeath implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        Battle battle = BlockBattles.getInstance().battles.get(player);
        HashMap<Player, String> playersWaiting = BlockBattles.getInstance().playersWaiting;
        if (battle != null) {
            Battle.removePlayers(player);
        } else if (playersWaiting.get(player) != null) {
            playersWaiting.remove(player);
        }
    }
}

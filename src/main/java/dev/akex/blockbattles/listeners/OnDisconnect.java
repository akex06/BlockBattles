package dev.akex.blockbattles.listeners;

import dev.akex.blockbattles.BlockBattles;
import dev.akex.blockbattles.utils.Battle;
import dev.akex.blockbattles.utils.Data;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class OnDisconnect implements Listener {
    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Battle battle = BlockBattles.getInstance().battles.get(player);
        if (battle != null) {
            Battle.removePlayers(player);
        } else {
            BlockBattles.getInstance().playersWaiting.remove(player);
        }
    }
}

package dev.akex.blockbattles.listeners;

import dev.akex.blockbattles.BlockBattles;
import dev.akex.blockbattles.utils.Battle;
import dev.akex.blockbattles.utils.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class OnPlayerMove implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Battle battle = BlockBattles.getInstance().battles.get(player);
        if (battle == null) { return; }
        Location playerLocation = player.getLocation();
        BoundingBox box = battle.box.clone();

        if (
                playerLocation.getBlockX() >= box.getMinX() && playerLocation.getBlockX() <= box.getMaxX()
                && playerLocation.getBlockY() >= box.getMinY() && playerLocation.getBlockY() <= box.getMaxY()
                && playerLocation.getBlockZ() >= box.getMinZ() && playerLocation.getBlockZ() <= box.getMaxZ()
        ) { return; }

        Location location = player == battle.player1 ? battle.player2Location : battle.player1Location;
        player.teleport(location);
        player.sendMessage(Color.getPrefix("&cStay inside the match area, if you want to leave use &f/leave"));
    }
}

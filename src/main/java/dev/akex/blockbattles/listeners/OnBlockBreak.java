package dev.akex.blockbattles.listeners;

import dev.akex.blockbattles.BlockBattles;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class OnBlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (BlockBattles.getInstance().playersInBattle.get(event.getPlayer()) != null) {
            event.setCancelled(true);
        }
    }
}

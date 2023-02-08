package dev.akex.blockbattles.listeners;

import dev.akex.blockbattles.utils.Color;
import dev.akex.blockbattles.utils.Data;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class OnBlockPlace implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        HashMap<Player, String> playersInBattle = Data.getInstance().playersInBattle;
        if (playersInBattle.get(player) == null) {
            return;
        }

        String battleName = playersInBattle.get(player);
        HashMap<String, Player> battleTurns = Data.getInstance().battleTurns;
        if (battleTurns.get(battleName) != player) {
            event.setCancelled(true);
            player.sendMessage(Color.getPrefix("&cIt is not your turn, please wait"));
            return;
        }

        Block block = event.getBlockPlaced();
        ItemStack item = event.getItemInHand();
        String itemName = String.valueOf(block.getType());

        if (!item.getItemMeta().hasCustomModelData()) {

        }


        Data.changeTurns(player, battleName);
    }
}

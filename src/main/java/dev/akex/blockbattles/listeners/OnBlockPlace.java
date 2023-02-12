package dev.akex.blockbattles.listeners;

import dev.akex.blockbattles.BlockBattles;
import dev.akex.blockbattles.utils.Battle;
import dev.akex.blockbattles.utils.Color;
import dev.akex.blockbattles.utils.Config;
import dev.akex.blockbattles.utils.Data;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OnBlockPlace implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        HashMap<Player, Battle> playersInBattle = BlockBattles.getInstance().battles;
        if (playersInBattle.get(player) == null) {
            return;
        }

        Battle battle = BlockBattles.getInstance().battles.get(player);
        if (battle.turn != player) {
            event.setCancelled(true);
            player.sendMessage(Color.getPrefix("&cIt is not your turn, please wait"));
            return;
        }

        Block block = event.getBlockPlaced();
        Material blockType = block.getType();
        ItemStack item = event.getItemInHand();
        String itemName = String.valueOf(block.getType());

        if (!item.getItemMeta().hasCustomModelData()) {
            Player player2 = player == battle.player1 ? battle.player2 : battle.player1;
            ConfigurationSection section = Config.getCounters();
            List<?> counterList = section.getList(itemName);

            if (battle.lastPlaced != null) {
                List<?> list = BlockBattles.getInstance().getCounters().getList("normal_items." + battle.lastPlaced);
                if (list != null) {
                    boolean hasPlacedCounter = false;
                    for (Object object : list) {
                        if (object.equals(itemName)) {
                            hasPlacedCounter = true;
                            break;
                        }
                    }

                    if (!hasPlacedCounter) {
                        player.sendMessage(Color.getPrefix("&cPlace a counter block"));
                        event.setCancelled(true);
                        return;
                    }
                }
            }

            if (blockType == Material.OAK_SAPLING) {
                block.getLocation().getBlock().setType(Material.AIR);
                block.getWorld().generateTree(block.getLocation(), TreeType.TREE);
            } else if(blockType == Material.LIGHTNING_ROD){
                block.getWorld().strikeLightning(block.getLocation());
            }

            if (counterList != null) {
                boolean hasCounter = false;
                for (Object counterItem : counterList) {
                    if (player2.getInventory().contains(Material.getMaterial((String) counterItem))) {
                        hasCounter = true;
                        break;
                    }
                }
                ArrayList<String> possesedCounters = Config.getOwnedCounters(player2, String.valueOf(item.getType()));

                if (hasCounter) {
                    battle.lastPlaced = item.getType();
                    player2.sendMessage(Color.getPrefix("&ePossible counters: " + WordUtils.capitalizeFully(String.join(",", possesedCounters).replace("_", " "))));
                } else {
                    battle.removePlayers(player);
                    return;
                }
            }
        }
        battle.changeTurns();
    }
}

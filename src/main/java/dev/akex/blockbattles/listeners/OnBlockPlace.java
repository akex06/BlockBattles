package dev.akex.blockbattles.listeners;

import dev.akex.blockbattles.BlockBattles;
import dev.akex.blockbattles.utils.Color;
import dev.akex.blockbattles.utils.Data;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

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
            for (Map.Entry<Player, String> entry : playersInBattle.entrySet()) {
                if (entry.getKey() != player && entry.getValue().equals(battleName)) {
                    Player player2 = entry.getKey();
                    ConfigurationSection section = Data.getCounters();
                    List<?> counterList = section.getList(itemName);

                    if (counterList != null) {
                        boolean hasCounter = false;
                        for (Object counterItem : counterList) {
                            if (player2.getInventory().contains(Objects.requireNonNull(Material.getMaterial((String) counterItem)))) {
                                hasCounter = true;
                                break;
                            }
                        }
                        ArrayList<String> possesedCounters = Data.getOwnedCounters(player2, String.valueOf(item.getType()));

                        if (hasCounter) {
                            player2.sendMessage(Color.getPrefix("&ePossible counters: " + WordUtils.capitalizeFully(String.join(",", possesedCounters).replace("_", " "))));
                        } else {
                            Data.sendMessages("&aYou won the game", "&f" + player.getName() + " &awon the game", player);
                            Data.removePlayers(player);
                            return;
                        }
                    }
                }
            }
        }

        Data.changeTurns(player);
    }
}

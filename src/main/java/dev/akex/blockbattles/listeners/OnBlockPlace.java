package dev.akex.blockbattles.listeners;

import dev.akex.blockbattles.BlockBattles;
import dev.akex.blockbattles.utils.Color;
import dev.akex.blockbattles.utils.Data;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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


        FileConfiguration counters = BlockBattles.getInstance().getCounters();
        Block block = event.getBlockPlaced();
        ItemStack item = event.getItemInHand();
        String itemName = String.valueOf(block.getType());

        if (!item.getItemMeta().hasCustomModelData()) {
            for (Map.Entry<Player, String> entry : playersInBattle.entrySet()) {
                if (entry.getKey() != player && entry.getValue().equals(battleName)) {
                    Player player2 = entry.getKey();
                    ConfigurationSection section = counters.getConfigurationSection("normal_items");
                    List<?> counterList = section.getList(itemName);

                    if (counterList != null) {
                        boolean hasCounter = false;
                        for (Object counterItem : counterList) {
                            if (player2.getInventory().contains(Objects.requireNonNull(Material.getMaterial((String) counterItem)))) {
                                System.out.println(counterItem);
                                hasCounter = true;
                                break;
                            }
                        }

                        if (!hasCounter) {
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

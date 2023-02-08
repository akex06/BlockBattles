package dev.akex.blockbattles.listeners;

import dev.akex.blockbattles.BlockBattles;
import dev.akex.blockbattles.utils.BattleManager;
import dev.akex.blockbattles.utils.Color;
import dev.akex.blockbattles.utils.Data;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class OnInventoryClick implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        FileConfiguration config =  Data.getConfig();
        if (event.getView().getTitle().equals(Color.translate(config.getString("inventory_title")))) {
            event.setCancelled(true);

            Player player = ((Player) event.getWhoClicked()).getPlayer();
            ItemStack item = event.getCurrentItem();

            HashMap<String, Integer> battles = Data.getInstance().battlePlayerAmount;
            HashMap<Player, String> players = Data.getInstance().playersInBattle;

            if (item.getType() == Material.GREEN_TERRACOTTA) {
                String rawName = item.getItemMeta().getDisplayName();
                String[] splittedName = ChatColor.stripColor(rawName).split(" ");
                String battleName = splittedName[splittedName.length - 1];

                int playerAmount = battles.get(battleName);
                if (playerAmount == 2) {
                    player.sendMessage(Color.getPrefix("&cThis match has already started"));
                } else {
                    playerAmount += 1;
                    battles.put(battleName, playerAmount);
                    players.put(player, battleName);
                    ItemMeta itemMeta = item.getItemMeta();
                    List<String> lore = itemMeta.getLore();
                    lore.set(0, Color.translate("&aThere are " + playerAmount + "/2 players"));
                    itemMeta.setLore(lore);
                    item.setItemMeta(itemMeta);

                    String pathLocation = playerAmount == 1 ? "battles." + battleName + "." + "p1_spawn." : "battles." + battleName + "." + "p2_spawn.";
                    Location location = Data.getLocation(pathLocation);

                    player.teleport(location);

                    if (playerAmount == 2) {
                        BattleManager.startBattle(battleName, player);
                    }
                }
            }
        }
    }
}

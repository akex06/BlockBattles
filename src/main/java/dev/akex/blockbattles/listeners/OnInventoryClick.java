package dev.akex.blockbattles.listeners;

import dev.akex.blockbattles.BlockBattles;
import dev.akex.blockbattles.utils.BattleManager;
import dev.akex.blockbattles.utils.Color;
import dev.akex.blockbattles.utils.Data;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OnInventoryClick implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        FileConfiguration config =  Data.getConfig();
        InventoryView view = event.getView();
        if (view.getTitle().equals(Color.translate(config.getString("inventory_title")))) {
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
                        Data.sendMessages("&aThe match is starting", "&aThe match is starting", player);
                    } else {
                        player.sendMessage(Color.getPrefix("&aOne more player is needed to play the game"));
                    }
                }
            }
        } else if (view.getTitle().equals(Color.translate("&fList of counters"))) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            Inventory inventory = event.getClickedInventory();
            Player player = ((Player) event.getWhoClicked()).getPlayer();

            String[] s = ChatColor.stripColor(inventory.getItem(49).getItemMeta().getDisplayName()).split(" ");
            int page = Integer.parseInt(s[s.length-1]);
            ArrayList<String> pages = new ArrayList<>(BlockBattles.getInstance().getConfig().getConfigurationSection("normal_items").getKeys(false));
            int pageAmount = pages.size()/28;
            if (item.getItemMeta().getDisplayName().equals(Color.translate("&eClick to go to the previous page")) && page > 0) {
                player.performCommand("counters " + (page-1));
            } else if(item.getItemMeta().getDisplayName().equals(Color.translate("&eClick to go to the next page")) && page < pageAmount) {
                player.performCommand("counters " + (page + 1));
            }
         }
    }
}

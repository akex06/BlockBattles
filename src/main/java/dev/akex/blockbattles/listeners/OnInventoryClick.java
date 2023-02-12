package dev.akex.blockbattles.listeners;

import dev.akex.blockbattles.BlockBattles;
import dev.akex.blockbattles.commands.Kits;
import dev.akex.blockbattles.utils.Battle;
import dev.akex.blockbattles.utils.Color;
import dev.akex.blockbattles.utils.Config;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OnInventoryClick implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        FileConfiguration config =  BlockBattles.getInstance().getConfig();
        InventoryView view = event.getView();
        if (view.getTitle().equals(Color.translate(config.getString("inventory_title")))) {
            event.setCancelled(true);

            Player player = ((Player) event.getWhoClicked()).getPlayer();
            ItemStack item = event.getCurrentItem();


            HashMap<Player, String> playersWaiting = BlockBattles.getInstance().playersWaiting;

            if (item.getType() == Material.GREEN_TERRACOTTA) {
                String battleName = Battle.getBattleID(item.getItemMeta().getDisplayName());

                int playerAmount = Battle.getPlayerWaitingAmount(battleName);
                playerAmount += 1;

                playersWaiting.put(player, battleName);
                ItemMeta itemMeta = item.getItemMeta();

                List<String> lore = itemMeta.getLore();
                lore.set(0, Color.translate("&aThere are " + playerAmount + "/2 players"));

                itemMeta.setLore(lore);
                item.setItemMeta(itemMeta);

                String pathLocation = playerAmount == 1 ? "battles." + battleName + "." + "p1_spawn." : "battles." + battleName + "." + "p2_spawn.";
                Location location = Config.getLocation(pathLocation);
                player.getInventory().clear();
                player.setGameMode(GameMode.SURVIVAL);

                player.teleport(location);

                if (playerAmount == 2) {
                    Player player2 = Battle.getOtherPlayer(player);
                    Battle battle = new Battle(battleName, player, player2);
                    battle.start();

                    playersWaiting.remove(player);
                    playersWaiting.remove(player2);

                } else {
                    player.sendMessage(Color.getPrefix("&aOne more player is needed to play the game"));
                    playersWaiting.put(player, battleName);
                }
            }
        } else if (view.getTitle().equals(Color.translate("&fList of counters"))) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            Inventory inventory = event.getClickedInventory();
            Player player = ((Player) event.getWhoClicked()).getPlayer();

            String[] s = ChatColor.stripColor(inventory.getItem(49).getItemMeta().getDisplayName()).split(" ");
            int page = Integer.parseInt(s[s.length-1]);
            List<String> items = new ArrayList<>(Config.getCounters().getKeys(false));

            int pageAmount = items.size()/28;
            if (item.getItemMeta().getDisplayName().equals(Color.translate("&eClick to go to the previous page")) && page > 0) {
                player.performCommand("counters " + (page-1));
            } else if(item.getItemMeta().getDisplayName().equals(Color.translate("&eClick to go to the next page")) && page < pageAmount) {
                player.performCommand("counters " + (page + 1));
            }
        } else if (view.getTitle().equals(Color.translate("&fKit list"))) {
            event.setCancelled(true);
            Player player = ((Player) event.getWhoClicked()).getPlayer();
            ItemStack item = event.getCurrentItem();
            String kitName = Kits.getKit(player);
            String selectedKit = ChatColor.stripColor(item.getItemMeta().getDisplayName().split(" ")[0]);
            if (!selectedKit.equals(kitName)) {
                Kits.updateKit(player, selectedKit);
                player.performCommand("kits");
            }
        }
    }
}

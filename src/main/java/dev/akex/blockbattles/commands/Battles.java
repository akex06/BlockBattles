package dev.akex.blockbattles.commands;

import dev.akex.blockbattles.BlockBattles;
import dev.akex.blockbattles.utils.Color;
import dev.akex.blockbattles.utils.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


public class Battles implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println(Color.getPrefix("&cThe console can't execute this command"));
            return true;
        }
        FileConfiguration config = BlockBattles.getInstance().getConfig();
        Inventory inventory = Bukkit.createInventory(null, 54, Color.translate(config.getString("inventory_title")));
        HashMap<Player, String> players = BlockBattles.getInstance().playersWaiting;
        Player player = ((Player) sender).getPlayer();

        if (players.get(player) != null) {
            player.sendMessage(Color.getPrefix("&cYou are already in a match, use &f/leave &c to leave the match"));
            return true;
        }

        ItemStack backgroundItem = Data.createItem(Material.BLACK_STAINED_GLASS_PANE, " ", null);
        for (int i = 0; i < 54; i++) {
            inventory.setItem(i, backgroundItem);
        }

        ConfigurationSection configBattles = config.getConfigurationSection("battles");

        int i = 0;
        for (String battleName : configBattles.getKeys(false)) {
            Material material;
            ArrayList<String> lore = new ArrayList<>();
            int playerAmount = Data.getInstance().battlePlayerAmount.get(battleName);

            lore.add(Color.translate("&aThere are " + playerAmount + "/2 players"));

            if (playerAmount == 2) {
                material = Material.RED_TERRACOTTA;
                lore.add(Color.translate("&cMatch in progress"));
            } else {
                material = Material.GREEN_TERRACOTTA;
                lore.add(Color.translate("&eClick to join!"));
            }

            ItemStack item = Data.createItem(material, Color.translate("&7Room: &f" + battleName), lore);

            inventory.setItem(Data.slots[i], item);
            i++;
        }

        player.openInventory(inventory);


        return true;
    }
}

package dev.akex.blockbattles.commands;

import dev.akex.blockbattles.utils.Color;
import dev.akex.blockbattles.utils.Data;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Kits implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println(Color.getPrefix("&cThe console can't use this command"));
            return true;
        }

        Inventory inventory = Bukkit.createInventory(null, 54, Color.translate("&7Kit list"));
        ItemStack backgroundItem = Data.createItem(Material.BLACK_STAINED_GLASS_PANE, " ", null);

        for (int i = 0; i < 54; i++) {
            inventory.setItem(i, backgroundItem);
        }

        return true;
    }
}

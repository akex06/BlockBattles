package dev.akex.blockbattles.commands;

import dev.akex.blockbattles.BlockBattles;
import dev.akex.blockbattles.utils.Color;
import dev.akex.blockbattles.utils.Inventories;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Kits implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println(Color.getPrefix("&cThe console can't use this command"));
            return true;
        }

        BlockBattles instance = BlockBattles.getInstance();

        Player player = ((Player) sender).getPlayer();
        if (instance.battles.get(player) != null || instance.playersWaiting.get(player) != null) {
            player.sendMessage(Color.getPrefix("&cYou can't use this command inside a battle"));
            return true;
        }

        Inventory inventory = Bukkit.createInventory(null, 54, Color.translate("&fKit list"));

        ItemStack backgroundItem = Inventories.createItem(Material.BLACK_STAINED_GLASS_PANE, " ", null);
        for (int i = 0; i < 54; i++) {
            inventory.setItem(i, backgroundItem);
        }

        int i = 0;
        FileConfiguration kits = instance.getKits();
        for (Object kitObject : kits.getConfigurationSection("kits.").getKeys(false)) {
            String kitName = (String) kitObject;
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Color.translate("&fThis kits contains: "));
            for (Object kitItemObject : kits.getList("kits." + kitName + ".content")) {
                lore.add(Color.translate("&e" + WordUtils.capitalizeFully(String.valueOf(kitItemObject).replace("_", " "))));
            }

            int rawPrice = kits.getInt("kits." + kitName + ".price");
            String price = rawPrice == 0 ? "Free" : String.valueOf(rawPrice);

            ItemStack item = Inventories.createItem(
                    Material.getMaterial(kits.getString("kits." + kitName + ".item")),
                    Color.translate(Color.translate("&f" + kitName + " - &bPrice: " + price)),
                    lore
            );

            inventory.setItem(Inventories.slots[i], item);

            i++;
        }

        player.openInventory(inventory);

        return true;
    }

    public static void getKit(Player player) {

    }
}

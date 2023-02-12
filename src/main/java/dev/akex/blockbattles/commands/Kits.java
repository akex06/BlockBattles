package dev.akex.blockbattles.commands;

import dev.akex.blockbattles.BlockBattles;
import dev.akex.blockbattles.utils.Color;
import dev.akex.blockbattles.utils.Inventories;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.xml.transform.Result;
import java.sql.*;
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
        String playerKit = getKit(player);
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

            if (kitName.equals(playerKit)) {
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.addEnchant(Enchantment.DURABILITY, 1, false);
                itemMeta.setDisplayName(Color.translate("&f" + kitName + " - &bSelected"));
                item.setItemMeta(itemMeta);
            }

            inventory.setItem(Inventories.slots[i], item);

            i++;
        }

        player.openInventory(inventory);

        return true;
    }
    public static String getDefaultKit() {
        return BlockBattles.getInstance().getKits().getString("default_kit");
    }

    public static String getKit(Player player) {
        String defaultKit = getDefaultKit();
        String kitName = defaultKit;
        try {
            PreparedStatement statement = BlockBattles.getInstance().getConnection().prepareStatement("SELECT kit FROM kits WHERE player = ?");
            statement.setString(1, String.valueOf(player.getUniqueId()));
            ResultSet set = statement.executeQuery();
            if (!set.isBeforeFirst()) {
                statement = BlockBattles.getInstance().getConnection().prepareStatement("INSERT INTO kits VALUES (?, ?)");
                statement.setString(1, String.valueOf(player.getUniqueId()));
                statement.setString(2, kitName);
                statement.execute();
            } else {
                while (set.next()){
                    kitName = set.getString("kit");
                }
            }

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return kitName;
    }

    public static void updateKit(Player player, String selectedKit) {
        try (final PreparedStatement statement = BlockBattles.getInstance().getConnection().prepareStatement("UPDATE kits SET kit = ? WHERE player = ?")) {
            statement.setMaxRows(1);
            statement.setString(1, selectedKit);
            statement.setString(2, String.valueOf(player.getUniqueId()));
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void giveKit(Player player) {
        FileConfiguration kits = BlockBattles.getInstance().getKits();
        String kit = getKit(player);
        int i = 0;
        for (Object object : kits.getList("kits." + kit + ".content")) {
            player.getInventory().setItem(i, Inventories.createItem(Material.getMaterial(String.valueOf(object)), null, null));

            i++;
        }
    }
}
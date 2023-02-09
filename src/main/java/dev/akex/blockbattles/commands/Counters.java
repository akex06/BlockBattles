package dev.akex.blockbattles.commands;

import dev.akex.blockbattles.BlockBattles;
import dev.akex.blockbattles.utils.Color;
import dev.akex.blockbattles.utils.Config;
import dev.akex.blockbattles.utils.Data;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.checkerframework.checker.units.qual.A;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Counters implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println(Color.getPrefix("&cThe console can't use this command"));
            return true;
        }

        int page;
        if (args.length == 0 || Integer.parseInt(args[0]) < 0) {
            page = 0;
        } else {
            try {
                page = Integer.valueOf(args[0]);
            } catch (Exception e) {
                page = 0;
            }
        }

        Inventory inventory = Bukkit.createInventory(null, 54, Color.translate("&fList of counters"));
        FileConfiguration config = BlockBattles.getInstance().getCounters();
        List<String> items = new ArrayList<>(config.getConfigurationSection("normal_items").getKeys(false));

        Player player = ((Player) sender).getPlayer();

        ItemStack backgroundItem = Data.createItem(Material.BLACK_STAINED_GLASS_PANE, " ", null);
        for (int i = 0; i < 54; i++) {
            inventory.setItem(i, backgroundItem);
        }


        int startIndex = (((int) Math.ceil((double) 1 / 2))-1) * 28;
        int endIndex;
        if ((page + 1) * 28 > items.size()) {
            System.out.println("asdS");
            endIndex = items.size();
        } else {
            endIndex = (page + 1) * 28;
        }

        int i = 0;
        System.out.println(startIndex);
        System.out.println(endIndex);
        System.out.println(items.subList(startIndex, endIndex));
        for (String materialName : items.subList(startIndex, endIndex)) {
            inventory.setItem(Data.slots[i], new ItemStack(Material.getMaterial(materialName)));

            i++;
        }

        ItemStack previousPage = Data.createItem(Material.PLAYER_HEAD, "&eClick to go to the previous page", null);
        Data.setSkullTexture(previousPage, "http://textures.minecraft.net/texture/bd69e06e5dadfd84e5f3d1c21063f2553b2fa945ee1d4d7152fdc5425bc12a9");

        ItemStack nextPage = Data.createItem(Material.PLAYER_HEAD, "&eClick to go to the next page", null);
        Data.setSkullTexture(nextPage, "https://textures.minecraft.net/texture/19bf3292e126a105b54eba713aa1b152d541a1d8938829c56364d178ed22bf");

        ItemStack currentPage = Data.createItem(Material.BLACK_STAINED_GLASS_PANE, "&eCurrent page: &f" + page, null);

        inventory.setItem(48, previousPage);
        inventory.setItem(49, currentPage);
        inventory.setItem(50, nextPage);

        player.openInventory(inventory);
        return true;
    }
}

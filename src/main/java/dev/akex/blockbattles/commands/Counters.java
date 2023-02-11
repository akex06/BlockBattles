package dev.akex.blockbattles.commands;

import dev.akex.blockbattles.BlockBattles;
import dev.akex.blockbattles.utils.Color;
import dev.akex.blockbattles.utils.Config;
import dev.akex.blockbattles.utils.Data;
import dev.akex.blockbattles.utils.Inventories;
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
import org.bukkit.inventory.meta.ItemMeta;
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

        FileConfiguration config = BlockBattles.getInstance().getCounters();
        List<String> items = new ArrayList<>(config.getConfigurationSection("normal_items").getKeys(false));
        int pages = (int) Math.ceil((double) items.size() / 28);

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

        if (page == pages) {
            return true;
        }

        Inventory inventory = Bukkit.createInventory(null, 54, Color.translate("&fList of counters"));
        Player player = ((Player) sender).getPlayer();

        ItemStack backgroundItem = Inventories.createItem(Material.BLACK_STAINED_GLASS_PANE, " ", null);
        for (int i = 0; i < 54; i++) {
            inventory.setItem(i, backgroundItem);
        }

        int startIndex = page * 28;
        int endIndex = (page + 1) * 28;
        if (endIndex > items.size()) {
            endIndex = items.size();
        }

        int i = 0;
        for (String materialName : items.subList(startIndex, endIndex)) {
            ItemStack item = new ItemStack(Material.getMaterial(materialName));
            ItemMeta meta = item.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Color.translate("&fCountered by:"));
            for (Object object : Objects.requireNonNull(BlockBattles.getInstance().getCounters().getList("normal_items." + materialName))) {
                String itemName = (String) object;
                lore.add(Color.translate("&e" + WordUtils.capitalizeFully(itemName.replace("_", " "))));
            }
            meta.setLore(lore);
            item.setItemMeta(meta);

            inventory.setItem(Inventories.slots[i], item);

            i++;
        }

        ItemStack previousPage = Inventories.createItem(Material.PLAYER_HEAD, "&eClick to go to the previous page", null);
        Inventories.setSkullTexture(previousPage, "http://textures.minecraft.net/texture/bd69e06e5dadfd84e5f3d1c21063f2553b2fa945ee1d4d7152fdc5425bc12a9");

        ItemStack nextPage = Inventories.createItem(Material.PLAYER_HEAD, "&eClick to go to the next page", null);
        Inventories.setSkullTexture(nextPage, "https://textures.minecraft.net/texture/19bf3292e126a105b54eba713aa1b152d541a1d8938829c56364d178ed22bf");

        ItemStack currentPage = Inventories.createItem(Material.BLACK_STAINED_GLASS_PANE, "&eCurrent page: &f" + page, null);

        inventory.setItem(48, previousPage);
        inventory.setItem(49, currentPage);
        inventory.setItem(50, nextPage);

        player.openInventory(inventory);
        return true;
    }
}

package dev.akex.blockbattles.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

public class Inventories {
    public static Integer[] slots = {
            10, 11, 12, 13, 14,
            15, 16, 19, 20, 21,
            22, 23, 24, 25, 28, 29, 30, 31,
            32, 33, 34, 37, 38,
            39, 40, 41, 42, 43
    };
    public static ItemStack createItem(Material material, String name, ArrayList<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Color.translate(name));

        if (lore != null) {
            meta.setLore(lore);
        }

        item.setItemMeta(meta);

        return item;
    }

    public static void setSkullTexture(ItemStack item, String url) {
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        PlayerProfile profile = Bukkit.getServer().createPlayerProfile(UUID.randomUUID());

        try {
            profile.getTextures().setSkin(new URL(url));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        meta.setOwnerProfile(profile);
        item.setItemMeta(meta);
    }
}

package dev.akex.blockbattles.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class OnArmorStandPlace implements Listener {
    @EventHandler
    public void onArmorStandPlace(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
    }
}

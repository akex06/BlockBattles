package dev.akex.blockbattles.listeners;

import dev.akex.blockbattles.utils.Color;
import dev.akex.blockbattles.utils.Data;
import org.bukkit.Art;
import org.bukkit.Particle;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class OnHangingPlace implements Listener {
    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        HashMap<Player, String> playersInBattle = Data.getInstance().playersInBattle;
        HashMap<String, Player> battleTurns = Data.getInstance().battleTurns;
        Player player = event.getPlayer();
        String battleName = Data.getInstance().playersInBattle.get(player);

        if (playersInBattle.get(player) == null || player == null) {
            return;
        }
        if (battleTurns.get(battleName) != player) {
            event.setCancelled(true);
            player.sendMessage(Color.getPrefix("&cIt is not your turn, please wait"));
            return;
        }

        Painting painting = (Painting) event.getEntity();
        Art art = painting.getArt();

            // -99 Luck
        if (art == Art.COURBET) {
            HashMap<Player, Integer> playerLuck = Data.getInstance().playerLuck;
            playerLuck.put(player, playerLuck.getOrDefault(player, 0) - 99);
            Data.sendMessages("&cYou got -99 luck", "&f" + player.getName() + " &agot -99 luck", player);

            // 99 Luck
        } else if (art == Art.CREEBET || art == Art.SEA || art == Art.WASTELAND || art == Art.POOL) {
            HashMap<Player, Integer> playerLuck = Data.getInstance().playerLuck;
            playerLuck.put(player, playerLuck.getOrDefault(player, 0) + 99);
            Data.sendMessages("&aYou got +99 luck", "&f" + player.getName() + " &cgot +99 luck", player);

            // Extra turn
        } else if (art == Art.SUNSET) {
            for (Map.Entry<Player, String> entry : playersInBattle.entrySet()) {
                if (entry.getKey() != player && entry.getValue().equals(battleName)) {
                    HashMap<Player, Integer> extraTurns = Data.getInstance().extraTurns;
                    extraTurns.put(player, extraTurns.getOrDefault(entry.getKey(), 0) + 1);
                }
            }
            Data.sendMessages("&aYou got +1 extra turn", "&f" + player.getName() + " &cgot +1 extra turn", player);

            // Extra 2 turns
        } else if (art == Art.BUST) {
            HashMap<Player, Integer> extraTurns = Data.getInstance().extraTurns;
            extraTurns.put(player, extraTurns.get(player) + 2);
            Data.sendMessages("&aYou got +2 extra turns", "&f" + player.getName() + " &cgot +2 extra turns", player);

            // Die
        } else if (art == Art.WITHER || art == Art.AZTEC) {
            Data.sendMessages("&cSorry for that, have more luck next time", "&f" + player.getName() + " &adied", player);
            player.setHealth(0);

            // Crash client
        } else if (art == Art.VOID) {
            Data.sendMessages("&cOh, that's unexpected, see you in the other world <3", "&f{player2} crashed lol", player);
            player.spawnParticle(Particle.EXPLOSION_HUGE, player.getLocation(), 2147483647);

            // Strength boost
        } else if (art == Art.FIGHTERS) {
            System.out.println("Strength boost");
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1000000, 1));
            Data.sendMessages("&aYou got strength", "&f" + player.getName() + " &cgot a strength boost", player);

            // Nether warp
        } else if (art == Art.SKELETON) {
            System.out.println("To the nether we go");

            // 20 Luck
        } else if (art == Art.AZTEC2) {
            System.out.println("20 luck");
            HashMap<Player, Integer> playerLuck = Data.getInstance().playerLuck;
            playerLuck.put(player, playerLuck.getOrDefault(player, 0) + 20);
            Data.sendMessages("&aYou got &f+20 &aluck", "&f" + player.getName() + " &cgot &f+20 &fluck", player);

            // Speed boost
        } else if (art == Art.BOMB) {
            System.out.println("Speed boost");
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 1));
            Data.sendMessages("&aYou got a speed boost", "&f" + player.getName() + " &cgot a speed boost", player);

            // Slowness debuff
        } else if (art == Art.ALBAN) {
            System.out.println("Slowness debuff");
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000, 1));
            Data.sendMessages("&cYou got a slowness debuff", "&f" + player.getName() + " &agot a slowness debuff", player);

            // All debuffs
        } else if (art == Art.KEBAB) {
            System.out.println("All debuffs");
            Data.sendMessages("&cYou got all debuffs", "&f" + player.getName() + " &agot all debuffs", player);

            // -50 Luck
        } else if (art == Art.DONKEY_KONG) {
            HashMap<Player, Integer> playerLuck = Data.getInstance().playerLuck;
            playerLuck.put(player, playerLuck.getOrDefault(player, 0) - 50);
            Data.sendMessages("&cYou got -50 luck", "&f" + player.getName() + " &agot -50 luck", player);

            // Debuff on wednesdays
        } else if (art == Art.PLANT) {
            System.out.println("-99 luck on wednesdays");
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
                Data.sendMessages(
                        "&cYou got all debuffs, wish it wasn't wednesday",
                        "&f" + player.getName() + " &agot all debuffs",
                        player
                );
            } else {
                player.sendMessage("&aYou were lucky it wasn't a wednesday");
            }

        } else {
            Data.sendMessages("&fYou got nothing", "&f" + player.getName() + " &fgot nothing", player);
        }

        Data.changeTurns(player);
    }
}

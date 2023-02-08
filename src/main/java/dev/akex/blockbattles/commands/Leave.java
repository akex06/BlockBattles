package dev.akex.blockbattles.commands;

import dev.akex.blockbattles.utils.Color;
import dev.akex.blockbattles.utils.Data;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Leave implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println(Color.getPrefix("&cThe console can't use this command"));
            return true;
        }

        Player player = ((Player) sender).getPlayer();
        HashMap<Player, String> players = Data.getInstance().playersInBattle;
        HashMap<String, Integer> battles = Data.getInstance().battlePlayerAmount;

        if (players.get(player) != null) {
            String battleName = players.get(player);
            Location location = Data.getLocation("spawn.");

            if (battles.get(battleName) == 2) {
                Data.removePlayers(player);
                player.teleport(location);
            } else {
                battles.put(players.get(player), battles.get(players.get(player)) - 1);
                players.remove(player);

                player.teleport(location);
            }
        } else {
            player.sendMessage(Color.getPrefix("&cYou can only use this command in a match"));
        }

        return true;
    }
}

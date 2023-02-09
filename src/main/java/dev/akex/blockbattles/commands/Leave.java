package dev.akex.blockbattles.commands;

import dev.akex.blockbattles.utils.Battle;
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
        HashMap<Player, Battle> battles = Data.getInstance().battles;
        HashMap<Player, String> playersWaiting = Data.getInstance().playersWaiting;
        Battle battle = battles.get(player);
        Location spawnLocation = Data.getLocation("spawn.");

        if (battle == null) {
            if (playersWaiting.get(player) != null) {
                playersWaiting.remove(player);
                player.teleport(spawnLocation);
            } else {
                player.sendMessage(Color.getPrefix("&cYou can only use this command in a match"));
            }
        } else {
            Battle.removePlayers(player);
        }

        return true;
    }
}

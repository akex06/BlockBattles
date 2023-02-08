package dev.akex.blockbattles.commands;

import dev.akex.blockbattles.utils.Color;
import dev.akex.blockbattles.utils.Data;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SetBattleSpawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println(Color.translate("&cThe console can't use this command"));
            return true;
        }

        Player player = ((Player) sender).getPlayer();

        if (args.length < 2) {
            player.sendMessage(Color.getPrefix("&cInvalid sintax: Use &f/setbattlespawn battleID[n] spawn[1/2]"));
            return false;
        }

        String battleName = args[0];
        String spawn = args[1] == "1" ? "p1_spawn" : "p2_spawn";

        Location location = player.getLocation();
        FileConfiguration config = Data.getConfig();

        config.set("battles." + battleName + "." + spawn + ".x", location.getX());
        config.set("battles." + battleName + "." + spawn + ".y", location.getY());
        config.set("battles." + battleName + "." + spawn + ".z", location.getZ());
        config.set("battles." + battleName + "." + spawn + ".yaw", location.getYaw());
        config.set("battles." + battleName + "." + spawn + ".pitch", location.getPitch());
        config.set("battles." + battleName + "." + spawn + ".world", location.getWorld().getName());

        Data.getInstance().saveConfig();
        return true;
    }
}

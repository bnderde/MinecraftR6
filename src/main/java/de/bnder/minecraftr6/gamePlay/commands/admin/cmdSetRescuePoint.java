package de.bnder.minecraftr6.gamePlay.commands.admin;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.minecraftr6.main.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class cmdSetRescuePoint implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.isOp()) {
                if (args.length == 1) {
                    Location pLoc = p.getLocation();
                    String gameName = args[0];

                    Main.gamesC.set("Game" + "." + gameName + ".rescue" + ".world", pLoc.getWorld().getUID().toString());
                    Main.gamesC.set("Game" + "." + gameName + ".rescue" + ".x", pLoc.getX());
                    Main.gamesC.set("Game" + "." + gameName + ".rescue" + ".y", pLoc.getY());
                    Main.gamesC.set("Game" + "." + gameName + ".rescue" + ".z", pLoc.getZ());
                    Main.gamesC.set("Game" + "." + gameName + ".rescue" + ".yaw", pLoc.getYaw());
                    Main.gamesC.set("Game" + "." + gameName + ".rescue" + ".pitch", pLoc.getPitch());

                    try {
                        Main.gamesC.save(Main.gamesFile);
                        p.sendMessage(Main.prefix + " §aRescue-Zone für §2" + gameName + " §agesetzt.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return false;
    }
}

package de.bnder.rainbowCraft.gamePlay.commands.admin;

/*
* Copyright (C) 2019 Jan Brinkmann
        *
        * Licensed under the Apache License, Version 2.0 (the "License");
        * you may not use this file except in compliance with the License.
        * You may obtain a copy of the License at
        *
        *      http://www.apache.org/licenses/LICENSE-2.0
        *
        * Unless required by applicable law or agreed to in writing, software
        * distributed under the License is distributed on an "AS IS" BASIS,
        * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        * See the License for the specific language governing permissions and
        * limitations under the License.
        */

import de.bnder.rainbowCraft.main.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class cmdSetHostageSpawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.isOp()) {
                if (args.length == 1) {
                    Location pLoc = p.getLocation();
                    String gameName = args[0];

                    Main.gamesC.set("Game" + "." + gameName + ".geisel" + ".world", pLoc.getWorld().getUID().toString());
                    Main.gamesC.set("Game" + "." + gameName + ".geisel" + ".x", pLoc.getX());
                    Main.gamesC.set("Game" + "." + gameName + ".geisel" + ".y", pLoc.getY());
                    Main.gamesC.set("Game" + "." + gameName + ".geisel" + ".z", pLoc.getZ());
                    Main.gamesC.set("Game" + "." + gameName + ".geisel" + ".yaw", pLoc.getYaw());
                    Main.gamesC.set("Game" + "." + gameName + ".geisel" + ".pitch", pLoc.getPitch());

                    try {
                        Main.gamesC.save(Main.gamesFile);
                        p.sendMessage(Main.prefix + " §aGeisel für §2" + gameName + " §agesetzt.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
}

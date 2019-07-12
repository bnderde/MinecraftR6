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
import java.util.ArrayList;

public class cmdSetLobby implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.isOp()) {
                if (args.length == 1) {
                    Location pLoc = p.getLocation();
                    String gameName = args[0];

                    Main.gamesC.set("Game" + "." + gameName + ".lobby" + ".world", pLoc.getWorld().getUID().toString());
                    Main.gamesC.set("Game" + "." + gameName + ".lobby" + ".x", pLoc.getX());
                    Main.gamesC.set("Game" + "." + gameName + ".lobby" + ".y", pLoc.getY());
                    Main.gamesC.set("Game" + "." + gameName + ".lobby" + ".z", pLoc.getZ());
                    Main.gamesC.set("Game" + "." + gameName + ".lobby" + ".yaw", pLoc.getYaw());
                    Main.gamesC.set("Game" + "." + gameName + ".lobby" + ".pitch", pLoc.getPitch());

                    if (!Main.gamesC.getStringList("GamesList").contains(gameName)) {
                        ArrayList<String> list = new ArrayList<String>();
                        list.addAll(Main.gamesC.getStringList("GamesList"));
                        list.add(gameName);
                        Main.gamesC.set("GamesList", list);
                    }

                    try {
                        Main.gamesC.save(Main.gamesFile);
                        p.sendMessage(Main.prefix + " §aLobby für §2" + gameName + " §agesetzt.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return false;
    }
}

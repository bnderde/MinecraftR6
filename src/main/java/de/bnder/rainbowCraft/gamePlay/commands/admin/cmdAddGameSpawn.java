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
import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;

public class cmdAddGameSpawn implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.isOp()) {
                if (args.length == 2) {
                    String map = args[0];
                    GameUtils gameUtils = new GameUtils(map);
                    if (gameUtils.gameExists()) {
                        String side = args[1];
                        if (side.equalsIgnoreCase("ct") || side.equalsIgnoreCase("t")) {
                            side = side.toLowerCase();
                            Location pLoc = p.getLocation();
                            ArrayList<String> spawnsList = new ArrayList<String>();
                            spawnsList.addAll(Main.gamesC.getStringList("Game" + "." + map + ".spawnList" + "." + side.toLowerCase()));
                            String spawn = String.valueOf(spawnsList.size());
                            spawnsList.add(spawn);
                            Main.gamesC.set("Game" + "." + map + ".spawnList" + "." + side.toLowerCase(), spawnsList);
                            Main.gamesC.set("Game" + "." + map + ".spawn" + "." + side.toLowerCase() + "." + spawn + ".world", pLoc.getWorld().getUID().toString());
                            Main.gamesC.set("Game" + "." + map + ".spawn" + "." + side.toLowerCase() + "." + spawn + ".x", pLoc.getX());
                            Main.gamesC.set("Game" + "." + map + ".spawn" + "." + side.toLowerCase() + "." + spawn + ".y", pLoc.getY());
                            Main.gamesC.set("Game" + "." + map + ".spawn" + "." + side.toLowerCase() + "." + spawn + ".z", pLoc.getZ());
                            Main.gamesC.set("Game" + "." + map + ".spawn" + "." + side.toLowerCase() + "." + spawn + ".yaw", pLoc.getYaw());
                            Main.gamesC.set("Game" + "." + map + ".spawn" + "." + side.toLowerCase() + "." + spawn + ".pitch", pLoc.getPitch());
                            try {
                                Main.gamesC.save(Main.gamesFile);
                                p.sendMessage(Main.prefix + " §2Spawn §a" + spawn + " §2erstellt.");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            p.sendMessage(Main.prefix +  " §cGültige Seiten sind: \"CT\" & \"T\"!");
                        }
                    } else {
                        p.sendMessage(Main.prefix + " §cDiese Map exisitert nicht!");
                    }
                } else {
                    p.sendMessage(Main.prefix + " §cBitte gib einen Mapnamen und CT/T an!");
                }
            }
        }
        return false;
    }
}

package de.bnder.rainbowCraft.buildMode.buildBase;

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

import de.bnder.rainbowCraft.buildMode.buildUtils.BuilderUtils;
import de.bnder.rainbowCraft.main.Main;
import de.bnder.rainbowCraft.utils.Connection;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LeaveBuildMode {

    public static void leave(Player p) {
        BuilderUtils builderUtils = new BuilderUtils(p);
        if (builderUtils.isBuilding()) {
            if (p.getWorld().getName().startsWith(BuilderUtils.buildMapPrefix)) {
                p.getInventory().clear();
                p.setGameMode(GameMode.SURVIVAL);
                p.performCommand("tptospawn");

                builderUtils.setBuilding(false);

                try {
                    ResultSet mapResult = Connection.mainConnection().prepareStatement("SELECT * FROM `MCR6_Player_Maps_General` WHERE `owner`='" + p.getUniqueId().toString() + "'").executeQuery();
                    if (mapResult.next()) {
                        String id = mapResult.getString("mapID");

                        if (Bukkit.getWorld(BuilderUtils.buildMapPrefix + id) != null) {
                            World buildWorld = Bukkit.getWorld(BuilderUtils.buildMapPrefix + id);
                            Bukkit.unloadWorld(buildWorld, true);
                            new File(BuilderUtils.buildMapPrefix + id).delete();
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv remove " + BuilderUtils.buildMapPrefix + id);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                p.sendMessage(Main.prefix + " §aDu hast den Bau-Modus verlassen.");
            } else {
                p.sendMessage(Main.prefix + " §cDu bist in keiner Bau-Welt!");
            }
        } else {
            p.sendMessage(Main.prefix + " §cDu bist nicht in dem Bau-Modus!");
        }
    }

}

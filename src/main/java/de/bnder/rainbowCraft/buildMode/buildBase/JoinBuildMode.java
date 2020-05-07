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
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JoinBuildMode {

    public static void join(Player p, String mapID) {
        BuilderUtils builderUtils = new BuilderUtils(p);
        try {
            if (!builderUtils.mapExists(mapID)) {
                Connection.mainConnection().prepareStatement("INSERT INTO `MCR6_Player_Maps_General`(`owner`, `mapName`, `mapID`) VALUES ('" + p.getUniqueId().toString() + "','Unbenannt','" + mapID + "')").executeUpdate();
            }

            //Bukkit.getBossBar(NamespacedKey.minecraft("lobby_bar")).removePlayer(p);
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

            p.getInventory().clear();

            String mapName = BuilderUtils.buildMapPrefix + mapID;

            ResultSet mapResult = Connection.mainConnection().prepareStatement("SELECT * FROM `MCR6_Player_Maps_General` WHERE `mapID`='" + mapID + "'").executeQuery();

            p.sendMessage(Main.prefix + " §7Deine eigene Bau-Welt wird erstellt... Dieser Prozess kann bis zu eine Minute in Anspruch nehmen.");

            if (Bukkit.getWorld("flatWorld") == null) {
                Bukkit.createWorld(new WorldCreator("flatWorld").type(WorldType.FLAT).generateStructures(false));
            }

            World buildWorld = Bukkit.createWorld(new WorldCreator(mapName).copy(Bukkit.getWorld("flatWorld")).generateStructures(false).type(WorldType.FLAT));

            Location spawnLoc = buildWorld.getSpawnLocation();
            if (mapResult.next()) {
                if (mapResult.getString("buildSpawnX") != null) {
                    spawnLoc.setX(mapResult.getDouble("buildSpawnX"));
                } else {
                    Connection.mainConnection().prepareStatement("UPDATE `MCR6_Player_Maps_General` SET `buildSpawnX`=" + spawnLoc.getX()).executeUpdate();
                }
                if (mapResult.getString("buildSpawnY") != null) {
                    spawnLoc.setY(mapResult.getDouble("buildSpawnY"));
                } else {
                    Connection.mainConnection().prepareStatement("UPDATE `MCR6_Player_Maps_General` SET `buildSpawnY`=" + spawnLoc.getY()).executeUpdate();
                }
                if (mapResult.getString("buildSpawnZ") != null) {
                    spawnLoc.setZ(mapResult.getDouble("buildSpawnZ"));
                } else {
                    Connection.mainConnection().prepareStatement("UPDATE `MCR6_Player_Maps_General` SET `buildSpawnZ`=" + spawnLoc.getZ()).executeUpdate();
                }
            }

            buildWorld.setSpawnLocation(spawnLoc);

            builderUtils.setBuilding(true);

            p.teleport(spawnLoc);

            ModifyWorld.modify(mapID, true);

            giveItems(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void giveItems(Player p) {
        ItemStack extrasItem = new ItemStack(Material.NETHER_STAR);
        ItemMeta extrasItemMeta = extrasItem.getItemMeta();
        extrasItemMeta.setDisplayName("§5Extras");
        extrasItem.setItemMeta(extrasItemMeta);

        p.getInventory().setItem(8, extrasItem);
        p.setGameMode(GameMode.CREATIVE);
    }

}

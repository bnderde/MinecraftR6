package de.bnder.rainbowCraft.buildMode.listeners;

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
import de.bnder.rainbowCraft.utils.Connection;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BlockBreak implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        BuilderUtils builderUtils = new BuilderUtils(p);
        if (p.getWorld().getName().startsWith(BuilderUtils.buildMapPrefix)) {
            if (builderUtils.isBuilding()) {
                String mapID = builderUtils.getMapID();

                String block = Material.AIR.name();
                String blockData = Material.AIR.createBlockData().getAsString();
                int x = e.getBlock().getX();
                int y = e.getBlock().getY();
                int z = e.getBlock().getZ();

                try {
                    ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `block` FROM `MCR6_Map_Blocks` WHERE `x`=" + x + " && `y`=" + y + " && `z`=" + z + " && `mapID`='" + mapID + "'").executeQuery();
                    if (rs.next()) {
//                        Connection.mainConnection().prepareStatement("UPDATE `MCR6_Map_Blocks` SET `block`='" + block + "', `blockData`='" + blockData + "' WHERE `x`=" + x + " && `y`=" + y + " && `z`=" + z + " && `mapID`='" + mapID + "'").executeUpdate();
                        Connection.mainConnection().prepareStatement("DELETE FROM `MCR6_Map_Blocks` WHERE `x`=" + x + " && `y`=" + y + " && `z`=" + z + " && `mapID`='" + mapID + "'").executeUpdate();
                    } else {
                        Connection.mainConnection().prepareStatement("INSERT INTO `MCR6_Map_Blocks`(`mapID`, `x`, `y`, `z`, `block`, `blockData`) VALUES ('" + mapID + "'," + x + "," + y + "," + z + ",'" + block + "','" + blockData + "')").executeUpdate();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}

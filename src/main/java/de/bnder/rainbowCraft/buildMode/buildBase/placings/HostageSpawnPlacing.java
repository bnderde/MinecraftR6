package de.bnder.rainbowCraft.buildMode.buildBase.placings;

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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HostageSpawnPlacing implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            BuilderUtils builderUtils = new BuilderUtils(p);
            if (builderUtils.isBuilding()) {
                if (p.getInventory().getItemInMainHand() != null) {
                    ItemStack item = p.getInventory().getItemInMainHand();
                    if (item.getType() == Material.EMERALD) {
                        String itemName = item.getItemMeta().getDisplayName();
                        if (itemName.equalsIgnoreCase("§aGeisel-Spawn")) {
                            e.setCancelled(true);
                            String mapID = builderUtils.getMapID();
                            try {
                                ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `mapID` FROM `MCR6_Map_Spawns` WHERE `mapID`='" + mapID + "' && `side`='hostage'").executeQuery();
                                int i = 0;
                                while (rs.next()) {
                                    i++;
                                }
                                if (i < 1) {
                                    Location loc = p.getLocation();
                                    ArmorStand camera = (ArmorStand) p.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
                                    camera.setHelmet(new ItemStack(Material.EMERALD_BLOCK));
                                    camera.setCustomName("Geisel Spawn");
                                    camera.setSmall(true);
                                    camera.setBasePlate(false);
                                    camera.setCollidable(false);
                                    camera.setGravity(false);
                                    camera.setCustomNameVisible(true);

                                    Connection.mainConnection().prepareStatement("INSERT INTO `MCR6_Map_Spawns`(`mapID`, `side`, `x`, `y`, `z`, `yaw`, `pitch`) VALUES ('" + mapID + "','hostage'," + Main.RoundTo2Decimals(loc.getX()) + "," + Main.RoundTo2Decimals(loc.getY()) + "," + Main.RoundTo2Decimals(loc.getZ()) + "," + (int) loc.getYaw() + "," + (int) loc.getPitch() + ")").executeUpdate();
                                } else {
                                    p.sendMessage(Main.prefix + " §cDu kannst nur maximal einen Spawn für die Geisel setzen!");
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}

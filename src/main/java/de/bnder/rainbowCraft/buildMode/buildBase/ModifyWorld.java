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
import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import de.bnder.rainbowCraft.utils.Connection;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;

public class ModifyWorld {

    public static void modify(String mapID, boolean build) {
        String mapName = BuilderUtils.buildMapPrefix + mapID;
        if (!build) {
            mapName = GameUtils.mapPrefix + mapID;
        }
        World world = Bukkit.getWorld(mapName);
        if (world != null) {
            world.setStorm(false);
            world.setGameRule(GameRule.SPAWN_RADIUS, 0);

            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_ENTITY_DROPS, false);
            world.setGameRule(GameRule.DO_TILE_DROPS, false);
            world.setGameRule(GameRule.DO_FIRE_TICK, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, false);
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.MOB_GRIEFING, false);
            world.setGameRule(GameRule.DO_MOB_LOOT, false);
            try {
                ResultSet rsBlocks = Connection.mainConnection().prepareStatement("SELECT * FROM `MCR6_Map_Blocks` WHERE `mapID`='" + mapID + "'").executeQuery();
                while (rsBlocks.next()) {
                    int x = rsBlocks.getInt("x");
                    int y = rsBlocks.getInt("y");
                    int z = rsBlocks.getInt("z");
                    String blockType = rsBlocks.getString("block");
                    String blockData = rsBlocks.getString("blockData");

                    Block block = world.getBlockAt(x, y, z);
                    block.setType(Material.valueOf(blockType));
                    block.setBlockData(Bukkit.createBlockData(blockData));
                }

                //Set Cams
                ResultSet rsCams = Connection.mainConnection().prepareStatement("SELECT * FROM `MCR6_Map_Cameras` WHERE `mapID`='" + mapID + "'").executeQuery();
                while (rsCams.next()) {
                    double x = rsCams.getDouble("x");
                    double y = rsCams.getDouble("y");
                    double z = rsCams.getDouble("z");
                    int yaw = rsCams.getInt("yaw");
                    int pitch = rsCams.getInt("pitch");

                    Location loc = new Location(world, x, y, z);
                    loc.setYaw(yaw);
                    loc.setPitch(pitch);
                    ArmorStand camera = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
                    camera.setHelmet(new ItemStack(Material.FURNACE));
                    camera.setSmall(true);
                    camera.setBasePlate(false);
                    camera.setCollidable(false);
                    camera.setGravity(false);
                    if (!build) {
                        camera.setVisible(false);
                    } else {
                        camera.setCustomName("Kamera");
                        camera.setCustomNameVisible(true);
                    }
                }

                if (build) {
                    //Set Spawns
                    world.setPVP(false);
                    ResultSet rsSpawns = Connection.mainConnection().prepareStatement("SELECT * FROM `MCR6_Map_Spawns` WHERE `mapID`='" + mapID + "'").executeQuery();
                    while (rsSpawns.next()) {
                        String side = rsSpawns.getString("side");
                        double x = rsSpawns.getDouble("x");
                        double y = rsSpawns.getDouble("y");
                        double z = rsSpawns.getDouble("z");
                        int yaw = rsSpawns.getInt("yaw");
                        int pitch = rsSpawns.getInt("pitch");


                        Location loc = new Location(world, x, y, z);
                        loc.setYaw(yaw);
                        loc.setPitch(pitch);
                        ArmorStand camera = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
                        camera.setSmall(true);
                        camera.setBasePlate(false);
                        camera.setCollidable(false);
                        camera.setGravity(false);
                        camera.setCustomNameVisible(true);

                        if (side.equalsIgnoreCase("t")) {
                            camera.setHelmet(new ItemStack(Material.REDSTONE_BLOCK));
                            camera.setCustomName("Verteidiger Spawn");
                        } else if (side.equalsIgnoreCase("ct")) {
                            camera.setHelmet(new ItemStack(Material.LAPIS_BLOCK));
                            camera.setCustomName("Angreifer Spawn");
                        } else if (side.equalsIgnoreCase("hostage")) {
                            camera.setHelmet(new ItemStack(Material.EMERALD_BLOCK));
                            camera.setCustomName("Geisel Spawn");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

package de.bnder.rainbowCraft.gamePlay.gameBase;

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

import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import de.bnder.rainbowCraft.main.Main;
import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MapReset implements Listener {

    public static File mapChanges = new File("plugins/MinecraftR6/mapChanges.yml");
    public static FileConfiguration mapChangesC = YamlConfiguration.loadConfiguration(mapChanges);

    public static void restore(final String map) {
        new BukkitRunnable() {
            @Override
            public void run() {
                int blocks = 0;
                for (String b : mapChangesC.getStringList("Map" + "." + map)) {
                    String[] blockdata = b.split(";");
                    String name = blockdata[0];
                    String data = blockdata[1];
                    World world = Bukkit.getWorld(blockdata[2]);
                    int x = Integer.parseInt(blockdata[3]);
                    int y = Integer.parseInt(blockdata[4]);
                    int z = Integer.parseInt(blockdata[5]);

                    world.getBlockAt(x, y, z).setType(Material.valueOf(name));
                    world.getBlockAt(x, y, z).setBlockData(Bukkit.getServer().createBlockData(data));
                    blocks++;
                    if (world.getEntitiesByClasses(Arrow.class).size() > 0) {
                        for (Arrow arrow : world.getEntitiesByClass(Arrow.class)) {
                            arrow.remove();
                        }
                    }if (world.getEntitiesByClasses(Zombie.class).size() > 0) {
                        for (Zombie zombie : world.getEntitiesByClass(Zombie.class)) {
                            zombie.remove();
                        }
                    }if (world.getEntitiesByClasses(ArmorStand.class).size() > 0) {
                        for (ArmorStand as : world.getEntitiesByClass(ArmorStand.class)) {
                            as.remove();
                        }
                    }
                }

                mapChangesC.set("Map" + "." + map, null);
                try {
                    mapChangesC.save(mapChanges);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Restored Map " + map + " with " + blocks + " changes");
            }
        }.runTaskLater(Main.plugin, 2 * 20);
    }

    @EventHandler
    public static void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() != Material.DEAD_FIRE_CORAL_FAN) {
            Player p = e.getPlayer();
            PlayerUtils playerUtils = new PlayerUtils(p);
            if (playerUtils.isInGame() && p.getWorld().getName().startsWith(GameUtils.mapPrefix)) {
                String path = "Map" + "." + p.getWorld().getName().replace(GameUtils.mapPrefix, "");
                Block b = e.getBlock();
                String block = b.getType().name() + ";" + b.getBlockData().getAsString() + ";" + b.getWorld().getName() + ";" + b.getX() + ";" + b.getY() + ";" + b.getZ();
                ArrayList<String> list = new ArrayList<String>();
                list.addAll(mapChangesC.getStringList(path));
                list.add(block);
                mapChangesC.set(path, list);
                try {
                    mapChangesC.save(mapChanges);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    @EventHandler
    public static void onBlockExplode(BlockExplodeEvent e) {
        if (e.getBlock().getType() != Material.DEAD_FIRE_CORAL_FAN) {
            if (e.getBlock().getWorld().getName().startsWith(GameUtils.mapPrefix)) {
                String path = "Map" + "." + e.getBlock().getWorld().getName().replace(GameUtils.mapPrefix, "");
                ArrayList<String> list = new ArrayList<String>();
                list.addAll(mapChangesC.getStringList(path));
                for (Block b : e.blockList()) {
                    if (b.getType() != Material.DEAD_FIRE_CORAL_FAN) {
                        String block = b.getType().name() + ";" + b.getBlockData().getAsString() + ";" + b.getWorld().getName() + ";" + b.getX() + ";" + b.getY() + ";" + b.getZ();
                        list.add(block);
                        b.setType(Material.AIR);
                    }
                }
                mapChangesC.set(path, list);
                try {
                    mapChangesC.save(mapChanges);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                e.setYield(0);
            }
        }
    }
    @EventHandler
    public static void oEntityExplode(EntityExplodeEvent e) {
        if (e.getEntity().getWorld().getName().startsWith(GameUtils.mapPrefix)) {
            String path = "Map" + "." + e.getEntity().getWorld().getName().replace(GameUtils.mapPrefix, "");
            ArrayList<String> list = new ArrayList<String>();
            list.addAll(mapChangesC.getStringList(path));
            for (Block b : e.blockList()) {
                if (b.getType() != Material.DEAD_FIRE_CORAL_FAN) {
                    String block = b.getType().name() + ";" + b.getBlockData().getAsString() + ";" + b.getWorld().getName() + ";" + b.getX() + ";" + b.getY() + ";" + b.getZ();
                    list.add(block);
                    b.setType(Material.AIR);
                }
            }
            mapChangesC.set(path, list);
            try {
                mapChangesC.save(mapChanges);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.setYield(0);
        }
    }
    @EventHandler
    public static void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        PlayerUtils playerUtils = new PlayerUtils(p);
        if (playerUtils.isInGame() && p.getWorld().getName().startsWith(GameUtils.mapPrefix)) {
            Block b = e.getBlock();
            String path = "Map" + "." + p.getWorld().getName().replace(GameUtils.mapPrefix, "");
            String block = Material.AIR.name() + ";" + Material.AIR.createBlockData().getAsString() + ";" + b.getWorld().getName() + ";" + b.getX() + ";" + b.getY() + ";" + b.getZ();
            ArrayList<String> list = new ArrayList<String>();
            list.addAll(mapChangesC.getStringList(path));
            list.add(block);
            mapChangesC.set(path, list);
            try {
                mapChangesC.save(mapChanges);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}

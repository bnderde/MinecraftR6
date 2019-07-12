package de.bnder.rainbowCraft.buildMode.commands;

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
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class cmdBuildExtras implements CommandExecutor {

    public static String invName = "§5Bau-Extras";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            BuilderUtils builderUtils = new BuilderUtils(p);
            if (builderUtils.isBuilding()) {

                Inventory inv = Bukkit.createInventory(null, 6 * 9, invName);

                ItemStack camera = new ItemStack(Material.FURNACE);
                ItemMeta cameraM = camera.getItemMeta();
                cameraM.setDisplayName("§3Kamera platzieren");
                cameraM.setLore(new ArrayList<String>() {{
                    add("§7Du erhälst ein Item, womit Positionen");
                    add("§7für Kameras gesetzt werden können.");
                }});
                camera.setItemMeta(cameraM);

                ItemStack tSpawn = new ItemStack(Material.REDSTONE);
                ItemMeta tSpawnM = tSpawn.getItemMeta();
                tSpawnM.setDisplayName("§cVerteidiger-Spawn");
                tSpawnM.setLore(new ArrayList<String>() {{
                    add("§7Du erhälst ein Item, womit Positionen");
                    add("§7für die Spawns der Verteidiger gesetzt");
                    add("§7werden können.");
                }});
                tSpawn.setItemMeta(tSpawnM);

                ItemStack ctSpawn = new ItemStack(Material.LAPIS_LAZULI);
                ItemMeta ctSpawnM = ctSpawn.getItemMeta();
                ctSpawnM.setDisplayName("§9Angreifer-Spawn");
                ctSpawnM.setLore(new ArrayList<String>() {{
                    add("§7Du erhälst ein Item, womit Positionen");
                    add("§7für die Spawns der Angreifer gesetzt");
                    add("§7werden können.");
                }});
                ctSpawn.setItemMeta(ctSpawnM);

                ItemStack geiselSpawn = new ItemStack(Material.EMERALD);
                ItemMeta geiselSpawnM = geiselSpawn.getItemMeta();
                geiselSpawnM.setDisplayName("§aGeisel-Spawn");
                geiselSpawnM.setLore(new ArrayList<String>() {{
                    add("§7Du erhälst ein Item, womit die");
                    add("§7Position der Geisel festgelegt");
                    add("§7werden kann.");
                }});
                geiselSpawn.setItemMeta(geiselSpawnM);

                ItemStack leave = new ItemStack(Material.OAK_DOOR);
                ItemMeta leaveM = leave.getItemMeta();
                leaveM.setDisplayName("§cBau-Modus verlassen");
                leaveM.setLore(new ArrayList<String>() {{
                    add("§7Verlasse den Bau-Modus und gelange");
                    add("§7zurück zur Lobby.");
                }});
                leave.setItemMeta(leaveM);

                ItemStack publish = new ItemStack(Material.EMERALD_BLOCK);
                ItemMeta publishM = publish.getItemMeta();
                if (builderUtils.isMapPublic(builderUtils.getMapID())) {
                    publishM.setDisplayName("§cPrivat setzen");
                } else {
                    publishM.setDisplayName("§aÖffentlich setzen");
                }
                publishM.setLore(new ArrayList<String>() {{
                    add("§7Ändere den Status deiner Karte,");
                    add("§7sodass sie spielbar/nicht spielbar ist.");
                }});
                publish.setItemMeta(publishM);

                inv.setItem(10, leave);
                inv.setItem(19, publish);
                inv.setItem(14, camera);
                inv.setItem(15, tSpawn);
                inv.setItem(23, geiselSpawn);
                inv.setItem(24, ctSpawn);

                p.openInventory(inv);
            } else {
                p.sendMessage(Main.prefix + " §cDu bist nicht im Bau-Modus!");
            }
        }
        return false;
    }
}

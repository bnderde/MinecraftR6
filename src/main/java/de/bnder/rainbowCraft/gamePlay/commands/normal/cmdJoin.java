package de.bnder.rainbowCraft.gamePlay.commands.normal;

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
import de.bnder.rainbowCraft.gamePlay.gameBase.lobby.GiveLobbyItems;
import de.bnder.rainbowCraft.gamePlay.gameBase.lobby.LobbyUtils;
import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import de.bnder.rainbowCraft.main.Main;
import de.bnder.rainbowCraft.utils.Connection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

public class cmdJoin implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            PlayerUtils playerUtils = new PlayerUtils(p);
            if (args.length == 1) {
                String gameName = args[0];
                GameUtils game = new GameUtils(gameName);
                if (!game.isRunning()) {
                    if (!playerUtils.isInGame()) {
                        BuilderUtils builderUtils = new BuilderUtils(p);
                        if (!builderUtils.isBuilding()) {

                            for (Entity entity : p.getPassengers()) {
                                p.removePassenger(entity);
                            }

                            p.teleport(game.lobbyLocation(p));

                            game.updateLobbyStatusToTrue(p);

                            LobbyUtils.userJoinsLobby(gameName);

                            for (Player all : game.players()) {
                                all.sendMessage(Main.prefix + " §e" + p.getName() + " §2ist dem Spiel beigetreten. §7(" + game.players().size() + "/" + game.maxPlayers() + ")");
                            }

                            GiveLobbyItems.give(p);
                        } else {
                            p.sendMessage(Main.prefix + " §cDu bist aktuell im Bau-Modus!");
                        }
                    } else {
                        p.sendMessage(Main.prefix + " §cDu bist bereits in einem Spiel!");
                    }
                } else {
                    p.sendMessage(Main.prefix + " §cDas Spiel läuft bereits!");
                }
            } else if (args.length == 0) {
                Inventory inv = Bukkit.createInventory(null, 6*9, "§3Maps");

                try {
                    final ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `owner`, `mapName`, `mapID`, `upvotes`, `downvotes`, `played` FROM `MCR6_Player_Maps_General` WHERE `public`=true LIMIT " + inv.getSize()).executeQuery();
                    int slot = 0;
                    while (rs.next()) {
                        ItemStack map = new ItemStack(Material.GRASS_BLOCK);
                        ItemMeta mapM = map.getItemMeta();
                        if (rs.getString("mapName") != null) {
                            mapM.setDisplayName("§e" + rs.getString("mapName"));
                        } else {
                            mapM.setDisplayName("§eUnbenannt");
                        }
                        mapM.setLore(new ArrayList<String>(){{
                            add("§eErbauer: §6" + Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("owner"))).getName());
                            add("§aUpvotes: §2" + rs.getLong("upvotes"));
                            add("§cDownvotes: §4" + rs.getLong("downvotes"));
                            add("§9Gespielt: §9" + rs.getLong("played"));

                            add("§7ID: " + rs.getString("mapID"));
                        }});
                        map.setItemMeta(mapM);
                        inv.setItem(slot, map);
                        slot++;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }

                p.openInventory(inv);
            } else {
                p.sendMessage(Main.prefix + " §cBitte gib einen Mapnamen an!");
            }
        }
        return false;
    }
}

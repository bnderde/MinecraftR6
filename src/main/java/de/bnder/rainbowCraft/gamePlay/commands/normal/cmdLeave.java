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

import de.bnder.rainbowCraft.gamePlay.gameBase.gameManager.EndGame;
import de.bnder.rainbowCraft.gamePlay.gameBase.lobby.LobbyItemInteract;
import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import de.bnder.rainbowCraft.main.Main;
import de.bnder.rainbowCraft.utils.Connection;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.sql.ResultSet;

public class cmdLeave implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            playerLeaveGame(p, true);
        }
        return false;
    }

    public static void playerLeaveGame(Player p, boolean sendMessage) {
        PlayerUtils pUtils = new PlayerUtils(p);
        if (pUtils.isInGame()) {
            try {
                ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `gameName` FROM `MCR6_Games`").executeQuery();
                while (rs.next()) {
                    String games = rs.getString("gameName");
                    GameUtils gameUtils = new GameUtils(games);
                    if (gameUtils.players().contains(p)) {
                        gameUtils.removePlayerComplete(p);

                        for (PotionEffect effect : p.getActivePotionEffects()) {
                            p.removePotionEffect(effect.getType());
                        }

                        Main.gamesC.set("Game" + "." + games + ".player" + "." + p.getUniqueId(), null);

                        Main.gamesC.save(Main.gamesFile);
                        for (Player all : gameUtils.lobbyPlayers()) {
                            if (all.getOpenInventory() != null) {
                                if (all.getOpenInventory().getTitle().equalsIgnoreCase(LobbyItemInteract.chooseOperatorInvName)) {
                                    LobbyItemInteract.openChooseOperatorInv(all, games);
                                }
                            }
                        }

                        p.sendMessage(Main.prefix + " §aDu hast das Spiel verlassen.");
                        p.getInventory().clear();
                        if (sendMessage) {
                            for (Player all : gameUtils.players()) {
                                all.sendMessage(Main.prefix + " §e" + p.getName() + " §chat das Spiel verlassen.");
                            }
                        }

                        if (gameUtils.isRunning()) {
                            if (gameUtils.players().size() <= 1 || gameUtils.getCTs().size() == 0 || gameUtils.getTs().size() == 0) {
                                EndGame.end(games, "null");
                            }
                        }

                        if (Bukkit.getBossBar(NamespacedKey.minecraft(games.toLowerCase() + "t")) != null) {
                            if (Bukkit.getBossBar(NamespacedKey.minecraft(games.toLowerCase() + "t")).getPlayers().contains(p)) {
                                Bukkit.getBossBar(NamespacedKey.minecraft(games.toLowerCase() + "t")).removePlayer(p);
                            }
                        }
                        if (Bukkit.getBossBar(NamespacedKey.minecraft(games.toLowerCase() + "ct")) != null) {
                            if (Bukkit.getBossBar(NamespacedKey.minecraft(games.toLowerCase() + "ct")).getPlayers().contains(p)) {
                                Bukkit.getBossBar(NamespacedKey.minecraft(games.toLowerCase() + "ct")).removePlayer(p);
                            }
                        }

                        for (Entity e : p.getPassengers()) {
                            e.remove();
                        }

                        if (sendMessage) {
                            p.performCommand("tptospawn");
                        }
                    } else {
                        if (gameUtils.lobbyPlayers().contains(p)) {
                            gameUtils.removePlayerComplete(p);
                            for (PotionEffect effect : p.getActivePotionEffects()) {
                                p.removePotionEffect(effect.getType());
                            }

                            Main.gamesC.set("Game" + "." + games + ".player" + "." + p.getUniqueId(), null);

                            Main.gamesC.save(Main.gamesFile);
                            for (Player all : gameUtils.lobbyPlayers()) {
                                if (all.getOpenInventory() != null) {
                                    if (all.getOpenInventory().getTitle().equalsIgnoreCase(LobbyItemInteract.chooseOperatorInvName)) {
                                        LobbyItemInteract.openChooseOperatorInv(all, games);
                                    }
                                }
                            }

                            p.sendMessage(Main.prefix + " §aDu hast das Spiel verlassen.");
                            p.getInventory().clear();
                            if (sendMessage) {
                                p.performCommand("tptospawn");
                            }
                            if (sendMessage) {
                                for (Player all : gameUtils.players()) {
                                    all.sendMessage(Main.prefix + " §e" + p.getName() + " §chat das Spiel verlassen.");
                                }
                            }

                            if (gameUtils.isRunning()) {

                                if (gameUtils.players().size() <= 1 || gameUtils.getCTs().size() == 0 || gameUtils.getTs().size() == 0) {
                                    EndGame.end(games, "null");
                                }
                            }

                            if (Bukkit.getBossBar(NamespacedKey.minecraft(games.toLowerCase() + "t")) != null) {
                                if (Bukkit.getBossBar(NamespacedKey.minecraft(games.toLowerCase() + "t")).getPlayers().contains(p)) {
                                    Bukkit.getBossBar(NamespacedKey.minecraft(games.toLowerCase() + "t")).removePlayer(p);
                                }
                            }
                            if (Bukkit.getBossBar(NamespacedKey.minecraft(games.toLowerCase() + "ct")) != null) {
                                if (Bukkit.getBossBar(NamespacedKey.minecraft(games.toLowerCase() + "ct")).getPlayers().contains(p)) {
                                    Bukkit.getBossBar(NamespacedKey.minecraft(games.toLowerCase() + "ct")).removePlayer(p);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (sendMessage) {
                p.sendMessage(Main.prefix + " §cDu bist in keinem Spiel!");
            }
        }
    }
}

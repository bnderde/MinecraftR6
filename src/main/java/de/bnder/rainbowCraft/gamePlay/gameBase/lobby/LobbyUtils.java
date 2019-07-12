package de.bnder.rainbowCraft.gamePlay.gameBase.lobby;

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

import de.bnder.rainbowCraft.buildMode.buildBase.ModifyWorld;
import de.bnder.rainbowCraft.gamePlay.gameBase.gameManager.StartGame;
import de.bnder.rainbowCraft.main.Main;
import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils.mapPrefix;

public class LobbyUtils {

    private static int secondsRemaining = 60;
    private static int countdown = 0;

    public static void userJoinsLobby(final String game) {
        final GameUtils gameUtils = new GameUtils(game);
        if (gameUtils.gameExists()) {
            final int min = gameUtils.minPlayers();
            final ArrayList<Player> list = gameUtils.lobbyPlayers();

            World world = Bukkit.getWorld(mapPrefix + game);
            if (world == null) {
                Bukkit.createWorld(new WorldCreator(mapPrefix + game).copy(Bukkit.getWorld("flatWorld")).generateStructures(false).type(WorldType.FLAT).environment(World.Environment.NORMAL));
            }
            ModifyWorld.modify(game, false);

            if (list.size() >= min) {
                if (!gameUtils.gameStarting()) {
                    countdown = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
                        public void run() {
                            ArrayList<Player> pList = gameUtils.lobbyPlayers();
                            if (pList.size() >= min) {
                                if (secondsRemaining > 0) {
                                    String scnd = String.valueOf(secondsRemaining);
                                    for (Player p : pList) {
                                        p.setLevel(secondsRemaining);
                                    }

                                    if (scnd.endsWith("0") || secondsRemaining <= 10) {
                                        for (Player p : pList) {
                                            p.sendMessage(Main.prefix + " §2Match startet in §a" + secondsRemaining + " Sekunden§2.");
                                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                        }
                                    }
                                    secondsRemaining--;
                                } else {
                                    for (Player p : pList) {
                                        p.sendMessage(Main.prefix + " §aDas Match startet nun.");
                                        p.setLevel(0);
                                    }
                                    StartGame.start(game);
                                    Bukkit.getScheduler().cancelTask(countdown);
                                }
                            } else {
                                for (Player p : pList) {
                                    p.sendMessage(Main.prefix + " §cDer Countdown wurde abgebrochen!");
                                    p.setLevel(0);
                                }
                                secondsRemaining = 60;
                                Bukkit.getScheduler().cancelTask(countdown);
                            }
                        }
                    },0, 20);
                }
                secondsRemaining = 60;
            }
        }
    }

}

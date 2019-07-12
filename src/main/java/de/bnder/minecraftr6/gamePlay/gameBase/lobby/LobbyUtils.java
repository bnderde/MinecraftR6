package de.bnder.minecraftr6.gamePlay.gameBase.lobby;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.minecraftr6.buildMode.buildBase.ModifyWorld;
import de.bnder.minecraftr6.gamePlay.gameBase.gameManager.StartGame;
import de.bnder.minecraftr6.main.Main;
import de.bnder.minecraftr6.gamePlay.gameUtils.GameUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static de.bnder.minecraftr6.gamePlay.gameUtils.GameUtils.mapPrefix;

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

package de.bnder.rainbowCraft.gamePlay.gameBase.gameManager;

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
import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import de.bnder.rainbowCraft.main.Main;
import de.bnder.rainbowCraft.utils.Config;
import de.bnder.rainbowCraft.utils.Connection;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public class EndGame {

    public static void end(String game, String roundWinner) {
        GameUtils gameUtils = new GameUtils(game);
        if (gameUtils.ctsRoundsWon() >= 2  || gameUtils.tsRoundsWon() >= 2 || gameUtils.players().size() <= 1) {
            String winner;
            if (gameUtils.ctsRoundsWon() > gameUtils.tsRoundsWon()) {
                winner = "§9Anti-Terrorist";
            } else if (gameUtils.tsRoundsWon() > gameUtils.ctsRoundsWon()){
                winner = "§cTerrorist";
            } else {
                winner = "§aUnentschieden";
            }

            if (winner.startsWith("§9")) {
                for (String uuid : gameUtils.getCTs()) {
                    Player p = Bukkit.getPlayer(UUID.fromString(uuid));
                    PlayerUtils playerUtils = new PlayerUtils(p);
                    long pMoney = playerUtils.getMoney();
                    long moneyWin = Config.moneyPerWin();
                    playerUtils.modifyMoney(pMoney + moneyWin);
                    p.sendMessage(Main.prefix + " §aFür den Sieg hast du §e" + moneyWin + Main.currency + " §aerhalten.");
                }
            } else if (winner.startsWith("§c")) {
                for (String uuid : gameUtils.getTs()) {
                    Player p = Bukkit.getPlayer(UUID.fromString(uuid));
                    PlayerUtils playerUtils = new PlayerUtils(p);
                    long pMoney = playerUtils.getMoney();
                    long moneyWin = Config.moneyPerWin();
                    playerUtils.modifyMoney(pMoney + moneyWin);
                    p.sendMessage(Main.prefix + " §aFür den Sieg hast du §e" + moneyWin + Main.currency + " §aerhalten.");
                }
            } else if (winner.startsWith("§a")) {
                for (Player p : gameUtils.players()) {
                    PlayerUtils playerUtils = new PlayerUtils(p);
                    long pMoney = playerUtils.getMoney();
                    long moneyWin = Config.moneyPerWin() / 2;
                    playerUtils.modifyMoney(pMoney + moneyWin);
                    p.sendMessage(Main.prefix + " §aFür das Unentschieden hast du §e" + moneyWin + Main.currency + " §aerhalten.");
                }
            }

            for (Player all : gameUtils.players()) {
                try {
                    all.sendMessage(Main.prefix + " §cDas Spiel ist vorbei! §aDer Gewinner ist: " + winner);

                    for (Entity entity : all.getPassengers()) {
                        all.removePassenger(entity);
                    }

                    all.getInventory().clear();
                    all.setHealth(all.getHealthScale());

                    all.setGameMode(GameMode.SURVIVAL);

                    all.performCommand("tptospawn");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                Connection.mainConnection().prepareStatement("DELETE FROM `MCR6_Games_Players` WHERE `name`='" + game + "'").executeUpdate();
//                Connection.mainConnection().prepareStatement("DELETE FROM `MCR6_Games` WHERE `gameName`='" + game + "'").executeUpdate();
                 Connection.mainConnection().prepareStatement("UPDATE `MCR6_Games` SET `running`=false, `ctWins`=0, `tWins`=0 WHERE `gameName`='" + game + "'").executeUpdate();

                 long played = gameUtils.getPlayed();
                 Connection.mainConnection().prepareStatement("UPDATE `MCR6_Player_Maps_General` SET `played`=" + (1 + played) + " WHERE `mapID`='" + game + "'").executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

//            Main.gamesC.set("Game" + "." + game + ".running", false);

            try {
                Bukkit.getBossBar(NamespacedKey.minecraft(game.toLowerCase() + "t")).removeAll();
                Bukkit.getBossBar(NamespacedKey.minecraft(game.toLowerCase() + "ct")).removeAll();
            } catch (Exception e) {}

            try {
                Bukkit.getScoreboardManager().getMainScoreboard().getTeam("r6" + game + "1").unregister();
                Bukkit.getScoreboardManager().getMainScoreboard().getTeam("r6" + game + "2").unregister();
            } catch (Exception e) {}

            Main.gamesC.set("Game" + "." + game + ".player", null);
            try {
                Main.gamesC.save(Main.gamesFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            MapReset.restore(game);
            World world = Bukkit.getWorld(GameUtils.mapPrefix + game);
            if (world != null) {
                Bukkit.unloadWorld(world, true);
                new File(world.getName()).delete();
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv remove " + GameUtils.mapPrefix + game);
            }
        } else {
            EndRound.end(game, roundWinner);
        }
    }

}

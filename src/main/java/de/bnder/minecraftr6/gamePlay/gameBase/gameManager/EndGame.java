package de.bnder.minecraftr6.gamePlay.gameBase.gameManager;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.minecraftr6.gamePlay.gameUtils.GameUtils;
import de.bnder.minecraftr6.gamePlay.gameUtils.PlayerUtils;
import de.bnder.minecraftr6.main.Main;
import de.bnder.minecraftr6.utils.Config;
import de.bnder.minecraftr6.utils.Connection;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
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
                try {
                    FileUtils.deleteDirectory(new File(world.getName()));
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv remove " + GameUtils.mapPrefix + game);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            EndRound.end(game, roundWinner);
        }
    }

}

package de.bnder.minecraftr6.gamePlay.gameBase.gameManager;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.minecraftr6.gamePlay.gameBase.MapReset;
import de.bnder.minecraftr6.gamePlay.gameBase.lobby.GiveLobbyItems;
import de.bnder.minecraftr6.main.Main;
import de.bnder.minecraftr6.utils.Connection;
import de.bnder.minecraftr6.gamePlay.gameUtils.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class EndRound {

    static int scheduler = 0;
    static int countdown = 20;

    public static void end(final String game, String winner) {
        GameUtils gameUtils = new GameUtils(game);
        MapReset.restore(game);
        for (Player p : gameUtils.players()) {
            for (PotionEffect potionEffect : p.getActivePotionEffects()) {
                p.removePotionEffect(potionEffect.getType());
            }
        }
        if ((gameUtils.ctsRoundsWon() < 2 && gameUtils.tsRoundsWon() < 2) && gameUtils.players().size() > 1) {

            String message = "";

            if (winner.equalsIgnoreCase("ct") || winner.equalsIgnoreCase("t")) {
                int won;
                if (winner.equalsIgnoreCase("ct")) {
                    won = gameUtils.ctsRoundsWon();
                    message = Main.prefix + " §9Anti-Terroristen haben die Runde gewonnen";

                } else {
                    won = gameUtils.tsRoundsWon();
                    message = Main.prefix + " §cTerroristen haben die Runde gewonnen";
                }
            }

            for (final Player p : gameUtils.players()) {
                gameUtils.updateLobbyStatusToTrue(p);
                p.setGameMode(GameMode.SURVIVAL);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
                    public void run() {
                        GiveLobbyItems.give(p);
                    }
                }, 30);
                p.sendMessage(message);
                p.sendMessage(Main.prefix + " §2Du hast §c" + countdown + " Sekunden §2um einen neuen Operator zu wählen.");
                p.sendMessage(Main.prefix + " §cDie Seiten werden gewechselt");
            }

            ArrayList<String> ts = gameUtils.getTs();

            for (String tUUID : ts) {
                Player t = Bukkit.getPlayer(UUID.fromString(tUUID));
                t.sendMessage(Main.prefix + " §eDu spielst nächste Runde als: §9Anti-Terrorist");
            }

            ArrayList<String> cts = gameUtils.getCTs();

            for (String ctUUID : cts) {
                Player ct = Bukkit.getPlayer(UUID.fromString(ctUUID));
                ct.sendMessage(Main.prefix + " §eDu spielst nächste Runde als: §cTerrorist");
            }


            try {
                Connection.mainConnection().prepareStatement("UPDATE `MCR6_Games_Players` SET `team`='ct2' WHERE `name`='" + game + "' && `team`='t'").executeUpdate();
                Connection.mainConnection().prepareStatement("UPDATE `MCR6_Games_Players` SET `team`='t2' WHERE `name`='" + game + "' && `team`='ct'").executeUpdate();
                Connection.mainConnection().prepareStatement("UPDATE `MCR6_Games_Players` SET `team`='t' WHERE `name`='" + game + "' && `team`='t2'").executeUpdate();
                Connection.mainConnection().prepareStatement("UPDATE `MCR6_Games_Players` SET `team`='ct' WHERE `name`='" + game + "' && `team`='ct2'").executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            int ctWins = gameUtils.ctsRoundsWon();
            int tWins = gameUtils.tsRoundsWon();
            if (winner.equalsIgnoreCase("ct")) {
                ctWins++;
            } else if (winner.equalsIgnoreCase("t")) {
                tWins++;
            }
            try {
                Connection.mainConnection().prepareStatement("UPDATE `MCR6_Games` SET `ctWins`=" + tWins + " WHERE `gameName`='" + game  + "'").executeUpdate();
                Connection.mainConnection().prepareStatement("UPDATE `MCR6_Games` SET `tWins`=" + ctWins + " WHERE `gameName`='" + game  + "'").executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
//            Main.gamesC.set("Game" + "." + game + ".running", false);
//            try {
//                Main.gamesC.save(Main.gamesFile);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            try {
                Bukkit.getBossBar(NamespacedKey.minecraft(game.toLowerCase() + "t")).removeAll();
                Bukkit.getBossBar(NamespacedKey.minecraft(game.toLowerCase() + "ct")).removeAll();
            } catch (Exception e) {}

            BossBar tBossBar = Bukkit.createBossBar(NamespacedKey.minecraft(game.toLowerCase() + "t"), "Spielstand: §c" + tWins + " §7- §9" + ctWins, BarColor.BLUE, BarStyle.SOLID);
            for (String tUUID : gameUtils.getTs()) {
                Player p = Bukkit.getPlayer(UUID.fromString(tUUID));
                p.sendMessage(Main.prefix + " §eDer aktuelle Spielstand ist: §c" + tWins + " §e- §9" + ctWins + "§e.");
                tBossBar.addPlayer(p);
            }

            BossBar ctBossBar = Bukkit.createBossBar(NamespacedKey.minecraft(game.toLowerCase() + "ct"), "Spielstand: §9" + ctWins + " §7- §c" + tWins, BarColor.BLUE, BarStyle.SOLID);
            for (String ctUUID : gameUtils.getCTs()) {
                Player p = Bukkit.getPlayer(UUID.fromString(ctUUID));
                p.sendMessage(Main.prefix + " §eDer aktuelle Spielstand ist: §9" + ctWins + " §e- §c" + tWins + "§e.");
                ctBossBar.addPlayer(p);
            }

            scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
                public void run() {
                    if (countdown > 0) {
                        countdown--;
                    } else {
                        StartRound.start(game);
                        Bukkit.getScheduler().cancelTask(scheduler);
                        countdown = 20;
                    }

                }
            }, 0, 20);
        } else {
            EndGame.end(game, winner);
        }
    }

}

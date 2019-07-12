package de.bnder.rainbowCraft.gamePlay.gameBase.gameManager;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.rainbowCraft.gamePlay.gameBase.hostage.SpawnHostage;
import de.bnder.rainbowCraft.gamePlay.operators.Lesion;
import de.bnder.rainbowCraft.utils.Connection;
import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class StartRound {

    public static void start(String game) {
        try {
            Connection.mainConnection().prepareStatement("UPDATE `MCR6_Games_Players` SET `inLobby`=false, `alive`=true WHERE `name`='" + game + "'").executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        GameUtils gameUtils = new GameUtils(game);

        ArrayList<Player> ctPlayers = new ArrayList<Player>();
        for (String uuid : gameUtils.getCTs()) {
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            ctPlayers.add(p);
            for (PotionEffect potionEffect : p.getActivePotionEffects()) {
                p.removePotionEffect(potionEffect.getType());
            }
        }
//        for (String ctUUID : Main.gamesC.getStringList("Game" + "." + game + ".teams" + ".ct")) {
//            Player p = Bukkit.getPlayer(UUID.fromString(ctUUID));
//            ctPlayers.add(p);
//            for (PotionEffect potionEffect : p.getActivePotionEffects()) {
//                p.removePotionEffect(potionEffect.getType());
//            }
//        }
        ArrayList<Player> tPlayers = new ArrayList<Player>();
        for (String uuid : gameUtils.getTs()) {
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            tPlayers.add(p);
            for (PotionEffect potionEffect : p.getActivePotionEffects()) {
                p.removePotionEffect(potionEffect.getType());
            }
        }
//        for (String tUUID : Main.gamesC.getStringList("Game" + "." + game + ".teams" + ".t")) {
//            Player p = Bukkit.getPlayer(UUID.fromString(tUUID));
//            tPlayers.add(p);
//            for (PotionEffect potionEffect : p.getActivePotionEffects()) {
//                p.removePotionEffect(potionEffect.getType());
//            }
//        }

        //CTs spawnen
        int get = 0;
        for (Location ctSpawn : gameUtils.spawns("ct")) {
            try {
                ctPlayers.get(get++).teleport(ctSpawn);
                if (ctSpawn.getWorld().getGameRuleValue(GameRule.DO_TILE_DROPS)) {
                    ctSpawn.getWorld().setGameRule(GameRule.DO_TILE_DROPS, false);
                }
            } catch (IndexOutOfBoundsException e) {
                break;
            } catch (Exception e) {

            }
        }

        //Ts spawnen
        get = 0;
        for (Location tSpawn : gameUtils.spawns("t")) {
            try {
                tPlayers.get(get++).teleport(tSpawn);
            } catch (IndexOutOfBoundsException e) {
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        StartGame.giveDefaultOperator(game, gameUtils);

        StartGame.giveItems(game, gameUtils);

        SpawnHostage.spawn(game);

//        Main.gamesC.set("Game" + "." + game + ".running", true);

        //            Main.gamesC.save(Main.gamesFile);

        for (Player all : gameUtils.players()) {
            all.setGameMode(GameMode.SURVIVAL);
            all.setFoodLevel(20);
            all.setHealth(all.getHealthScale());
        }

        Lesion.checkPlayers(game);
        CheckRescuePoint.check(game);
    }

}

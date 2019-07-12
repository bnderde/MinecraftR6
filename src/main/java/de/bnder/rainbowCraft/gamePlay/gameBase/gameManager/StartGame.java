package de.bnder.rainbowCraft.gamePlay.gameBase.gameManager;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.rainbowCraft.gamePlay.camera.CameraItem;
import de.bnder.rainbowCraft.gamePlay.gameBase.features.Stacheldraht;
import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import de.bnder.rainbowCraft.gamePlay.gameUtils.OperatorUtils;
import de.bnder.rainbowCraft.main.Main;
import de.bnder.rainbowCraft.utils.Connection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class StartGame {

    public static void start(String gameName) {
        GameUtils gameUtils = new GameUtils(gameName);

        ArrayList<Player> ctPlayers = new ArrayList<Player>();
        ArrayList<Player> tPlayers = new ArrayList<Player>();

        int side = 0;
        for (Player p : gameUtils.lobbyPlayers()) {
            if (side == 0) {
                ctPlayers.add(p);
                side = 1;
                p.sendMessage(Main.prefix + " §eDu bist §9Anti-Terrorist");
            } else if (side == 1) {
                tPlayers.add(p);
                p.sendMessage(Main.prefix + " §eDu bist §cTerrorist");
                side = 0;
            }

            p.getInventory().clear();
        }

        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.registerNewTeam("r6" + gameName + "1");
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OWN_TEAM);
        Team team2 = board.registerNewTeam("r6" + gameName + "2");
        team2.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OWN_TEAM);

        for (Player ct : ctPlayers) {
            try {
                Connection.mainConnection().prepareStatement("UPDATE `MCR6_Games_Players` SET `team`='ct' WHERE `name`='" + gameName + "' && `playerUUID`='" + ct.getUniqueId().toString() + "'").executeUpdate();
                team.addPlayer(Bukkit.getOfflinePlayer(ct.getUniqueId()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        for (Player t : tPlayers) {
            try {
                Connection.mainConnection().prepareStatement("UPDATE `MCR6_Games_Players` SET `team`='t' WHERE `name`='" + gameName + "' && `playerUUID`='" + t.getUniqueId().toString() + "'").executeUpdate();
                team2.addPlayer(Bukkit.getOfflinePlayer(t.getUniqueId()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            Connection.mainConnection().prepareStatement("UPDATE `MCR6_Games` SET `running`=true WHERE `gameName`='" + gameName + "'").executeUpdate();
        } catch (Exception e) {}

//        Main.gamesC.set("Game" + "." + gameName + ".running", true);

        for (Player p : gameUtils.lobbyPlayers()) {
            gameUtils.updateLobbyStatusToFalse(p);
        }

        //            Main.gamesC.save(Main.gamesFile);

//        ModifyWorld.modify(gameName);

        StartRound.start(gameName);

        Stacheldraht.check(gameName);
    }

    static void giveDefaultOperator(String game, GameUtils gameUtils) {
        //TODO: CHANGE TO MYSQL
        for (Player p : gameUtils.players()) {
            if (Main.gamesC.get("Game" + "." + game + ".player" + "." + p.getUniqueId().toString() + ".operator") == null) {
                Main.gamesC.set("Game" + "." + game + ".player" + "." + p.getUniqueId().toString() + ".operator", "rekrut");
            }
        }
        try {
            Main.gamesC.save(Main.gamesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String c4ItemName = "§4Sprengstoff";

    static void giveItems(final String game, GameUtils gameUtils) {
        ArrayList<String> terrorists = gameUtils.getTs();
        for (Player p : gameUtils.players()) {
            p.getInventory().clear();
            String operator = Main.gamesC.getString("Game" + "." + game + ".player" + "." + p.getUniqueId().toString() + ".operator");
            OperatorUtils operatorUtils = new OperatorUtils(operator);
            p.getInventory().setItem(0, operatorUtils.weaponItem());
            for (ItemStack is : operatorUtils.items()) {
                p.getInventory().addItem(is);
            }

            if (terrorists.contains(p.getUniqueId().toString())) {
                ItemStack itemStack = new ItemStack(Material.DEAD_FIRE_CORAL_FAN);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName("§7Stacheldraht");
                itemStack.setAmount(2);
                itemStack.setItemMeta(itemMeta);
                p.getInventory().addItem(itemStack);

                p.getInventory().setItem(8, CameraItem.cameraItem());
            } else {
                ItemStack c4 = new ItemStack(Material.STONE_BUTTON);
                ItemMeta c4Meta = c4.getItemMeta();
                c4Meta.setDisplayName(c4ItemName);
                c4.setItemMeta(c4Meta);

                p.getInventory().addItem(c4);

                ItemStack hook = new ItemStack(Material.FISHING_ROD);
                ItemMeta hookm = hook.getItemMeta();
                hookm.setDisplayName("§5Kletterseil");
                hook.setItemMeta(hookm);

                p.getInventory().addItem(hook);
            }

        }
    }


}

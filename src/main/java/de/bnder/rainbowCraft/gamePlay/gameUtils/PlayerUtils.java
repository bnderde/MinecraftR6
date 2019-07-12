package de.bnder.rainbowCraft.gamePlay.gameUtils;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.rainbowCraft.utils.Connection;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

import static de.bnder.rainbowCraft.gamePlay.gameBase.core.Reload.reloadingPlayers;

public class PlayerUtils {

    Player p;

    public PlayerUtils(Player player) {
        p = player;
    }

    public long getMoney() {
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `money` FROM `PlayerData` WHERE `uuid`='" + p.getUniqueId().toString() + "'").executeQuery();
            if (rs.next()) {
                return rs.getLong("money");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void modifyMoney(long newValue) {
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `money` FROM `PlayerData` WHERE `uuid`='" + p.getUniqueId().toString() + "'").executeQuery();
            if (rs.next()) {
                Connection.mainConnection().prepareStatement("UPDATE `PlayerData` SET `money`=" + newValue + " WHERE `uuid`='" + p.getUniqueId().toString() + "'").executeUpdate();
            } else {
                Connection.mainConnection().prepareStatement("INSERT INTO `PlayerData`(`uuid`,`money`) VALUES ('" + p.getUniqueId().toString() + "'," + newValue + ")").executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isInGame() {
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `name` FROM `MCR6_Games_Players` WHERE `playerUUID`='" + p.getUniqueId().toString() + "'").executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        for (String games : Main.gamesC.getStringList("GamesList")) {
//            GameUtils gameUtils = new GameUtils(games);
//            if (gameUtils.players().contains(p) || gameUtils.lobbyPlayers().contains(p)) {
//                return true;
//            }
//        }
        return false;
    }

    public String getGame() {
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `name` FROM `MCR6_Games_Players` WHERE `playerUUID`='" + p.getUniqueId().toString() + "'").executeQuery();
            if (rs.next()) {
                String sqlGame = rs.getString("name");
                return sqlGame;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        for (String games : Main.gamesC.getStringList("GamesList")) {
//            GameUtils gameUtils = new GameUtils(games);
//            if (gameUtils.players().contains(p)) {
//                return games;
//            }
//        }
        return null;
    }


    public boolean isReloading() {
        if (reloadingPlayers.contains(p)) {
            return true;
        }
        return false;
    }

    public void changeReloadingState() {
        if (isReloading()) {
            reloadingPlayers.remove(p);
        } else {
            reloadingPlayers.add(p);
        }
    }

    public long getLifetimeKills() {
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `kills` FROM `MCR6_Stats_Lifetime` WHERE `uuid`='" + p.getUniqueId().toString() + "'").executeQuery();
            if (rs.next()) {
                return rs.getLong("kills");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void addLifetimeKills() {
        long currentKills = getLifetimeKills();
        try {
            if (currentKills > 0) {
                currentKills += 1;
                Connection.mainConnection().prepareStatement("UPDATE `MCR6_Stats_Lifetime` SET `kills`=" + currentKills + " WHERE `uuid`='" + p.getUniqueId().toString() + "'").executeUpdate();
            } else {
                ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `kills` FROM `MCR6_Stats_Lifetime` WHERE `uuid`='" + p.getUniqueId().toString() + "'").executeQuery();
                currentKills += 1;
                if (rs.next()) {
                    Connection.mainConnection().prepareStatement("UPDATE `MCR6_Stats_Lifetime` SET `kills`=" + currentKills + " WHERE `uuid`='" + p.getUniqueId().toString() + "'").executeUpdate();
                } else {
                    Connection.mainConnection().prepareStatement("INSERT INTO `MCR6_Stats_Lifetime`(`uuid`, `kills`, `deaths`, `roundsWon`, `roundsLost`) VALUES ('" + p.getUniqueId().toString() + "'," + currentKills + ",0,0,0)").executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getLifetimeDeaths() {
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `deaths` FROM `MCR6_Stats_Lifetime` WHERE `uuid`='" + p.getUniqueId().toString() + "'").executeQuery();
            if (rs.next()) {
                return rs.getLong("deaths");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void addLifetimeDeaths() {
        long currentDeaths = getLifetimeDeaths();
        try {
            if (currentDeaths > 0) {
                currentDeaths += 1;
                Connection.mainConnection().prepareStatement("UPDATE `MCR6_Stats_Lifetime` SET `deaths`=" + currentDeaths + " WHERE `uuid`='" + p.getUniqueId().toString() + "'").executeUpdate();
            } else {
                ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `deaths` FROM `MCR6_Stats_Lifetime` WHERE `uuid`='" + p.getUniqueId().toString() + "'").executeQuery();
                currentDeaths += 1;
                if (rs.next()) {
                    Connection.mainConnection().prepareStatement("UPDATE `MCR6_Stats_Lifetime` SET `deaths`=" + currentDeaths + " WHERE `uuid`='" + p.getUniqueId().toString() + "'").executeUpdate();
                } else {
                    Connection.mainConnection().prepareStatement("INSERT INTO `MCR6_Stats_Lifetime`(`uuid`, `kills`, `deaths`, `roundsWon`, `roundsLost`) VALUES ('" + p.getUniqueId().toString() + "',0," + currentDeaths + ",0,0)").executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasOperator(String operator) {
        if(operator.equalsIgnoreCase("rekrut")) {
            return true;
        }
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `" + operator.toLowerCase() + "` FROM `MCR6_Player_Operators` WHERE `uuid`='" + p.getUniqueId().toString() + "'").executeQuery();
            if (rs.next()) {
                return rs.getBoolean(operator.toLowerCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void buyOperator(String operator) {
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `uuid` FROM `MCR6_Player_Operators` WHERE `uuid`='" + p.getUniqueId().toString() + "'").executeQuery();
            if (rs.next()) {
                Connection.mainConnection().prepareStatement("UPDATE `MCR6_Player_Operators` SET `" + operator + "`=true WHERE `uuid`='" + p.getUniqueId().toString() + "'").executeUpdate();
            } else {
                Connection.mainConnection().prepareStatement("INSERT INTO `MCR6_Player_Operators`(`uuid`,`" + operator.toLowerCase() + "`) VALUES ('" + p.getUniqueId().toString() + "',true)").executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

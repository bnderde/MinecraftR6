package de.bnder.rainbowCraft.gamePlay.gameUtils;

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

import de.bnder.rainbowCraft.gamePlay.configs.OperatorConfigs;
import de.bnder.rainbowCraft.main.Main;
import de.bnder.rainbowCraft.utils.Connection;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class GameUtils {

    String game;
    String path;
    public static String mapPrefix = "r6map_";

    public GameUtils(String gameName) {
        game = gameName;
        path = "Game" + "." + gameName + ".";
    }

    public boolean gameExists() {
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT * FROM `MCR6_Games` WHERE `gameName`='" + game + "'").executeQuery();
            return rs.next();
        } catch (Exception e) {}
        return Main.gamesC.get("Game" + "." + game) != null;
    }



    public boolean isRunning() {
        if (gameExists()) {
            try {
                ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `running` FROM `MCR6_Games` WHERE `gameName`='" + game + "'").executeQuery();
                if (rs.next()) return rs.getBoolean("running");
            } catch (Exception e) {}
            if (Main.gamesC.get(path + "running") != null) {
                return Main.gamesC.getBoolean(path + "running");
            }
        }
        return false;
    }


    public ArrayList<String> freeOperators() {
        ArrayList<String> list = new ArrayList<String>(OperatorConfigs.fc.getStringList("Operators"));
        for (Player p : lobbyPlayers()) {
            if (Main.gamesC.get(path + ".player" + "." + p.getUniqueId().toString() + ".operator") != null) {
                try {
                    list.remove(Main.gamesC.getString(path + ".player" + "." + p.getUniqueId().toString() + ".operator"));
                } catch (NullPointerException ingored){}
            }
        }
        if (!list.contains("rekrut")) {
            list.add("rekrut");
        }
        return list;
    }

    public boolean isInLobby(Player p) {
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `inLobby` FROM `MCR6_Games_Players` WHERE `name`='" + game + "' && `playerUUID`='" + p.getUniqueId().toString() + "'").executeQuery();
            if (rs.next()) {
                return  rs.getBoolean("inLobby");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void updateLobbyStatusToTrue(Player p) {
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `inLobby` FROM `MCR6_Games_Players` WHERE `name`='" + game + "' && `playerUUID`='" + p.getUniqueId().toString() + "'").executeQuery();
            if (rs.next()) {
                boolean inLobby = rs.getBoolean("inLobby");
                if (inLobby) {
                    Connection.mainConnection().prepareStatement("UPDATE `MCR6_Games_Players` SET `inLobby`=false WHERE `name`='" + game + "' && `playerUUID`='" + p.getUniqueId().toString() + "'").executeUpdate();
                } else {
                    Connection.mainConnection().prepareStatement("UPDATE `MCR6_Games_Players` SET `inLobby`=true WHERE `name`='" + game + "' && `playerUUID`='" + p.getUniqueId().toString() + "'").executeUpdate();
                }
            } else {
                Connection.mainConnection().prepareStatement("INSERT INTO `MCR6_Games_Players`(`name`, `playerUUID`, `inLobby`) VALUES ('" + game + "', '" + p.getUniqueId().toString() + "', true)").executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateLobbyStatusToFalse(Player p) {
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `playerUUID` FROM `MCR6_Games_Players` WHERE `name`='" + game + "' && `inLobby`=true && `playerUUID`='" + p.getUniqueId().toString() + "'").executeQuery();
            if (rs.next()) {
                Connection.mainConnection().prepareStatement("UPDATE `MCR6_Games_Players` SET `inLobby`=false WHERE `name`='" + game + "' && `playerUUID`='" + p.getUniqueId().toString() + "' && `inLobby`=true").executeUpdate();
            }else {
                Connection.mainConnection().prepareStatement("INSERT INTO `MCR6_Games_Players`(`name`, `playerUUID`, `inLobby`) VALUES ('" + game + "', '" + p.getUniqueId().toString() + "', false)").executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Player> lobbyPlayers() {
        ArrayList<Player> list = new ArrayList<Player>();
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `playerUUID` FROM `MCR6_Games_Players` WHERE `name`='" + game + "' && `inLobby`=true").executeQuery();
            while (rs.next()) {
                list.add(Bukkit.getPlayer(UUID.fromString(rs.getString("playerUUID"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean gameStarting() {
        if (gameExists()) {
            if (Main.gamesC.get(path + game + ".starting") != null) {
                return Main.gamesC.getBoolean(path + game + ".starting");
            }
        }
        return false;
    }

    public void addPlayer(Player p) {
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `playerUUID` FROM `MCR6_Games_Players` WHERE `playerUUID`='" + p.getUniqueId().toString() +"' && `name`='" + game + "'").executeQuery();
            if (!rs.next()) {
                 Connection.mainConnection().prepareStatement("INSERT INTO `MCR6_Games_Players`(`name`, `playerUUID`, `inLobby`) VALUES ('" + game + "','" + p.getUniqueId().toString() + "',true)").executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int ctsRoundsWon() {
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `ctWins` FROM `MCR6_Games` WHERE `gameName`='" + game + "'").executeQuery();
            if (rs.next()) {
                return rs.getInt("ctWins");
            } else {
                Connection.mainConnection().prepareStatement("INSERT INTO `MCR6_Games`(`gameName`, `ctWins`, `tWins`) VALUES ('" + game + "',0,0)").executeUpdate();
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int tsRoundsWon() {
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `tWins` FROM `MCR6_Games` WHERE `gameName`='" + game + "'").executeQuery();
            if (rs.next()) {
                return rs.getInt("tWins");
            } else {
                Connection.mainConnection().prepareStatement("INSERT INTO `MCR6_Games`(`gameName`, `ctWins`, `tWins`) VALUES ('" + game + "',0,0)").executeUpdate();
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void removePlayerComplete(Player p) {
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `playerUUID` FROM `MCR6_Games_Players` WHERE `playerUUID`='" + p.getUniqueId().toString() + "' && `name`='" + game + "'").executeQuery();
            if (rs.next()) {
                Connection.mainConnection().prepareStatement("DELETE FROM `MCR6_Games_Players` WHERE `playerUUID`='" + p.getUniqueId().toString() + "' && `name`='" + game + "'").executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int minPlayers() {
        return 2;
    }

    public long getPlayed() {
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `played` FROM `MCR6_Player_Maps_General` WHERE `mapID`='" + game + "'").executeQuery();
            if (rs.next()) {
                return rs.getLong("played");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int maxPlayers() {
        return 10;
    }

    public ArrayList<String> getCTs() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `playerUUID` FROM `MCR6_Games_Players` WHERE `name`='" + game + "' && `team`='ct'").executeQuery();
            while (rs.next()) {
                list.add(rs.getString("playerUUID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public ArrayList<String> getTs() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `playerUUID` FROM `MCR6_Games_Players` WHERE `name`='" + game + "' && `team`='t'").executeQuery();
            while (rs.next()) {
                list.add(rs.getString("playerUUID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Location> spawns(String side) {
        if (side.equalsIgnoreCase("ct") || side.equalsIgnoreCase("t")) {
            ArrayList<Location> list = new ArrayList<Location>();
            try {
                ResultSet rs = Connection.mainConnection().prepareStatement("SELECT * FROM `MCR6_Map_Spawns` WHERE `mapID`='" + game + "' && `side`='" + side.toLowerCase() + "'").executeQuery();
                World world = Bukkit.getWorld(mapPrefix + game);
                if (world == null) {
                    world = Bukkit.createWorld(new WorldCreator(mapPrefix + game).copy(Bukkit.getWorld("flatWorld")).generateStructures(false).type(WorldType.FLAT).environment(World.Environment.NORMAL));
                }
                while (rs.next()) {
                    double x = rs.getDouble("x");
                    double y = rs.getDouble("y");
                    double z = rs.getDouble("z");
                    double yaw = rs.getDouble("yaw");
                    double pitch = rs.getDouble("pitch");
                    Location loc = new Location(world, x, y, z);
                    loc.setYaw((float) yaw);
                    loc.setPitch((float) pitch);
                    list.add(loc);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            for (String spawn : Main.gamesC.getStringList(path + "spawnList" + "." + side.toLowerCase())) {
//                Location loc = new Location(Bukkit.getWorld(UUID.fromString(Main.gamesC.getString(path + "spawn" + "." + side.toLowerCase() + "." + spawn + ".world"))),
//                        Main.gamesC.getDouble(path + "spawn" + "." + side.toLowerCase() + "." + spawn + ".x"),
//                        Main.gamesC.getDouble(path + "spawn" + "." + side.toLowerCase() + "." + spawn + ".y"),
//                        Main.gamesC.getDouble(path + "spawn" + "." + side.toLowerCase() + "." + spawn + ".z"));
//                list.add(loc);
//            }
            return list;
        }
        return null;
    }

    public ArrayList<Player> players() {
        ArrayList<Player> list = new ArrayList<Player>();
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `playerUUID` FROM `MCR6_Games_Players` WHERE `name`='" + game + "' && `inLobby`=false").executeQuery();
            while (rs.next()) {
                list.add(Bukkit.getPlayer(UUID.fromString(rs.getString("playerUUID"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Location lobbyLocation(Player p) {
        if (gameExists()) {
            //TODO: Apply for BuildMode
//            Location location = new Location(Bukkit.getWorld(UUID.fromString(Main.gamesC.getString(path + "lobby" + ".world"))), Main.gamesC.getDouble(path + "lobby" + ".x"), Main.gamesC.getDouble(path + "lobby" + ".y"), Main.gamesC.getDouble(path + "lobby" + ".z"));
//            location.setYaw(Main.gamesC.getInt(path + "lobby" + ".yaw"));
//            location.setPitch(Main.gamesC.getInt(path + "lobby" + ".pitch"));
//            return location;
//            return Bukkit.getWorld("world").getSpawnLocation();
        }
        return p.getLocation();
    }
    public Location hostageLocation() {
        if (gameExists()) {
            //TODO: Apply for BuildMode

            try {
                ResultSet rs = Connection.mainConnection().prepareStatement("SELECT * FROM `MCR6_Map_Spawns` WHERE `mapID`='" + game + "' && `side`='hostage'").executeQuery();
                if (rs.next()) {
                    double x = rs.getDouble("x");
                    double y = rs.getDouble("y");
                    double z = rs.getDouble("z");
                    double yaw = rs.getDouble("yaw");
                    double pitch = rs.getDouble("pitch");
                    World world = Bukkit.getWorld(mapPrefix + game);
                    if (world == null) {
                        world = Bukkit.createWorld(new WorldCreator(mapPrefix + game).copy(Bukkit.getWorld("flatWorld")).generateStructures(false).type(WorldType.FLAT).environment(World.Environment.NORMAL));
                    }
                    Location loc = new Location(world, x, y, z);
                    loc.setYaw((float) yaw);
                    loc.setPitch((float) pitch);
                    return loc;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Location location = new Location(Bukkit.getWorld(UUID.fromString(Main.gamesC.getString(path + "geisel" + ".world"))), Main.gamesC.getDouble(path + "geisel" + ".x"), Main.gamesC.getDouble(path + "geisel" + ".y"), Main.gamesC.getDouble(path + "geisel" + ".z"));
            location.setYaw(Main.gamesC.getInt(path + "geisel" + ".yaw"));
            location.setPitch(Main.gamesC.getInt(path + "geisel" + ".pitch"));
            return location;
        }
        return null;
    }
    public Location rescuePointLocation() {
        if (gameExists()) {
            try {
                ResultSet rs = Connection.mainConnection().prepareStatement("SELECT * FROM `MCR6_Map_Spawns` WHERE `mapID`='" + game + "' && `side`='ct' LIMIT 1").executeQuery();
                if (rs.next()) {
                    World world = Bukkit.getWorld(mapPrefix + game);
                    if (world == null) {
                        world = Bukkit.createWorld(new WorldCreator(mapPrefix + game).copy(Bukkit.getWorld("flatWorld")).generateStructures(false).type(WorldType.FLAT).environment(World.Environment.NORMAL));
                    }
                    return new Location(world, rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Location location = new Location(Bukkit.getWorld(UUID.fromString(Main.gamesC.getString(path + "rescue" + ".world"))), Main.gamesC.getDouble(path + "rescue" + ".x"), Main.gamesC.getDouble(path + "rescue" + ".y"), Main.gamesC.getDouble(path + "rescue" + ".z"));
            location.setYaw(Main.gamesC.getInt(path + "rescue" + ".yaw"));
            location.setPitch(Main.gamesC.getInt(path + "rescue" + ".pitch"));
            return location;
        }
        return null;
    }

}

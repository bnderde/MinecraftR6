package de.bnder.rainbowCraft.utils;

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

import de.bnder.rainbowCraft.main.Main;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Connection {

    public static java.sql.Connection mainConnection() {
        try {
            PreparedStatement statement = con.prepareStatement("SELECT `*` FROM `MCR6_Games_Players` WHERE 1=1 LIMIT 1");
            ResultSet rs = statement.executeQuery();
            rs.next();
        } catch (Exception e) {

        }

        return con;
    }

    private static java.sql.Connection con;

    public void defineConnection() {
        System.out.println(Main.prefix + " Creating Connection");
        String hostname = Main.configC.getString("MySQL" + ".hostname");
        String dbname = Main.configC.getString("MySQL" + ".dbname");
        String user = Main.configC.getString("MySQL" + ".username");
        String pw = Main.configC.getString("MySQL" + ".password");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + hostname + "/" + dbname + "?autoReconnect=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=CET", user, pw);
            System.out.println(Main.prefix + " Connection created");

            createTables(dbname);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTables(String dbname) throws SQLException {
        System.out.println(Main.prefix + " Checking Tables");
        String tableName = "mcr6_builders";
        if (!con.prepareStatement("SELECT * FROM information_schema.tables WHERE table_schema='" + dbname + "' AND table_name='" + tableName + "' LIMIT 1;").executeQuery().next()) {
            con.prepareStatement("CREATE TABLE " + tableName + " (" +
                    "isBuilding tinyint(1)," +
                    "uuid TEXT NOT NULL" +
                    ")").execute();
            System.out.println(Main.prefix + " Created Table " + tableName);
        }
        tableName = "mcr6_games";
        if (!con.prepareStatement("SELECT * FROM information_schema.tables WHERE table_schema='" + dbname + "' AND table_name='" + tableName + "' LIMIT 1;").executeQuery().next()) {
            con.prepareStatement("CREATE TABLE " + tableName + " (" +
                    "gameName TEXT NOT NULL," +
                    "ctWins bigint(20) DEFAULT 0," +
                    "tWins bigint(20) DEFAULT 0," +
                    "running tinyint(1)" +
                    ")").execute();
            System.out.println(Main.prefix + " Created Table " + tableName);
        }
        tableName = "mcr6_games_players";
        if (!con.prepareStatement("SELECT * FROM information_schema.tables WHERE table_schema='" + dbname + "' AND table_name='" + tableName + "' LIMIT 1;").executeQuery().next()) {
            con.prepareStatement("CREATE TABLE " + tableName + " (" +
                    "name TEXT NOT NULL," +
                    "playerUUID TEXT NOT NULL," +
                    "inLobby tinyint(1) DEFAULT 0," +
                    "team TEXT NULL DEFAULT NULL," +
                    "alive tinyint(1) NOT NULL DEFAULT 1" +
                    ")").execute();
            System.out.println(Main.prefix + " Created Table " + tableName);
        }
        tableName = "mcr6_map_blocks";
        if (!con.prepareStatement("SELECT * FROM information_schema.tables WHERE table_schema='" + dbname + "' AND table_name='" + tableName + "' LIMIT 1;").executeQuery().next()) {
            con.prepareStatement("CREATE TABLE " + tableName + " (" +
                    "mapID text NOT NULL,"+
                    "x int(11) NOT NULL,"+
                    "y int(11) NOT NULL,"+
                    "z int(11) NOT NULL,"+
                    "block text NOT NULL,"+
                    "blockData text NOT NULL"+
                    ")").execute();
            System.out.println(Main.prefix + " Created Table " + tableName);
        }
        tableName = "mcr6_map_cameras";
        if (!con.prepareStatement("SELECT * FROM information_schema.tables WHERE table_schema='" + dbname + "' AND table_name='" + tableName + "' LIMIT 1;").executeQuery().next()) {
            con.prepareStatement("CREATE TABLE " + tableName + " (" +
                    "mapID text NOT NULL,"+
                    "x float NOT NULL,"+
                    "y float NOT NULL,"+
                    "z float NOT NULL,"+
                    "yaw float NOT NULL,"+
                    "pitch float NOT NULL"+
                    ")").execute();
            System.out.println(Main.prefix + " Created Table " + tableName);
        }
        tableName = "mcr6_map_spawns";
        if (!con.prepareStatement("SELECT * FROM information_schema.tables WHERE table_schema='" + dbname + "' AND table_name='" + tableName + "' LIMIT 1;").executeQuery().next()) {
            con.prepareStatement("CREATE TABLE " + tableName + " (" +
                    "mapID text NOT NULL,"+
                    "side text NOT NULL," +
                    "x float NOT NULL,"+
                    "y float NOT NULL,"+
                    "z float NOT NULL,"+
                    "yaw float NOT NULL,"+
                    "pitch float NOT NULL"+
                    ")").execute();
            System.out.println(Main.prefix + " Created Table " + tableName);
        }
        tableName = "mcr6_player_maps_general";
        if (!con.prepareStatement("SELECT * FROM information_schema.tables WHERE table_schema='" + dbname + "' AND table_name='" + tableName + "' LIMIT 1;").executeQuery().next()) {
            con.prepareStatement("CREATE TABLE " + tableName + " (" +
                    "owner text NOT NULL,"+
                    "mapID text NOT NULL," +
                    "mapName text NOT NULL," +
                    "buildSpawnX text NULL DEFAULT NULL," +
                    "buildSpawnY text NULL DEFAULT NULL," +
                    "buildSpawnZ text NULL DEFAULT NULL," +
                    "public tinyint(1) NOT NULL DEFAULT 0," +
                    "upvotes bigint(20) NOT NULL DEFAULT 0," +
                    "downvotes bigint(20) NOT NULL DEFAULT 0," +
                    "played bigint(20) NOT NULL DEFAULT 0" +
                    ")").execute();
            System.out.println(Main.prefix + " Created Table " + tableName);
        }
        tableName = "mcr6_player_operators";
        if (!con.prepareStatement("SELECT * FROM information_schema.tables WHERE table_schema='" + dbname + "' AND table_name='" + tableName + "' LIMIT 1;").executeQuery().next()) {
            con.prepareStatement("CREATE TABLE " + tableName + " (" +
                    "uuid text NOT NULL,"+
                    "sledge tinyint(1) NOT NULL," +
                    "montagne tinyint(1) NOT NULL," +
                    "smoke tinyint(1) NOT NULL," +
                    "lesion tinyint(1) NOT NULL," +
                    "dokkaebi tinyint(1) NOT NULL," +
                    "fuze tinyint(1) NOT NULL," +
                    "flasher tinyint(1) NOT NULL" +
                    ")").execute();
            System.out.println(Main.prefix + " Created Table " + tableName);
        }
        tableName = "mcr6_stats_lifetime";
        if (!con.prepareStatement("SELECT * FROM information_schema.tables WHERE table_schema='" + dbname + "' AND table_name='" + tableName + "' LIMIT 1;").executeQuery().next()) {
            con.prepareStatement("CREATE TABLE " + tableName + " (" +
                    "uuid text NOT NULL,"+
                    "kills bigint(20) NOT NULL DEFAULT 0," +
                    "deaths bigint(20) NOT NULL DEFAULT 0," +
                    "roundsWon bigint(20) NOT NULL DEFAULT 0," +
                    "roundsLost bigint(20) NOT NULL DEFAULT 0" +
                    ")").execute();
            System.out.println(Main.prefix + " Created Table " + tableName);
        }
        System.out.println(Main.prefix + " Tables checked");
    }

}

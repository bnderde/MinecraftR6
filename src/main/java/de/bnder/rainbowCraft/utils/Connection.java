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
        String hostname = Main.configC.getString("MySQL" + ".hostname");
        String dbname = Main.configC.getString("MySQL" + ".dbname");
        String user = Main.configC.getString("MySQL" + ".username");
        String pw = Main.configC.getString("MySQL" + ".password");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + hostname + "/" + dbname + "?autoReconnect=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=CET", user, pw);
        } catch (Exception e) {
            System.exit(900);
        }
    }

}

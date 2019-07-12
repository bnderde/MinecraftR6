package de.bnder.minecraftr6.utils;

import de.bnder.minecraftr6.main.Main;

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

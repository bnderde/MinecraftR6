package de.bnder.rainbowCraft.buildMode.buildUtils;

import de.bnder.rainbowCraft.utils.Connection;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BuilderUtils {

    Player p;

    public static final String buildMapPrefix = "r6Build_";

    public BuilderUtils(Player player) {
        this.p = player;
    }

    public boolean isBuilding() {
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `isBuilding` FROM `MCR6_Builders` WHERE `uuid`='" + p.getUniqueId().toString() + "'").executeQuery();
            if (rs.next()) {
                return rs.getBoolean("isBuilding");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getMapID() {
        if (isBuilding()) {
            String world = p.getWorld().getName();
            if (world.startsWith(buildMapPrefix)) {
                String mapID = world.replaceAll(buildMapPrefix, "");
                try {
                    ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `mapID` FROM `MCR6_Player_Maps_General` WHERE `mapID`='" + mapID + "' && `owner`='" + p.getUniqueId().toString() + "'").executeQuery();
                    if (rs.next()) {
                        String id = rs.getString("mapID");
                        return id;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public boolean isMapPublic(String mapID) {
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `public` FROM `MCR6_Player_Maps_General` WHERE `mapID`='" + mapID + "'").executeQuery();
            if (rs.next()) {
                return rs.getBoolean("public");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setBuilding(boolean status) {
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `isBuilding` FROM `MCR6_Builders` WHERE `uuid`='" + p.getUniqueId().toString() + "'").executeQuery();
            if (rs.next()) {
                Connection.mainConnection().prepareStatement("UPDATE `MCR6_Builders` SET `isBuilding`=" + status + " WHERE `uuid`='" + p.getUniqueId().toString() + "'").executeUpdate();
            } else {
                Connection.mainConnection().prepareStatement("INSERT INTO `MCR6_Builders`(`uuid`, `isBuilding`) VALUES ('" + p.getUniqueId().toString() + "'," + status + ")").executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean mapExists(String mapID) {
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `mapID` FROM `MCR6_Player_Maps_General` WHERE `mapID`='" + mapID + "'").executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}

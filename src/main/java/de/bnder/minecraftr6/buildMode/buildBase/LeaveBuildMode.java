package de.bnder.minecraftr6.buildMode.buildBase;

import de.bnder.minecraftr6.buildMode.buildUtils.BuilderUtils;
import de.bnder.minecraftr6.main.Main;
import de.bnder.minecraftr6.utils.Connection;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LeaveBuildMode {

    public static void leave(Player p) {
        BuilderUtils builderUtils = new BuilderUtils(p);
        if (builderUtils.isBuilding()) {
            if (p.getWorld().getName().startsWith(BuilderUtils.buildMapPrefix)) {
                p.getInventory().clear();
                p.setGameMode(GameMode.SURVIVAL);
                p.performCommand("tptospawn");

                builderUtils.setBuilding(false);

                try {
                    ResultSet mapResult = Connection.mainConnection().prepareStatement("SELECT * FROM `MCR6_Player_Maps_General` WHERE `owner`='" + p.getUniqueId().toString() + "'").executeQuery();
                    if (mapResult.next()) {
                        String id = mapResult.getString("mapID");

                        if (Bukkit.getWorld(BuilderUtils.buildMapPrefix + id) != null) {
                            World buildWorld = Bukkit.getWorld(BuilderUtils.buildMapPrefix + id);
                            Bukkit.unloadWorld(buildWorld, true);
                            try {
                                FileUtils.deleteDirectory(new File(BuilderUtils.buildMapPrefix + id));
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv remove " + BuilderUtils.buildMapPrefix + id);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                p.sendMessage(Main.prefix + " §aDu hast den Bau-Modus verlassen.");
            } else {
                p.sendMessage(Main.prefix + " §cDu bist in keiner Bau-Welt!");
            }
        } else {
            p.sendMessage(Main.prefix + " §cDu bist nicht in dem Bau-Modus!");
        }
    }

}

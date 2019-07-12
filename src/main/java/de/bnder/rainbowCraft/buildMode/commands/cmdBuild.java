package de.bnder.rainbowCraft.buildMode.commands;

import de.bnder.rainbowCraft.buildMode.buildBase.JoinBuildMode;
import de.bnder.rainbowCraft.buildMode.buildUtils.BuilderUtils;
import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import de.bnder.rainbowCraft.main.Main;
import de.bnder.rainbowCraft.utils.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;

public class cmdBuild implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            PlayerUtils playerUtils = new PlayerUtils(p);
            if (!playerUtils.isInGame()) {
                BuilderUtils builderUtils = new BuilderUtils(p);
                if (!builderUtils.isBuilding()) {
                    if (args.length == 0) {
                        String mapID = null;
                        try {
                            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `mapID` FROM `MCR6_Player_Maps_General` WHERE `owner`='" + p.getUniqueId().toString() + "'").executeQuery();
                            if (rs.next()) {
                                mapID = rs.getString("mapID");
                            } else {
                                mapID = newMapID();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (mapID != null) {

                            JoinBuildMode.join(p, mapID);
                        } else {
                            p.sendMessage(Main.prefix + " §cFehler: Es konnte keine Map-ID erfasst werden!");
                        }
                    } else if (args.length == 1) {

                    }
                } else {
                    p.sendMessage(Main.prefix + " §cDu bist bereits im Baumodus!");
                }
            } else {
                p.sendMessage(Main.prefix + " §cDu darfst dafür nicht in einer Runde sein!");
            }
        }
        return false;
    }

    public static String newMapID() {
        try {
            String id = genMapID();
            while (Connection.mainConnection().prepareStatement("SELECT `mapID` FROM `MCR6_Player_Maps_General` WHERE `mapID`='" + id + "'").executeQuery().next()) {
                id = genMapID();
            }
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static String genMapID() {
        StringBuilder builder = new StringBuilder();
        int count = 5;
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}

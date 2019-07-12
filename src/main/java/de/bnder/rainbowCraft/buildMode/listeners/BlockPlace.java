package de.bnder.rainbowCraft.buildMode.listeners;

import de.bnder.rainbowCraft.buildMode.buildUtils.BuilderUtils;
import de.bnder.rainbowCraft.utils.Connection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BlockPlace implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        BuilderUtils builderUtils = new BuilderUtils(p);
        if (p.getWorld().getName().startsWith(BuilderUtils.buildMapPrefix)) {
            if (builderUtils.isBuilding()) {
                String mapID = builderUtils.getMapID();

                String block = e.getBlockPlaced().getBlockData().getMaterial().name();
                String blockData = e.getBlockPlaced().getBlockData().getAsString();
                int x = e.getBlockPlaced().getX();
                int y = e.getBlockPlaced().getY();
                int z = e.getBlockPlaced().getZ();

                try {
                    ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `block` FROM `MCR6_Map_Blocks` WHERE `x`=" + x + " && `y`=" + y + " && `z`=" + z + " && `mapID`='" + mapID + "'").executeQuery();
                    if (rs.next()) {
                        Connection.mainConnection().prepareStatement("UPDATE `MCR6_Map_Blocks` SET `block`='" + block + "', `blockData`='" + blockData + "' WHERE `x`=" + x + " && `y`=" + y + " && `z`=" + z + " && `mapID`='" + mapID + "'").executeUpdate();
                    } else {
                        Connection.mainConnection().prepareStatement("INSERT INTO `MCR6_Map_Blocks`(`mapID`, `x`, `y`, `z`, `block`, `blockData`) VALUES ('" + mapID + "'," + x + "," + y + "," + z + ",'" + block + "','" + blockData + "')").executeUpdate();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}

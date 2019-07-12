package de.bnder.minecraftr6.buildMode.buildBase.placings;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.minecraftr6.buildMode.buildUtils.BuilderUtils;
import de.bnder.minecraftr6.main.Main;
import de.bnder.minecraftr6.utils.Connection;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DefenseSpawnPlacing implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            BuilderUtils builderUtils = new BuilderUtils(p);
            if (builderUtils.isBuilding()) {
                if (p.getInventory().getItemInMainHand() != null) {
                    ItemStack item = p.getInventory().getItemInMainHand();
                    if (item.getType() == Material.REDSTONE) {
                        String itemName = item.getItemMeta().getDisplayName();
                        if (itemName.equalsIgnoreCase("§cVerteidiger-Spawn")) {
                            e.setCancelled(true);
                            String mapID = builderUtils.getMapID();
                            try {
                                ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `mapID` FROM `MCR6_Map_Spawns` WHERE `mapID`='" + mapID + "' && `side`='t'").executeQuery();
                                int i = 0;
                                while (rs.next()) {
                                    i++;
                                }
                                if (i < 5) {
                                    Location loc = p.getLocation();
                                    ArmorStand camera = (ArmorStand) p.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
                                    camera.setHelmet(new ItemStack(Material.REDSTONE_BLOCK));
                                    camera.setCustomName("Verteidiger Spawn");
                                    camera.setSmall(true);
                                    camera.setBasePlate(false);
                                    camera.setCollidable(false);
                                    camera.setGravity(false);
                                    camera.setCustomNameVisible(true);

                                    Connection.mainConnection().prepareStatement("INSERT INTO `MCR6_Map_Spawns`(`mapID`, `side`, `x`, `y`, `z`, `yaw`, `pitch`) VALUES ('" + mapID + "','t'," + Main.RoundTo2Decimals(loc.getX()) + "," + Main.RoundTo2Decimals(loc.getY()) + "," + Main.RoundTo2Decimals(loc.getZ()) + "," + (int) loc.getYaw() + "," + (int) loc.getPitch() + ")").executeUpdate();
                                } else {
                                    p.sendMessage(Main.prefix + " §cDu kannst nur maximal 5 Spawns für eine Seite setzen!");
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}

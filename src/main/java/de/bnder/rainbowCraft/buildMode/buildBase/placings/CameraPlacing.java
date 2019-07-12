package de.bnder.rainbowCraft.buildMode.buildBase.placings;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.rainbowCraft.buildMode.buildUtils.BuilderUtils;
import de.bnder.rainbowCraft.main.Main;
import de.bnder.rainbowCraft.utils.Connection;
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

public class CameraPlacing implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            BuilderUtils builderUtils = new BuilderUtils(p);
            if (builderUtils.isBuilding()) {
                if (p.getInventory().getItemInMainHand() != null) {
                    ItemStack item = p.getInventory().getItemInMainHand();
                    if (item.getType() == Material.ARMOR_STAND) {
                        String itemName = item.getItemMeta().getDisplayName();
                        if (itemName.equalsIgnoreCase("§3Kamera platzieren")) {
                            e.setCancelled(true);
                            String mapID = builderUtils.getMapID();
                            try {
                                ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `mapID` FROM `MCR6_Map_Cameras` WHERE `mapID`='" + mapID + "'").executeQuery();
                                int i = 0;
                                while (rs.next()) {
                                    i++;
                                }
                                if (i < 30) {
                                    Location loc = p.getLocation();
                                    loc.setY(loc.getY() + 0.5);
                                    ArmorStand camera = (ArmorStand) p.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
                                    camera.setHelmet(new ItemStack(Material.FURNACE));
                                    camera.setCustomName("Kamera");
                                    camera.setSmall(true);
                                    camera.setBasePlate(false);
                                    camera.setCollidable(false);
                                    camera.setGravity(false);
                                    camera.setCustomNameVisible(true);

                                    Connection.mainConnection().prepareStatement("INSERT INTO `MCR6_Map_Cameras`(`mapID`, `x`, `y`, `z`, `yaw`, `pitch`) VALUES ('" + mapID + "'," + Main.RoundTo2Decimals(loc.getX()) + "," + Main.RoundTo2Decimals(loc.getY()) + "," + Main.RoundTo2Decimals(loc.getZ()) + "," + (int) loc.getYaw() + "," + (int) loc.getPitch() + ")").executeUpdate();
                                } else {
                                    p.sendMessage(Main.prefix + " §cDu kannst nur maximal 30 Kameras platzieren!");
                                }
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

}

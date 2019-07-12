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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CameraDestroy implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntityType() == EntityType.ARMOR_STAND) {
            ArmorStand entity = (ArmorStand) e.getEntity();
            if (entity.getCustomName() != null && entity.getCustomName().equalsIgnoreCase("Kamera")) {
                if (entity.getWorld().getName().startsWith(BuilderUtils.buildMapPrefix)) {
                    if (entity.getHelmet().getType() == Material.FURNACE) {
                        Location loc = entity.getLocation();
                        try {
                            System.out.println("X: " + Main.RoundTo2Decimals(loc.getX()) + " Y: " + Main.RoundTo2Decimals(loc.getY()) + " Z:" + Main.RoundTo2Decimals(loc.getZ()));
                            String mapID = entity.getWorld().getName().replaceFirst(BuilderUtils.buildMapPrefix, "");
                            Connection.mainConnection().prepareStatement("DELETE FROM `MCR6_Map_Cameras` WHERE `mapID`='" + mapID + "' && `x`=" + Main.RoundTo2Decimals(loc.getX()) + " && `y`=" + Main.RoundTo2Decimals(loc.getY()) + " && `z`=" + Main.RoundTo2Decimals(loc.getZ()) + "").executeUpdate();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}

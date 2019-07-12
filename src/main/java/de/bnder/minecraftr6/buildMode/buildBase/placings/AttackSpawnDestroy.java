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

public class AttackSpawnDestroy implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntityType() == EntityType.ARMOR_STAND) {
            ArmorStand entity = (ArmorStand) e.getEntity();
            if (entity.getCustomName() != null && entity.getCustomName().equalsIgnoreCase("Angreifer Spawn")) {
                if (entity.getWorld().getName().startsWith(BuilderUtils.buildMapPrefix)) {
                    if (entity.getHelmet().getType() == Material.LAPIS_BLOCK) {
                        Location loc = entity.getLocation();
                        try {
                            String mapID = entity.getWorld().getName().replaceFirst(BuilderUtils.buildMapPrefix, "");
                            Connection.mainConnection().prepareStatement("DELETE FROM `MCR6_Map_Spawns` WHERE `mapID`='" + mapID + "' && `side`='ct' && `x`=" + Main.RoundTo2Decimals(loc.getX()) + " && `y`=" +  Main.RoundTo2Decimals(loc.getY()) + " && `z`=" +  Main.RoundTo2Decimals(loc.getZ()) + " && `yaw`=" + (int) loc.getYaw() + " && `pitch`=" + (int) loc.getPitch() + " LIMIT 1").executeUpdate();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}

package de.bnder.rainbowCraft.gamePlay.camera;

import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import de.bnder.rainbowCraft.utils.Connection;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;

public class JoinCameraView {

    public static void join(Player p, String game, String camera) {
        int i = 0;
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT * FROM `MCR6_Map_Cameras` WHERE `mapID`='" + game + "'").executeQuery();
            while (rs.next()) {
                int cam = Integer.parseInt(camera);
                System.out.println(i);
                System.out.println(cam);
                if (i == cam) {
                    Location cameraLoc = getCameraLocation(game, camera);

                    boolean spawnCamera = true;
                    Entity cameraEntity = null;
                    if (cameraLoc.getWorld().getNearbyEntities(cameraLoc, 2, 2, 2).size() > 0) {
                        for (Entity entity : cameraLoc.getWorld().getNearbyEntities(cameraLoc, 2, 2, 2)) {
                            if (entity.getType() == EntityType.ARMOR_STAND) {
                                spawnCamera = false;
                                cameraEntity = entity;
                            }
                        }
                    }

                    if (spawnCamera || cameraEntity == null) {
                        cameraEntity = spawnCamera(cameraLoc);
                    }


                    boolean spawnVillager = true;
                    if (p.getGameMode() != GameMode.SPECTATOR) {
                        for (Entity entity : p.getWorld().getEntitiesByClasses(Villager.class)) {
                            if (entity.getCustomName() != null && entity.getCustomName().equalsIgnoreCase(p.getName())) {
                                spawnVillager = false;
                            }
                        }
                        if (spawnVillager) {
                            Villager villager = (Villager) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
                            villager.setCustomName(p.getName());
                            villager.setCustomNameVisible(true);
                            villager.setCanPickupItems(false);
                            villager.setAI(false);
                            villager.setSilent(true);
                            villager.setAdult();
                            villager.setProfession(Villager.Profession.NONE);
                            villager.setVillagerType(Villager.Type.PLAINS);
                        }
                    }

                    if (p.getSpectatorTarget() != null) {
                        p.setSpectatorTarget(null);
                    }

                    p.setGameMode(GameMode.SPECTATOR);
                    p.setSpectatorTarget(cameraEntity);
                }
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Entity spawnCamera(Location loc) {
        ArmorStand armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        armorStand.setGravity(false);
        armorStand.setArms(false);
        armorStand.setBasePlate(false);
        armorStand.setHelmet(new ItemStack(Material.FURNACE));
        armorStand.setCanPickupItems(false);
        armorStand.setCollidable(false);
        armorStand.setSmall(true);
        armorStand.setVisible(false);
        return armorStand;
    }

    public static Location getCameraLocation(String game, String camera) {
        try {
            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT * FROM `MCR6_Map_Cameras` WHERE `mapID`='" + game + "'").executeQuery();
            int i = 0;
            while (rs.next()) {
                int cam = Integer.parseInt(camera);
                if (i == cam) {
                    Location cameraLoc = new Location(Bukkit.getWorld(GameUtils.mapPrefix + game), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"));
                    cameraLoc.setPitch((float) rs.getDouble("pitch"));
                    cameraLoc.setYaw((float) rs.getDouble("yaw"));
                    return cameraLoc;
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

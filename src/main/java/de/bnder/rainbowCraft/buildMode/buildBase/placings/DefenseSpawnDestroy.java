package de.bnder.rainbowCraft.buildMode.buildBase.placings;

/*
 * Copyright (C) 2019 Jan Brinkmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import de.bnder.rainbowCraft.buildMode.buildUtils.BuilderUtils;
import de.bnder.rainbowCraft.main.Main;
import de.bnder.rainbowCraft.utils.Connection;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DefenseSpawnDestroy implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntityType() == EntityType.ARMOR_STAND) {
            ArmorStand entity = (ArmorStand) e.getEntity();
            if (entity.getCustomName() != null && entity.getCustomName().equalsIgnoreCase("Verteidiger Spawn")) {
                if (entity.getWorld().getName().startsWith(BuilderUtils.buildMapPrefix)) {
                    if (entity.getHelmet().getType() == Material.REDSTONE_BLOCK) {
                        Location loc = entity.getLocation();
                        try {
                            String mapID = entity.getWorld().getName().replaceFirst(BuilderUtils.buildMapPrefix, "");
                            Connection.mainConnection().prepareStatement("DELETE FROM `MCR6_Map_Spawns` WHERE `mapID`='" + mapID + "' && `side`='t' && `x`=" +  Main.RoundTo2Decimals(loc.getX()) + " && `y`=" +  Main.RoundTo2Decimals(loc.getY()) + " && `z`=" +  Main.RoundTo2Decimals(loc.getZ()) + " && `yaw`=" + (int) loc.getYaw() + " && `pitch`=" + (int) loc.getPitch() + " LIMIT 1").executeUpdate();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}

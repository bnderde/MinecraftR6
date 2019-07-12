package de.bnder.rainbowCraft.gamePlay.camera;

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

import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CancelTeleport implements Listener {

    @EventHandler
    public void onTP(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE) {
            PlayerUtils playerUtils = new PlayerUtils(p);
            if (playerUtils.isInGame()) {
                Location pLoc = p.getLocation();
                if (p.getGameMode() == GameMode.SPECTATOR) {
                    if (pLoc.getWorld().getNearbyEntities(e.getFrom(), 2, 2, 2).size() > 0) {
                        for (Entity entity : e.getFrom().getWorld().getNearbyEntities(e.getFrom(), 2, 2, 2)) {
                            if (entity.getType() == EntityType.ARMOR_STAND) {
                                ArmorStand armorStand = (ArmorStand) entity;
                                if (armorStand.getHelmet().getType() == Material.FURNACE) {
                                    p.setGameMode(GameMode.SPECTATOR);
                                    p.setSpectatorTarget(entity);
                                    e.setCancelled(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

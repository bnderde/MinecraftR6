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
import de.bnder.rainbowCraft.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CancelTeleport implements Listener {

    @EventHandler
    public void onTP(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE) {
            if (p.getGameMode() == GameMode.SPECTATOR && JoinCameraView.inCam.contains(p.getUniqueId().toString())) {
                PlayerUtils playerUtils = new PlayerUtils(p);
                if (playerUtils.isInGame()) {
                    if (e.getTo().getWorld() != e.getFrom().getWorld()) {
                        e.setCancelled(true);
                    }
                    if (p.getSpectatorTarget() == null) {
                        e.setCancelled(true);
                        tpPlayerToLoc(p, e);
                    }
                }
            }
        }
    }

    void tpPlayerToLoc(final Player p, PlayerTeleportEvent e) {
        for (final Entity entity : e.getFrom().getWorld().getEntitiesByClasses(Villager.class)) {
            if (entity.getCustomName() != null && entity.getCustomName().equals(p.getName())) {
                p.setGameMode(GameMode.SURVIVAL);
                p.teleport(entity);
                entity.remove();
                e.setCancelled(true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
                    @Override
                    public void run() {
                        p.teleport(entity.getLocation());
                    }
                }, 1);
                break;
            }
        }
    }

}

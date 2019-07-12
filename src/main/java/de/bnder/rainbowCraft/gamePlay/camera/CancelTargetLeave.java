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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class CancelTargetLeave implements Listener {

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        if (e.isSneaking()) {
            if (p.getGameMode() == GameMode.SPECTATOR) {
                PlayerUtils playerUtils = new PlayerUtils(p);
                if (playerUtils.isInGame()) {
                    for (Entity entity : p.getWorld().getEntitiesByClasses(Villager.class)) {
                        if (entity.getCustomName() != null && entity.getCustomName().equals(p.getName())) {
                            p.setGameMode(GameMode.SURVIVAL);
                            p.teleport(entity);
                            entity.remove();
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

}

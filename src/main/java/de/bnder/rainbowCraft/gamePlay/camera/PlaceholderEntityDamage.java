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
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlaceholderEntityDamage implements Listener {

    @EventHandler
    public void onDmg(EntityDamageEvent e) {
        if (e.getEntityType() == EntityType.VILLAGER) {
            Entity entity = e.getEntity();
            if (entity.getCustomName() != null) {
                if (Bukkit.getPlayer(entity.getCustomName()) != null) {
                    Player p = Bukkit.getPlayer(entity.getCustomName());
                    PlayerUtils playerUtils = new PlayerUtils(p);
                    if (playerUtils.isInGame()) {
                        p.setHealth(p.getHealth() - e.getDamage());
                        e.setDamage(0);
                    }
                }
            }
        }
    }

}

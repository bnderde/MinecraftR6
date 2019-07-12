package de.bnder.rainbowCraft.buildMode.buildBase.extrasItem;

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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Interact implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            BuilderUtils builderUtils = new BuilderUtils(p);
            if (builderUtils.isBuilding()) {
                if (p.getInventory().getItemInMainHand().getType() == Material.NETHER_STAR) {
                    if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName() != null && p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§5Extras")) {
                        p.performCommand("buildextras");
                    }
                }
            }

        }
    }

}

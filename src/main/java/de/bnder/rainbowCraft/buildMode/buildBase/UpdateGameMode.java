package de.bnder.rainbowCraft.buildMode.buildBase;

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
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateGameMode implements Listener {

    @EventHandler
    public void onTP(PlayerTeleportEvent e) {
        final Player p = e.getPlayer();
        String worldTo = e.getTo().getWorld().getName();
        System.out.println(worldTo);
        if (worldTo.startsWith(BuilderUtils.buildMapPrefix)) {
            System.out.println("a");
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.setGameMode(GameMode.CREATIVE);
                }
            }.runTaskLater(Main.plugin, 1 * 20);
        }
    }

}

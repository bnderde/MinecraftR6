package de.bnder.rainbowCraft.gamePlay.gameBase.gameManager;

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

import de.bnder.rainbowCraft.main.Main;
import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckRescuePoint {

    public static void check(final String game) {
        final GameUtils gameUtils = new GameUtils(game);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (gameUtils.isRunning()) {
                    Location loc = gameUtils.rescuePointLocation();
                    for (Entity entity : loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
                        if (entity.getType() == EntityType.PLAYER) {
                            Player p = (Player) entity;
                            if (entity.getPassengers().size() > 0) {
                                for (Entity passenger : entity.getPassengers()) {
                                    if (passenger.getType() == EntityType.ARMOR_STAND || passenger.getType() == EntityType.ZOMBIE) {
                                        EndRound.end(game, "ct");
                                        for (Entity passengerPassenger : passenger.getPassengers()) {
                                            passenger.removePassenger(passengerPassenger);
                                        }
                                        p.removePassenger(passenger);
                                        passenger.remove();
                                        cancel();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Main.plugin, 10 * 20, 10);
    }

}

package de.bnder.rainbowCraft.gamePlay.gameBase.features;

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

import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

public class GraplingHook implements Listener {

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if (e.getState() == PlayerFishEvent.State.IN_GROUND) {
            Player p = e.getPlayer();
            PlayerUtils playerUtils = new PlayerUtils(p);
            if (playerUtils.isInGame()) {
                String game = playerUtils.getGame();
                GameUtils gameUtils = new GameUtils(game);
                if (gameUtils.isRunning()) {
                    if (gameUtils.getCTs().contains(p.getUniqueId().toString())) {
                        e.setCancelled(true);
                        e.getHook().remove();
                        Location lc = p.getLocation();
                        Location to = e.getHook().getLocation();
                        lc.setY(lc.getY() + 0.8D);
                        p.teleport(lc);
                        double g = -0.02D;
                        double d = to.distance(lc);
                        double t = d;
                        double v_x = (1.0D + 0.07D * t) * (to.getX() - lc.getX()) / t;
                        double v_y = (1.0D + 0.03D * t) * (to.getY() - lc.getY()) / t - 0.5D * g * t;
                        double v_z = (1.0D + 0.07D * t) * (to.getZ() - lc.getZ()) / t;
                        Vector v = p.getVelocity();
                        v.setX(v_x);
                        v.setY(v_y);
                        v.setZ(v_z);
                        p.setVelocity(v);

                    }
                }
            }
        }
    }

    private void pullEntityToLocation(Entity e, Location loc)
    {
        Location entityLoc = e.getLocation();

        entityLoc.setY(entityLoc.getY() + 0.5D);
        e.teleport(entityLoc);

        double g = -0.08D;
        double d = loc.distance(entityLoc);
        double t = d;

        double v_x = (1.0D + 0.07000000000000001D * t) * (loc.getX() - entityLoc.getX()) / t;
        double v_y = (1.0D + 0.03D * t) * (loc.getY() - entityLoc.getY()) / t - 0.5D * g * t;
        double v_z = (1.0D + 0.07000000000000001D * t) * (loc.getZ() - entityLoc.getZ()) / t;

        Vector v = e.getVelocity();

        v.setX(v_x);
        v.setY(v_y);
        v.setZ(v_z);

        e.setVelocity(v);
    }

}

package de.bnder.rainbowCraft.gamePlay.operators;

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
import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Smoke implements Listener {

    static int count = 0;

    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        Player p = (Player) e.getEntity().getShooter();
        PlayerUtils playerUtils = new PlayerUtils(p);
        if (playerUtils.isInGame()) {
            String game = playerUtils.getGame();
            if (Main.gamesC.getString("Game" + "." + game + ".player" + "." + p.getUniqueId().toString() + ".operator") != null && Main.gamesC.getString("Game" + "." + game + ".player" + "." + p.getUniqueId().toString() + ".operator").equalsIgnoreCase("smoke")) {
                Location hit;
                if (e.getHitEntity() != null) {
                    hit = e.getHitEntity().getLocation();
                } else if (e.getHitBlock() != null) {
                    hit = e.getHitBlock().getLocation();
                } else {
                    hit = e.getEntity().getLocation();
                }
                hit.setY(hit.getY() + 2);
                if (e.getEntity().getType() == EntityType.EGG) {
                    AreaEffectCloud potion = (AreaEffectCloud) hit.getWorld().spawnEntity(hit,
                            EntityType.AREA_EFFECT_CLOUD);
                    potion.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 2 * 20, 0), false);
                    potion.setSource(p);
                    potion.setDuration(10 * 20);
                } else if (e.getEntity().getType() == EntityType.SNOWBALL) {
                    final Location finalHit = hit;
                    new BukkitRunnable()
                    {
                        public void run()
                        {
                            if (count < 25) {
                                finalHit.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, finalHit, 3);
                                count++;
                            } else {
                                count = 0;
                                cancel();
                            }
                        }
                    }.runTaskTimer(Main.plugin, 5, 5);
                }
            }
        }
    }

}

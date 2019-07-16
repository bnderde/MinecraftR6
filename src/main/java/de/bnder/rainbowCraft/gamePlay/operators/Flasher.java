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

import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import de.bnder.rainbowCraft.main.Main;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static de.bnder.rainbowCraft.main.Main.plugin;

public class Flasher implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            final Player p = e.getPlayer();
            PlayerUtils playerUtils = new PlayerUtils(p);
            if (playerUtils.isInGame()) {
                final String game = playerUtils.getGame();
                final GameUtils gameUtils = new GameUtils(playerUtils.getGame());
                if (Main.gamesC.getString("Game" + "." + game + ".player" + "." + p.getUniqueId().toString() + ".operator") != null && Main.gamesC.getString("Game" + "." + game + ".player" + "." + p.getUniqueId().toString() + ".operator").equalsIgnoreCase("flasher")) {
                    if (p.getInventory().getItemInMainHand().getType() == Material.FIREWORK_STAR) {
                        p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);

                        final Location shootFromLoc = p.getLocation();

                        Item grenade = shootFromLoc.getWorld().dropItem(shootFromLoc, new ItemStack(Material.FIREWORK_STAR));
                        grenade.setPickupDelay(400);
                        Vector vector = shootFromLoc.getDirection();
                        vector.multiply(2.5);
                        grenade.setVelocity(vector);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                List<Entity> entList = shootFromLoc.getWorld().getEntities();
                                for (Entity current : entList) {
                                    if (current.getType() == EntityType.DROPPED_ITEM) {
                                        Item item = (Item) current;
                                        if (item.getItemStack().getType() == Material.FIREWORK_STAR) {
                                            ArrayList<String> targets = null;
                                            if (gameUtils.getCTs().contains(p.getUniqueId().toString())) {
                                                targets = gameUtils.getTs();
                                            } else if (gameUtils.getTs().contains(p.getUniqueId().toString())) {
                                                targets = gameUtils.getCTs();
                                            }
                                            targets.add(p.getUniqueId().toString());

                                            for (String uuid : targets) {
                                                Player pl = Bukkit.getPlayer(UUID.fromString(uuid));
                                                if (pl.hasLineOfSight(current)) {
                                                    pl.spawnParticle(Particle.FLASH, current.getLocation(), 2);
                                                    pl.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3*20, 3, false, false));
                                                }
                                                pl.playSound(current.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, 1);
                                            }
                                            current.remove();
                                        }
                                    }
                                }
                            }
                        }.runTaskLater(plugin, 3 * 20);
                    }
                }
            }
        }
    }
}

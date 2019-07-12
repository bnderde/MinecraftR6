package de.bnder.rainbowCraft.gamePlay.gameBase.core;

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
import de.bnder.rainbowCraft.gamePlay.gameUtils.WeaponUtils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class Shoot implements Listener {

    HashMap<Player, Long> playerTimestampMap = new HashMap<Player, Long>();

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            PlayerUtils pUtils = new PlayerUtils(p);

            if (pUtils.isInGame()) {
                if (p.getInventory().getItemInMainHand() != null) {
                    ItemStack itemInHand = p.getInventory().getItemInMainHand();
                    ItemMeta itemInHandMeta = itemInHand.getItemMeta();
                    if (itemInHandMeta != null && itemInHandMeta.getDisplayName() != null) {
                        if (itemInHandMeta.getDisplayName().startsWith(WeaponUtils.WEAPON_NAME_PREFIX)) {
                            WeaponUtils weaponUtils = new WeaponUtils(itemInHandMeta.getDisplayName());
                            long ammo = weaponUtils.getMagazinAmmo();
                            long maxAmmo = weaponUtils.getMaxMagazinAmmo();
                            if (ammo > 0) {
                                ammo -= 1;
                                if (!playerTimestampMap.containsKey(p) || playerTimestampMap.get(p) < System.currentTimeMillis() - (weaponUtils.getTicksBetweenShots() * 50)) {
                                    if (!pUtils.isReloading()) {

                                        Vector vector = p.getLocation().getDirection();

                                        Arrow arrow = p.launchProjectile(Arrow.class, vector);

                                        arrow.setGravity(false);
                                        arrow.setPickupStatus(Arrow.PickupStatus.CREATIVE_ONLY);
                                        arrow.setVelocity(vector.multiply(7.5D));
                                        arrow.setKnockbackStrength(0);
                                        arrow.setDamage(weaponUtils.getDamage());

                                        itemInHandMeta.setDisplayName(itemInHandMeta.getDisplayName().split("\\(")[0] + "(" + ammo + "/" + maxAmmo + ")");
                                        itemInHand.setItemMeta(itemInHandMeta);

                                        p.setLevel(0);

                                        float percent = (((float) ammo / maxAmmo));
                                        p.setExp(percent);

                                        e.setCancelled(true);

                                        playerTimestampMap.remove(p);
                                        playerTimestampMap.put(p, System.currentTimeMillis());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

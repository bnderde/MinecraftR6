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

import de.bnder.rainbowCraft.main.Main;
import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import de.bnder.rainbowCraft.gamePlay.gameUtils.WeaponUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Reload implements Listener {

    public static ArrayList<Player> reloadingPlayers = new ArrayList<Player>();

    @EventHandler
    public void onDrop(final PlayerDropItemEvent e) {
        final Player p = e.getPlayer();
        final PlayerUtils pUtils = new PlayerUtils(p);
        if (pUtils.isInGame()) {
            final ItemStack itemStack = e.getItemDrop().getItemStack();
            if (itemStack.getType() == Material.STICK) {
                final ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta.getDisplayName() != null && itemMeta.getDisplayName().startsWith(WeaponUtils.WEAPON_NAME_PREFIX)) {
                    e.setCancelled(true);
                    if (!pUtils.isReloading()) {
                        final WeaponUtils weaponUtils = new WeaponUtils(itemMeta.getDisplayName());

                        final int heldItemSlot = p.getInventory().getHeldItemSlot();

                        final long toReach = weaponUtils.getMaxMagazinAmmo();
                        int ammo = Integer.parseInt(itemMeta.getDisplayName().split("\\(")[1].split("/")[0]);
                        if (ammo < toReach) {
                            pUtils.changeReloadingState();
                            new BukkitRunnable() {
                                public void run() {
                                    if (p.getInventory().getItem(0).getType() == Material.STICK) {
                                        int ammo = Integer.parseInt(itemMeta.getDisplayName().split("\\(")[1].split("/")[0]);

                                        itemMeta.setDisplayName(itemMeta.getDisplayName().split("\\(")[0] + "(" + ++ammo + "/" + toReach + ")");
                                        itemStack.setItemMeta(itemMeta);
                                        p.getInventory().setHeldItemSlot(heldItemSlot);
                                        p.getInventory().setItemInMainHand(itemStack);

                                        if (ammo == toReach) {
                                            pUtils.changeReloadingState();
                                            cancel();
                                        }
                                    } else {
                                        cancel();
                                    }
                                }
                            }.runTaskTimer(Main.plugin, 0, weaponUtils.getReloadPeriodTicks());
                        }
                    }
                }
            }
        }
    }

}

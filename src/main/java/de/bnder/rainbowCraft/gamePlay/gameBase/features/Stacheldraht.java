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

import de.bnder.rainbowCraft.main.Main;
import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Stacheldraht implements Listener {

    public static void check(final String game) {
        new BukkitRunnable() {

            @Override
            public void run() {
                GameUtils gameUtils = new GameUtils(game);
                if (gameUtils.isRunning()) {
                    for (Player p : gameUtils.players()) {
                        if (gameUtils.getCTs().contains(p.getUniqueId().toString())) {
                            Location pLoc = p.getLocation();
                            if (pLoc.getBlock().getType() == Material.DEAD_FIRE_CORAL_FAN) {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 5, false, false));
                            }
                        }
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Main.plugin, 0, 3);
    }

    @EventHandler
    public void onStacheldrahtPlace(BlockBreakEvent e) {
        Player p = e.getPlayer();
        PlayerUtils playerUtils = new PlayerUtils(p);
        if (playerUtils.isInGame() && p.getWorld().getName().toLowerCase().startsWith(GameUtils.mapPrefix)) {
            if (e.getBlock().getType() == Material.DEAD_FIRE_CORAL_FAN) {
                GameUtils gameUtils = new GameUtils(playerUtils.getGame());
                if (gameUtils.getTs().contains(p.getUniqueId().toString())) {
                    ItemStack itemStack = new ItemStack(Material.DEAD_FIRE_CORAL_FAN);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName("§7Stacheldraht");
                    itemStack.setAmount(1);
                    itemStack.setItemMeta(itemMeta);
                    e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), itemStack);
                }
            }
        }
    }

}

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

import de.bnder.rainbowCraft.gamePlay.gameBase.gameManager.StartGame;
import de.bnder.rainbowCraft.main.Main;
import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class C4Placement implements Listener {

    @EventHandler
    public void onPlace(final PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            PlayerUtils playerUtils = new PlayerUtils(p);
            if (playerUtils.isInGame()) {
                if (p.getInventory().getItemInMainHand() != null) {
                    ItemStack itemStack = p.getInventory().getItemInMainHand();
                    if (itemStack.getType() == Material.STONE_BUTTON) {
                        if (itemStack.getItemMeta().getDisplayName() != null) {
                            if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(StartGame.c4ItemName)) {
                                GameUtils gameUtils = new GameUtils(playerUtils.getGame());
                                final ArrayList<Player> players = new ArrayList<Player>();
                                players.addAll(gameUtils.players());
                                for (Player all : players) {
                                    all.playSound(e.getClickedBlock().getLocation(), Sound.BLOCK_GRASS_PLACE, 10, 1);
                                }

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        TNTPrimed tnt = (TNTPrimed) e.getClickedBlock().getWorld().spawnEntity(e.getClickedBlock().getLocation(), EntityType.PRIMED_TNT);
                                        tnt.setFuseTicks(0);
                                    }
                                }.runTaskLater(Main.plugin, 3*20);
                                p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
                            }
                        }
                    }
                }
            }
        }
    }

}

package de.bnder.rainbowCraft.gamePlay.gameBase.lobby;

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
import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import de.bnder.rainbowCraft.main.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MapChoseListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() != null && e.getClickedInventory() != null && e.getInventory() != null) {
            Player p = (Player) e.getWhoClicked();
            if (e.getView().getTitle().equalsIgnoreCase("§3Maps")) {
                e.setCancelled(true);
                BuilderUtils builderUtils = new BuilderUtils(p);
                if (!builderUtils.isBuilding()) {
                    PlayerUtils playerUtils = new PlayerUtils(p);
                    if (!playerUtils.isInGame()) {
                        ItemStack item = e.getCurrentItem();
                        ItemMeta itemMeta = item.getItemMeta();
                        if (itemMeta.getLore().get(itemMeta.getLore().size() - 1).startsWith("§7ID: ")) {
                            String id = itemMeta.getLore().get(itemMeta.getLore().size() - 1).replace("§7ID: ", "");
                            GameUtils game = new GameUtils(id);
                            if (!game.isRunning()) {
                                p.performCommand("join " + id);
                            } else {
                                p.sendMessage(Main.prefix + " §cDu bist bereits in einem Spiel!");
                            }
                        } else {
                            p.sendMessage(Main.prefix + " §cDas Spiel läuft bereits!");
                        }
                    } else {
                        p.sendMessage(Main.prefix + " §cDu bist bereits in einem Spiel!");
                    }
                } else {
                    p.sendMessage(Main.prefix + " §cDu bist aktuell im Bau-Modus!");
                }
            }
        }
    }
}

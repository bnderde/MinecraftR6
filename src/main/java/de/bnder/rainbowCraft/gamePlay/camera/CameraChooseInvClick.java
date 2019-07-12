package de.bnder.rainbowCraft.gamePlay.camera;

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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CameraChooseInvClick implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null) {
            if (e.getInventory() != null) {
                if (e.getCurrentItem() != null) {
                    if (e.getView().getTitle().equalsIgnoreCase("§5Kameras")) {
                        e.setCancelled(true);
                        Player p = (Player) e.getWhoClicked();
                        PlayerUtils playerUtils = new PlayerUtils(p);
                        if (playerUtils.isInGame()) {
                            String itemName = e.getCurrentItem().getItemMeta().getDisplayName();
                            if (itemName != null) {
                                String cam = itemName.split(": ")[1];
                                JoinCameraView.join(p, playerUtils.getGame(), cam);
                                e.setCancelled(true);
                                p.closeInventory();
                            }
                        }
                    }
                }
            }
        }
    }
}

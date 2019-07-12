package de.bnder.rainbowCraft.buildMode.buildBase.extrasItem;

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
import de.bnder.rainbowCraft.utils.Connection;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;

public class RenameInvClick implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() != null && e.getClickedInventory() != null && e.getInventory() != null) {
            if (e.getView().getTitle().equalsIgnoreCase("§eName ändern")) {
                if (e.getInventory().getType() == InventoryType.ANVIL) {
                    Player p = (Player) e.getWhoClicked();
                    AnvilInventory inv = (AnvilInventory) e.getInventory();
                    String name = inv.getRenameText();
                    BuilderUtils builderUtils = new BuilderUtils(p);
                    String mapID = builderUtils.getMapID();

                    try {
                        Connection.mainConnection().prepareStatement("UPDATE `MCR6_Player_Maps_General` SET `mapName`='" + name + "' WHERE `owner`='" + p.getUniqueId().toString() + "' && `mapID`='" +mapID + "'").executeUpdate();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3, 3);
                }
            }
        }
    }

}

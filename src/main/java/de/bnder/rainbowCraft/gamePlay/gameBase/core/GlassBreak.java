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

import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.io.IOException;
import java.util.ArrayList;

import static de.bnder.rainbowCraft.gamePlay.gameBase.MapReset.mapChanges;
import static de.bnder.rainbowCraft.gamePlay.gameBase.MapReset.mapChangesC;

public class GlassBreak implements Listener {

    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        if (e.getHitBlock() != null && e.getHitEntity() == null && e.getEntityType() == EntityType.ARROW) {
            Block b = e.getHitBlock();

            if (b.getType().name().toLowerCase().contains("glass")) {
                String path = "Map" + "." + b.getWorld().getName().replace(GameUtils.mapPrefix, "");
                String block = b.getType().name() + ";" + b.getBlockData().getAsString() + ";" + b.getWorld().getName() + ";" + b.getX() + ";" + b.getY() + ";" + b.getZ();
                ArrayList<String> list = new ArrayList<String>();
                list.addAll(mapChangesC.getStringList(path));
                list.add(block);
                mapChangesC.set(path, list);
                try {
                    mapChangesC.save(mapChanges);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }


                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.playSound(b.getLocation(), Sound.BLOCK_GLASS_BREAK, 100, 1);
                }
                b.breakNaturally();

                e.getEntity().remove();
            }
        }
    }
}

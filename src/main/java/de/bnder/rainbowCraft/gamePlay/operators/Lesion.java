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
import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Lesion {

    public static void checkPlayers(String game) {
        final GameUtils gameUtils = new GameUtils(game);
        final ArrayList<Player> players = gameUtils.players();
        new BukkitRunnable()
        {
            public void run()
            {
                if (gameUtils.isRunning()) {
                    for (Player p : players) {
                        Block block = p.getWorld().getBlockAt(p.getLocation());
                        if (block.getType() == Material.OAK_BUTTON) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 5 * 20, 0));
                            block.setType(Material.AIR);
                            p.sendMessage(Main.prefix + " §cDu bist in einen §aGiftstachel §cgelaufen!");
                        }
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Main.plugin, 5*20, 5);
    }

}

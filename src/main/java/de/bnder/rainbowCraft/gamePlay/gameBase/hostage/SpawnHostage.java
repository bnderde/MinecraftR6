package de.bnder.rainbowCraft.gamePlay.gameBase.hostage;

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
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;

public class SpawnHostage {

    public static void spawn(String map) {
        GameUtils gameUtils = new GameUtils(map);
        Location loc = gameUtils.hostageLocation();
        World world = loc.getWorld();
        if (world.getEntitiesByClasses(Zombie.class).size() > 0) {
            for (Zombie zombie : world.getEntitiesByClass(Zombie.class)) {
                zombie.remove();
            }
        }

        Zombie zombie = (Zombie) world.spawnEntity(loc, EntityType.ZOMBIE);
        zombie.setAI(false);
        zombie.setCanPickupItems(false);
        zombie.setSeed(0);
        zombie.setSilent(true);
        zombie.setBaby(false);
        zombie.setCollidable(true);
    }

}

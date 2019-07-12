package de.bnder.minecraftr6.gamePlay.gameBase.hostage;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.minecraftr6.gamePlay.gameUtils.GameUtils;
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

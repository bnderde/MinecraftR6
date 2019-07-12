package de.bnder.minecraftr6.gamePlay.gameBase.gameManager;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.minecraftr6.main.Main;
import de.bnder.minecraftr6.gamePlay.gameUtils.GameUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckRescuePoint {

    public static void check(final String game) {
        final GameUtils gameUtils = new GameUtils(game);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (gameUtils.isRunning()) {
                    Location loc = gameUtils.rescuePointLocation();
                    for (Entity entity : loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
                        if (entity.getType() == EntityType.PLAYER) {
                            Player p = (Player) entity;
                            if (entity.getPassengers().size() > 0) {
                                for (Entity passenger : entity.getPassengers()) {
                                    if (passenger.getType() == EntityType.ARMOR_STAND || passenger.getType() == EntityType.ZOMBIE) {
                                        EndRound.end(game, "ct");
                                        for (Entity passengerPassenger : passenger.getPassengers()) {
                                            passenger.removePassenger(passengerPassenger);
                                        }
                                        p.removePassenger(passenger);
                                        passenger.remove();
                                        cancel();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Main.plugin, 10 * 20, 10);
    }

}

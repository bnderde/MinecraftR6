package de.bnder.minecraftr6.gamePlay.gameBase.features;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.minecraftr6.gamePlay.gameUtils.GameUtils;
import de.bnder.minecraftr6.gamePlay.gameUtils.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

public class GraplingHook implements Listener {

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if (e.getState() == PlayerFishEvent.State.IN_GROUND) {
            Player p = e.getPlayer();
            PlayerUtils playerUtils = new PlayerUtils(p);
            if (playerUtils.isInGame()) {
                String game = playerUtils.getGame();
                GameUtils gameUtils = new GameUtils(game);
                if (gameUtils.isRunning()) {
                    if (gameUtils.getCTs().contains(p.getUniqueId().toString())) {
                        e.setCancelled(true);
                        e.getHook().remove();
                        Location lc = p.getLocation();
                        Location to = e.getHook().getLocation();
                        lc.setY(lc.getY() + 0.8D);
                        p.teleport(lc);
                        double g = -0.02D;
                        double d = to.distance(lc);
                        double t = d;
                        double v_x = (1.0D + 0.07D * t) * (to.getX() - lc.getX()) / t;
                        double v_y = (1.0D + 0.03D * t) * (to.getY() - lc.getY()) / t - 0.5D * g * t;
                        double v_z = (1.0D + 0.07D * t) * (to.getZ() - lc.getZ()) / t;
                        Vector v = p.getVelocity();
                        v.setX(v_x);
                        v.setY(v_y);
                        v.setZ(v_z);
                        p.setVelocity(v);

                    }
                }
            }
        }
    }

    private void pullEntityToLocation(Entity e, Location loc)
    {
        Location entityLoc = e.getLocation();

        entityLoc.setY(entityLoc.getY() + 0.5D);
        e.teleport(entityLoc);

        double g = -0.08D;
        double d = loc.distance(entityLoc);
        double t = d;

        double v_x = (1.0D + 0.07000000000000001D * t) * (loc.getX() - entityLoc.getX()) / t;
        double v_y = (1.0D + 0.03D * t) * (loc.getY() - entityLoc.getY()) / t - 0.5D * g * t;
        double v_z = (1.0D + 0.07000000000000001D * t) * (loc.getZ() - entityLoc.getZ()) / t;

        Vector v = e.getVelocity();

        v.setX(v_x);
        v.setY(v_y);
        v.setZ(v_z);

        e.setVelocity(v);
    }

}

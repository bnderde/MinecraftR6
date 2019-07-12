package de.bnder.rainbowCraft.gamePlay.camera;

import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CancelTeleport implements Listener {

    @EventHandler
    public void onTP(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE) {
            PlayerUtils playerUtils = new PlayerUtils(p);
            if (playerUtils.isInGame()) {
                Location pLoc = p.getLocation();
                if (p.getGameMode() == GameMode.SPECTATOR) {
                    if (pLoc.getWorld().getNearbyEntities(e.getFrom(), 2, 2, 2).size() > 0) {
                        for (Entity entity : e.getFrom().getWorld().getNearbyEntities(e.getFrom(), 2, 2, 2)) {
                            if (entity.getType() == EntityType.ARMOR_STAND) {
                                ArmorStand armorStand = (ArmorStand) entity;
                                if (armorStand.getHelmet().getType() == Material.FURNACE) {
                                    p.setGameMode(GameMode.SPECTATOR);
                                    p.setSpectatorTarget(entity);
                                    e.setCancelled(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

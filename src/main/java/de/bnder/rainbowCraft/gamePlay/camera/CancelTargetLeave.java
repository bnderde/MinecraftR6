package de.bnder.rainbowCraft.gamePlay.camera;

import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class CancelTargetLeave implements Listener {

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        if (e.isSneaking()) {
            if (p.getGameMode() == GameMode.SPECTATOR) {
                PlayerUtils playerUtils = new PlayerUtils(p);
                if (playerUtils.isInGame()) {
                    for (Entity entity : p.getWorld().getEntitiesByClasses(Villager.class)) {
                        if (entity.getCustomName() != null && entity.getCustomName().equals(p.getName())) {
                            p.setGameMode(GameMode.SURVIVAL);
                            p.teleport(entity);
                            entity.remove();
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

}

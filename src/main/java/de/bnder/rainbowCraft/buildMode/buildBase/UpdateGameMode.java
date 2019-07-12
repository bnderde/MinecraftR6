package de.bnder.rainbowCraft.buildMode.buildBase;

import de.bnder.rainbowCraft.buildMode.buildUtils.BuilderUtils;
import de.bnder.rainbowCraft.main.Main;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateGameMode implements Listener {

    @EventHandler
    public void onTP(PlayerTeleportEvent e) {
        final Player p = e.getPlayer();
        String worldTo = e.getTo().getWorld().getName();
        System.out.println(worldTo);
        if (worldTo.startsWith(BuilderUtils.buildMapPrefix)) {
            System.out.println("a");
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.setGameMode(GameMode.CREATIVE);
                }
            }.runTaskLater(Main.plugin, 1 * 20);
        }
    }

}

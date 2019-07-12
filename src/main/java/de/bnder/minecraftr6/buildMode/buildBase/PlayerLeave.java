package de.bnder.minecraftr6.buildMode.buildBase;

import de.bnder.minecraftr6.buildMode.buildUtils.BuilderUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        BuilderUtils builderUtils = new BuilderUtils(p);
        if (builderUtils.isBuilding()) {
            LeaveBuildMode.leave(p);
        }
    }

}

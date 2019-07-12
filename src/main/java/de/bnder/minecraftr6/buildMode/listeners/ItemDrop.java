package de.bnder.minecraftr6.buildMode.listeners;

import de.bnder.minecraftr6.buildMode.buildUtils.BuilderUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDrop implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        BuilderUtils builderUtils = new BuilderUtils(p);
        if (builderUtils.isBuilding()) {
            e.getItemDrop().remove();
        }
    }

}

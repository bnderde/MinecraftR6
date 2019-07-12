package de.bnder.minecraftr6.buildMode.buildBase.extrasItem;

import de.bnder.minecraftr6.buildMode.buildUtils.BuilderUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Interact implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            BuilderUtils builderUtils = new BuilderUtils(p);
            if (builderUtils.isBuilding()) {
                if (p.getInventory().getItemInMainHand().getType() == Material.NETHER_STAR) {
                    if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName() != null && p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§5Extras")) {
                        p.performCommand("buildextras");
                    }
                }
            }

        }
    }

}

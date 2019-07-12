package de.bnder.minecraftr6.buildMode.buildBase.extrasItem;

import de.bnder.minecraftr6.buildMode.buildUtils.BuilderUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CancelDrop implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        BuilderUtils builderUtils = new BuilderUtils(p);
        if (builderUtils.isBuilding()) {
            ItemStack itemStack = e.getItemDrop().getItemStack();
            if(itemStack.getType() == Material.NETHER_STAR) {
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta.getDisplayName().equalsIgnoreCase("§5Extras")) {
                    e.setCancelled(true);
                }
            }
        }
    }

}

package de.bnder.rainbowCraft.gamePlay.camera;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CameraChooseInvClick implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null) {
            if (e.getInventory() != null) {
                if (e.getCurrentItem() != null) {
                    if (e.getView().getTitle().equalsIgnoreCase("§5Kameras")) {
                        e.setCancelled(true);
                        Player p = (Player) e.getWhoClicked();
                        PlayerUtils playerUtils = new PlayerUtils(p);
                        if (playerUtils.isInGame()) {
                            String itemName = e.getCurrentItem().getItemMeta().getDisplayName();
                            if (itemName != null) {
                                String cam = itemName.split(": ")[1];
                                JoinCameraView.join(p, playerUtils.getGame(), cam);
                                e.setCancelled(true);
                                p.closeInventory();
                            }
                        }
                    }
                }
            }
        }
    }
}

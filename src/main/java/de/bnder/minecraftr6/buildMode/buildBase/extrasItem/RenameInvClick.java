package de.bnder.minecraftr6.buildMode.buildBase.extrasItem;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.minecraftr6.buildMode.buildUtils.BuilderUtils;
import de.bnder.minecraftr6.utils.Connection;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;

public class RenameInvClick implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() != null && e.getClickedInventory() != null && e.getInventory() != null) {
            if (e.getView().getTitle().equalsIgnoreCase("§eName ändern")) {
                if (e.getInventory().getType() == InventoryType.ANVIL) {
                    Player p = (Player) e.getWhoClicked();
                    AnvilInventory inv = (AnvilInventory) e.getInventory();
                    String name = inv.getRenameText();
                    BuilderUtils builderUtils = new BuilderUtils(p);
                    String mapID = builderUtils.getMapID();

                    try {
                        Connection.mainConnection().prepareStatement("UPDATE `MCR6_Player_Maps_General` SET `mapName`='" + name + "' WHERE `owner`='" + p.getUniqueId().toString() + "' && `mapID`='" +mapID + "'").executeUpdate();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3, 3);
                }
            }
        }
    }

}

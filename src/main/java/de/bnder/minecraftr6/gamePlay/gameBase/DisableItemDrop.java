package de.bnder.minecraftr6.gamePlay.gameBase;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.minecraftr6.gamePlay.gameUtils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DisableItemDrop implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        PlayerUtils playerUtils = new PlayerUtils(p);
        if (playerUtils.isInGame()) {
            e.setCancelled(true);
        }
    }

}

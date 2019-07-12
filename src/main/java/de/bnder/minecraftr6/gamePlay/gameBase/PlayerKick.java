package de.bnder.minecraftr6.gamePlay.gameBase;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.minecraftr6.gamePlay.commands.normal.cmdLeave;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class PlayerKick implements Listener {

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        Player p = e.getPlayer();
        cmdLeave.playerLeaveGame(p, false);
    }

}

package de.bnder.rainbowCraft.gamePlay.gameBase.lobby;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class NoDamage implements Listener {

    @EventHandler
    public void onDMG(EntityDamageEvent e) {
        if (e.getEntity().getType() == EntityType.PLAYER) {
            Player p = (Player) e.getEntity();
            PlayerUtils playerUtils = new PlayerUtils(p);
            if (playerUtils.isInGame()) {
                GameUtils gameUtils = new GameUtils(playerUtils.getGame());
                if (gameUtils.isInLobby(p)) {
                    e.setCancelled(true);
                }
            }
        }
    }

}

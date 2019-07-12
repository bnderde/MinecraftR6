package de.bnder.rainbowCraft.gamePlay.gameBase.core;

import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class AntiDamageSlowDown implements Listener {

    @EventHandler
    public void onDMG(EntityDamageEvent e) {
        if (e.getEntity().getType() == EntityType.PLAYER) {
            Player p = (Player) e.getEntity();
            PlayerUtils playerUtils = new PlayerUtils(p);
            if (playerUtils.isInGame()) {
                p.setNoDamageTicks(1);
            }
        }
    }

}

package de.bnder.minecraftr6.gamePlay.camera;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.minecraftr6.gamePlay.gameUtils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlaceholderEntityDamage implements Listener {

    @EventHandler
    public void onDmg(EntityDamageEvent e) {
        if (e.getEntityType() == EntityType.VILLAGER) {
            Entity entity = e.getEntity();
            if (entity.getCustomName() != null) {
                if (Bukkit.getPlayer(entity.getCustomName()) != null) {
                    Player p = Bukkit.getPlayer(entity.getCustomName());
                    PlayerUtils playerUtils = new PlayerUtils(p);
                    if (playerUtils.isInGame()) {
                        p.setHealth(p.getHealth() - e.getDamage());
                        e.setDamage(0);
                    }
                }
            }
        }
    }

}

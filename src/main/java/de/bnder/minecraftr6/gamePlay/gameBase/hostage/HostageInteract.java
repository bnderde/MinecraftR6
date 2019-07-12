package de.bnder.minecraftr6.gamePlay.gameBase.hostage;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.minecraftr6.gamePlay.gameUtils.GameUtils;
import de.bnder.minecraftr6.gamePlay.gameUtils.PlayerUtils;
import de.bnder.minecraftr6.main.Main;
import de.bnder.minecraftr6.utils.Connection;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.ResultSet;
import java.util.UUID;

public class HostageInteract implements Listener {

    @EventHandler
    public void onInteract(EntityDamageByEntityEvent e) {
        if (e.getEntity().getType() == EntityType.ZOMBIE) {
            if (e.getDamager().getType() == EntityType.PLAYER) {
                Player p = (Player) e.getDamager();
                PlayerUtils playerUtils = new PlayerUtils(p);
                if (playerUtils.isInGame()) {
                    GameUtils gameUtils = new GameUtils(playerUtils.getGame());
                    if (gameUtils.getCTs().contains(p.getUniqueId().toString())) {
                        Zombie zombie = (Zombie) e.getEntity();
                        if (zombie.getLocation().distance(p.getLocation()) <= 2) {
                            p.sendMessage(Main.prefix + " §aDu hast die Geisel. Trage sie nun zu deinem Spawn zurück. Solange du die Geisel trägst, kannst du nicht schießen und bist angreifbar.");
                            for (String uuid : gameUtils.getTs()) {
                                Player player = Bukkit.getPlayer(UUID.fromString(uuid));
                                player.sendMessage(Main.prefix + " §4" + p.getName() + " §chat die Geisel und versucht sie nun zu seinem Spawn zu bringen!");
                                player.playSound(zombie.getLocation(), Sound.ENTITY_WITHER_SPAWN, 3, 3);
                            }
                            zombie.setHealth(zombie.getMaxHealth());
                            ArmorStand as = (ArmorStand) p.getWorld().spawn(p.getLocation(), ArmorStand.class);
                            as.setSmall(true);
                            as.setVisible(false);
                            as.setGravity(false);
                            as.setInvulnerable(true);
                            as.setCanPickupItems(false);
                            as.addPassenger(zombie);
                            p.addPassenger(as);
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 2, false, false));
                        }
                    }
                }
            }
        }
    }


    @EventHandler
    public void onGeiselDamage(EntityDamageEvent e) {
        if (e.getEntityType() == EntityType.ZOMBIE) {
            try {
                ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `gameName` FROM `MCR6_Games`").executeQuery();
                while (rs.next()) {
                    String game = rs.getString("gameName");
                    GameUtils gameUtils = new GameUtils(game);
                    if (gameUtils.hostageLocation().getWorld() == e.getEntity().getWorld()) {
                        if (gameUtils.isRunning()) {
                            e.setCancelled(true);
                        }
                    }
                }
            } catch (Exception e1) {

            }
        }
    }

}

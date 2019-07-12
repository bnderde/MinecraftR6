package de.bnder.minecraftr6.gamePlay.gameBase.core;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.minecraftr6.gamePlay.gameUtils.PlayerUtils;
import de.bnder.minecraftr6.gamePlay.gameUtils.WeaponUtils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class Shoot implements Listener {

    HashMap<Player, Long> playerTimestampMap = new HashMap<Player, Long>();

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            PlayerUtils pUtils = new PlayerUtils(p);

            if (pUtils.isInGame()) {
                if (p.getInventory().getItemInMainHand() != null) {
                    ItemStack itemInHand = p.getInventory().getItemInMainHand();
                    ItemMeta itemInHandMeta = itemInHand.getItemMeta();
                    if (itemInHandMeta != null && itemInHandMeta.getDisplayName() != null) {
                        if (itemInHandMeta.getDisplayName().startsWith(WeaponUtils.WEAPON_NAME_PREFIX)) {
                            WeaponUtils weaponUtils = new WeaponUtils(itemInHandMeta.getDisplayName());
                            long ammo = weaponUtils.getMagazinAmmo();
                            long maxAmmo = weaponUtils.getMaxMagazinAmmo();
                            if (ammo > 0) {
                                ammo -= 1;
                                if (!playerTimestampMap.containsKey(p) || playerTimestampMap.get(p) < System.currentTimeMillis() - (weaponUtils.getTicksBetweenShots() * 50)) {
                                    if (!pUtils.isReloading()) {

                                        Vector vector = p.getLocation().getDirection();

                                        Arrow arrow = p.launchProjectile(Arrow.class, vector);

                                        arrow.setGravity(false);
                                        arrow.setPickupStatus(Arrow.PickupStatus.CREATIVE_ONLY);
                                        arrow.setVelocity(vector.multiply(7.5D));
                                        arrow.setKnockbackStrength(0);
                                        arrow.setDamage(weaponUtils.getDamage());

                                        itemInHandMeta.setDisplayName(itemInHandMeta.getDisplayName().split("\\(")[0] + "(" + ammo + "/" + maxAmmo + ")");
                                        itemInHand.setItemMeta(itemInHandMeta);

                                        p.setLevel(0);

                                        float percent = (((float) ammo / maxAmmo));
                                        p.setExp(percent);

                                        e.setCancelled(true);

                                        playerTimestampMap.remove(p);
                                        playerTimestampMap.put(p, System.currentTimeMillis());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

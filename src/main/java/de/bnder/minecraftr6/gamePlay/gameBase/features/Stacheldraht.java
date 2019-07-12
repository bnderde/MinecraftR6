package de.bnder.minecraftr6.gamePlay.gameBase.features;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.minecraftr6.main.Main;
import de.bnder.minecraftr6.gamePlay.gameUtils.GameUtils;
import de.bnder.minecraftr6.gamePlay.gameUtils.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Stacheldraht implements Listener {

    public static void check(final String game) {
        new BukkitRunnable() {

            @Override
            public void run() {
                GameUtils gameUtils = new GameUtils(game);
                if (gameUtils.isRunning()) {
                    for (Player p : gameUtils.players()) {
                        if (gameUtils.getCTs().contains(p.getUniqueId().toString())) {
                            Location pLoc = p.getLocation();
                            if (pLoc.getBlock().getType() == Material.DEAD_FIRE_CORAL_FAN) {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 5, false, false));
                            }
                        }
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Main.plugin, 0, 3);
    }

    @EventHandler
    public void onStacheldrahtPlace(BlockBreakEvent e) {
        Player p = e.getPlayer();
        PlayerUtils playerUtils = new PlayerUtils(p);
        if (playerUtils.isInGame() && p.getWorld().getName().toLowerCase().startsWith(GameUtils.mapPrefix)) {
            if (e.getBlock().getType() == Material.DEAD_FIRE_CORAL_FAN) {
                GameUtils gameUtils = new GameUtils(playerUtils.getGame());
                if (gameUtils.getTs().contains(p.getUniqueId().toString())) {
                    ItemStack itemStack = new ItemStack(Material.DEAD_FIRE_CORAL_FAN);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName("§7Stacheldraht");
                    itemStack.setAmount(1);
                    itemStack.setItemMeta(itemMeta);
                    e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), itemStack);
                }
            }
        }
    }

}

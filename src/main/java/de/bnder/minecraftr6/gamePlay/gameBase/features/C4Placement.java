package de.bnder.minecraftr6.gamePlay.gameBase.features;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.minecraftr6.gamePlay.gameBase.gameManager.StartGame;
import de.bnder.minecraftr6.main.Main;
import de.bnder.minecraftr6.gamePlay.gameUtils.GameUtils;
import de.bnder.minecraftr6.gamePlay.gameUtils.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class C4Placement implements Listener {

    @EventHandler
    public void onPlace(final PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            PlayerUtils playerUtils = new PlayerUtils(p);
            if (playerUtils.isInGame()) {
                if (p.getInventory().getItemInMainHand() != null) {
                    ItemStack itemStack = p.getInventory().getItemInMainHand();
                    if (itemStack.getType() == Material.STONE_BUTTON) {
                        if (itemStack.getItemMeta().getDisplayName() != null) {
                            if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(StartGame.c4ItemName)) {
                                GameUtils gameUtils = new GameUtils(playerUtils.getGame());
                                final ArrayList<Player> players = new ArrayList<Player>();
                                players.addAll(gameUtils.players());
                                for (Player all : players) {
                                    all.playSound(e.getClickedBlock().getLocation(), Sound.BLOCK_GRASS_PLACE, 10, 1);
                                }

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        TNTPrimed tnt = (TNTPrimed) e.getClickedBlock().getWorld().spawnEntity(e.getClickedBlock().getLocation(), EntityType.PRIMED_TNT);
                                        tnt.setFuseTicks(0);
                                    }
                                }.runTaskLater(Main.plugin, 3*20);
                                p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
                            }
                        }
                    }
                }
            }
        }
    }

}

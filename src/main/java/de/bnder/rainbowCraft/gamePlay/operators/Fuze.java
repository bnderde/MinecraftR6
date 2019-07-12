package de.bnder.rainbowCraft.gamePlay.operators;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.rainbowCraft.main.Main;
import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

import static de.bnder.rainbowCraft.main.Main.plugin;

public class Fuze implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        final Player p = e.getPlayer();
        PlayerUtils playerUtils = new PlayerUtils(p);
        if (playerUtils.isInGame()) {
            String game = playerUtils.getGame();
            GameUtils gameUtils = new GameUtils(game);
            if (gameUtils.isRunning()) {
                final Block blockPlaced = e.getBlock();
                Block blockAgainst = e.getBlockAgainst();
                if (blockPlaced.getType() == Material.DARK_OAK_BUTTON) {
                    if (Main.gamesC.getString("Game" + "." + game + ".player" + "." + p.getUniqueId().toString() + ".operator") != null && Main.gamesC.getString("Game" + "." + game + ".player" + "." + p.getUniqueId().toString() + ".operator").equalsIgnoreCase("fuze")) {
                        int placedX = blockPlaced.getX();
                        int againstX = blockAgainst.getX();
                        int placedZ = blockPlaced.getZ();
                        int againstZ = blockAgainst.getZ();

                        final Location shootFromLoc = blockAgainst.getLocation();

                        if (placedX != againstX) {
                            if (placedX > againstX) {
                                shootFromLoc.setX(placedX - 2);
                            } else {
                                shootFromLoc.setX(placedX + 2);
                            }
                        } else if (placedZ != againstZ) {
                            if (placedZ > againstZ) {
                                shootFromLoc.setZ(placedZ - 2);
                            } else {
                                shootFromLoc.setZ(placedZ + 2);
                            }
                        } else {
                            e.setCancelled(true);
                            return;
                        }

                        for (int i = 0; i < 5; i++) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    Item grenade = shootFromLoc.getWorld().dropItem(shootFromLoc, new ItemStack(Material.SLIME_BALL));
                                    Vector vector = p.getLocation().getDirection();
                                    vector.multiply(2);
                                    vector.setX(vector.getX() + (-7 + new Random().nextInt(7)));
                                    vector.setZ(vector.getZ() + (-7 + new Random().nextInt(7)));
                                    vector.setY(vector.getY() + (-7 + new Random().nextInt(7)));
                                    grenade.setVelocity(vector);
                                }
                            }.runTaskLater(plugin, new Random().nextInt( 2 + 1 * 20));
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                List<Entity> entList = shootFromLoc.getWorld().getEntities();
                                for (Entity current : entList) {
                                    if (current.getType() == EntityType.DROPPED_ITEM) {
                                        TNTPrimed tnt = (TNTPrimed) current.getWorld().spawnEntity(current.getLocation(), EntityType.PRIMED_TNT);
                                        tnt.setFuseTicks(0);
                                        current.remove();
                                        blockPlaced.setType(Material.AIR);
                                    }
                                }
                            }
                        }.runTaskLater(plugin, 3 * 20);
                    }
                }
            }
        }
    }

}

package de.bnder.minecraftr6.gamePlay.operators;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.minecraftr6.main.Main;
import de.bnder.minecraftr6.gamePlay.gameUtils.GameUtils;
import de.bnder.minecraftr6.gamePlay.gameUtils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

public class Dokkaebi implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            Player p = e.getPlayer();
            PlayerUtils playerUtils = new PlayerUtils(p);
            if (playerUtils.isInGame()) {
                String game = playerUtils.getGame();
                if (Main.gamesC.getString("Game" + "." + game + ".player" + "." + p.getUniqueId().toString() + ".operator") != null && Main.gamesC.getString("Game" + "." + game + ".player" + "." + p.getUniqueId().toString() + ".operator").equalsIgnoreCase("dokkaebi")) {
                    if (p.getInventory().getItemInMainHand().getType() == Material.IRON_INGOT) {
                        GameUtils gameUtils = new GameUtils(game);
                        final ArrayList<String> targets;
                        final ArrayList<String> enemys;
                        if (gameUtils.getCTs().contains(p.getUniqueId().toString())) {
                            targets = gameUtils.getTs();
                            enemys = gameUtils.getCTs();
                        } else {
                            targets = gameUtils.getCTs();
                            enemys = gameUtils.getTs();
                        }
                        p.getInventory().remove(p.getInventory().getItemInMainHand());

                        final ItemStack handy = new ItemStack(Material.IRON_INGOT);
                        ItemMeta handyMeta = handy.getItemMeta();
                        handyMeta.setDisplayName("§3Handy");
                        ArrayList<String> handyLore = new ArrayList<String>();
                        handyLore.add("§7Rechtsklick zum deaktivieren.");
                        handyMeta.setLore(handyLore);
                        handy.setItemMeta(handyMeta);
                        for (String target : targets) {
                            Player t = Bukkit.getPlayer(UUID.fromString(target));
                            t.getInventory().addItem(handy);
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                for (String uuid : targets) {
                                    Player target = Bukkit.getPlayer(UUID.fromString(uuid));
                                    try {
                                        if (target.getInventory().contains(handy)) {
                                            for (String enemyUUID : enemys) {
                                                Player enemy = Bukkit.getPlayer(UUID.fromString(enemyUUID));
                                                enemy.playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                                            }
                                        }
                                    } catch (NullPointerException e) {

                                    }
                                }
                            }
                        }.runTaskTimer(Main.plugin, 0, 20);
                    }
                } else {
                    if (p.getInventory().getItemInMainHand().getType() == Material.IRON_INGOT) {
                        p.getInventory().remove(p.getInventory().getItemInMainHand());
                    }
                }
            }
        }
    }

}

package de.bnder.rainbowCraft.gamePlay.operators;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.rainbowCraft.main.Main;
import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Lesion {

    public static void checkPlayers(String game) {
        final GameUtils gameUtils = new GameUtils(game);
        final ArrayList<Player> players = gameUtils.players();
        new BukkitRunnable()
        {
            public void run()
            {
                if (gameUtils.isRunning()) {
                    for (Player p : players) {
                        Block block = p.getWorld().getBlockAt(p.getLocation());
                        if (block.getType() == Material.OAK_BUTTON) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 5 * 20, 0));
                            block.setType(Material.AIR);
                            p.sendMessage(Main.prefix + " §cDu bist in einen §aGiftstachel §cgelaufen!");
                        }
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Main.plugin, 5*20, 5);
    }

}

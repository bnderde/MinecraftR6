package de.bnder.minecraftr6.gamePlay.gameBase.core;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.minecraftr6.gamePlay.gameUtils.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.io.IOException;
import java.util.ArrayList;

import static de.bnder.minecraftr6.gamePlay.gameBase.MapReset.mapChanges;
import static de.bnder.minecraftr6.gamePlay.gameBase.MapReset.mapChangesC;

public class GlassBreak implements Listener {

    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        if (e.getHitBlock() != null && e.getHitEntity() == null && e.getEntityType() == EntityType.ARROW) {
            Block b = e.getHitBlock();

            if (b.getType().name().toLowerCase().contains("glass")) {
                String path = "Map" + "." + b.getWorld().getName().replace(GameUtils.mapPrefix, "");
                String block = b.getType().name() + ";" + b.getBlockData().getAsString() + ";" + b.getWorld().getName() + ";" + b.getX() + ";" + b.getY() + ";" + b.getZ();
                ArrayList<String> list = new ArrayList<String>();
                list.addAll(mapChangesC.getStringList(path));
                list.add(block);
                mapChangesC.set(path, list);
                try {
                    mapChangesC.save(mapChanges);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }


                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.playSound(b.getLocation(), Sound.BLOCK_GLASS_BREAK, 100, 1);
                }
                b.breakNaturally();

                e.getEntity().remove();
            }
        }
    }
}

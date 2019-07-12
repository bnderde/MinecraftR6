package de.bnder.minecraftr6.gamePlay.operators;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.minecraftr6.main.Main;
import de.bnder.minecraftr6.gamePlay.gameUtils.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class Sledge implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        PlayerUtils playerUtils = new PlayerUtils(p);
        if (playerUtils.isInGame()) {
            String game = playerUtils.getGame();
            if (Main.gamesC.getString("Game" + "." + game + ".player" + "." + p.getUniqueId().toString() + ".operator") != null && Main.gamesC.getString("Game" + "." + game + ".player" + "." + p.getUniqueId().toString() + ".operator").equalsIgnoreCase("sledge")) {
                if (p.getInventory().getItemInMainHand().getType() == Material.DIAMOND_PICKAXE) {
                    e.setDropItems(false);
                    e.setExpToDrop(0);
                }
            }
        }
    }

}

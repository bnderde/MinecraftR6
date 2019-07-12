package de.bnder.minecraftr6.gamePlay.gameBase.lobby;

import de.bnder.minecraftr6.buildMode.buildUtils.BuilderUtils;
import de.bnder.minecraftr6.gamePlay.gameUtils.GameUtils;
import de.bnder.minecraftr6.gamePlay.gameUtils.PlayerUtils;
import de.bnder.minecraftr6.main.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MapChoseListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() != null && e.getClickedInventory() != null && e.getInventory() != null) {
            Player p = (Player) e.getWhoClicked();
            if (e.getView().getTitle().equalsIgnoreCase("§3Maps")) {
                e.setCancelled(true);
                BuilderUtils builderUtils = new BuilderUtils(p);
                if (!builderUtils.isBuilding()) {
                    PlayerUtils playerUtils = new PlayerUtils(p);
                    if (!playerUtils.isInGame()) {
                        ItemStack item = e.getCurrentItem();
                        ItemMeta itemMeta = item.getItemMeta();
                        if (itemMeta.getLore().get(itemMeta.getLore().size() - 1).startsWith("§7ID: ")) {
                            String id = itemMeta.getLore().get(itemMeta.getLore().size() - 1).replace("§7ID: ", "");
                            GameUtils game = new GameUtils(id);
                            if (!game.isRunning()) {
                                p.performCommand("join " + id);
                            } else {
                                p.sendMessage(Main.prefix + " §cDu bist bereits in einem Spiel!");
                            }
                        } else {
                            p.sendMessage(Main.prefix + " §cDas Spiel läuft bereits!");
                        }
                    } else {
                        p.sendMessage(Main.prefix + " §cDu bist bereits in einem Spiel!");
                    }
                } else {
                    p.sendMessage(Main.prefix + " §cDu bist aktuell im Bau-Modus!");
                }
            }
        }
    }
}

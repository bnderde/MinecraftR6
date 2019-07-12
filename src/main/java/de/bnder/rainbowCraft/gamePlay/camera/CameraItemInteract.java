package de.bnder.rainbowCraft.gamePlay.camera;

import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import de.bnder.rainbowCraft.utils.Connection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.util.ArrayList;

public class CameraItemInteract implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        PlayerUtils playerUtils = new PlayerUtils(p);
        if (playerUtils.isInGame()) {
            GameUtils gameUtils = new GameUtils(playerUtils.getGame());
            if (gameUtils.getTs().contains(p.getUniqueId().toString())) {
                if (p.getInventory().getItemInMainHand() != null) {
                    ItemStack itemInHand = p.getInventory().getItemInMainHand();
                    if (itemInHand.getType() == CameraItem.cameraItem().getType()) {
                        e.setCancelled(true);
                        Inventory inv = Bukkit.createInventory(null, 3 * 9, "§5Kameras");
                        int slot = 0;
                        try {
                            ResultSet rs = Connection.mainConnection().prepareStatement("SELECT * FROM `MCR6_Map_Cameras` WHERE `mapID`='" + playerUtils.getGame() + "'").executeQuery();
                            int cam = 0;
                            while (rs.next()) {
                                String camera = String.valueOf(cam);
                                ItemStack itemStack = new ItemStack(Material.FURNACE);
                                ItemMeta itemMeta = itemStack.getItemMeta();
                                itemMeta.setDisplayName("§5Kamera: " + camera);
                                itemMeta.setLore(new ArrayList<String>() {{
                                    add("§7Klicke um in die Kamera-Sicht zu wechseln");
                                }});
                                itemStack.setItemMeta(itemMeta);

                                inv.setItem(slot, itemStack);
                                slot++;
                                cam++;
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        p.openInventory(inv);
                    }
                }
            }
        }
    }

}

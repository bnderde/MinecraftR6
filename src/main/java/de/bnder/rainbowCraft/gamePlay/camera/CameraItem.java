package de.bnder.rainbowCraft.gamePlay.camera;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CameraItem {

    public static ItemStack cameraItem() {
        ItemStack itemStack = new ItemStack(Material.FURNACE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§5Kamera");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}

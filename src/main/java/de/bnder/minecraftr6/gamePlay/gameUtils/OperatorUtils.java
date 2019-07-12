package de.bnder.minecraftr6.gamePlay.gameUtils;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.minecraftr6.gamePlay.configs.OperatorConfigs;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class OperatorUtils {

    String op;
    FileConfiguration fc = OperatorConfigs.fc;

    public OperatorUtils(String operator) {
        op = operator;
    }

    public String getName() {
        return fc.getString("Operator" + "." + op + ".name");
    }

    public String weaponName() {
        return fc.getString("Operator" + "." + op + ".weapon");
    }

    public ItemStack weaponItem() {
        String wpName = weaponName();
        WeaponUtils weaponUtils = new WeaponUtils(wpName);
        ItemStack itemStack = new ItemStack(WeaponUtils.WEAPON_MATERIAL);
        ItemMeta itemMeta = itemStack.getItemMeta();
        long ammo = weaponUtils.getMaxMagazinAmmo();
        itemMeta.setDisplayName(WeaponUtils.WEAPON_NAME_PREFIX + weaponUtils.getName() + " (" + ammo + "/" + ammo + ")");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public ArrayList<ItemStack> items() {
        ArrayList<ItemStack> list = new ArrayList<ItemStack>();
        for (String itemName : fc.getStringList("Operator" + "." + op + ".items")) {
            try {
                ItemStack is = new ItemStack(Material.valueOf(itemName));
                ItemMeta im = is.getItemMeta();
                if (fc.get("Operator" + "." + op + ".item" + "." + itemName + ".name") != null) {
                    im.setDisplayName(fc.getString("Operator" + "." + op + ".item" + "." + itemName + ".name"));
                }
                is.setItemMeta(im);

                list.add(is);
            } catch (Exception e) {}
        }
        return list;
    }

    public long price() {
        return fc.getLong("Operator" + "." + op + ".price");
    }

}

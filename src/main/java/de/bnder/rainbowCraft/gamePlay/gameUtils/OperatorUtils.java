package de.bnder.rainbowCraft.gamePlay.gameUtils;

/*
 * Copyright (C) 2019 Jan Brinkmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import de.bnder.rainbowCraft.gamePlay.configs.OperatorConfigs;
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

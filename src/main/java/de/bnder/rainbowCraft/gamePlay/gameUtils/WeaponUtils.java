package de.bnder.rainbowCraft.gamePlay.gameUtils;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.rainbowCraft.gamePlay.configs.WeaponsConfig;
import org.bukkit.Material;

public class WeaponUtils {

    public final static String WEAPON_NAME_PREFIX = "§c";
    public final static Material WEAPON_MATERIAL = Material.STICK;


    String wpName;
    String path;
    String wpNameFull;

    public WeaponUtils(String weaponName) {
        if (weaponName.contains("(")) {
            wpName = weaponName.replace(WEAPON_NAME_PREFIX, "").split("\\(")[0].replaceAll(" ", "");
        } else {
            wpName = weaponName;
        }
        wpNameFull = weaponName;
        path = "Weapon" + "." + wpName.toLowerCase() + ".";
    }

    public long getTicksBetweenShots() {
        if (weaponExists()) {
            return WeaponsConfig.fc.getInt(path + "ticksBetweenShots");
        }
        return 0;
    }

    public String getName() {
        return WeaponsConfig.fc.getString(path + "name");
    }

    private boolean weaponExists() {
        if (WeaponsConfig.fc.get(path + "name") != null) {
            return true;
        }
        return false;
    }

    public long getMaxMagazinAmmo() {
        if (weaponExists()) {
            return WeaponsConfig.fc.getInt(path + "ammoSize");
        }
        return 0;
    }

    public long getMagazinAmmo() {
        try {
            return Long.valueOf(wpNameFull.split("\\(")[1].split("/")[0]);
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }

    public long getReloadPeriodTicks() {
        if (weaponExists()) {
            return WeaponsConfig.fc.getInt(path + "reloadPeriodTicks");
        }
        return 0;
    }

    public double getDamage() {
        if (weaponExists()) {
            return WeaponsConfig.fc.getDouble(path + "damage");
        }
        return 0;
    }

}

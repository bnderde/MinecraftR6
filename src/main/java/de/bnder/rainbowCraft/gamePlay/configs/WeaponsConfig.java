package de.bnder.rainbowCraft.gamePlay.configs;

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

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class WeaponsConfig {

    public static File file = new File("plugins/RainbowCraft/weapons.yml");
    public static FileConfiguration fc = YamlConfiguration.loadConfiguration(file);

    public static void prepare() {

        set("AK-74M", 40, 3, 2, "Assault Rifle", 0.4);

        set("G36C", 30, 4, 2, "Assault Rifle", 0.3);

        set("L85A2", 31, 2, 3, "Assault Rifle", 0.2);

        set("P9", 16, 3, 5, "Pistol", 0.1);

        set("FMG-9", 30, 2, 1, "Machine Gun", 0.1);

        set("T5 SMG", 30, 2, 0, "Machine Gun", 0.1);

        set("MK-14", 30, 3, 5, "Marksman Rifle", 0.2);

        set("AK-12", 30, 2, 1, "Assault Rifle", 0.2);

        try {
            fc.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void set(String weapon, int ammoSize, long reloadPeriodTicks, long ticksBetweenShots, String category, double damage) {
        String path = "Weapon" + "." + weapon.toLowerCase() + ".";
        if (fc.get(path + "name") == null) {
            fc.set(path + "name", weapon);
        }
        if (fc.get(path + "ammoSize") == null) {
            fc.set(path + "ammoSize", ammoSize);
        }
        if (fc.get(path + "reloadPeriodTicks") == null) {
            fc.set(path + "reloadPeriodTicks", reloadPeriodTicks);
        }
        if (fc.get(path + "ticksBetweenShots") == null) {
            fc.set(path + "ticksBetweenShots", ticksBetweenShots);
        }
        if (fc.get(path + "category") == null) {
            fc.set(path + "category", category);
        }

        if (fc.get(path + "damage") == null) {
            fc.set(path + "damage", damage);
        }
    }

}

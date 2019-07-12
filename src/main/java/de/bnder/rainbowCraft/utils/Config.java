package de.bnder.rainbowCraft.utils;

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

import de.bnder.rainbowCraft.main.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;

public class Config {

    static FileConfiguration fc = Main.configC;

    public static void prepare() {
        if (Main.configC.getString("MySQL" + ".hostname") == null) {
            Main.configC.set("MySQL" + ".hostname", "---");
            Main.configC.set("MySQL" + ".dbname", "---");
            Main.configC.set("MySQL" + ".username", "---");
            Main.configC.set("MySQL" + ".password", "---");
            try {
                Main.configC.save(Main.config);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static long moneyPerWin() {
        if (fc.get("MoneyPerWin") == null) {
            fc.set("MoneyPerWin", 100);
            try {
                fc.save(Main.config);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return fc.getLong("MoneyPerWin");
        }
        return 100;
    }

}

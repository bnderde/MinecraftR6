package de.bnder.rainbowCraft.utils;

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

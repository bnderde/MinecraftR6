package de.bnder.rainbowCraft.gamePlay.configs;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class OperatorConfigs {

    public static File file = new File("plugins/MinecraftR6/operators.yml");
    public static FileConfiguration fc = YamlConfiguration.loadConfiguration(file);

    public static void prepare() {

        set("Sledge", "Kann Blöcke abbauen", Material.DIAMOND_PICKAXE, "L85A2", new ArrayList<ItemStack>(){{
            ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
            ItemMeta pickaxeMeta = pickaxe.getItemMeta();
            pickaxeMeta.setDisplayName("§bSpitzhacke");
            pickaxe.setItemMeta(pickaxeMeta);

            add(pickaxe);
        }}, "at", 1500);

        set("Montagne", "Hat einen taktisches Schild", Material.SHIELD, "P9", new ArrayList<ItemStack>(){{
            ItemStack pickaxe = new ItemStack(Material.SHIELD);
            ItemMeta pickaxeMeta = pickaxe.getItemMeta();
            pickaxeMeta.setDisplayName("§9Schild");
            pickaxe.setItemMeta(pickaxeMeta);

            add(pickaxe);
        }}, "at", 1500);

        set("Smoke", "Erhält eine Gasgranate & eine Rauchgranate", Material.EGG, "FMG-9", new ArrayList<ItemStack>(){{

            ItemStack gasGranate = new ItemStack(Material.EGG);
            ItemMeta pickaxeMeta = gasGranate.getItemMeta();
            pickaxeMeta.setDisplayName("§aGiftgasgranate");
            gasGranate.setItemMeta(pickaxeMeta);

            ItemStack rauchGranate = new ItemStack(Material.SNOWBALL);
            ItemMeta rauchMeta = gasGranate.getItemMeta();
            rauchMeta .setDisplayName("§fRauchgranate");
            rauchGranate.setItemMeta(rauchMeta );

            add(gasGranate);
            add(rauchGranate);

        }}, "t", 2000);

        set("Lesion", "Erhält 2 Giftstachel", Material.OAK_BUTTON, "T5 SMG", new ArrayList<ItemStack>(){{

            ItemStack button = new ItemStack(Material.OAK_BUTTON);
            ItemMeta pickaxeMeta = button.getItemMeta();
            pickaxeMeta.setDisplayName("§aGiftstachel");
            button.setItemMeta(pickaxeMeta);

            add(button);
            add(button);

        }}, "t", 3500);

        set("Dokkaebi", "Hackt gegnerische Telefone", Material.IRON_INGOT, "MK-14", new ArrayList<ItemStack>(){{

            ItemStack button = new ItemStack(Material.IRON_INGOT);
            ItemMeta pickaxeMeta = button.getItemMeta();
            pickaxeMeta.setDisplayName("§6Hacking Gerät");
            button.setItemMeta(pickaxeMeta);

            add(button);

        }}, "at & t", 2500);

        set("Fuze", "Sprengt Wände", Material.DARK_OAK_BUTTON, "AK-12", new ArrayList<ItemStack>(){{

            ItemStack button = new ItemStack(Material.DARK_OAK_BUTTON);
            ItemMeta pickaxeMeta = button.getItemMeta();
            pickaxeMeta.setDisplayName("§4Cluster Bombe");
            button.setItemMeta(pickaxeMeta);

            add(button);
            add(button);

        }}, "at", 4000);

        set("Rekrut", "Standard Operator", Material.COAL, "G36C", new ArrayList<ItemStack>(), "at & t", 0);

        try {
            fc.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void set(String name, String description, Material item, String weapon, ArrayList<ItemStack> list, String recommendedSide, long price) {
        String path = "Operator" + "." + name.toLowerCase() + ".";

        ArrayList<String> operatorList = new ArrayList<String>();
        operatorList.addAll(fc.getStringList("Operators"));
        if (!operatorList.contains(name.toLowerCase())) {
            operatorList.add(name.toLowerCase());
            fc.set("Operators", operatorList);
        }

        if (fc.get(path + "name") == null) {
            fc.set(path + "name", name);
        }
        if (fc.get(path + "description") == null) {
            fc.set(path + "description", description);
        }
        if (fc.get(path + "invItem") == null) {
            fc.set(path + "invItem", item.name());
        }
        if (fc.get(path + "weapon") == null) {
            fc.set(path + "weapon", weapon.toLowerCase());
        }
        if (fc.get(path + "items") == null) {
            ArrayList<String> items = new ArrayList<String>();
            for (ItemStack is : list) {
                items.add(is.getType().name());
                fc.set(path + ".item" +  "." + is.getType().name() + ".name", is.getItemMeta().getDisplayName());
            }
            fc.set(path + "items", items);
        }
        if (fc.get(path + "recommendedSide") == null) {
            fc.set(path + "recommendedSide", recommendedSide.toLowerCase());
        }
        if (fc.get(path + "price") == null) {
            fc.set(path + "price", price);
        }
    }

}

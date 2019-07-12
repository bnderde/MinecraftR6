package de.bnder.minecraftr6.gamePlay.gameBase.lobby;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GiveLobbyItems {

    public static void give(Player p) {
        p.setGameMode(GameMode.SURVIVAL);

        p.getInventory().clear();

        p.getInventory().setItem(0, chooseOperator());
        p.getInventory().setItem(8, leaveLobby());
    }

    public static ItemStack chooseOperator() {
        ItemStack chooseOperator = new ItemStack(Material.BOOK);
        ItemMeta chooseOperatorMeta = chooseOperator.getItemMeta();
        chooseOperatorMeta.setDisplayName("§9Operator");
        ArrayList<String> chooseOperatorLore = new ArrayList<String>();
        chooseOperatorLore.add("§7Wähle einen Operator aus");
        chooseOperatorMeta.setLore(chooseOperatorLore);
        chooseOperator.setItemMeta(chooseOperatorMeta);
        return chooseOperator;
    }

    public static ItemStack leaveLobby() {
        ItemStack leaveLobby = new ItemStack(Material.SLIME_BALL);
        ItemMeta leaveLobbyMeta = leaveLobby.getItemMeta();
        leaveLobbyMeta.setDisplayName("§cLobby verlassen");
        leaveLobby.setItemMeta(leaveLobbyMeta);
        return leaveLobby;
    }

}

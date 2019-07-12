package de.bnder.rainbowCraft.gamePlay.gameBase.lobby;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.rainbowCraft.gamePlay.configs.OperatorConfigs;
import de.bnder.rainbowCraft.main.Main;
import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import de.bnder.rainbowCraft.gamePlay.gameUtils.OperatorUtils;
import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import de.bnder.rainbowCraft.gamePlay.gameUtils.WeaponUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class LobbyItemInteract implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getItem() != null) {
                Player p = e.getPlayer();
                PlayerUtils playerUtils = new PlayerUtils(p);
                if (playerUtils.isInGame()) {
                    Material itemType = e.getItem().getType();
                    String displayName = e.getItem().getItemMeta().getDisplayName();
                    if (itemType == GiveLobbyItems.chooseOperator().getType()
                            && displayName.equalsIgnoreCase(GiveLobbyItems.chooseOperator().getItemMeta().getDisplayName())) {
                        e.setCancelled(true);
                        openChooseOperatorInv(p, playerUtils.getGame());
                    } else if (itemType == GiveLobbyItems.leaveLobby().getType() &&
                    displayName.equalsIgnoreCase(GiveLobbyItems.leaveLobby().getItemMeta().getDisplayName())) {
                        e.setCancelled(true);
                        p.performCommand("leave");
                    }
                }
            }
        }
    }

    public static String chooseOperatorInvName = "§9Operator";

    public static void openChooseOperatorInv(Player p, String game) {
        Inventory inv = Bukkit.createInventory(null, 6 * 9, chooseOperatorInvName);

        int slot = 0;

        GameUtils gameUtils = new GameUtils(game);
        PlayerUtils playerUtils = new PlayerUtils(p);

        for (String operator : gameUtils.freeOperators()) {
            OperatorUtils operatorUtils = new OperatorUtils(operator);
            String path = "Operator" + "." + operator + ".";
            ItemStack itemStack = new ItemStack(Material.valueOf(OperatorConfigs.fc.getString(path + "invItem")));
            ItemMeta itemMeta = itemStack.getItemMeta();
//            itemMeta.setDisplayName("§9" + OperatorConfigs.fc.getString(path + "name"));
            itemMeta.setDisplayName("§9" + operatorUtils.getName());
            ArrayList<String> lore = new ArrayList<String>();
//            String weaponBaseName = OperatorConfigs.fc.getString(path + "weapon");
            String weaponBaseName = operatorUtils.weaponName();
//            lore.add("§3Waffe: " + new WeaponUtils(weaponBaseName).getName());
            lore.add("§3Waffe: " + new WeaponUtils(weaponBaseName).getName());
            String loreSource = OperatorConfigs.fc.getString(path + "description");
            String loreFormat;
            while (loreSource.length() >= 25) {
                loreFormat = "§7" + loreSource.subSequence(0, 25);
                while (loreFormat.startsWith(" ")) {
                    loreFormat = loreFormat.replaceFirst(" ", "");
                }
                lore.add(loreFormat);
                loreSource = loreSource.substring(25);
            }
            if (loreSource.length() >= 0) {
                lore.add("§7" + loreSource);
            }
            lore.add("§eEmpfohlene Seite: §6" + OperatorConfigs.fc.getString(path + ".recommendedSide").toUpperCase());

            if (!playerUtils.hasOperator(operator)) {
                lore.add("§6Preis: §e" + operatorUtils.price() + Main.currency);
            } else {
                lore.add("§aGekauft!");
            }

            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);

            inv.setItem(slot, itemStack);
            slot++;
        }

        p.openInventory(inv);
    }

}

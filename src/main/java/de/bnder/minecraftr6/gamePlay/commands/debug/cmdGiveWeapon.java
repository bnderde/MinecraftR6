package de.bnder.minecraftr6.gamePlay.commands.debug;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.minecraftr6.gamePlay.gameUtils.WeaponUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class cmdGiveWeapon implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if (p.isOp()) {
                String weapon = "AK-74M";
                if (strings.length == 1) {
                    weapon = strings[0];
                }
                ItemStack itemStack = new ItemStack(Material.STICK);
                ItemMeta itemMeta = itemStack.getItemMeta();
                WeaponUtils weaponUtils = new WeaponUtils(weapon);
                itemMeta.setDisplayName(WeaponUtils.WEAPON_NAME_PREFIX + weapon + " (" + weaponUtils.getMaxMagazinAmmo() + "/" + weaponUtils.getMaxMagazinAmmo() + ")");
                itemStack.setItemMeta(itemMeta);
                p.getInventory().addItem(itemStack);
            }
        }

        return false;
    }
}

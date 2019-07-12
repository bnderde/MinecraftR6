package de.bnder.rainbowCraft.gamePlay.gameBase.lobby;

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

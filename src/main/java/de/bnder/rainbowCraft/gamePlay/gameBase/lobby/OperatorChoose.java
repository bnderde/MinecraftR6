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

import de.bnder.rainbowCraft.main.Main;
import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import de.bnder.rainbowCraft.gamePlay.gameUtils.OperatorUtils;
import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;

public class OperatorChoose implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null) {
            if (e.getInventory() != null) {
                if (e.getCurrentItem() != null) {
                    if (e.getView().getTitle().equalsIgnoreCase(LobbyItemInteract.chooseOperatorInvName)) {
                        e.setCancelled(true);
                        Player p = (Player) e.getWhoClicked();
                        PlayerUtils playerUtils = new PlayerUtils(p);
                        if (playerUtils.isInGame()) {
                            String game = playerUtils.getGame();
                            GameUtils gameUtils = new GameUtils(game);
                            ItemStack itemStack = e.getCurrentItem();
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            String operatorName = itemMeta.getDisplayName().substring(2);
                            if (gameUtils.freeOperators().contains(operatorName.toLowerCase())) {
                                if (playerUtils.hasOperator(operatorName)) {
                                    Main.gamesC.set("Game" + "." + game + ".player" + "." + p.getUniqueId().toString() + ".operator", operatorName.toLowerCase());
                                    try {
                                        Main.gamesC.save(Main.gamesFile);
                                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3, 3);
                                        p.sendMessage(Main.prefix + " §aDu hast den Operator " + itemMeta.getDisplayName() + " §aausgewählt.");
                                        for (Player all : gameUtils.lobbyPlayers()) {
                                            if (all.getOpenInventory() != null) {
                                                if (all.getOpenInventory().getTitle().equalsIgnoreCase(LobbyItemInteract.chooseOperatorInvName)) {
                                                    LobbyItemInteract.openChooseOperatorInv(all, game);
                                                }
                                            }
                                        }
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                } else {
                                    OperatorUtils operatorUtils = new OperatorUtils(operatorName.toLowerCase());
                                    long money = playerUtils.getMoney();
                                    long price = operatorUtils.price();
                                    if (money >= price) {
                                        playerUtils.modifyMoney(money - price);
                                        playerUtils.buyOperator(operatorName);
                                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3, 3);
                                        p.sendMessage(Main.prefix + " §aDu hast den Operator " + itemMeta.getDisplayName() + " §afür §e"+ price + Main.currency + " §agekauft.");
                                        LobbyItemInteract.openChooseOperatorInv(p, game);
                                    } else {
                                        p.sendMessage(Main.prefix + " §cDu hast nicht genügend Geld! Du benötigst §4" + price + Main.currency + "§c! Dein Geld: §e" + money + Main.currency + "§c.");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

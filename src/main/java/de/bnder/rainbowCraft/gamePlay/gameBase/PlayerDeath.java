package de.bnder.rainbowCraft.gamePlay.gameBase;

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

import de.bnder.rainbowCraft.gamePlay.gameBase.gameManager.EndRound;
import de.bnder.rainbowCraft.main.Main;
import de.bnder.rainbowCraft.utils.Connection;
import de.bnder.rainbowCraft.gamePlay.gameUtils.GameUtils;
import de.bnder.rainbowCraft.gamePlay.gameUtils.PlayerUtils;
import net.minecraft.server.v1_14_R1.PacketPlayInClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.sql.ResultSet;
import java.util.ArrayList;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        try {
            final Player p = e.getEntity();
            PlayerUtils playerUtils = new PlayerUtils(p);
            if (playerUtils.isInGame()) {
                String game = playerUtils.getGame();
                GameUtils gameUtils = new GameUtils(game);

                for (Entity entity : p.getPassengers()) {
                    if (entity.getType() == EntityType.ARMOR_STAND) {
                        for (Entity entity2 : entity.getPassengers()) {
                            entity.removePassenger(entity2);
                        }
                        entity.remove();
                    }
                }

                for (Entity entity : p.getPassengers()) {
                    p.removePassenger(entity);
                }

                Connection.mainConnection().prepareStatement("UPDATE `MCR6_Games_Players` SET `alive`=false WHERE `name`='" + game + "' && `playerUUID`='" + p.getUniqueId().toString() + "'").executeUpdate();

                playerUtils.addLifetimeDeaths();

                if (p.getKiller() != null) {
                    PlayerUtils killerUtils = new PlayerUtils(p.getKiller());
                    killerUtils.addLifetimeKills();
                }

                e.getDrops().clear();
                e.setDroppedExp(0);

                ArrayList<String> alives = new ArrayList<String>();
                ResultSet rs = Connection.mainConnection().prepareStatement("SELECT `playerUUID` FROM `MCR6_Games_Players` WHERE `name`='" + game + "' && `alive`=true").executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("playerUUID");
                    alives.add(uuid);
                }

                ArrayList<String> ts = gameUtils.getTs();
                ArrayList<String> cts = gameUtils.getCTs();

                ArrayList<String> tsAlive = new ArrayList<String>();
                ArrayList<String> ctsAlive = new ArrayList<String>();

                for (String t : ts) {
                    if (alives.contains(t)) {
                        tsAlive.add(t);
                    }
                }
                for (String ct : cts) {
                    if (alives.contains(ct)) {
                        ctsAlive.add(ct);
                    }
                }

                Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {
                    public void run() {
                        ((CraftPlayer) p).getHandle().playerConnection.a(new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN));
                    }
                }, 20);

                p.setGameMode(GameMode.SPECTATOR);

                Main.gamesC.save(Main.gamesFile);


                if (tsAlive.size() == 0) {
                    EndRound.end(game, "ct");
                } else if (ctsAlive.size() == 0) {
                    EndRound.end(game, "t");
                }

                e.setDeathMessage(null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

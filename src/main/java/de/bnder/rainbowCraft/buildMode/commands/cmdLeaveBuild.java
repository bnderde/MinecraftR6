package de.bnder.rainbowCraft.buildMode.commands;

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

import de.bnder.rainbowCraft.buildMode.buildBase.LeaveBuildMode;
import de.bnder.rainbowCraft.buildMode.buildUtils.BuilderUtils;
import de.bnder.rainbowCraft.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdLeaveBuild implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            BuilderUtils builderUtils = new BuilderUtils(p);
            if (builderUtils.isBuilding()) {
                LeaveBuildMode.leave(p);
            } else {
                p.sendMessage(Main.prefix + " §cDu bist nicht in dem Bau-Modus!");
            }
        }
        return false;
    }
}

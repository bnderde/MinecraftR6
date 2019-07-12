package de.bnder.rainbowCraft.buildMode.commands;

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

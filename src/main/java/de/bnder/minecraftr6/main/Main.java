package de.bnder.minecraftr6.main;

//Made by EnderLPs | bnder.de
//https://bnder.de
//©Jan Brinkmann (EnderLPs)

import de.bnder.minecraftr6.buildMode.buildBase.PlayerLeave;
import de.bnder.minecraftr6.buildMode.buildBase.UpdateGameMode;
import de.bnder.minecraftr6.buildMode.buildBase.extrasItem.CancelDrop;
import de.bnder.minecraftr6.buildMode.buildBase.extrasItem.ExtrasItemInvClick;
import de.bnder.minecraftr6.buildMode.buildBase.extrasItem.Interact;
import de.bnder.minecraftr6.buildMode.buildBase.extrasItem.RenameInvClick;
import de.bnder.minecraftr6.buildMode.buildBase.placings.*;
import de.bnder.minecraftr6.buildMode.commands.cmdBuild;
import de.bnder.minecraftr6.buildMode.commands.cmdBuildExtras;
import de.bnder.minecraftr6.buildMode.commands.cmdLeaveBuild;
import de.bnder.minecraftr6.buildMode.listeners.BlockBreak;
import de.bnder.minecraftr6.buildMode.listeners.BlockPlace;
import de.bnder.minecraftr6.buildMode.listeners.ItemDrop;
import de.bnder.minecraftr6.gamePlay.camera.*;
import de.bnder.minecraftr6.gamePlay.commands.admin.*;
import de.bnder.minecraftr6.gamePlay.commands.debug.cmdGiveWeapon;
import de.bnder.minecraftr6.gamePlay.commands.normal.cmdJoin;
import de.bnder.minecraftr6.gamePlay.commands.normal.cmdLeave;
import de.bnder.minecraftr6.gamePlay.configs.OperatorConfigs;
import de.bnder.minecraftr6.gamePlay.configs.WeaponsConfig;
import de.bnder.minecraftr6.gamePlay.gameBase.*;
import de.bnder.minecraftr6.gamePlay.gameBase.core.*;
import de.bnder.minecraftr6.gamePlay.gameBase.features.C4Placement;
import de.bnder.minecraftr6.gamePlay.gameBase.features.GraplingHook;
import de.bnder.minecraftr6.gamePlay.gameBase.features.Stacheldraht;
import de.bnder.minecraftr6.gamePlay.gameBase.hostage.HostageInteract;
import de.bnder.minecraftr6.gamePlay.gameBase.lobby.*;
import de.bnder.minecraftr6.gamePlay.operators.Dokkaebi;
import de.bnder.minecraftr6.gamePlay.operators.Fuze;
import de.bnder.minecraftr6.gamePlay.operators.Sledge;
import de.bnder.minecraftr6.gamePlay.operators.Smoke;
import de.bnder.minecraftr6.utils.Config;
import de.bnder.minecraftr6.utils.Connection;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.text.DecimalFormat;

public class Main extends JavaPlugin {

    public static Plugin plugin;
    public static String prefix = "§7<< §9Rainbow Craft §7>>";

    public static File gamesFile = new File("plugins/MinecraftR6/games.yml");
    public static FileConfiguration gamesC = YamlConfiguration.loadConfiguration(gamesFile);

    public static File config = new File("plugins/MinecraftR6/configs.yml");
    public static FileConfiguration configC = YamlConfiguration.loadConfiguration(config);

    public static String currency = "€";

    @Override
    public void onEnable() {
        plugin = this;
        Config.prepare();
        new Connection().defineConnection();
        WeaponsConfig.prepare();
        OperatorConfigs.prepare();

        getCommand("giveWeapon").setExecutor(new cmdGiveWeapon());
        getCommand("setlobby").setExecutor(new cmdSetLobby());
        getCommand("join").setExecutor(new cmdJoin());
        getCommand("leave").setExecutor(new cmdLeave());
        getCommand("addgamespawn").setExecutor(new cmdAddGameSpawn());
        getCommand("removegamespawn").setExecutor(new cmdRemoveGameSpawn());
        getCommand("setgeisel").setExecutor(new cmdSetHostageSpawn());
        getCommand("setrescuepoint").setExecutor(new cmdSetRescuePoint());
        getCommand("createcamera").setExecutor(new cmdCreateCamera());

        Bukkit.getPluginManager().registerEvents(new Shoot(), this);
        Bukkit.getPluginManager().registerEvents(new Reload(), this);
        Bukkit.getPluginManager().registerEvents(new GlassBreak(), this);
        Bukkit.getPluginManager().registerEvents(new LobbyItemInteract(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLeaveEvent(), this);
        Bukkit.getPluginManager().registerEvents(new OperatorChoose(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeath(), this);
        Bukkit.getPluginManager().registerEvents(new DisableOffHand(), this);
        Bukkit.getPluginManager().registerEvents(new MapReset(), this);
        Bukkit.getPluginManager().registerEvents(new C4Placement(), this);
        Bukkit.getPluginManager().registerEvents(new HostageInteract(), this);
        Bukkit.getPluginManager().registerEvents(new FoodLevelChange(), this);
        Bukkit.getPluginManager().registerEvents(new GraplingHook(), this);
        Bukkit.getPluginManager().registerEvents(new DisableItemDrop(), this);
        Bukkit.getPluginManager().registerEvents(new Stacheldraht(), this);
        Bukkit.getPluginManager().registerEvents(new AntiDamageSlowDown(), this);
        Bukkit.getPluginManager().registerEvents(new NoDamage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerKick(), this);
        Bukkit.getPluginManager().registerEvents(new MapChoseListener(), this);
        Bukkit.getPluginManager().registerEvents(new NoPlace(), this);
        Bukkit.getPluginManager().registerEvents(new NoBreak(), this);

        //Cameras
        Bukkit.getPluginManager().registerEvents(new CancelTeleport(), this);
        Bukkit.getPluginManager().registerEvents(new CancelTargetLeave(), this);
        Bukkit.getPluginManager().registerEvents(new CameraItemInteract(), this);
        Bukkit.getPluginManager().registerEvents(new CameraChooseInvClick(), this);
        Bukkit.getPluginManager().registerEvents(new PlaceholderEntityDamage(), this);

//        Operators
        Bukkit.getPluginManager().registerEvents(new Sledge(), this);
        Bukkit.getPluginManager().registerEvents(new Smoke(), this);
        Bukkit.getPluginManager().registerEvents(new Dokkaebi(), this);
        Bukkit.getPluginManager().registerEvents(new Fuze(), this);


        //##################################################################
        //##########################    Buildmode   ########################
        //##################################################################

        getCommand("build").setExecutor(new cmdBuild());
        getCommand("leavebuild").setExecutor(new cmdLeaveBuild());
        getCommand("buildextras").setExecutor(new cmdBuildExtras());

        Bukkit.getPluginManager().registerEvents(new PlayerLeave(), this);
        Bukkit.getPluginManager().registerEvents(new Interact(), this);
        Bukkit.getPluginManager().registerEvents(new ExtrasItemInvClick(), this);
        Bukkit.getPluginManager().registerEvents(new BlockPlace(), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreak(), this);
        Bukkit.getPluginManager().registerEvents(new UpdateGameMode(), this);
        Bukkit.getPluginManager().registerEvents(new CameraPlacing(), this);
        Bukkit.getPluginManager().registerEvents(new CameraDestroy(), this);
        Bukkit.getPluginManager().registerEvents(new DefenseSpawnPlacing(), this);
        Bukkit.getPluginManager().registerEvents(new DefenseSpawnDestroy(), this);
        Bukkit.getPluginManager().registerEvents(new AttackSpawnPlacing(), this);
        Bukkit.getPluginManager().registerEvents(new AttackSpawnDestroy(), this);
        Bukkit.getPluginManager().registerEvents(new HostageSpawnPlacing(), this);
        Bukkit.getPluginManager().registerEvents(new HostageSpawnDestroy(), this);
        Bukkit.getPluginManager().registerEvents(new RenameInvClick(), this);
        Bukkit.getPluginManager().registerEvents(new CancelDrop(), this);
        Bukkit.getPluginManager().registerEvents(new ItemDrop(), this);
    }

    @Override
    public void onDisable() {

    }

    public static double RoundTo2Decimals(double val) {
        DecimalFormat df2 = new DecimalFormat("###.##");
        return Double.valueOf(df2.format(val));
    }

}

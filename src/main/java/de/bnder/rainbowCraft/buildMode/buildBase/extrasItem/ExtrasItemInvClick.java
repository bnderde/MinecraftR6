package de.bnder.rainbowCraft.buildMode.buildBase.extrasItem;

import de.bnder.rainbowCraft.buildMode.buildBase.LeaveBuildMode;
import de.bnder.rainbowCraft.buildMode.buildUtils.BuilderUtils;
import de.bnder.rainbowCraft.buildMode.commands.cmdBuildExtras;
import de.bnder.rainbowCraft.main.Main;
import de.bnder.rainbowCraft.utils.Connection;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.util.ArrayList;

public class ExtrasItemInvClick implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null) {
            if (e.getCurrentItem() != null) {
                if (e.getView().getTitle().equalsIgnoreCase(cmdBuildExtras.invName)) {
                    e.setCancelled(true);
                    if (e.getCurrentItem() != null) {
                        Player p = (Player) e.getWhoClicked();
                        BuilderUtils builderUtils = new BuilderUtils(p);
                        if (builderUtils.isBuilding()) {
                            ItemStack clicked = e.getCurrentItem();
                            String clickedName = clicked.getItemMeta().getDisplayName();
                            if (clickedName.equalsIgnoreCase("§cBau-Modus verlassen")) {
                                LeaveBuildMode.leave(p);
                            } else if (clickedName.equalsIgnoreCase("§3Kamera platzieren")) {
                                ItemStack camera = new ItemStack(Material.ARMOR_STAND);
                                ItemMeta cameraM = camera.getItemMeta();
                                cameraM.setDisplayName(clickedName);
                                cameraM.setLore(new ArrayList<String>() {{
                                    add("§7Rechtsklick um eine Kamera an deiner");
                                    add("§7Position zu erstellen.");
                                }});
                                camera.setItemMeta(cameraM);
                                try {
                                    p.getInventory().addItem(camera);
                                    p.closeInventory();
                                } catch (IllegalArgumentException ex) {
                                    ex.printStackTrace();
                                    p.sendMessage(Main.prefix + " §cEin Fehler ist aufgetreten!");
                                }
                            } else if (clickedName.equalsIgnoreCase("§cVerteidiger-Spawn")) {
                                ItemStack camera = new ItemStack(Material.REDSTONE);
                                ItemMeta cameraM = camera.getItemMeta();
                                cameraM.setDisplayName(clickedName);
                                cameraM.setLore(new ArrayList<String>() {{
                                    add("§7Rechtsklick um einen Spawnpunkt an deiner");
                                    add("§7Position zu erstellen.");
                                }});
                                camera.setItemMeta(cameraM);
                                try {
                                    p.getInventory().addItem(camera);
                                    p.closeInventory();
                                } catch (IllegalArgumentException ex) {
                                    ex.printStackTrace();
                                    p.sendMessage(Main.prefix + " §cEin Fehler ist aufgetreten!");
                                }
                            } else if (clickedName.equalsIgnoreCase("§9Angreifer-Spawn")) {
                                ItemStack camera = new ItemStack(Material.LAPIS_LAZULI);
                                ItemMeta cameraM = camera.getItemMeta();
                                cameraM.setDisplayName(clickedName);
                                cameraM.setLore(new ArrayList<String>() {{
                                    add("§7Rechtsklick um einen Spawnpunkt an deiner");
                                    add("§7Position zu erstellen.");
                                }});
                                camera.setItemMeta(cameraM);
                                try {
                                    p.getInventory().addItem(camera);
                                    p.closeInventory();
                                } catch (IllegalArgumentException ex) {
                                    ex.printStackTrace();
                                    p.sendMessage(Main.prefix + " §cEin Fehler ist aufgetreten!");
                                }
                            } else if (clickedName.equalsIgnoreCase("§aGeisel-Spawn")) {
                                ItemStack camera = new ItemStack(Material.EMERALD);
                                ItemMeta cameraM = camera.getItemMeta();
                                cameraM.setDisplayName(clickedName);
                                cameraM.setLore(new ArrayList<String>() {{
                                    add("§7Rechtsklick um den Spawnpunkt der Geisel");
                                    add("§7an deiner Position zu erstellen.");
                                }});
                                camera.setItemMeta(cameraM);
                                try {
                                    p.getInventory().addItem(camera);
                                    p.closeInventory();
                                } catch (IllegalArgumentException ex) {
                                    ex.printStackTrace();
                                    p.sendMessage(Main.prefix + " §cEin Fehler ist aufgetreten!");
                                }
                            } else if (clickedName.equalsIgnoreCase("§aÖffentlich setzen")) {
                                String mapID = builderUtils.getMapID();

                                try {
                                    boolean hasCams = false;
                                    ResultSet camCheckRS = Connection.mainConnection().prepareStatement("SELECT `mapID` FROM `MCR6_Map_Cameras` WHERE `mapID`='" + mapID + "'").executeQuery();
                                    if (camCheckRS.next()) {
                                        hasCams = true;
                                    }
                                    ResultSet spawnsCT = Connection.mainConnection().prepareStatement("SELECT `mapID` FROM `MCR6_Map_Spawns` WHERE `mapID`='" + mapID + "' && `side`='ct'").executeQuery();
                                    boolean hasAllCTSpawns = false;
                                    int i = 0;
                                    while (spawnsCT.next()) {
                                        i++;
                                    }
                                    if (i > 4) {
                                        hasAllCTSpawns = true;
                                    }
                                    ResultSet spawnsT = Connection.mainConnection().prepareStatement("SELECT `mapID` FROM `MCR6_Map_Spawns` WHERE `mapID`='" + mapID + "' && `side`='t'").executeQuery();
                                    boolean hasAllTSpawns = false;
                                    i = 0;
                                    while (spawnsT.next()) {
                                        i++;
                                    }
                                    if (i > 4) {
                                        hasAllTSpawns = true;
                                    }
                                    ResultSet hostageRS = Connection.mainConnection().prepareStatement("SELECT `mapID` FROM `MCR6_Map_Spawns` WHERE `mapID`='" + mapID + "' && `side`='hostage'").executeQuery();
                                    boolean hasHostageSpawn = false;
                                    if (hostageRS.next()) {
                                        hasHostageSpawn = true;
                                    }


                                    if (hasCams && hasAllCTSpawns && hasAllTSpawns && hasHostageSpawn) {
                                        Connection.mainConnection().prepareStatement("UPDATE `MCR6_Player_Maps_General` SET `public`=true WHERE `mapID`='" + mapID + "'").executeUpdate();
                                        p.sendMessage(Main.prefix + " §aDeine Karte ist nun öffentlich Spielbar.");
                                        p.closeInventory();
                                        p.performCommand("buildextras");

                                        ResultSet rs = Connection.mainConnection().prepareStatement("SELECT * FROM `MCR6_Games` WHERE `gameName`='" + mapID + "'").executeQuery();
                                        if (!rs.next()) {
                                            Connection.mainConnection().prepareStatement("INSERT INTO `MCR6_Games`(`gameName`, `ctWins`, `tWins`, `running`) VALUES ('" + mapID + "',0,0,false)").executeUpdate();
                                        }
                                    } else {
                                        p.sendMessage(Main.prefix + " §cDeine Karte konnte nicht veröffentlicht werden. Du benötigst mindestens eine Kamera, den Spawn für die Geisel und für jede Seite 5 Spawns, damit die Karte öffentlich gespielt werden kann.");
                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            } else if (clickedName.equalsIgnoreCase("§cPrivat setzen")) {
                                String mapID = builderUtils.getMapID();
                                try {
                                    Connection.mainConnection().prepareStatement("UPDATE `MCR6_Player_Maps_General` SET `public`=false WHERE `mapID`='" + mapID + "'").executeUpdate();
                                    p.sendMessage(Main.prefix + " §cDeine Karte ist nun nicht mehr öffentlich Spielbar.");
                                    p.closeInventory();
                                    p.performCommand("buildextras");
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

/*Copyright (C) Harry5573 2013-14

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package net.harry5573.legendary;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Harry5573
 */
public class LegendaryItem extends JavaPlugin {

    public static LegendaryItem plugin;
    
    private String version;
    
    public LegendaryItemGenerator ig;
    
    @Override
    public void onEnable() {
        plugin = this;
        
        this.version = this.getDescription().getVersion();
        
        log("==[ Plugin version " + version + " starting up ]==");
        
        this.saveDefaultConfig();
        
        this.ig = new LegendaryItemGenerator(this);
        
        this.getCommand("legendary").setExecutor(new LegendaryCommand(this));
        
        this.getServer().getPluginManager().registerEvents(new LegendaryListener(this), this);
        
        this.scheduleAutoSpawns();
        this.scheduleMessage();

        log("==[ Plugin started up ]==");
    }

    @Override
    public void onDisable() {
        log("==[ Plugin stopping ]==");
        this.suicide();
    }

    public enum ItemType {
        DIAMOND_SWORD, DIAMOND_AXE, BOW, DIAMOND_PICKAXE, DIAMOND_SPADE, DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS;
    }

    public enum SwordAxeEnchant {
        DURABILITY, FIRE_ASPECT, LOOT_BONUS_MOBS, KNOCKBACK, DAMAGE_UNDEAD;
    }

    public enum PShovelEnchant {
        DURABILITY, DIG_SPEED, LOOT_BONUS_BLOCKS;
    }

    public enum BowEnchant {
        ARROW_DAMAGE, ARROW_FIRE, ARROW_INFINITE, ARROW_KNOCKBACK;
    }

    public enum ArmorEnchant {
        DURABILITY, PROTECTION_ENVIRONMENTAL, THORNS;
    }

    //
    //Ease of access methods
    //
    public void log(String msg) {
        this.getLogger().info(msg);
    }
    
    public void suicide() {
        Bukkit.getPluginManager().disablePlugin(this);
    }

    public void loadConfigs() {
        this.saveDefaultConfig();
        log("Configuration loaded!");
    }

    public void scheduleAutoSpawns() {
        this.getServer().getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "legendary spawnnow");
                plugin.log("Legendary item spawned!");
            }
        }, 60 * 20, this.getConfig().getInt("TimeBetweenSpawnsInSeconds") * 20);
    }
 
    public void scheduleMessage() {
        this.getServer().getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                for (String str : plugin.getConfig().getStringList("ItemCoords")) {
                    Location l = null;
                    String[] spawnfinal3 = str.split(",");
                    if (spawnfinal3.length == 3) {
                        Double x = Double.valueOf(Integer.parseInt(spawnfinal3[0]));
                        Double y = Double.valueOf(Integer.parseInt(spawnfinal3[1]));
                        Double z = Double.valueOf(Integer.parseInt(spawnfinal3[2]));
                        l = new Location(Bukkit.getWorld(plugin.getConfig().getString("WorldToDrop")), x, y, z);
                    }

                    Bukkit.broadcastMessage(ChatColor.YELLOW + "[LegendaryItems]" + ChatColor.GOLD + " Theres currently a legendary item spawned at " + ChatColor.GREEN + l.getBlockX() + ChatColor.GOLD + ", " + ChatColor.GREEN + l.getBlockY() + ChatColor.GOLD + ", " + ChatColor.GREEN + l.getBlockZ() + ChatColor.GOLD + "! Go get it!");
                }
            }
        }, 60, this.getConfig().getInt("TimeBetweenRemindMessageInSeconds") * 20);
    }

    public void winItem(Player p, Location l) {

        if (p == null) {

            int blockX = l.getBlockX();
            int blockY = l.getBlockY();
            int blockZ = l.getBlockZ();

            Bukkit.broadcastMessage(ChatColor.YELLOW + "[LegendaryItems]" + ChatColor.AQUA + " Someone has blown up the chest containing the legendary item at " + ChatColor.GREEN + l.getBlockX() + ChatColor.AQUA + ", " + ChatColor.GREEN + l.getBlockY() + ChatColor.AQUA + ", " + ChatColor.GREEN + l.getBlockZ() + ChatColor.AQUA + "! Well done!");
            return;
        }

        int blockX = l.getBlockX();
        int blockY = l.getBlockY();
        int blockZ = l.getBlockZ();

        Bukkit.broadcastMessage(ChatColor.YELLOW + "[LegendaryItems]" + ChatColor.DARK_RED + p.getName() + ChatColor.AQUA + " Has claimed the legendary item at " + ChatColor.GREEN + l.getBlockX() + ChatColor.AQUA + ", " + ChatColor.GREEN + l.getBlockY() + ChatColor.AQUA + ", " + ChatColor.GREEN + l.getBlockZ() + ChatColor.AQUA + "! Well done!");
    }
}

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

import java.util.List;
import static net.harry5573.legendary.ItemGenerator.plugin;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

/**
 *
 * @author Harry5573
 */
public class LegendaryListener implements Listener {

    public static LegendaryItem plugin;
    
    public LegendaryListener(LegendaryItem instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onUnloadEvent(ChunkUnloadEvent e) {
        Chunk c = e.getChunk();
        
        if (plugin.ig.chunks.contains(c)) {
            e.setCancelled(true);
            c.load(true);
        }   
    }
    
    @EventHandler(priority= EventPriority.LOW)
    public void onClickChest(PlayerInteractEvent e) {
        
        if (e.getAction() == null) {
            return;
        }
        
        if (e.getClickedBlock() == null) {
            return;
        }
        
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
     
            for (String str : plugin.getConfig().getStringList("ItemCoords")) {
                Location l = null;
                String[] spawnfinal3 = str.split(",");
                if (spawnfinal3.length == 3) {
                    Double x = Double.valueOf(Integer.parseInt(spawnfinal3[0]));
                    Double y = Double.valueOf(Integer.parseInt(spawnfinal3[1]));
                    Double z = Double.valueOf(Integer.parseInt(spawnfinal3[2]));
                    l = new Location(Bukkit.getWorld(plugin.getConfig().getString("WorldToDrop")), x, y, z);
                }
                
                Block configblock = Bukkit.getWorld(plugin.getConfig().getString("WorldToDrop")).getBlockAt(l);

                Block newBlock = e.getClickedBlock();

                if (configblock.toString().equals(newBlock.toString())) {
                    List<String> ex = plugin.getConfig().getStringList("ItemCoords");
                    ex.remove(str);

                    plugin.getConfig().set("ItemCoords", ex);
                    plugin.saveConfig();

                    plugin.winItem(e.getPlayer(), newBlock.getLocation());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBreak(BlockBreakEvent e) {

        if (e.getBlock() == null) {
            return;
        }


        for (String str : plugin.getConfig().getStringList("ItemCoords")) {
            Location l = null;
            String[] spawnfinal3 = str.split(",");
            if (spawnfinal3.length == 3) {
                Double x = Double.valueOf(Integer.parseInt(spawnfinal3[0]));
                Double y = Double.valueOf(Integer.parseInt(spawnfinal3[1]));
                Double z = Double.valueOf(Integer.parseInt(spawnfinal3[2]));
                l = new Location(Bukkit.getWorld(plugin.getConfig().getString("WorldToDrop")), x, y, z);
            }

            Block configblock = Bukkit.getWorld(plugin.getConfig().getString("WorldToDrop")).getBlockAt(l);

            Block newBlock = e.getBlock();

            if (configblock.toString().equals(newBlock.toString())) {
                List<String> ex = plugin.getConfig().getStringList("ItemCoords");
                ex.remove(str);

                plugin.getConfig().set("ItemCoords", ex);
                plugin.saveConfig();

                plugin.winItem(e.getPlayer(), newBlock.getLocation());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityExplode(EntityExplodeEvent e) {
        for (Block b : e.blockList()) {
            for (String str : plugin.getConfig().getStringList("ItemCoords")) {
                Location l = null;
                String[] spawnfinal3 = str.split(",");
                if (spawnfinal3.length == 3) {
                    Double x = Double.valueOf(Integer.parseInt(spawnfinal3[0]));
                    Double y = Double.valueOf(Integer.parseInt(spawnfinal3[1]));
                    Double z = Double.valueOf(Integer.parseInt(spawnfinal3[2]));
                    l = new Location(Bukkit.getWorld(plugin.getConfig().getString("WorldToDrop")), x, y, z);
                }

                Block configblock = Bukkit.getWorld(plugin.getConfig().getString("WorldToDrop")).getBlockAt(l);

                for (Block newBlock : e.blockList()) {

                    if (configblock.toString().equals(newBlock.toString())) {
                        List<String> ex = plugin.getConfig().getStringList("ItemCoords");
                        ex.remove(str);

                        plugin.getConfig().set("ItemCoords", ex);
                        plugin.saveConfig();

                        plugin.winItem(null, newBlock.getLocation());
                    }
                }
            }
        }
    }
}

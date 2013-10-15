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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.harry5573.legendary.LegendaryItem.ArmorEnchant;
import net.harry5573.legendary.LegendaryItem.BowEnchant;
import net.harry5573.legendary.LegendaryItem.ItemType;
import net.harry5573.legendary.LegendaryItem.PShovelEnchant;
import net.harry5573.legendary.LegendaryItem.SwordAxeEnchant;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Harry5573
 */
public class ItemGenerator {

    public static LegendaryItem plugin;
    
    public ItemGenerator(LegendaryItem instance) {
        this.plugin = instance;
    }
   
    List<Chunk> chunks = new ArrayList<>();
    
    public int generateRandomEnchantLevel() {
        int max = plugin.getConfig().getInt("MaxEnchantLevel");
        
        Random random = new Random();
        
        int number = Math.abs(random.nextInt(max) + 1);
        
        return number;
    }

    public int generateAmountOfEnchants() {
        int max = plugin.getConfig().getInt("MaxEnchantsOnItem");

        Random random = new Random();

        int number = Math.abs(random.nextInt(max) + 1);
        
        return number;
    }

    public ItemType generateItemType() {
        int pick = new Random().nextInt(ItemType.values().length);
        return ItemType.values()[pick];
    }

    public SwordAxeEnchant generateRandomWeaponEnchant() {
        int pick = new Random().nextInt(SwordAxeEnchant.values().length);
        return SwordAxeEnchant.values()[pick];
    }

    public PShovelEnchant generatePShovelEnchant() {
        int pick = new Random().nextInt(PShovelEnchant.values().length);
        return PShovelEnchant.values()[pick];
    }

    public BowEnchant generateBowEnchant() {
        int pick = new Random().nextInt(BowEnchant.values().length);
        return BowEnchant.values()[pick];
    }

    public ArmorEnchant generateArmorEnchant() {
        int pick = new Random().nextInt(ArmorEnchant.values().length);
        return ArmorEnchant.values()[pick];
    }

    public Location generateRandomLocation() {
        String s = (String) plugin.getConfig().getString("WorldToDrop");

        World world = Bukkit.getWorld(s);

        if (world == null) {
            plugin.log("Could not get the world to get a location. Check the config");
            return null;
        }

        int radius = plugin.getConfig().getInt("WorldRadius");
        
        Random random = new Random();
        
        double maxX = radius;
        double maxZ = radius;
        double minX = -radius;
        double minZ = -radius;
        int x = (int) (random.nextInt((int) (maxX - minX)) + minX);
        int z = (int) (random.nextInt((int) (maxZ - minZ)) + minZ);
        int y = random.nextInt(250);

        return new Location(world, x, y, z);
    }

    public ItemStack createRandomLegendary() {
        ItemType type = this.generateItemType();

        Material itemtype = Material.valueOf(type.toString());

        final ItemStack stack = new ItemStack(itemtype, 1);

        List<String> lore = new ArrayList<>();

        int amountenchants = this.generateAmountOfEnchants();
        
        //Add the enchantments
        for (int i = 0; i < amountenchants; i++) {

            if (type == ItemType.DIAMOND_AXE || type == ItemType.DIAMOND_SWORD) {
                final SwordAxeEnchant enchant = this.generateRandomWeaponEnchant();
                int level = this.generateRandomEnchantLevel();
                stack.addUnsafeEnchantment(Enchantment.getByName(enchant.toString()), level);
            }

            if (type == ItemType.DIAMOND_PICKAXE || type == ItemType.DIAMOND_SPADE) {
                final PShovelEnchant enchant = this.generatePShovelEnchant();
                int level = this.generateRandomEnchantLevel();
                stack.addUnsafeEnchantment(Enchantment.getByName(enchant.toString()), level);
            }
            if (type == ItemType.BOW) {
                final BowEnchant enchant = this.generateBowEnchant();
                int level = this.generateRandomEnchantLevel();
                stack.addUnsafeEnchantment(Enchantment.getByName(enchant.toString()), level);
            }
            if (type == ItemType.DIAMOND_HELMET || type == ItemType.DIAMOND_CHESTPLATE || type == ItemType.DIAMOND_LEGGINGS || type == ItemType.DIAMOND_BOOTS) {
                final ArmorEnchant enchant = this.generateArmorEnchant();
                int level = this.generateRandomEnchantLevel();
                stack.addUnsafeEnchantment(Enchantment.getByName(enchant.toString()), level);
            }
        }

        ItemMeta meta = stack.getItemMeta();

        String name = plugin.getConfig().getString("LegendaryName").replaceAll("(&([a-f0-9]))", "\u00A7$2");

        meta.setDisplayName(name);

        lore.add(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Official Legendary Item");

        meta.setLore(lore);

        stack.setItemMeta(meta);

        return stack;
    }

    public void dropRandomLegendaryAtLocation(Location l, ItemStack i) {
        World world = l.getWorld();

        //Load the chunk
        l.getChunk().load(true);

        //Add chunk to list
        this.chunks.add(l.getChunk());

        int blockX = l.getBlockX();
        int blockY = l.getBlockY();
        int blockZ = l.getBlockZ();

        String coords = blockX + "," + blockY + "," + blockZ;
        
        List<String> ex = plugin.getConfig().getStringList("ItemCoords");
        ex.add(coords);
        
        plugin.getConfig().set("ItemCoords", ex);
        plugin.saveConfig();

        Block b = world.getBlockAt(l);

        b.setType(Material.CHEST);

        Chest chest = (Chest) b.getState();

        Inventory inventory = chest.getInventory();

        inventory.addItem(i);

        //Add some fun
        Location l1 = new Location(l.getWorld(), l.getBlockX(), l.getBlockY() - 1, l.getBlockZ());
        l1.getBlock().setType(Material.BEACON);

        Bukkit.broadcastMessage(ChatColor.YELLOW + "[LegendaryItems] " + ChatColor.RED + "Random legendary item has been placed in a chest at " + ChatColor.GREEN + l.getBlockX() + ChatColor.RED + ", " + ChatColor.GREEN + l.getBlockY() + ChatColor.RED + ", " + ChatColor.GREEN + l.getBlockZ() + ChatColor.RED + "! Go get it!");
    }
}

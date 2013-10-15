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

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Harry5573
 */
public class LegendaryCommand implements CommandExecutor {

    public static LegendaryItem plugin;
    
    public LegendaryCommand(LegendaryItem instance) {
        this.plugin = instance;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {


        if (commandLabel.equalsIgnoreCase("legendary")) {


            if (!sender.hasPermission("legendary.admin")) {
                sender.sendMessage(ChatColor.RED + "Permission denied.");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage(ChatColor.YELLOW + "[LegendaryItems] " + ChatColor.RED + "Usage: /legendary <spawnnow|reload>");
                return true;
            }

            if (args.length == 1) {

                if (args[0].equalsIgnoreCase("spawnnow")) {
                    plugin.ig.dropRandomLegendaryAtLocation(plugin.ig.generateRandomLocation(), plugin.ig.createRandomLegendary());
                    sender.sendMessage(ChatColor.YELLOW + "[LegendaryItems] " + ChatColor.GREEN + "Item spawned!");
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    plugin.reloadConfig();
                    sender.sendMessage(ChatColor.YELLOW + "[LegendaryItems] " + ChatColor.GREEN + "Configuration reloaded!");
                    return true;
                }

                sender.sendMessage(ChatColor.YELLOW + "[LegendaryItems] " + ChatColor.RED + "Usage: /legendary <spawnnow|reload>");
            }
        }
        return false;

    }
}

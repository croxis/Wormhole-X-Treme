/*
 *   Wormhole X-Treme Plugin for Bukkit
 *   Copyright (C) 2011  Ben Echols
 *                       Dean Bailey
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.wormhole_xtreme.wormhole.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wormhole_xtreme.wormhole.config.ConfigManager;
import com.wormhole_xtreme.wormhole.model.Stargate;
import com.wormhole_xtreme.wormhole.model.StargateManager;
import com.wormhole_xtreme.wormhole.permissions.WXPermissions;
import com.wormhole_xtreme.wormhole.permissions.WXPermissions.PermissionType;

/**
 * The Class WXIDC.
 *
 * @author alron
 */
public class WXIDC implements CommandExecutor {

    /* (non-Javadoc)
     * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        args = CommandUtilities.commandEscaper(args);  
        Player p = null;
        if ( CommandUtilities.playerCheck(sender) )
        {
            p = (Player) sender;
        }
        if ( args.length >= 1 )
        {
            Stargate s = StargateManager.getStargate(args[0]);
            if ( s != null )
            {
                if ((p != null && (WXPermissions.checkWXPermissions(p, PermissionType.CONFIG) || (s.owner != null && s.owner.equals(p.getName())))) || !CommandUtilities.playerCheck(sender))
                {
                    // 2. if args other than name - do a set                
                    if ( args.length >= 2 )
                    {
                        if ( args[1].equals("-clear") )
                        {
                            // Remove from big list of all blocks
                            StargateManager.removeBlockIndex(s.irisActivationBlock);
                            // Set code to "" and then remove it from stargates block list
                            s.setIrisDeactivationCode("");
                        }
                        else
                        {
                            // Set code
                            s.setIrisDeactivationCode(args[1]);
                            // Make sure that block is in index
                            StargateManager.addBlockIndex(s.irisActivationBlock, s);
                        }
                    }
                        
                    // 3. always display current value at end.
                    sender.sendMessage(ConfigManager.MessageStrings.normalHeader.toString() + "IDC for gate: " + s.name + " is:" + s.irisDeactivationCode);
                }
                else
                {
                    sender.sendMessage(ConfigManager.MessageStrings.permissionNo.toString());
                }
            }
            else
            {
                sender.sendMessage(ConfigManager.MessageStrings.errorHeader.toString() + "Invalid Stargate: " + args[0]);

            }
            return true;
        }
        return false;
    }

}

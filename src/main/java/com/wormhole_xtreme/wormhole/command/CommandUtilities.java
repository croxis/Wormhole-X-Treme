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

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wormhole_xtreme.wormhole.model.Stargate;
import com.wormhole_xtreme.wormhole.model.StargateManager;

/**
 * WormholeXTreme Commands and command specific methods.
 *
 * @author Dean Bailey (alron)
 * @author Ben Echols (Lologarithm)
 */
class CommandUtilities {


	/**
	 * Player check.
	 *
	 * @param sender the sender
	 * @return true, if successful
	 */
	static boolean playerCheck(CommandSender sender) {
		if (sender instanceof Player)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Command escaper.
	 * Checks for " and escapes it.
	 *
	 * @param args The String[] argument list to escape quotes on.
	 * @return String[] with properly escaped quotes.
	 */
	static String[] commandEscaper(String[] args)
	{
		StringBuilder temp_string = new StringBuilder();
		boolean start_quote_found = false;
		boolean end_quote_found = false;
		
		ArrayList<String> args_parts_list = new ArrayList<String>();
		
		for(String part : args)
		{
			// First check to see if we have a starting or stopping quote
			if ( part.contains("\"") && !start_quote_found)
			{
				// Two quotes in same string = no spaces in quoted text;
				if ( !part.replaceFirst("\"", "").contains("\"") )
				{
					start_quote_found = true;
				}
			}
			else if ( part.contains("\"") && start_quote_found)
			{
				end_quote_found = true;
			}

			// If no quotes yet, we just append to list
			if ( !start_quote_found )
				args_parts_list.add(part);
			
			// If we have quotes we should make sure to append the values
			// if we found the last quote we should stop adding.
			if ( start_quote_found)
			{
				temp_string.append(part.replace("\"", ""));
				if ( end_quote_found )
				{
					args_parts_list.add(temp_string.toString());
					start_quote_found = false;
					end_quote_found = false;
					temp_string = new StringBuilder();
				}
				else
					temp_string.append(" ");
			}
		}
		return args_parts_list.toArray(new String[] {});
	}
	
	/**
	 * Gate remove.
	 *
	 * @param stargate the stargate
	 * @param destroy true to destroy gate blocks
	 */
	static void gateRemove(Stargate stargate, boolean destroy)
	{
	    stargate.setupGateSign(false);
        stargate.resetTeleportSign();
        if (!stargate.irisDeactivationCode.equals(""))
        {
            if (stargate.irisActive)
            {
                stargate.toggleIrisActive();
            }
            stargate.setupIrisLever(false);
        }
        if (destroy)
        {
            stargate.deleteGateBlocks();
            stargate.deletePortalBlocks();
            stargate.deleteTeleportSign();
        }
        StargateManager.removeStargate(stargate);
	}
	
	/**
	 * Gets the gate network.
	 *
	 * @param stargate the stargate
	 * @return the gate network
	 */
	static String getGateNetwork(Stargate stargate)
	{
	    if (stargate != null)
	    {
	        if (stargate.network != null)
	        {
	            return stargate.network.netName;
	        }
	    }
	    return "Public";
	}
	
	/**
	 * Close gate.
	 *
	 * @param stargate the stargate
	 * @param player the player
	 */
	static final void closeGate(Stargate stargate, Player player)
	{
	    if (stargate != null && player != null)
	    {
	        final Stargate gate = stargate;
	        final Player p = player;
            gate.stopActivationTimer(p);
            gate.deActivateStargate();
            gate.unLightStargate();
	    }
	}
}

package com.antarescraft.kloudy.wonderhud.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.antarescraft.kloudy.wonderhud.WonderHUD;
import com.antarescraft.kloudy.wonderhud.util.ConfigValues;

public class OnPlayerRespawnEvent implements Listener
{
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		Player player = event.getPlayer();
		
		if(player.hasPermission("wh.see") && !WonderHUD.PlayerNoShows.containsKey(player.getUniqueId()) &&
				WonderHUD.getNumPlayersViewing() < ConfigValues.getMaxPlayers())
		{
			WonderHUD.initPlayerHUD(player);
		}
	}
}
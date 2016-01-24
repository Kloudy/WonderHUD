package com.antarescraft.kloudy.wonderhud.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import com.antarescraft.kloudy.wonderhud.PlayerHUD;
import com.antarescraft.kloudy.wonderhud.WonderHUD;
import com.antarescraft.kloudy.wonderhud.util.ConfigValues;

public class OnPlayerChangeWorldEvent implements Listener
{
	@EventHandler
	public void onPlayerChangeWorld(PlayerChangedWorldEvent event)
	{
		Player player = event.getPlayer();
		
		PlayerHUD playerHUD = WonderHUD.PlayerHUDs.get(player.getUniqueId());
		if(playerHUD != null)
		{
			WonderHUD.cancelTimers(player);
			
			playerHUD.destroy();
			
			if(player.hasPermission("wh.see") && ConfigValues.getRestartHUDsOnWorldChange())
			{
				WonderHUD.initPlayerJoinHUD(player);
			}
		}
	}
}
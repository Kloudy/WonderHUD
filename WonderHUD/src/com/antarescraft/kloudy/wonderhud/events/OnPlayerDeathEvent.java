package com.antarescraft.kloudy.wonderhud.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.antarescraft.kloudy.wonderhud.PlayerHUD;
import com.antarescraft.kloudy.wonderhud.WonderHUD;

public class OnPlayerDeathEvent implements Listener
{
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event)
	{
		if(event.getEntity() instanceof Player)
		{
			Player player = (Player)event.getEntity();
			
			WonderHUD.cancelTimers(player);
			
			PlayerHUD playerHUD = WonderHUD.PlayerHUDs.get(player.getUniqueId());
			
			if(playerHUD != null)
			{
				playerHUD.destroy();
			}
		}
	}
}

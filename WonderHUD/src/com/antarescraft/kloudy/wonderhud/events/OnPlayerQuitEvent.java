package com.antarescraft.kloudy.wonderhud.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.antarescraft.kloudy.wonderhud.WonderHUD;

public class OnPlayerQuitEvent implements Listener
{
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		
		WonderHUD.cancelTimers(player);

		WonderHUD.NextEntityId.remove(player.getUniqueId());
		WonderHUD.PlayerHUDs.remove(player.getUniqueId());
	}
}

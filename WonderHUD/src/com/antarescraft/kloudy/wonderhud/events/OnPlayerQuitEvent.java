package com.antarescraft.kloudy.wonderhud.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.antarescraft.kloudy.plugincore.protocol.PacketManager;
import com.antarescraft.kloudy.wonderhud.WonderHUD;

public class OnPlayerQuitEvent implements Listener
{
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		
		WonderHUD.cancelTimers(player);

		WonderHUD.PlayerHUDs.remove(player.getUniqueId());
		
		PacketManager.resetPlayerNextAvailableEntityId(player);
	}
}

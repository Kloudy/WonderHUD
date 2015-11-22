package com.antarescraft.kloudy.wonderhud.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.antarescraft.kloudy.wonderhud.HUD;
import com.antarescraft.kloudy.wonderhud.PlayerHUD;
import com.antarescraft.kloudy.wonderhud.WonderHUD;
import com.antarescraft.kloudy.wonderhud.protocol.FakeDisplay;

public class OnMoveEvent implements Listener
{
	@EventHandler
	public void onMoveEvent(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		
		if(player.hasPermission("wh.see"))
		{
			PlayerHUD playerHUD = WonderHUD.PlayerHUDs.get(player.getUniqueId());
			
			if(playerHUD != null)
			{
				for(HUD hud : playerHUD.getHUDs())
				{
					FakeDisplay.updateDisplayLocation(hud);
				}
			}
		}
	}
}
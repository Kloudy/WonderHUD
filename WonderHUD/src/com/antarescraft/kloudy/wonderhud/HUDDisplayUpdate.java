package com.antarescraft.kloudy.wonderhud;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.antarescraft.kloudy.wonderhud.hudtypes.BaseHUDType;
import com.antarescraft.kloudy.wonderhud.hudtypes.ImageHUD;
import com.antarescraft.kloudy.wonderhud.protocol.FakeDisplay;

public class HUDDisplayUpdate extends BukkitRunnable
{
	public void start()
	{
		this.runTaskTimer(WonderHUD.plugin, 0, 2);
	}
	
	@Override
	public void run()
	{	
		ArrayList<Player> removePlayers = new ArrayList<Player>();
		for(PlayerHUD playerHUD : WonderHUD.PlayerHUDs.values())
		{
			Player player = playerHUD.getPlayer();
			if(player.hasPermission("wh.see"))
			{
				for(HUD hud : playerHUD.getHUDs())
				{
					for(int i = 0; i < hud.getHudType().getLines(hud.getPlayer()).size(); i++)
					{
						FakeDisplay.updateDisplayLine(hud, i, hud.getHudType().getLines(player).get(i));
					}
				}
			}
			else
			{
				removePlayers.add(playerHUD.getPlayer());
			}
		}
		
		for(Player player : removePlayers)
		{
			PlayerHUD playerHUD = WonderHUD.PlayerHUDs.get(player.getUniqueId());
			playerHUD.destroy();
		}
		
		//increment all ImageHUD currentFrames
		for(BaseHUDType hudType : WonderHUD.HUDObjects)
		{
			if(hudType instanceof ImageHUD)
			{
				ImageHUD imageHUD = (ImageHUD)hudType;
				imageHUD.incrementCurrentFrame();
			}
		}
	}
}
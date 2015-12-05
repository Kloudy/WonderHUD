package com.antarescraft.kloudy.wonderhud;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.antarescraft.kloudy.wonderhud.hudtypes.BaseHUDType;
import com.antarescraft.kloudy.wonderhud.hudtypes.ImageHUD;
import com.antarescraft.kloudy.wonderhud.protocol.FakeDisplay;

public class HUDDisplayUpdate extends BukkitRunnable
{
	boolean updateDisplay = true;
	
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
					hud.evaluateLines();//evaluate placeholders
					
					for(int i = 0; i < hud.getHudType().getLines(hud.getPlayer()).size(); i++)
					{
						if(hud.lineChanged(i))//don't send update packet if the line hasn't changed
						{
							FakeDisplay.updateDisplayLine(hud, i, hud.getEvaluatedLines().get(i));
						}
					}
					
					hud.updatePrevEvaluatedLines();//update prevEvaluated lines for comparison
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
			
		for(PlayerHUD playerHUD : WonderHUD.PlayerHUDs.values())
		{
			for(HUD hud : playerHUD.getHUDs())
			{
				if(playerHUD.moved())
				{
					FakeDisplay.updateDisplayLocation(hud);
				}
			}
		}
	}
}
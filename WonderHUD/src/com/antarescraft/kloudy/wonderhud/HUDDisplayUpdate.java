package com.antarescraft.kloudy.wonderhud;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.antarescraft.kloudy.plugincore.protocol.PacketManager;
import com.antarescraft.kloudy.wonderhud.hudtypes.BaseHUDType;
import com.antarescraft.kloudy.wonderhud.hudtypes.ImageHUD;
import com.antarescraft.kloudy.wonderhud.hudtypes.RegionImageHUD;

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
		//check if player walks into a regionHUD region
		if(WonderHUD.WorldGuard != null)
		{
			for(Player player : Bukkit.getOnlinePlayers())
			{
				WonderHUD.checkRegionHUD(player);
			}
		}
		
		ArrayList<Player> removePlayers = new ArrayList<Player>();
		for(PlayerHUD playerHUD : WonderHUD.PlayerHUDs.values())
		{
			Player player = playerHUD.getPlayer();
			
			if(player.hasPermission("wh.see"))
			{
				for(HUD hud : playerHUD.getHUDs())
				{
					if(playerHUD.moved())
					{
						for(int i = 0; i < hud.getHudType().getLines(player).size(); i++)
						{
							Location location = hud.calculateNewLocation(i, hud.getHudType().getDistance(), hud.getHudType().getDeltaTheta(), hud.getHudType().getOffsetAngle());
							PacketManager.updateEntityLocation(player, hud.getEntityIds().get(i), location);
						}
					}
					
					hud.evaluateLines();//evaluate placeholders
					
					for(int i = 0; i < hud.getHudType().getLines(hud.getPlayer()).size(); i++)
					{
						if(hud.lineChanged(i))//don't send update packet if the line hasn't changed
						{
							PacketManager.updateEntityText(player, hud.getEntityIds().get(i), hud.getEvaluatedLines().get(i));
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
			if(hudType instanceof ImageHUD || hudType instanceof RegionImageHUD)
			{
				ImageHUD imageHUD = (ImageHUD)hudType;
				imageHUD.incrementCurrentFrame();
			}
		}
	}
}
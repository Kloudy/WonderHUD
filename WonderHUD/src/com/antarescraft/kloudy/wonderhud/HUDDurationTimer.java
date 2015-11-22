package com.antarescraft.kloudy.wonderhud;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

public class HUDDurationTimer extends BukkitRunnable
{
	PlayerHUD playerHUD;
	HUD hud;
	
	public HUDDurationTimer(PlayerHUD playerHUD, HUD hud)
	{
		this.playerHUD = playerHUD;
		this.hud = hud;
	}
	
	public int getId()
	{
		return hud.getId();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		boolean equal = false;
		
		if(obj instanceof HUDDurationTimer)
		{
			HUDDurationTimer timerObj = (HUDDurationTimer)obj;
			
			if(timerObj.getId() == getId())
			{
				equal = true;
			}
		}
		
		return equal;
	}
	
	public void start()
	{
		int duration = hud.getHudType().getDuration();

		if(duration >= 0)
		{
			duration = duration * 20;//convert seconds to ticks
			
			this.runTaskLater(WonderHUD.plugin, duration);
		}
	}
	
	@Override
	public void run()
	{
		hud.destroyDisplay();
		
		playerHUD.getHUDs().remove(hud);
		
		ArrayList<HUDDurationTimer> durationTimers = WonderHUD.HUDDurationTimers.get(hud.getPlayer().getUniqueId());
		if(durationTimers != null)
		{
			durationTimers.remove(this);
		}
	}
}

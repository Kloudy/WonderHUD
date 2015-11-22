package com.antarescraft.kloudy.wonderhud;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

public class HUDStartTimer extends BukkitRunnable
{
	private PlayerHUD playerHUD;
	private HUD hud;
	
	public HUDStartTimer(PlayerHUD playerHUD, HUD hud)
	{
		this.playerHUD = playerHUD;
		this.hud = hud;
	}
	
	public void start(int startTime)
	{
		this.runTaskLater(WonderHUD.plugin, startTime * 20);
	}
	
	public int getId()
	{
		return hud.getId();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		boolean equal = false;
		
		if(obj instanceof HUDStartTimer)
		{
			HUDStartTimer timerObj = (HUDStartTimer)obj;
			
			if(timerObj.getId() == getId())
			{
				equal = true;
			}
		}
		
		return equal;
	}
	
	@Override
	public void run()
	{
		hud.spawnDisplay();
		playerHUD.getHUDs().add(hud);
		hud.setId(playerHUD.getNextId());
		
		ArrayList<HUDStartTimer> startTimers = WonderHUD.HUDStartTimers.get(hud.getPlayer().getUniqueId());
		if(startTimers != null)
		{
			startTimers.remove(this);
		}
		
		HUDDurationTimer durationTimer = new HUDDurationTimer(playerHUD, hud);
		
		ArrayList<HUDDurationTimer> durationTimers = WonderHUD.HUDDurationTimers.get(hud.getPlayer().getUniqueId());
		if(durationTimers == null)
		{
			durationTimers = new ArrayList<HUDDurationTimer>();
			WonderHUD.HUDDurationTimers.put(hud.getPlayer().getUniqueId(), durationTimers);
		}
		
		if(hud.getHudType().getDuration() > 0)
		{
			durationTimers.add(durationTimer);
		}

		durationTimer.start();
	}
}
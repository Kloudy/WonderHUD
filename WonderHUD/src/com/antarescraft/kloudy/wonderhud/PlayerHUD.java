package com.antarescraft.kloudy.wonderhud;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerHUD 
{
	private Player player;
	private ArrayList<HUD> HUDs;
	private int nextId;
	private Location prevLocation;
	private boolean isRegionHUD;
	
	public PlayerHUD(Player player)
	{
		this(player, false);
	}
	
	public PlayerHUD(Player player, boolean isRegionHUD)
	{
		this.player = player;
		HUDs = new ArrayList<HUD>();
		prevLocation = player.getLocation();
		nextId = 0;
		this.isRegionHUD = isRegionHUD;
	}
	
	public void destroy()
	{
		for(HUD hud : HUDs)
		{
			hud.destroyDisplay();
		}
		
		WonderHUD.cancelTimers(player);
		WonderHUD.PlayerHUDs.remove(player.getUniqueId());
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public ArrayList<HUD> getHUDs()
	{
		return HUDs;
	}
	
	public int getNextId()
	{
		nextId++;
		return nextId;
	}
	
	public Location getPrevLocation()
	{
		return prevLocation;
	}
	
	public boolean isRegionHUD()
	{
		return isRegionHUD;
	}
	
	public boolean moved()
	{
		Location loc = player.getLocation();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		double yaw = loc.getYaw();
		double pitch = loc.getPitch();
		
		double pX = prevLocation.getX();
		double pY = prevLocation.getY();
		double pZ = prevLocation.getZ();
		double pYaw = prevLocation.getYaw();
		double pPitch = prevLocation.getPitch();
		
		boolean moved = false;
		if(x != pX || y != pY || z != pZ || yaw != pYaw || pitch != pPitch)
		{
			moved = true;
		}
		
		prevLocation = loc.clone();
		
		return moved;
	}
}
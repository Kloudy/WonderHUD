package com.antarescraft.kloudy.wonderhud;

import org.bukkit.entity.Player;

public class PlayerRegionHUD extends PlayerHUD
{
	private String regionName;
	
	public PlayerRegionHUD(Player player, String regionName)
	{
		super(player);
		
		this.regionName = regionName;
	}
	
	public String getRegionName()
	{
		return regionName;
	}
}
package com.antarescraft.kloudy.wonderhud;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class PlayerHUD 
{
	private Player player;
	private ArrayList<HUD> HUDs;
	private int nextId;
	
	public PlayerHUD(Player player)
	{
		this.player = player;
		HUDs = new ArrayList<HUD>();
		nextId = 0;
	}
	
	public void destroy()
	{
		for(HUD hud : HUDs)
		{
			hud.destroyDisplay();
		}
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
}
package com.antarescraft.kloudy.wonderhud.hudtypes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;

import com.antarescraft.kloudy.wonderhud.util.CustomPlaceholders;

public abstract class BasicHUD extends BaseHUDType
{
	public BasicHUD(List<String> lines, boolean active, int duration, int startTime, String location,
			String showPermission, String hidePermission, boolean loopAfter)
	{
		super(active, duration, startTime, location, showPermission, hidePermission, loopAfter);
		
		this.lines = lines;
	}
	
	@Override
	public int getWidth()
	{
		return -1;
	}
	
	@Override
	public int getHeight()
	{
		return lines.size();
	}
	
	@Override
	public double getDistance()
	{
		return 8;
	}
	
	@Override 
	public double getDeltaTheta()
	{
		return Math.PI / 118;
	}
	
	@Override
	public double getOffsetAngle()
	{
		return Math.PI / 6.2;
	}
	
	@Override
	public ArrayList<String> getLines(Player player)
	{
		ArrayList<String> evaluatedLines = new ArrayList<String>();
		
		for(String line : lines)
		{	
			String str = PlaceholderAPI.setPlaceholders(player, line);
			str = CustomPlaceholders.setCustomPlaceholders(str);
			str = ChatColor.translateAlternateColorCodes('&', str);
			
			evaluatedLines.add(str);
		}
		
		if(evaluatedLines.size() == 0)
		{
			evaluatedLines.add("Add text lines in the config file for this HUD");
		}
		
		return evaluatedLines;
	}
}
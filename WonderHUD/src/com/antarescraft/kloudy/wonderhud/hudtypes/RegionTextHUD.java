package com.antarescraft.kloudy.wonderhud.hudtypes;

import java.util.List;

public class RegionTextHUD extends BasicHUD implements RegionActivatable
{
	private String regionName;
	
	public RegionTextHUD(List<String> lines, boolean active, int duration, int startTime, String location,
			String showPermission, String hidePermission, boolean loopAfter, String regionName)
	{
		super(lines, active, duration, startTime, location, showPermission, hidePermission, loopAfter);
		
		this.regionName = regionName;
	}
	
	@Override
	public String getRegionName()
	{
		return regionName;
	}
}
package com.antarescraft.kloudy.wonderhud.hudtypes;

public class RegionImageHUD extends ImageHUD implements RegionActivatable
{
	private String regionName;
	
	public RegionImageHUD(String imageSource, int width, int height, boolean active, int duration, int startTime, String location,
			String showPermission, String hidePermission, boolean loopAfter, String regionName)
	{
		super(imageSource, width, height, active, duration, startTime, location, showPermission, hidePermission, loopAfter);
		
		this.regionName = regionName;
	}
	
	@Override
	public String getRegionName()
	{
		return regionName;
	}
}
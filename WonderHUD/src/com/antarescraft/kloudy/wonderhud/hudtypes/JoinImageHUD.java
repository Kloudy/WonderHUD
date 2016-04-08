package com.antarescraft.kloudy.wonderhud.hudtypes;

public class JoinImageHUD extends ImageHUD implements JoinActivatable
{
	public JoinImageHUD(String imageSource, int width, int height, boolean active, int duration, int startTime, String location,
			String showPermission, String hidePermission, boolean loopAfter)
	{
		super(imageSource, width, height, active, duration, startTime, location, showPermission, hidePermission, loopAfter);
	}
}

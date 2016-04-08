package com.antarescraft.kloudy.wonderhud.hudtypes;

import java.util.List;

public class JoinTextHUD extends BasicHUD implements JoinActivatable
{
	public JoinTextHUD(List<String> lines, boolean active, int duration, int startTime, String location, 
			String showPermission, String hidePermission, boolean loopAfter) {
		super(lines, active, duration, startTime, location, showPermission, hidePermission, loopAfter);
	}
}
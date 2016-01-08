package com.antarescraft.kloudy.wonderhud.hudtypes;

import java.util.List;

import org.bukkit.entity.Player;

public abstract class BaseHUDType 
{
	protected boolean active;
	protected int duration;
	protected int startTime;
	protected int width;
	protected int height;
	protected String showPermission;
	protected String hidePermission;
	protected String location;
	protected List<String> lines;//unevaluated lines, containing the placeholder strings
	protected boolean loopAfter;
	
	public BaseHUDType(boolean active, int duration, int startTime, String location, String showPermission, String hidePermission, boolean loopAfter)
	{
		this.active = active;
		this.duration = duration;
		this.startTime = startTime;
		this.location = location;
		this.showPermission = showPermission;
		this.hidePermission = hidePermission;
		this.loopAfter = loopAfter;
	}
	
	public abstract List<String> getLines(Player player);
	public abstract int getWidth();
	public abstract int getHeight();
	public abstract double getDistance();
	public abstract double getDeltaTheta();
	public abstract double getOffsetAngle();
	
	public boolean getActive()
	{
		return active;
	}
	
	public int getDuration()
	{
		return duration;
	}
	
	public int getStartTime()
	{
		return startTime;
	}
	
	public String getLocation()
	{
		return location;
	}
	
	public String getShowPermission()
	{
		return showPermission;
	}
	
	public String getHidePermission()
	{
		return hidePermission;
	}
	
	public boolean getLoopAfter()
	{
		return loopAfter;
	}
}
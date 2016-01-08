package com.antarescraft.kloudy.wonderhud.hudtypes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.wonderhud.WonderHUD;

public class ImageHUD extends BaseHUDType
{
	private int currentFrame = 0;
	private String imageSource;
	private int width;
	private double distance, deltaTheta, offsetAngle;
	
	public ImageHUD(String imageSource, int width, int height, boolean active, int duration, int startTime, String location,
			String showPermission, String hidePermission, boolean loopAfter)
	{
		super(active, duration, startTime, location, showPermission, hidePermission, loopAfter);
		
		this.imageSource = imageSource;
		this.width = width;
		this.height = height;
		lines = new ArrayList<String>();
		
		distance = 20;
		deltaTheta = Math.PI / 288;
		offsetAngle = Math.PI / 5.7;
	}
	
	@Override
	public List<String> getLines(Player player)
	{	
		lines.clear();
		String[][] frames = WonderHUD.ImageLines.get(imageSource);
		if(frames != null)
		{
			for(String l : frames[currentFrame])
			{
				lines.add(l);
			}
		}
		
		if(lines.size() > 0 && lines.get(0).equals("§4§lThere was an error when processing your image"))
		{
			distance = 8;
			deltaTheta = Math.PI / 118;
			offsetAngle = Math.PI / 6;
		}
		
		return lines;
	}
	
	public void incrementCurrentFrame()
	{
		currentFrame++;
		
		if(currentFrame >= WonderHUD.ImageLines.get(imageSource).length)
		{
			currentFrame = 0;
		}
	}
	
	@Override
	public int getWidth()
	{
		return width;
	}
	
	@Override
	public int getHeight()
	{
		return height;
	}
	
	@Override
	public double getDistance()
	{
		return distance;
	}
	
	@Override
	public double getDeltaTheta()
	{
		return deltaTheta;
	}
	
	@Override
	public double getOffsetAngle()
	{
		return offsetAngle;
	}
	
	public String getImageSource()
	{
		return imageSource;
	}
}
package com.antarescraft.kloudy.wonderhud.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.antarescraft.kloudy.wonderhud.WonderHUD;
import com.antarescraft.kloudy.wonderhud.hudtypes.BaseHUDType;
import com.antarescraft.kloudy.wonderhud.hudtypes.BasicHUD;
import com.antarescraft.kloudy.wonderhud.hudtypes.ImageHUD;

public class ConfigValues 
{
	private static final String HUD_SHOW_TEXT = "hud-show-text";
	private static final String HUD_HIDE_TEXT= "hud-hide-text";
	private static final String HUD_SHOW_ALL_TEXT = "hud-show-all-text";
	private static final String HUD_HIDE_ALL_TEXT = "hud-hide-all-text";
	private static final String HUD_OBJECTS = "hud-objects";
	private static final String HUD_TYPE = "type";
	private static final String HUD_ACTIVE = "active";
	private static final String HUD_DURATION = "duration";
	private static final String HUD_START_TIME = "start-time";
	private static final String HUD_LOCATION = "location";
	private static final String HUD_LINES = "lines";
	private static final String HUD_IMAGE = "image-src";
	private static final String HUD_WIDTH = "width";
	private static final String HUD_HEIGHT = "height";
	private static final String BASIC_HUD = "basic-hud";
	private static final String IMAGE_HUD = "image-hud";
	private static final String MAX_PLAYERS = "max-players";
	
	public static void reloadConfig()
	{
		WonderHUD.plugin.reloadConfig();
	}
	
	public static int getMaxPlayers()
	{
		FileConfiguration config = WonderHUD.plugin.getConfig();
		
		return config.getInt(MAX_PLAYERS, 25);
	}
	
	public static String getHUDShowText()
	{
		FileConfiguration config = WonderHUD.plugin.getConfig();
		
		return config.getString(HUD_SHOW_TEXT, null);
	}
	
	public static String getHUDHideText()
	{
		FileConfiguration config = WonderHUD.plugin.getConfig();
		
		return config.getString(HUD_HIDE_TEXT, null);
	}
	
	public static String getHUDShowAllText()
	{
		FileConfiguration config = WonderHUD.plugin.getConfig();
		
		return config.getString(HUD_SHOW_ALL_TEXT, null);
	}
	
	public static String getHUDHideAllText()
	{
		FileConfiguration config = WonderHUD.plugin.getConfig();
		
		return config.getString(HUD_HIDE_ALL_TEXT, null);
	}
	
	public static ArrayList<BaseHUDType> getHUDObjects()
	{
		FileConfiguration config = WonderHUD.plugin.getConfig();
		ArrayList<BaseHUDType> hudObjects = new ArrayList<BaseHUDType>();
		
		Set<String> hudObjectKeys = config.getConfigurationSection(HUD_OBJECTS).getKeys(false);
		
		//read user defined HUD objects
		for(String key : hudObjectKeys)
		{
			ConfigurationSection hudObject = config.getConfigurationSection(HUD_OBJECTS + "." + key);
			if(hudObject != null)
			{
				String type = hudObject.getString(HUD_TYPE);
				if(type != null)
				{
					boolean active = hudObject.getBoolean(HUD_ACTIVE, true);
					int duration = hudObject.getInt(HUD_DURATION, -1);
					int startTime = hudObject.getInt(HUD_START_TIME, 0);
					String location = hudObject.getString(HUD_LOCATION, "top");
					
					if(type.equals(BASIC_HUD))
					{
						List<String> lines = hudObject.getStringList(HUD_LINES);
						if(lines != null)
						{
							BasicHUD basicHUD = new BasicHUD(lines, active, duration, startTime, location);
							hudObjects.add(basicHUD);
						}
					}
					else if(type.equals(IMAGE_HUD))
					{
						String imageSource = hudObject.getString(HUD_IMAGE);
						if(imageSource != null)
						{
							int width = hudObject.getInt(HUD_WIDTH, 40);
							int height = hudObject.getInt(HUD_HEIGHT, 20);
							
							ImageHUD imageHUD = new ImageHUD(imageSource, width, height, active, duration, startTime, location);
							hudObjects.add(imageHUD);
						}
					}
				}
			}
		}

		return hudObjects;
	}
}
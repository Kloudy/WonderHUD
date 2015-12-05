package com.antarescraft.kloudy.wonderhud;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.antarescraft.kloudy.wonderhud.events.*;
import com.antarescraft.kloudy.wonderhud.hudtypes.BaseHUDType;
import com.antarescraft.kloudy.wonderhud.hudtypes.ImageHUD;
import com.antarescraft.kloudy.wonderhud.util.ConfigValues;
import com.antarescraft.kloudy.wonderhud.util.IOManager;

public class WonderHUD extends JavaPlugin
{
	public static JavaPlugin plugin;
	public static Hashtable<UUID, PlayerHUD> PlayerHUDs;
	public static Hashtable<UUID, Integer> NextEntityId;//<player_uuid, next available entity id number>
	public static Hashtable<UUID, Player> PlayerNoShows;
	public static Hashtable<String, String[][]> ImageLines;
	public static ArrayList<BaseHUDType> HUDObjects;
	public static Hashtable<UUID, ArrayList<HUDStartTimer>> HUDStartTimers;
	public static Hashtable<UUID, ArrayList<HUDDurationTimer>> HUDDurationTimers;

	@Override
	public void onEnable()
	{
		plugin = this;
		NextEntityId = new Hashtable<UUID, Integer>();
		PlayerHUDs = new Hashtable<UUID, PlayerHUD>();
		PlayerNoShows = new Hashtable<UUID, Player>();
		ImageLines = new Hashtable<String, String[][]>();
		HUDStartTimers = new Hashtable<UUID, ArrayList<HUDStartTimer>>();
		HUDDurationTimers = new Hashtable<UUID, ArrayList<HUDDurationTimer>>();

		saveDefaultConfig();
		
		HUDObjects = ConfigValues.getHUDObjects();
		
		getCommand("wh").setExecutor(new OnCommandEvent());
		getServer().getPluginManager().registerEvents(new OnPlayerJoinEvent(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerQuitEvent(), this);
		//getServer().getPluginManager().registerEvents(new OnMoveEvent(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerDeathEvent(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerRespawnEvent(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerChangeWorldEvent(), this);
		
		IOManager.initFileStructure();

		displayHUDs();
		
		HUDDisplayUpdate update =  new HUDDisplayUpdate();
		update.start();
	}
	
	@Override 
	public void onDisable()
	{
		for(PlayerHUD playerHUD : PlayerHUDs.values())
		{
			playerHUD.destroy();
		}
	}
	
	//WonderHUD
	public static void initPlayerHUD(Player player)
	{
		PlayerHUD playerHUD = new PlayerHUD(player);
		
		for(BaseHUDType hudType : HUDObjects)
		{
			HUD hud = new HUD(player, hudType);
			
			HUDStartTimer timer = new HUDStartTimer(playerHUD, hud);

			ArrayList<HUDStartTimer> startTimers = WonderHUD.HUDStartTimers.get(player.getUniqueId());
			if(startTimers == null)
			{
				startTimers = new ArrayList<HUDStartTimer>();
				WonderHUD.HUDStartTimers.put(player.getUniqueId(), startTimers);
			}
			
			startTimers.add(timer);
			
			timer.start(hudType.getStartTime());
		}
		
		WonderHUD.PlayerHUDs.put(player.getUniqueId(), playerHUD);
	}
	
	public static void displayHUDs()
	{
		WonderHUD.ImageLines.clear();
		
		//load images defined in the config file
		for(BaseHUDType hudType : HUDObjects)
		{
			if(hudType instanceof ImageHUD)
			{
				ImageHUD imageHUD = (ImageHUD)hudType;
				IOManager.loadImage(imageHUD.getImageSource(), imageHUD.getWidth(), imageHUD.getHeight());
			}
		}
		
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(player.hasPermission("wh.see") && !PlayerNoShows.containsKey(player.getUniqueId()) &&
					WonderHUD.getNumPlayersViewing() < ConfigValues.getMaxPlayers())
			{
				initPlayerHUD(player);		
			}
		}
	}
	
	public static void cancelTimers(Player player)
	{
		ArrayList<HUDStartTimer> startTimers = WonderHUD.HUDStartTimers.get(player.getUniqueId());
		if(startTimers != null)
		{
			for(HUDStartTimer timer : startTimers)
			{
				timer.cancel();
			}
			startTimers.clear();
		}
		
		ArrayList<HUDDurationTimer> durationTimers = WonderHUD.HUDDurationTimers.get(player.getUniqueId());
		if(durationTimers != null)
		{
			for(HUDDurationTimer timer : durationTimers)
			{
				timer.cancel();
			}
			durationTimers.clear();
		}
	}
	
	public static int getNumPlayersViewing()
	{
		return PlayerHUDs.size() - PlayerNoShows.size();
	}
}
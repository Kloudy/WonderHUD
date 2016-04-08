package com.antarescraft.kloudy.wonderhud;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.antarescraft.kloudy.wonderhud.events.*;
import com.antarescraft.kloudy.wonderhud.hudtypes.BaseHUDType;
import com.antarescraft.kloudy.wonderhud.hudtypes.ImageHUD;
import com.antarescraft.kloudy.wonderhud.hudtypes.JoinActivatable;
import com.antarescraft.kloudy.wonderhud.hudtypes.RegionActivatable;
import com.antarescraft.kloudy.wonderhud.util.ConfigValues;
import com.antarescraft.kloudy.wonderhud.util.IOManager;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WonderHUD extends JavaPlugin
{
	public static JavaPlugin plugin;
	public static Hashtable<UUID, PlayerHUD> PlayerHUDs;
	public static Hashtable<UUID, Integer> NextEntityId;//<player_uuid, next available entity id number>
	public static ArrayList<UUID> PlayerNoShows;
	public static Hashtable<String, String[][]> ImageLines;
	public static ArrayList<BaseHUDType> HUDObjects;
	public static Hashtable<UUID, ArrayList<HUDStartTimer>> HUDStartTimers;
	public static Hashtable<UUID, ArrayList<HUDDurationTimer>> HUDDurationTimers;
	public static boolean hasRegionHUDs;
	public static ArrayList<String> regionNames;//list for quickly looking up regionNames
	public static WorldGuardPlugin WorldGuard;
	public static String MinecraftVersion;

	@Override
	public void onEnable()
	{
		plugin = this;
		NextEntityId = new Hashtable<UUID, Integer>();
		PlayerHUDs = new Hashtable<UUID, PlayerHUD>();
		ImageLines = new Hashtable<String, String[][]>();
		HUDStartTimers = new Hashtable<UUID, ArrayList<HUDStartTimer>>();
		HUDDurationTimers = new Hashtable<UUID, ArrayList<HUDDurationTimer>>();
		hasRegionHUDs = false;
		regionNames = new ArrayList<String>();
		WorldGuard = getWorldGuard();
		
		System.out.println(Bukkit.getVersion());
		MinecraftVersion = Bukkit.getVersion().split(" ")[1];

		saveDefaultConfig();
		
		ConfigValues.loadHUDObjects();
		
		getCommand("wh").setExecutor(new CommandEvent());
		getServer().getPluginManager().registerEvents(new OnPlayerJoinEvent(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerQuitEvent(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerDeathEvent(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerRespawnEvent(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerChangeWorldEvent(), this);
		
		IOManager.initFileStructure();
		IOManager.saveWonderHUDImage();
		
		PlayerNoShows = IOManager.getPlayerNoShowsFromFile();

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
		
		IOManager.savePlayerNoShows();
	}

	private static void initPlayerHUD(PlayerHUD playerHUD, BaseHUDType hudType)
	{
		Player player = playerHUD.getPlayer();
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
	
	//initializes regular HUDs (non-region HUDs)
	public static void initPlayerJoinHUD(Player player)
	{
		PlayerHUD playerHUD = new PlayerHUD(player);
		
		for(BaseHUDType hudType : HUDObjects)
		{
			if(hudType instanceof JoinActivatable)
			{
				String showPermission = hudType.getShowPermission();
				String hidePermission = hudType.getHidePermission();
				if((showPermission.equals("") || player.hasPermission(showPermission)) && 
						(hidePermission.equals("") || !player.hasPermission(hidePermission)))
				{
					initPlayerHUD(playerHUD, hudType);
				}
			}
		}
		
		WonderHUD.PlayerHUDs.put(player.getUniqueId(), playerHUD);
	}
	
	//initializes region specific HUDs
	public static void initPlayerRegionHUD(Player player, String regionName)
	{
		//flag to check if the player can see at least one regionHUD
		//don't remove their joinHUD if they can't see at least one regionHUD
		boolean canSee = false;
		
		PlayerRegionHUD playerRegionHUD = new PlayerRegionHUD(player, regionName);
		
		for(BaseHUDType hudType : HUDObjects)
		{
			if(hudType instanceof RegionActivatable)
			{
				RegionActivatable regionActivatable = (RegionActivatable)hudType;
				String showPermission = hudType.getShowPermission();
				String hidePermission = hudType.getHidePermission();
				if((showPermission.equals("") || player.hasPermission(showPermission)) && 
						(hidePermission.equals("") || !player.hasPermission(hidePermission)) &&
						regionActivatable.getRegionName().equals(regionName))
				{
					canSee = true;
					initPlayerHUD(playerRegionHUD, hudType);
				}
			}
		}
		
		if(canSee)
		{
			PlayerHUD playerHUD = WonderHUD.PlayerHUDs.get(player.getUniqueId());//destroy any existing playerHUD
			if(playerHUD != null)
			{
				playerHUD.destroy();
			}

			WonderHUD.PlayerHUDs.put(player.getUniqueId(), playerRegionHUD);
		}
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
			if(player.hasPermission("wh.see") && !PlayerNoShows.contains(player.getUniqueId()) &&
					WonderHUD.getNumPlayersViewing() < ConfigValues.getMaxPlayers())
			{
				initPlayerJoinHUD(player);
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
		return PlayerHUDs.size() + PlayerNoShows.size();
	}
	
	public static boolean checkRegionHUD(Player player)
	{
		boolean showRegionHUD = false;
		if(player.hasPermission("wh.see") && !PlayerNoShows.contains(player.getUniqueId()))
		{
			if(WonderHUD.hasRegionHUDs)
			{
				//get all regions that exist at the player's location
				ApplicableRegionSet regionSet = WorldGuard.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
				if(regionSet.size() > 0)
				{
					for(ProtectedRegion region : regionSet)
					{
						if(regionNames.contains(region.getId()))
						{
							showRegionHUD = true;
							
							//check if player is currently viewing a joinHUD
							PlayerHUD playerHUD = WonderHUD.PlayerHUDs.get(player.getUniqueId());
							if(playerHUD != null && !(playerHUD instanceof PlayerRegionHUD))
							{
								playerHUD.destroy();
							}
							
							//check to see if the player already has a regionHUD up for this region
							boolean alreadyViewing = false;
							if(playerHUD != null && playerHUD instanceof PlayerRegionHUD)
							{
								PlayerRegionHUD playerRegionHUD = (PlayerRegionHUD)playerHUD;
								if(playerRegionHUD.getRegionName().equals(region.getId()))
								{
									alreadyViewing = true;
								}
							}
							
							if(!alreadyViewing)
							{
								initPlayerRegionHUD(player, region.getId());
							}
							break;//break after finding first region, whether a regionHUD gets displayed or not
						}
					}
				}
				else//no region at the player's current location - destroy the player's regionHUD if it exists
				{
					PlayerHUD playerHUD = WonderHUD.PlayerHUDs.get(player.getUniqueId());
					if(playerHUD != null && playerHUD instanceof PlayerRegionHUD)
					{
						playerHUD.destroy();
						
						if(ConfigValues.getRestartHUDsOnRegionExit())
						{
							initPlayerJoinHUD(player);
						}
					}
				}
			}
		}
		else
		{
			PlayerHUD playerHUD = WonderHUD.PlayerHUDs.get(player.getUniqueId());
			if(playerHUD != null)
			{
				playerHUD.destroy();
			}
		}
		
		return showRegionHUD;
	}
	
	private WorldGuardPlugin getWorldGuard() 
	{
		Plugin plugin = null;
		
		try
		{
			plugin = getServer().getPluginManager().getPlugin("WorldGuard");
		}
		catch(Exception e)
		{
			getLogger().info(e.getMessage());
		}
	 
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	        return null;
	    }
	 
	    return (WorldGuardPlugin) plugin;
	}
}
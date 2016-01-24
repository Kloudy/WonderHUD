package com.antarescraft.kloudy.wonderhud.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.wonderhud.PlayerHUD;
import com.antarescraft.kloudy.wonderhud.WonderHUD;
import com.antarescraft.kloudy.wonderhud.util.CommandHandler;
import com.antarescraft.kloudy.wonderhud.util.CommandParser;
import com.antarescraft.kloudy.wonderhud.util.ConfigValues;
import com.antarescraft.kloudy.wonderhud.util.MessageManager;

public class CommandEvent implements CommandExecutor
{	
	public static String baseCommand = "wh";
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		return CommandParser.parseCommand(this, cmd.getName(), sender, args);
	}
	
	@CommandHandler(argsDescription = "", subcommand = "", description = "Author, Version, Website",
			mustBePlayer = false, numArgs = 0, permission = "wh.admin")
	public void wh(CommandSender sender, String[] args)
	{
		String message = ChatColor.BOLD + "==========WonderHUD==========\n";
		message += ChatColor.GRAY + "Author: " + ChatColor.RED + "Kloudy\n";
		message += ChatColor.GRAY + "Version: " + ChatColor.AQUA + WonderHUD.plugin.getDescription().getVersion() + "\n";
		message += ChatColor.GREEN + "Website: " + ChatColor.GRAY + "" + ChatColor.UNDERLINE + "playminecraft.net\n";
		message += ChatColor.WHITE + "" + ChatColor.BOLD + "=============================";
		
		sender.sendMessage(message);
	}
	
	@CommandHandler(argsDescription = "", description = "Displays all WonderHUD commands",
			mustBePlayer = false, numArgs = 1, permission = "wh.see", subcommand = "help")
	public void help(CommandSender sender, String[] args)
	{
		MessageManager.pageList(sender, CommandParser.gatherHelpStrings(this), "WonderHUD Help");
	}
	
	@CommandHandler(argsDescription = "", description = "Reload the config file", 
			mustBePlayer = false, numArgs = 1, permission = "wh.admin", subcommand = "reloadconfig")
	public void reloadConfig(CommandSender sender, String[] args)
	{
		MessageManager.info(sender, "reloading config file values...");
		
		for(PlayerHUD playerHUD : WonderHUD.PlayerHUDs.values())
		{
			WonderHUD.cancelTimers(playerHUD.getPlayer());
			
			playerHUD.destroy();
		}
		
		ConfigValues.reloadConfig();
		ConfigValues.loadHUDObjects();
		WonderHUD.displayHUDs();
		
		MessageManager.success(sender, "config file reloaded");
	}
	
	@CommandHandler(argsDescription = "", description = "Hides your HUD", 
			mustBePlayer = true, numArgs = 1, permission = "wh.visibility", subcommand = "hide")
	public void hide(CommandSender sender, String[] args)
	{
		Player player = (Player)sender;
		if(!WonderHUD.PlayerNoShows.contains(player.getUniqueId()))
		{
			PlayerHUD playerHUD = WonderHUD.PlayerHUDs.get(player.getUniqueId());
			if(playerHUD != null)
			{
				WonderHUD.cancelTimers(playerHUD.getPlayer());
				
				playerHUD.destroy();
				
				WonderHUD.PlayerNoShows.add(player.getUniqueId());
				
				MessageManager.success(sender, ConfigValues.getHUDHideText());
			}
			else
			{
				MessageManager.error(sender, "You do not currently have an active HUD to hide.");
			}
		}
		else
		{
			MessageManager.error(sender, "You do not currently have an active HUD to hide.");
		}
	}
	
	@CommandHandler(argsDescription = "", description = "Shows your HUD",
			mustBePlayer = true, numArgs = 1, permission = "wh.visibility", subcommand = "show")
	public void show(CommandSender sender, String[] args)
	{
		Player player = (Player)sender;
		if(WonderHUD.PlayerNoShows.contains(player.getUniqueId()))
		{
			if(WonderHUD.getNumPlayersViewing() < ConfigValues.getMaxPlayers())
			{
				if(!WonderHUD.checkRegionHUD(player))
				{
					WonderHUD.initPlayerJoinHUD(player);
				}

				WonderHUD.PlayerNoShows.remove(player.getUniqueId());
				
				MessageManager.success(sender, ConfigValues.getHUDShowText());
			}
			else
			{
				String message = "The maximum number of players allowed to view their HUDs has been reached. ";
				message += "Increase the maximum number of players in the config file, or try again later when fewer players are viewing their HUDs.";
				MessageManager.error(sender, message);
			}
		}
		else
		{
			MessageManager.error(sender, "You do not currently have a hidden HUD to show.");
		}
	}
	
	@CommandHandler(argsDescription = "", description = "Hides HUDs for all players",
			mustBePlayer = false, numArgs = 1, permission = "wh.admin", subcommand = "hideall")
	public void hideall(CommandSender sender, String[] args)
	{
		for(PlayerHUD playerHUD : WonderHUD.PlayerHUDs.values())
		{
			WonderHUD.PlayerNoShows.add(playerHUD.getPlayer().getUniqueId());
			WonderHUD.cancelTimers(playerHUD.getPlayer());
			playerHUD.destroy();
		}
		
		MessageManager.success(sender, ConfigValues.getHUDHideAllText());
	}
	
	@CommandHandler(argsDescription = "", description = "Shows HUDs for all players", 
			mustBePlayer = false, numArgs = 1, permission = "wh.admin", subcommand = "showall")
	public void showall(CommandSender sender, String[] args)
	{
		for(UUID uuid : WonderHUD.PlayerNoShows)
		{
			Player player = Bukkit.getPlayer(uuid);
			if(player != null && player.hasPermission("wh.see") && WonderHUD.getNumPlayersViewing() < ConfigValues.getMaxPlayers())
			{
				if(!WonderHUD.checkRegionHUD(player))
				{
					WonderHUD.initPlayerJoinHUD(player);
				}
			}
		}
		
		WonderHUD.PlayerNoShows.clear();
		
		MessageManager.success(sender, ConfigValues.getHUDShowAllText());
	}
	
	@CommandHandler(argsDescription = "", description = "Displays how many players are currently viewing their HUDs",
			mustBePlayer = false, numArgs = 1, permission = "wh.admin", subcommand = "playersviewing")
	public void playersviewing(CommandSender sender, String[] args)
	{
		if(WonderHUD.getNumPlayersViewing() == 1)
		{
			MessageManager.success(sender, WonderHUD.getNumPlayersViewing() + " player is currently viewing their HUD");
		}
		else
		{
			MessageManager.success(sender, WonderHUD.getNumPlayersViewing() + " players are currently viewing their HUDs");
		}
	}
}
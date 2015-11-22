package com.antarescraft.kloudy.wonderhud.events;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.wonderhud.PlayerHUD;
import com.antarescraft.kloudy.wonderhud.WonderHUD;
import com.antarescraft.kloudy.wonderhud.util.ConfigValues;
import com.antarescraft.kloudy.wonderhud.util.MessageManager;

public class OnCommandEvent implements CommandExecutor
{	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(cmd.getName().equals("wh"))
		{
			//wh
			if(args.length == 0)
			{
				String message = ChatColor.BOLD + "==========WonderHUD==========\n";
				message += ChatColor.GRAY + "Author: " + ChatColor.RED + "Kloudy\n";
				message += ChatColor.GRAY + "Version: " + ChatColor.AQUA + WonderHUD.plugin.getDescription().getVersion() + "\n";
				message += ChatColor.GREEN + "Website: " + ChatColor.GRAY + "" + ChatColor.UNDERLINE + "playminecraft.net\n";
				message += ChatColor.WHITE + "" + ChatColor.BOLD + "=============================";
				
				sender.sendMessage(message);
			}
			
			//wh help
			else if(args.length >= 0 && args[0].equalsIgnoreCase("help"))
			{
				String wh = MessageManager.helpString("wh", "Author and version");
				String reloadConfig = MessageManager.helpString("wh reloadconfig", "Reload the config file");
				String hide = MessageManager.helpString("wh hide", "Hides the HUD for you (Will be visible after relog)");
				String show = MessageManager.helpString("wh show", "Shows the HUD for you");
				String hideAll = MessageManager.helpString("wh hideall", "Hides HUDs for all players");
				String showAll = MessageManager.helpString("wh showall", "Shows HUDs for all players");
				String playersViewing = MessageManager.helpString("wh playersviewing", "Displays how many players are currently viewing their HUDs");
				
				ArrayList<String> commandHelpStrings = new ArrayList<String>();
				commandHelpStrings.add(wh);
				commandHelpStrings.add(reloadConfig);
				commandHelpStrings.add(hide);
				commandHelpStrings.add(show);
				commandHelpStrings.add(hideAll);
				commandHelpStrings.add(showAll);
				commandHelpStrings.add(playersViewing);
				
				if(args.length > 1)
				{
					MessageManager.pageList(sender, commandHelpStrings, "WonderHUD Commands", args[1], 10);
				}
				else
				{
					MessageManager.pageList(sender, commandHelpStrings, "WonderHUD Commands");
				}
			}
			
			//wh reloadconfig
			else if(args.length == 1 && args[0].equalsIgnoreCase("reloadconfig"))
			{
				if(sender.hasPermission("wh.admin"))
				{
					MessageManager.info(sender, "reloading config file values...");
					
					for(PlayerHUD playerHUD : WonderHUD.PlayerHUDs.values())
					{
						WonderHUD.cancelTimers(playerHUD.getPlayer());
						
						playerHUD.destroy();
					}
					
					ConfigValues.reloadConfig();
					
					WonderHUD.HUDObjects = ConfigValues.getHUDObjects();
					
					WonderHUD.displayHUDs();
					
					MessageManager. success(sender, "config file reloaded");
				}
				else
				{
					MessageManager.noPermission(sender);
				}
			}
			
			//wh hide
			else if(args.length == 1 && args[0].equalsIgnoreCase("hide"))
			{
				if(sender instanceof Player)
				{
					Player player = (Player)sender;
					
					if(sender.hasPermission("wh.visibility"))
					{
						if(!WonderHUD.PlayerNoShows.containsKey(player.getUniqueId()))
						{
							PlayerHUD playerHUD = WonderHUD.PlayerHUDs.get(player.getUniqueId());
							if(playerHUD != null)
							{
								WonderHUD.cancelTimers(playerHUD.getPlayer());
								
								playerHUD.destroy();
								
								WonderHUD.PlayerNoShows.put(player.getUniqueId(), player);
								
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
					else
					{
						MessageManager.noPermission(sender);
					}
				}
				else
				{
					MessageManager.mustBePlayer(sender);
				}
			}
			
			//wh show
			else if(args.length == 1 && args[0].equalsIgnoreCase("show"))
			{
				if(sender instanceof Player)
				{
					Player player = (Player)sender;
					
					if(sender.hasPermission("wh.visibility"))
					{
						if(WonderHUD.PlayerNoShows.containsKey(player.getUniqueId()))
						{
							if(WonderHUD.getNumPlayersViewing() < ConfigValues.getMaxPlayers())
							{
								WonderHUD.initPlayerHUD(player);
								
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
					else
					{
						MessageManager.noPermission(sender);
					}
				}
				else
				{
					MessageManager.mustBePlayer(sender);
				}
			}
			
			//wh hideall
			else if(args.length == 1 && args[0].equalsIgnoreCase("hideall"))
			{
				if(sender.hasPermission("wh.*"))
				{
					for(PlayerHUD playerHUD : WonderHUD.PlayerHUDs.values())
					{
						WonderHUD.PlayerNoShows.put(playerHUD.getPlayer().getUniqueId(), playerHUD.getPlayer());
						
						WonderHUD.cancelTimers(playerHUD.getPlayer());
						
						playerHUD.destroy();
					}
					
					MessageManager.success(sender, ConfigValues.getHUDHideAllText());
				}
				else
				{
					MessageManager.noPermission(sender);
				}
			}
			
			//wh showall
			else if(args.length == 1 && args[0].equalsIgnoreCase("showall"))
			{
				if(sender.hasPermission("wh.*"))
				{
					for(Player player : WonderHUD.PlayerNoShows.values())
					{
						if(player.hasPermission("wh.see") && WonderHUD.getNumPlayersViewing() < ConfigValues.getMaxPlayers())
						{
							WonderHUD.initPlayerHUD(player);
						}
					}
					
					WonderHUD.PlayerNoShows.clear();
					
					MessageManager.success(sender, ConfigValues.getHUDShowAllText());
				}
				else
				{
					MessageManager.noPermission(sender);
				}
			}
			
			//wh playersviewing
			else if(args.length == 1 && args[0].equalsIgnoreCase("playersviewing"))
			{
				if(sender.hasPermission("wh.admin"))
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
				else
				{
					MessageManager.noPermission(sender);
				}
			}
			
			return true;
		}
		return false;
	}
}
package com.antarescraft.kloudy.wonderhud.util;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageManager 
{
	public static final int ELEMENTS_PER_PAGE = 10;
	
	public static String helpString(String command, String description)
	{
		return ChatColor.GOLD + "/" + command + " - " + ChatColor.AQUA + description;
	}
	
	public static void error(CommandSender sender, String message)
	{
		if(sender != null && message != null && !message.equals(""))
		{
			sender.sendMessage(ChatColor.RED + message);
		}
	}
	
	public static void success(CommandSender sender, String message)
	{
		if(sender != null && message != null && !message.equals(""))
		{
			sender.sendMessage(ChatColor.GREEN + message);
		}	
	}
	
	public static void noPermission(CommandSender sender)
	{
		if(sender != null)
		{
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
		}
	}
	
	public static void mustBePlayer(CommandSender sender)
	{
		if(sender != null)
		{
			sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
		}
	}
	
	public static void info(CommandSender sender, String message)
	{
		if(sender != null && message != null && !message.equals(""))
		{
			sender.sendMessage(ChatColor.GOLD + message);
		}
	}
	
	public static void pageList(CommandSender sender, ArrayList<String> elements, String listTitle)
	{
		pageList(sender, elements, listTitle, "0", ELEMENTS_PER_PAGE);
	}
	
	public static void pageList(CommandSender sender, ArrayList<String> elements, String listTitle, 
			String pageString, int elementsPerPage)
	{
		int page = 0;
		try
		{
			page = Integer.parseInt(pageString) -1;
			
			if(page < 0)
			{
				page = 0;
			}
		}
		catch(NumberFormatException e)
		{
			pageList(sender, elements, listTitle);
		}
		
		int totalPages = 0;
		
		if(elements.size() % elementsPerPage > 0)
		{
			totalPages = (elements.size() / elementsPerPage) + 1;
		}
		else
		{
			totalPages = (elements.size() / elementsPerPage);
		}
		
		String message = ChatColor.GOLD + "===============================\n";
		message += String.format("%s List - Page: %d Total Pages: %d\n" + ChatColor.AQUA, listTitle, page+1, totalPages);
		
		for(int i = page * elementsPerPage; i <= ((page*elementsPerPage) + elementsPerPage)-1; i++)
		{
			if(i >= elements.size())
			{
				break;
			}
			else
			{
				message +=  elements.get(i).toString() + "\n";
			}
		}
		
		message += ChatColor.GOLD + "===============================";
		
		sender.sendMessage(message);
	}
}
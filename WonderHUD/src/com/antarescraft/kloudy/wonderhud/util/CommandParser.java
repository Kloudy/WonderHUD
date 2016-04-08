package com.antarescraft.kloudy.wonderhud.util;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.wonderhud.events.CommandEvent;

/**
 * CommandParser - Easy Command Parsing Utility Class
 * 
 * @author Kloudy
 *
 */
public class CommandParser 
{
	public static boolean parseCommand(Object invoker, String cmdName, CommandSender sender, String[] args)
	{
		if(cmdName.equals(CommandEvent.baseCommand))
		{
			Method[] commands = invoker.getClass().getDeclaredMethods();		
			for(Method method : commands)
			{
				if(method.isAnnotationPresent(CommandHandler.class))
				{
					CommandHandler handler = method.getAnnotation(CommandHandler.class);
					if(args.length == 0 && handler.numArgs() == 0)
					{
						if((handler.mustBePlayer() && sender instanceof Player) || !handler.mustBePlayer())
						{
							if(sender.hasPermission(handler.permission()) || handler.permission().equals(""))
							{
								executeCommand(invoker, method, sender, args);
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
						
						return true;
					}
					else if(args.length > 0 && args[0].equalsIgnoreCase(handler.subcommand()))
					{
						if((handler.mustBePlayer() && sender instanceof Player) || !handler.mustBePlayer())
						{
							if(sender.hasPermission(handler.permission()) || handler.permission().equals(""))
							{
								if(args.length >= handler.numArgs())
								{
									executeCommand(invoker, method, sender, args);
								}
								else
								{
									MessageManager.invalidArgs(sender, handler.subcommand(), handler.argsDescription(), handler.description());
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
						
						return true;
					}
				}
			}
			
			MessageManager.error(sender, "Unknown command");
		}
		
		return false;
	}
	
	public static ArrayList<String> gatherHelpStrings(Object invoker)
	{
		ArrayList<String> helpStrings = new ArrayList<String>(); 
		
		Method[] methods = invoker.getClass().getDeclaredMethods();
		for(Method method : methods)
		{
			if(method.isAnnotationPresent(CommandHandler.class))
			{
				CommandHandler handler = method.getAnnotation(CommandHandler.class);
				
				helpStrings.add(MessageManager.helpString(handler.subcommand(), handler.argsDescription(), handler.description()));
			}
		}
		
		return helpStrings;
	}
	
	private static void executeCommand(Object invoker, Method command, CommandSender sender, String[] args)
	{
		try
		{
			command.invoke(invoker, sender, args);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
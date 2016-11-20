package com.antarescraft.kloudy.wonderhud.util;

public class CustomPlaceholders
{
	public static String setCustomPlaceholders(String string)
	{
		string = string.replace("|black|", "§0");
		string = string.replace("|dark_blue|", "§1");
		string = string.replace("|dark_green|", "§2");
		string = string.replace("|dark_aqua|", "§3");
		string = string.replace("|dark_red|", "§4");
		string = string.replace("|dark_purple|", "§5");
		string = string.replace("|gold|", "§6");
		string = string.replace("|gray|", "§7");
		string = string.replace("|dark_gray|", "§8");
		string = string.replace("|blue|", "§9");
		string = string.replace("|green|", "§a");
		string = string.replace("|aqua|", "§b");
		string = string.replace("|red|", "§c");
		string = string.replace("|light_purple|", "§d");
		string = string.replace("|yellow|", "§e");
		string = string.replace("|white|", "§f");
		string = string.replace("|bold|", "§l");
		string = string.replace("|strikethrough|", "§m");
		string = string.replace("|obfuscated|", "§k");
		string = string.replace("|underline|", "§n");
		string = string.replace("|italic|", "§o");
		string = string.replace("|reset|", "§r");

		return string;
	}
}
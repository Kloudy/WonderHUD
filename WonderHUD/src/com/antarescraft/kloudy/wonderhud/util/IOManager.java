package com.antarescraft.kloudy.wonderhud.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import com.antarescraft.kloudy.wonderhud.WonderHUD;
import com.antarescraft.kloudy.wonderhud.imageprocessing.GifProcessor;
import com.antarescraft.kloudy.wonderhud.imageprocessing.PngJpgProcessor;

public class IOManager
{
	private static final String PATH_TO_ROOT = "plugins/WonderHUD";
	private static final String PATH_TO_IMAGES = PATH_TO_ROOT + "/images";
	
	public static void initFileStructure()
	{
		try
		{
			File folder = new File(PATH_TO_ROOT);
			if(!folder.exists())
			{
				folder.mkdir();
			}
			
			folder = new File(PATH_TO_IMAGES);
			if(!folder.exists())
			{
				folder.mkdir();
			}
		}
		catch(Exception e){}
	}
	
	public static void loadImage(String imageName, int width, int height)
	{
		File imageFile = new File(PATH_TO_IMAGES + "/" + imageName);
		
		if(imageName.contains(".gif"))
		{
			GifProcessor.processGif(imageName, imageFile, width, height);
		}
		else if(imageName.contains(".jpg") || imageName.contains(".png"))
		{
			PngJpgProcessor.processImage(imageName, imageFile, width, height);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<UUID> getPlayerNoShowsFromFile()
	{
		ArrayList<UUID> playerNoShows = new ArrayList<UUID>();
		try
		{
			File file = new File(PATH_TO_ROOT + "/PlayerNoShows.dat");
			if(!file.exists())
			{
				file.createNewFile();
			}
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream inputStream = null;
			
			if(file.length() > 0)
			{
				inputStream = new ObjectInputStream(fileIn);
				playerNoShows = (ArrayList<UUID>)inputStream.readObject();
				inputStream.close();
			}
		}
		catch(IOException | ClassNotFoundException e){}
		
		return playerNoShows;
	}
	
	public static void savePlayerNoShows()
	{
		try
		{
			FileOutputStream fileOut = new FileOutputStream(PATH_TO_ROOT + "/PlayerNoShows.dat");
			ObjectOutputStream outputStream = new ObjectOutputStream(fileOut);
			outputStream.writeObject(WonderHUD.PlayerNoShows);
			outputStream.close();
		} 
		catch (IOException e) {}
	}
	
	public static void saveWonderHUDImage()
	{
		InputStream inputStream = WonderHUD.plugin.getResource("WonderHUD.gif");
		try
		{
			File imageFile = new File(PATH_TO_IMAGES + "/WonderHUD.gif");
			if(!imageFile.exists())
			{
				FileOutputStream output = new FileOutputStream(imageFile);
				output.write(IOUtils.toByteArray(inputStream));
				
				inputStream.close();
				output.close();
			}

		} 
		catch (IOException e){}
	}
}
package com.antarescraft.kloudy.wonderhud.util;

import java.io.File;

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
}
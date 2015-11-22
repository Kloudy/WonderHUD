package com.antarescraft.kloudy.wonderhud.imageprocessing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import com.antarescraft.kloudy.wonderhud.WonderHUD;

public class GifProcessor 
{
	public static void processGif(String gifName, File imageFile, int width, int height)
	{
		String[][] lines = null;
		
		try
		{
			FileInputStream fileInput = new FileInputStream(imageFile);
			
			GifDecoder decoder = new GifDecoder();
			int code = decoder.read(fileInput);
			
			if(code == 0)
			{	
				BufferedImage[] frames = new BufferedImage[decoder.getFrameCount()];
				for(int i = 0; i < decoder.getFrameCount(); i++)
				{
					frames[i] = decoder.getFrame(i);
				}
				
				lines = ImageProcessor.processImage(frames, width, height);
			}
			else
			{
				lines = ImageProcessor.processingErrorLines();
			}
			
			WonderHUD.ImageLines.put(gifName, lines);
			
			fileInput.close();
		}
		catch(Exception e)
		{
			lines = ImageProcessor.processingErrorLines();
			
			WonderHUD.ImageLines.put(gifName, lines);
			
			System.out.println("ImageLines: " + WonderHUD.ImageLines.toString());
		}
	}
}
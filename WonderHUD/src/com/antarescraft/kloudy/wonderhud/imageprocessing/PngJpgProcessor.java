package com.antarescraft.kloudy.wonderhud.imageprocessing;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.antarescraft.kloudy.wonderhud.WonderHUD;

public class PngJpgProcessor 
{
	public static void processImage(String imageName, File imageFile, int width, int height)
	{
		String[][] lines = null;
		
		try
		{
			BufferedImage image = ImageIO.read(imageFile);
			
			BufferedImage[] frames = {image};
			
			lines = ImageProcessor.processImage(frames, width, height);
			
			WonderHUD.ImageLines.put(imageName, lines);
		}
		catch(Exception e)
		{
			lines = ImageProcessor.processingErrorLines();
			WonderHUD.ImageLines.put(imageName, lines);
			
			System.out.println("Error encountered processing png or jpg");
		}
	}
}
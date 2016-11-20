package com.antarescraft.kloudy.wonderhud;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.antarescraft.kloudy.plugincore.protocol.PacketManager;
import com.antarescraft.kloudy.wonderhud.hudtypes.BaseHUDType;

/**
 * Represents a single HUD Display in the player's view
 */

public class HUD
{	
	private Player player;
	private ArrayList<Integer> entityIds;
	private BaseHUDType hudType;
	private int id;
	private ArrayList<String> evaluatedLines;//List of current evaluated placeholder strings
	private ArrayList<String> prevEvaluatedLines;//List of previous evaluated placeholder strings (used to see if lines have changed)
	
	public HUD(Player player, BaseHUDType hudType)
	{
		this.player = player;
		this.hudType = hudType;
		entityIds = new ArrayList<Integer>();
		prevEvaluatedLines = new ArrayList<String>();
		id = 0;
	}

	public void spawnDisplay()
	{
		for(int i = 0; i < hudType.getLines(player).size(); i++)
		{
			Location lineLocation = calculateNewLocation(i, hudType.getDistance(), hudType.getDeltaTheta(), hudType.getOffsetAngle());
			int entityId = PacketManager.spawnEntity(EntityType.ARMOR_STAND, player, lineLocation, hudType.getLines(player).get(i), true);
			entityIds.add(entityId);
		}
	}
	
	public void destroyDisplay()
	{
		int[] entityIdsArray = new int[entityIds.size()];
		for(int i = 0; i < entityIds.size(); i++)
		{
			entityIdsArray[i] = entityIds.get(i);
		}
		PacketManager.destroyEntities(player, entityIdsArray);
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public ArrayList<Integer> getEntityIds()
	{
		return entityIds;
	}
	
	public BaseHUDType getHudType()
	{
		return hudType;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		boolean equal = false;
		
		if(obj instanceof HUD)
		{
			HUD hud = (HUD)obj;
			
			if(hud.getId() == getId())
			{
				equal = true;
			}
		}
		
		return equal;
	}
	
	public void evaluateLines()//evaluate placeholders in lines for this HUD
	{
		evaluatedLines = (ArrayList<String>) hudType.getLines(player);
	}
	
	public void updatePrevEvaluatedLines()
	{
		prevEvaluatedLines = new ArrayList<String>(evaluatedLines);
	}
	
	public boolean lineChanged(int i)
	{
		boolean changed = false;
		
		if(i >= prevEvaluatedLines.size())//this case shoudn't happen, but ya know, safety first
		{
			changed  = true;
		}
		else if(!prevEvaluatedLines.get(i).equals(evaluatedLines.get(i)))
		{
			changed = true;
		}		
		
		return changed;
	}
	
	public ArrayList<String> getEvaluatedLines()
	{
		return evaluatedLines;
	}
	
	public Location calculateNewLocation(int rowIndex, double distance, double deltaTheta, double offsetAngle)
	{	
		Location l = player.getLocation();
		double xi = l.getX();
		double yi = l.getY();
		double zi = l.getZ();
		
		Vector playerDirection = player.getLocation().getDirection();//player direction unit vector

		//doing rotation relative origin <0,0,0> will add xi,yi,zi in at the end
		double x0 = (playerDirection.getX() * distance);
		double y0 = (playerDirection.getY() * distance);
		double z0 = (playerDirection.getZ() * distance);
		
		//cartesian conversion to spherical coordinates of where player is looking 'distance' blocks away
		double r0 = Math.sqrt((x0*x0)+(y0*y0)+(z0*z0));
		double theta0 = Math.acos((y0 / r0));
		double phi0 = Math.atan2(z0,  x0);
		
		//rotate theta0 by deltaTheta
		double r1 = r0;
		double theta1 = theta0 + (rowIndex * deltaTheta) - offsetAngle;
		double phi1 = phi0;
		
		//convert spherical coordinates back into cartesian coordinates
		double y1 = r1 * Math.cos(theta1);
		double x1 = r1 * Math.cos(phi1) * Math.sin(theta1);
		double z1 = r1 * Math.sin(theta1) * Math.sin(phi1);
		x1 += xi;
		y1 += yi;
		z1 += zi;
		
		return new Location(l.getWorld(), x1, y1,  z1);
	}
}
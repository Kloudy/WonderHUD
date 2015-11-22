package com.antarescraft.kloudy.wonderhud.protocol;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.wonderhud.HUD;
import com.antarescraft.kloudy.wonderhud.hudtypes.BaseHUDType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;

public class FakeDisplay
{
	private static final int ENTITY_FLAGS_INDEX = 0;
	private static final int ARMORSTAND_FLAGS_INDEX = 10;
	private static final int CUSTOM_NAME_INDEX = 2;
	private static final int CUSTOM_NAME_VISIBLE_INDEX = 3;
	
	public static void spawnFakeDisplay(HUD hud)
	{
		Player player = hud.getPlayer();

		for(int i = 0; i < hud.getHudType().getLines(player).size(); i++)
		{
			int entityId = hud.getEntityIds().get(i);
			
			WrapperPlayServerSpawnEntityLiving spawnEntityPacket = new WrapperPlayServerSpawnEntityLiving();
			
			spawnEntityPacket.setEntityID(entityId);
			spawnEntityPacket.setType(EntityType.ARMOR_STAND);
			
			BaseHUDType hudType = hud.getHudType();
			Location location = hud.calculateNewLocation(i, hudType.getDistance(), hudType.getDeltaTheta(), hudType.getOffsetAngle());
			spawnEntityPacket.setX(location.getX());
			spawnEntityPacket.setY(location.getY());
			spawnEntityPacket.setZ(location.getZ());
			
			sendPacket(player, spawnEntityPacket.handle);
			
			WrapperPlayServerEntityMetadata entityMetadataPacket = new WrapperPlayServerEntityMetadata();
			entityMetadataPacket.setEntityID(entityId);
			List<WrappedWatchableObject> metadata = entityMetadataPacket.getMetadata();
			
			String customName = hud.getHudType().getLines(player).get(i);
			
			byte entityFlags = 32;//00100000 - Invisible flag is the 6th bit
			byte armorStandFlags = 1;//00000001 - Small armorStand is first bit
			
			metadata.add(new WrappedWatchableObject(ENTITY_FLAGS_INDEX, entityFlags));
			metadata.add(new WrappedWatchableObject(ARMORSTAND_FLAGS_INDEX, armorStandFlags));
			metadata.add(new WrappedWatchableObject(CUSTOM_NAME_INDEX, customName));
			metadata.add(new WrappedWatchableObject(CUSTOM_NAME_VISIBLE_INDEX, (byte)1));
			
			entityMetadataPacket.setMetadata(metadata);

			sendPacket(player, entityMetadataPacket.handle);
		}
	}
	
	public static void updateDisplayLocation(HUD hud)
	{
		Player player = hud.getPlayer();
		
		for(int i = 0; i < hud.getHudType().getLines(player).size(); i++)
		{
			int entityId = hud.getEntityIds().get(i);
			
			BaseHUDType hudType = hud.getHudType();
			Location location = hud.calculateNewLocation(i, hudType.getDistance(), hudType.getDeltaTheta(), hudType.getOffsetAngle());

			WrapperPlayServerEntityTeleport teleportEntityPacket = new WrapperPlayServerEntityTeleport();
			teleportEntityPacket.setEntityID(entityId);
			teleportEntityPacket.setX(location.getX());
			teleportEntityPacket.setY(location.getY());
			teleportEntityPacket.setZ(location.getZ());
			
			sendPacket(player, teleportEntityPacket.handle);
		}
	}
	
	public static void updateDisplayLine(HUD hud, int rowIndex, String text)
	{
		WrapperPlayServerEntityMetadata entityMetadataPacket = new WrapperPlayServerEntityMetadata();
		entityMetadataPacket.setEntityID(hud.getEntityIds().get(rowIndex));
		
		List<WrappedWatchableObject> metadata = entityMetadataPacket.getMetadata();
		metadata.add(new WrappedWatchableObject(CUSTOM_NAME_INDEX, text));
		
		entityMetadataPacket.setMetadata(metadata);
		
		sendPacket(hud.getPlayer(), entityMetadataPacket.handle);
	}
	
	public static void destroyDisplay(HUD hud)
	{
		WrapperPlayServerEntityDestroy destroyEntityPacket = new WrapperPlayServerEntityDestroy();
		
		int[] ids = new int[hud.getEntityIds().size()];
		
		for(int i = 0; i < hud.getEntityIds().size(); i++)
		{
			ids[i] = hud.getEntityIds().get(i);
		}
		
		destroyEntityPacket.setEntityIds(ids);
		
		sendPacket(hud.getPlayer(), destroyEntityPacket.handle);
	}
	
	private static void sendPacket(Player player, PacketContainer packet)
	{
		ProtocolManager manager = ProtocolLibrary.getProtocolManager();
		try 
		{
			manager.sendServerPacket(player, packet);
		} 
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}
}
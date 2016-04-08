package com.antarescraft.kloudy.wonderhud.protocol;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.wonderhud.HUD;
import com.antarescraft.kloudy.wonderhud.WonderHUD;
import com.antarescraft.kloudy.wonderhud.hudtypes.BaseHUDType;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;

public class FakeDisplay
{
	private static final int ENTITY_FLAGS_INDEX = 0;
	private static final int ARMORSTAND_FLAGS_INDEX = 10;
	private static final int CUSTOM_NAME_INDEX = 2;
	private static final int CUSTOM_NAME_VISIBLE_INDEX = 3;
	
	@SuppressWarnings("deprecation")
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
			
			byte entityFlags = 32;//00100000 - Invisible flag is the 6th bit
			byte armorStandFlags = 1;//00000001 - Small armorStand is first bit
			String customName = hud.getHudType().getLines(player).get(i);
			
			//WrappedDataWatcher metadata = spawnEntityPacket.getMetadata();
			/*metadata.setObject(ENTITY_FLAGS_INDEX, entityFlags);
			metadata.setObject(ARMORSTAND_FLAGS_INDEX, armorStandFlags);
			metadata.setObject(CUSTOM_NAME_INDEX, customName);
			metadata.setObject(CUSTOM_NAME_VISIBLE_INDEX, (byte)1);*/
			
			//spawnEntityPacket.setMetadata(metadata);
			
			//sendPacket(player, spawnEntityPacket.handle);
			
			//WrapperPlayServerEntityMetadata entityMetadataPacket = new WrapperPlayServerEntityMetadata();
			//entityMetadataPacket.setEntityID(entityId);
			//List<WrappedWatchableObject> metadata = entityMetadataPacket.getMetadata();
			WrappedDataWatcher metadata = spawnEntityPacket.getMetadata();
			try
			{
				if(WonderHUD.MinecraftVersion.equals("(MC: 1.8)"))
				{
					metadata.setObject(ENTITY_FLAGS_INDEX, entityFlags);
					metadata.setObject(ARMORSTAND_FLAGS_INDEX, armorStandFlags);
					metadata.setObject(CUSTOM_NAME_INDEX, customName);
					metadata.setObject(CUSTOM_NAME_VISIBLE_INDEX, (byte)1);
					
					spawnEntityPacket.setMetadata(metadata);
					/*WrappedWatchableObject entityflags = WrappedWatchableObject.class.getConstructor(int.class, Object.class).newInstance(ENTITY_FLAGS_INDEX, entityFlags);
					WrappedWatchableObject armorstandflags = WrappedWatchableObject.class.getConstructor(int.class, Object.class).newInstance(ARMORSTAND_FLAGS_INDEX, armorStandFlags);
					WrappedWatchableObject customname = WrappedWatchableObject.class.getConstructor(int.class, Object.class).newInstance(CUSTOM_NAME_INDEX, customName);
					WrappedWatchableObject customnamevisible = WrappedWatchableObject.class.getConstructor(int.class, Object.class).newInstance(CUSTOM_NAME_VISIBLE_INDEX, (byte)1);
					metadata.add(entityflags);
					metadata.add(armorstandflags);
					metadata.add(customname);
					metadata.add(customnamevisible);*/
				}
				else if(WonderHUD.MinecraftVersion.equals("(MC: 1.9)"))
				{
					WrappedWatchableObject entityflags = WrappedWatchableObject.class.getConstructor(WrappedDataWatcherObject.class, Object.class).newInstance(ENTITY_FLAGS_INDEX, entityFlags);
					WrappedWatchableObject armorstandflags = WrappedWatchableObject.class.getConstructor(WrappedDataWatcherObject.class, Object.class).newInstance(ARMORSTAND_FLAGS_INDEX, armorStandFlags);
					WrappedWatchableObject customname = WrappedWatchableObject.class.getConstructor(WrappedDataWatcherObject.class, Object.class).newInstance(CUSTOM_NAME_INDEX, customName);
					WrappedWatchableObject customnamevisible = WrappedWatchableObject.class.getConstructor(WrappedDataWatcherObject.class, Object.class).newInstance(CUSTOM_NAME_VISIBLE_INDEX, (byte)1);
					metadata.setObject(ENTITY_FLAGS_INDEX, entityFlags);
					metadata.setObject(ARMORSTAND_FLAGS_INDEX, armorStandFlags);
					metadata.setObject(CUSTOM_NAME_INDEX, customName);
					metadata.setObject(CUSTOM_NAME_VISIBLE_INDEX, (byte)1);
				}
				
				
			}catch(Exception e){}
			/*metadata.add(new WrappedWatchableObject(ENTITY_FLAGS_INDEX, entityFlags));
			metadata.add(new WrappedWatchableObject(ARMORSTAND_FLAGS_INDEX, armorStandFlags));
			metadata.add(new WrappedWatchableObject(CUSTOM_NAME_INDEX, customName));
			metadata.add(new WrappedWatchableObject(CUSTOM_NAME_VISIBLE_INDEX, (byte)1));*/
			
			//entityMetadataPacket.setMetadata(metadata);

			//sendPacket(player, entityMetadataPacket.handle);
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
			
			teleportEntityPacket.sendPacket(hud.getPlayer());
		}
	}
	
	public static void updateDisplayLine(HUD hud, int rowIndex, String text)
	{
		/*WrapperPlayServerEntityMetadata entityMetadataPacket = new WrapperPlayServerEntityMetadata();
		entityMetadataPacket.setEntityID(hud.getEntityIds().get(rowIndex));
		
		List<WrappedWatchableObject> metadata = entityMetadataPacket.getMetadata();
		
		if(WonderHUD.MinecraftVersion.equals("1.8"))
		{
			
		}
		else if(WonderHUD.MinecraftVersion.equals("1.9"))
		{
			
		}
		
		WrappedDataWatcher.Serializer ser = new WrappedDataWatcher.Serializer(String.class, null, true);
		WrappedDataWatcherObject watcherObj = new WrappedDataWatcherObject(CUSTOM_NAME_INDEX, ser);
		WrappedWatchableObject obj = new WrappedWatchableObject(watcherObj, text);
		metadata.add(obj);
		
		entityMetadataPacket.setMetadata(metadata);
		
		entityMetadataPacket.sendPacket(hud.getPlayer());*/
		//sendPacket(hud.getPlayer(), entityMetadataPacket.handle);
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
		
		destroyEntityPacket.sendPacket(hud.getPlayer());
		//sendPacket(hud.getPlayer(), destroyEntityPacket.handle);
	}
	
	/*private static void sendPacket(Player player, PacketContainer packet)
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
	}*/
}
package com.antarescraft.kloudy.wonderhud.protocol;

import org.bukkit.World;
import org.bukkit.entity.Entity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class WrapperPlayServerRelEntityMove extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.REL_ENTITY_MOVE;

    public WrapperPlayServerRelEntityMove() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerRelEntityMove(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieve Entity ID.
     * <p>
     * Notes: entity's ID
     * @return The current Entity ID
     */
    public int getEntityID() {
        return handle.getIntegers().read(0);
    }

    /**
     * Set Entity ID.
     * @param value - new value.
     */
    public void setEntityID(int value) {
        handle.getIntegers().write(0, value);
    }

    /**
     * Retrieve the entity of the painting that will be spawned.
     * @param world - the current world of the entity.
     * @return The spawned entity.
     */
    public Entity getEntity(World world) {
        return handle.getEntityModifier(world).read(0);
    }

    /**
     * Retrieve the entity of the painting that will be spawned.
     * @param event - the packet event.
     * @return The spawned entity.
     */
    public Entity getEntity(PacketEvent event) {
        return getEntity(event.getPlayer().getWorld());
    }

    /**
     * Retrieve DX.
     * @return The current DX
     */
    public double getDx() {
        return handle.getBytes().read(0) / 32D;
    }

    /**
     * Set DX.
     * @param value - new value.
     */
    public void setDx(double value) {
        handle.getBytes().write(0, (byte) (value * 32));
    }

    /**
     * Retrieve DY.
     * @return The current DY
     */
    public double getDy() {
        return handle.getBytes().read(1) / 32D;
    }

    /**
     * Set DY.
     * @param value - new value.
     */
    public void setDy(double value) {
        handle.getBytes().write(1, (byte) (value * 32));
    }

    /**
     * Retrieve DZ.
     * @return The current DZ
     */
    public double getDz() {
        return handle.getBytes().read(2) / 32D;
    }

    /**
     * Set DZ.
     * @param value - new value.
     */
    public void setDz(double value) {
        handle.getBytes().write(2, (byte) (value * 32));
    }

    /**
     * Retrieve On Ground.
     * @return The current On Ground
     */
    public boolean getOnGround() {
        return handle.getSpecificModifier(boolean.class).read(0);
    }

    /**
     * Set On Ground.
     * @param value - new value.
     */
    public void setOnGround(boolean value) {
        handle.getSpecificModifier(boolean.class).write(0, value);
    }
}
package com.gitlab.zachdeibert.jnet;

import java.io.Serializable;

/**
 * A packet that can be sent across a network.
 * This should be extended to add extra data to the packet.
 * 
 * @author Zach Deibert
 * @see PacketHandler
 * @serial
 * @since 1.0
 * @version 1.0
 */
public class Packet implements Serializable
{
    /**
     * The UID to use for serialization.
     * This should be reset for each subclass.
     * 
     * @author Zach Deibert
     * @see Serializable
     * @since 1.0
     */
    private static final long serialVersionUID = 8570521268445574243L;
    /**
     * The ID of this packet.
     * Used to find the correct handler when the packet is recieved.
     * 
     * @author Zach Deibert
     * @serialField
     * @since 1.0
     */
    int                       id;

    /**
     * Constructs a packet with id 0
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    Packet()
    {
    }

    /**
     * Constructs a packet
     * 
     * @author Zach Deibert
     * @param id
     *            The ID of this packet. It must be the same as the ID for the
     *            PacketHandler.
     * @since 1.0
     */
    public Packet(final int id)
    {
        this.id = id;
    }
}

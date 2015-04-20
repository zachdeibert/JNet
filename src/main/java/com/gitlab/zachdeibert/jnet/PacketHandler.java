package com.gitlab.zachdeibert.jnet;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles a packet.
 * This must be extended to add code to process the packet.
 * 
 * @author Zach Deibert
 * @see Packet
 * @since 1.0
 * @version 1.0
 */
public abstract class PacketHandler
{
    /**
     * Every packet handler that is registered and its id
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    private static final Map<Integer, PacketHandler> handlers = new HashMap<Integer, PacketHandler>();

    /**
     * Abstract function to handle a packet
     * 
     * @author Zach Deibert
     * @param p
     *            The packet to handle
     * @param sender
     *            The node that received the packet
     * @since 1.0
     */
    protected abstract void handle(Packet p, NetworkNode sender);

    /**
     * Processes a packet
     * 
     * @author Zach Deibert
     * @param p
     *            The packet to process
     * @param sender
     *            The node that received the packet
     * @since 1.0
     */
    static void process(final Packet p, final NetworkNode sender)
    {
        if (handlers.containsKey(p.id))
        {
            final PacketHandler handler = handlers.get(p.id);
            handler.handle(p, sender);
        }
    }

    /**
     * Constructs a new packet handler.
     * Automatically registers the new handler.
     * 
     * @author Zach Deibert
     * @param id
     *            The ID of the packet handler. It must be the same as the ID
     *            for the
     *            Packet.
     * @see Packet.id
     * @since 1.0
     */
    protected PacketHandler(final int id)
    {
        handlers.put(id, this);
    }
}

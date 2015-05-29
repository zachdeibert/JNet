package com.gitlab.zachdeibert.jnet;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A handler to handle the TrackerPackets
 * 
 * @author Zach Deibert
 * @see TrackerPacket
 * @since 1.0
 * @version 1.2
 */
public class TrackerPacketHandler extends PacketHandler
{
    /**
     * A list of all the packets that have been recieved
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    protected static final List<Object> recievedPackets = Collections.synchronizedList(new LinkedList<Object>());

    /**
     * Gets the first packet that was recieved
     * 
     * @author Zach Deibert
     * @return The data from the first packet
     * @since 1.0
     */
    public static Object getPacketData()
    {
        Object obj = null;
        synchronized (recievedPackets)
        {
            if (recievedPackets.size() > 0)
            {
                obj = recievedPackets.get(0);
                recievedPackets.remove(0);
            }
        }
        return obj;
    }

    /**
     * Handles the packet when it is received
     * 
     * @author Zach Deibert
     * @param p
     *            The packet that was received
     * @param sender
     *            The node that sent the packet
     * @since 1.0
     */
    @Override
    protected void handle(final Packet p, final NetworkNode sender)
    {
        if (p instanceof TrackerPacket)
        {
            synchronized (recievedPackets)
            {
                recievedPackets.add(((TrackerPacket) p).data);
            }
        }
    }

    /**
     * Default constructor
     * 
     * @author Zach Deibert
     * @param id
     *            The packet ID
     * @since 1.0
     */
    public TrackerPacketHandler(final int id)
    {
        super(id);
    }
}

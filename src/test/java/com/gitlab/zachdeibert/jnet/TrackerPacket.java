package com.gitlab.zachdeibert.jnet;

/**
 * A packet that can easily be tracked
 * 
 * @author Zach Deibert
 * @see TrackerPacketHandler
 * @since 1.0
 * @version 1.0
 */
public class TrackerPacket extends Packet
{
    /**
     * The serialization UID
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    private static final long serialVersionUID = 190874732393526943L;
    /**
     * The data sent with the packet
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    Object                    data;

    /**
     * Default constructor
     * 
     * @author Zach Deibert
     * @param data
     *            The data to put in the packet
     * @param id
     *            The packet ID
     * @since 1.0
     */
    public TrackerPacket(Object data, int id)
    {
        super(id);
        this.data = data;
    }
}

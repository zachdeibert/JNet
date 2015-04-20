package com.gitlab.zachdeibert.jnet;

import java.io.IOException;

/**
 * An interface that represents a node on the network
 * 
 * @author Zach Deibert
 * @since 1.0
 * @version 1.0
 */
public interface NetworkNode
{
    /**
     * Sends a packet
     * 
     * @author Zach Deibert
     * @param packet
     *            The packet to send
     * @since 1.0
     * @throws IOException
     *             An I/O error has occurred
     */
    public void sendPacket(Packet packet) throws IOException;

    /**
     * Disconencts this node
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws IOException
     *             An I/O error has occurred
     */
    public void disconnect() throws IOException;
}

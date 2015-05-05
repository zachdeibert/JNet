package com.gitlab.zachdeibert.jnet;

import java.io.IOException;

/**
 * A class that will be asynchronously be run by AsyncRunner
 * 
 * @author Zach Deibert
 * @see AsyncRunner
 * @since 1.0
 * @version 1.1
 */
abstract class AsyncReadable implements NetworkNode
{
    /**
     * If this instance should be removed from the asynchronous run queue
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    boolean closed;

    /**
     * Checks if there is a packet available to be read
     * 
     * @author Zach Deibert
     * @return If there is a packet available to be read
     * @since 1.0
     */
    protected abstract boolean packetAvailable();

    /**
     * Reads a packet
     * 
     * @author Zach Deibert
     * @return The packet that was read
     * @since 1.0
     * @throws IOException
     *             An I/O error has occurred
     */
    protected abstract Packet readPacket() throws IOException;

    /**
     * Runs an iteration of the asynchronous loop
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws IOException
     *             An I/O error has occurred
     */
    void iteration() throws IOException
    {
        if (packetAvailable())
        {
            final Packet p = readPacket();
            if (p != null)
            {
                PacketHandler.process(p, this);
            }
        }
    }

    /**
     * Constructs a new AsyncReadable.
     * This new instance is automatically added to the asynchronous queue.
     * The asynchronous execution thread is started it it is not already
     * running.
     * 
     * @author Zach Deibert
     * @see AsyncRunner.add
     * @see AsyncRunner.init
     * @since 1.0
     */
    AsyncReadable()
    {
        closed = false;
        AsyncRunner.add(this);
        AsyncRunner.init();
    }
}

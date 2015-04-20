package com.gitlab.zachdeibert.jnet;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * This class will asynchronously read packets from a socket
 * 
 * @author Zach Deibert
 * @since 1.0
 * @version 1.0
 */
abstract class AsyncDeserializer extends AsyncReadable
{
    /**
     * The socket associated with this network connection
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    protected Socket            socket;
    /**
     * The raw input stream for the socket
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    protected InputStream       ristream;
    /**
     * The object input stream for the socket
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    protected ObjectInputStream istream;

    /**
     * Checks to see if a packet can be read from the socket
     * 
     * @author Zach Deibert
     * @return If a packet is available to be read
     * @since 1.0
     */
    @Override
    protected boolean packetAvailable()
    {
        try
        {
            if (socket == null)
            {
                ristream = null;
                istream = null;
                return false;
            }
            if (ristream == null)
            {
                ristream = socket.getInputStream();
            }
            if (istream == null)
            {
                istream = new ObjectInputStream(ristream);
            }
            return ristream.available() > 0;
        }
        catch (final IOException ex)
        {
            return false;
        }
    }

    /**
     * Reads a single packet from the socket
     * 
     * @author Zach Deibert
     * @return The packet that has been deserialized from the socket
     * @since 1.0
     * @throws IOException
     *             An I/O error has occurred
     */
    @Override
    protected Packet readPacket() throws IOException
    {
        try
        {
            final Object obj = istream.readObject();
            if (obj instanceof Packet)
            {
                return (Packet) obj;
            }
        }
        catch (final Exception ex)
        {
            throw new IOException(ex);
        }
        throw new IOException("Corrupted network stream");
    }
}

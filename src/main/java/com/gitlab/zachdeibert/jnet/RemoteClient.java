package com.gitlab.zachdeibert.jnet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * A class to represent a client connected to a server
 * 
 * @author Zach Deibert
 * @see NetworkServer
 * @since 1.0
 * @version 1.0
 */
final public class RemoteClient extends AsyncDeserializer implements
                NetworkNode
{
    /**
     * The server that this client is connected to
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    private final NetworkServer      server;
    /**
     * The stream used to send packets to the server
     * 
     * @author Zach Deibert
     * @see sendPacket()
     * @since 1.0
     */
    private final ObjectOutputStream ostream;

    /**
     * Sends a packet to the server
     * 
     * @author Zach Deibert
     * @param packet
     *            The packet to send
     * @see ostream
     * @since 1.0
     * @throws IOException
     *             An I/O error has occurred
     */
    @Override
    public void sendPacket(final Packet packet) throws IOException
    {
        ostream.writeObject(packet);
    }

    /**
     * Disconnects this client from the server
     * 
     * @author Zach Deibert
     * @see NetworkServer.disconnect()
     * @since 1.0
     * @throws IOException
     *             An I/O error has occurred
     */
    @Override
    public void disconnect() throws IOException
    {
        server.disconnect(this);
        closed = true;
    }

    /**
     * Constructs a new remote client
     * 
     * @author Zach Deibert
     * @param server
     *            The server that the client is connected to
     * @param client
     *            The client's socket
     * @since 1.0
     * @throws IOException
     *             An I/O error has occurred
     */
    RemoteClient(final NetworkServer server, final Socket client)
                    throws IOException
    {
        socket = client;
        this.server = server;
        this.ostream = new ObjectOutputStream(client.getOutputStream());
    }
}

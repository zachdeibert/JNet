package com.gitlab.zachdeibert.jnet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * A networking client
 * 
 * @author Zach Deibert
 * @since 1.0
 * @version 1.2
 */
public final class NetworkClient extends AsyncDeserializer implements
                LocalNetworkNode
{
    /**
     * The event to call when the client connects to a server
     * 
     * @author Zach Deibert
     * @see ConnectEvent#add
     * @since 1.0
     */
    public ConnectEvent        onConnect;
    /**
     * The stream to write packets to
     * 
     * @author Zach Deibert
     * @see sendPacket
     * @since 1.0
     */
    private ObjectOutputStream ostream;

    /**
     * Writes a packet to the socket
     * 
     * @author Zach Deibert
     * @param packet
     *            The packet to write
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
     * Connects the client to a remote server
     * 
     * @author Zach Deibert
     * @param IP
     *            The IP or host name to connect to
     * @param port
     *            The port to connect to
     * @since 1.0
     * @throws UnknownHostException
     *             The DNS lookup failed
     * @throws IOException
     *             An I/O error has occurred
     */
    @Override
    public void connect(final String IP, final short port)
                    throws UnknownHostException, IOException
    {
        connect(IP, port, 0);
    }

    /**
     * Connects the client to a remote server
     * 
     * @author Zach Deibert
     * @param IP
     *            The IP or host name to connect to
     * @param port
     *            The port to connect to
     * @param timeout
     *            The amount of milliseconds to timeout the connection after
     * @since 1.0
     * @throws UnknownHostException
     *             The DNS lookup failed
     * @throws IOException
     *             An I/O error has occurred
     * @throws SocketTimeoutException
     *             The client could not connect within the given timeout
     */
    public void connect(final String IP, final short port, final int timeout)
                    throws UnknownHostException, IOException
    {
        socket = new Socket();
        socket.connect(new InetSocketAddress(IP, port), timeout);
        ostream = new ObjectOutputStream(socket.getOutputStream());
        closed = false;
        if (onConnect != null)
        {
            onConnect.onConnect(this, this);
        }
    }

    /**
     * Disconnects the client from the server
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws IOException
     *             An I/O error has occurred
     */
    @Override
    public void disconnect() throws IOException
    {
        if (istream != null)
        {
            istream.close();
        }
        if (ostream != null)
        {
            ostream.close();
        }
        if (socket != null)
        {
            socket.close();
        }
        closed = true;
    }

    /**
     * Disconnects the client from the server
     * 
     * @author Zach Deibert
     * @see disconnect
     * @since 1.1
     * @throws Throwable
     *             An error has occurred
     */
    @Override
    protected void finalize() throws Throwable
    {
        disconnect();
        super.finalize();
    }

    /**
     * Gets the IP of the server
     * 
     * @author Zach Deibert
     * @return The IP of the server
     * @since 1.1
     * @throws IOException
     *             An I/O error has occurred
     */
    @Override
    public String getIP() throws IOException
    {
        final SocketAddress server = socket.getRemoteSocketAddress();
        if (server == null)
        {
            throw new IOException("The client is not connected to a server");
        }
        if (server instanceof InetSocketAddress)
        {
            return ((InetSocketAddress) server).getAddress().getHostAddress();
        }
        else
        {
            throw new IOException("Cannot get remote IP");
        }
    }

    /**
     * Gets the port of the server
     * 
     * @author Zach Deibert
     * @return The port of the server
     * @since 1.1
     * @throws IOException
     *             An I/O error has occurred
     */
    @Override
    public int getPort() throws IOException
    {
        final SocketAddress server = socket.getRemoteSocketAddress();
        if (server == null)
        {
            throw new IOException("The client is not connected to a server");
        }
        if (server instanceof InetSocketAddress)
        {
            return ((InetSocketAddress) server).getPort();
        }
        else
        {
            throw new IOException("Cannot get remote port");
        }
    }
}

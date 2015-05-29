package com.gitlab.zachdeibert.jnet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A networking server
 * 
 * @author Zach Deibert
 * @since 1.0
 * @version 1.2.1
 */
public final class NetworkServer implements LocalNetworkNode
{
    /**
     * The event to call when the client connects to a server
     * 
     * @author Zach Deibert
     * @see ConnectEvent#add
     * @since 1.0
     */
    public ConnectEvent              onConnect;
    /**
     * The socket to bind and listen on
     * 
     * @author Zach Deiber
     * @since 1.0
     */
    private ServerSocket             socket;
    /**
     * The thread running the listener
     * 
     * @author Zach Deibert
     * @see acceptClient()
     * @since 1.0
     */
    private ServerListener           listener;
    /**
     * A list of all of the clients that are connected to this server
     * 
     * @author Zach Deibert
     * @see acceptClient()
     * @since 1.0
     */
    private final List<RemoteClient> clients;

    /**
     * Accepts a client that is trying to connect. This method will block until
     * a client starts connecting.
     * 
     * @author Zach Deibert
     * @see listener
     * @see clients
     * @since 1.0
     * @throws IOException
     *             An I/O error has occurred
     */
    void acceptClient() throws IOException
    {
        final Socket socket = this.socket.accept();
        final RemoteClient client = new RemoteClient(this, socket);
        synchronized (clients)
        {
            clients.add(client);
        }
        if (onConnect != null)
        {
            onConnect.onConnect(this, client);
        }
    }

    /**
     * Sends a packet to all connected clients
     * 
     * @author Zach Deibert
     * @param packet
     *            The packet to send
     * @see clients
     * @since 1.0
     * @throws IOException
     *             An I/O error has occurred
     */
    @Override
    public void sendPacket(final Packet packet) throws IOException
    {
        synchronized (clients)
        {
            final List<RemoteClient> disconnected = new LinkedList<RemoteClient>();
            for (final RemoteClient client : clients)
            {
                try
                {
                    client.sendPacket(packet);
                }
                catch (final SocketException ex)
                {
                    if (ex.getMessage().equals("Broken pipe"))
                    {
                        disconnected.add(client);
                    }
                    else
                    {
                        throw ex;
                    }
                }
            }
            clients.removeAll(disconnected);
        }
    }

    /**
     * Disconnects a single client from the server
     * 
     * @author Zach Deibert
     * @param client
     *            The client to disconnect
     * @since 1.0
     * @throws IOException
     *             An I/O error has occurred
     */
    void disconnect(final RemoteClient client) throws IOException
    {
        synchronized (clients)
        {
            clients.remove(client);
        }
        client.closed = true;
        client.socket.close();
    }

    /**
     * Disconnects all clients from the server and shuts down the server socket
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws IOException
     *             An I/O error has occurred
     */
    @Override
    public void disconnect() throws IOException
    {
        synchronized (clients)
        {
            for (final RemoteClient client : clients)
            {
                client.closed = true;
                client.socket.close();
            }
            clients.clear();
        }
        socket.close();
        listener.interrupt();
    }

    /**
     * Disconnects all clients and shuts down the server socket
     * 
     * @author Zach Deibert
     * @see disconnect
     * @since 1.1
     * @throws Throwable
     *             An error occurred
     */
    @Override
    protected void finalize() throws Throwable
    {
        disconnect();
        super.finalize();
    }

    /**
     * Binds the socket and starts the listener thread
     * 
     * @author Zach Deibert
     * @param IP
     *            The IP to listen on (currently does nothing)
     * @param port
     *            The port to listen on
     * @since 1.0
     * @throws IOException
     *             An I/O error has occurred
     */
    @Override
    public void connect(final String IP, final short port) throws IOException
    {
        socket = new ServerSocket(port);
        listener = new ServerListener(this);
    }

    /**
     * Binds the socket and starts the listener thread
     * 
     * @author Zach Deibert
     * @param port
     *            The port to listen on
     * @since 1.0
     * @throws IOException
     *             An I/O error has occurred
     */
    public void connect(final short port) throws IOException
    {
        connect(null, port);
    }

    /**
     * Gets the IP the server is listening on
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
        return "0.0.0.0";
    }

    /**
     * Gets the port the server is listening on
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
        return socket.getLocalPort();
    }

    /**
     * Creates a new network server
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    public NetworkServer()
    {
        clients = Collections.synchronizedList(new LinkedList<RemoteClient>());
    }
}

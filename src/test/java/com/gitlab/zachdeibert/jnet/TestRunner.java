package com.gitlab.zachdeibert.jnet;

import org.junit.Assert;

/**
 * Runs the majority of the tests
 * 
 * @author Zach Deibert
 * @since 1.0
 * @version 1.1
 */
public class TestRunner
{
    /**
     * The port to test on
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    protected final short         port;
    /**
     * The packets to test with
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    protected final Packet        packets[];
    /**
     * The server to test with
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    protected final NetworkServer server;
    /**
     * The client to test with
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    protected final NetworkClient client;

    /**
     * Gets the IP to have the client conenct to
     * 
     * @author Zach Deibert
     * @return This computer's IP
     * @since 1.0
     * @throws Throwable
     */
    protected String getLocalIp() throws Throwable
    {
        return "127.0.0.1";
    }

    /**
     * Verifies that the packet to send is trackable
     * 
     * @author Zach Deibert
     * @param p
     *            The packet to verify
     * @return If it is trackable
     * @since 1.0
     * @throws Throwable
     */
    protected boolean verifyPacket(final Packet p) throws Throwable
    {
        return p instanceof TrackerPacket;
    }

    /**
     * Binds the server socket
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws Throwable
     */
    protected void connectServer() throws Throwable
    {
        server.connect(port);
    }

    /**
     * Connects the client to the server
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws Throwable
     */
    protected void connectClient() throws Throwable
    {
        client.connect(getLocalIp(), port);
    }

    /**
     * Connects the client and server together
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws Throwable
     */
    protected void setup() throws Throwable
    {
        connectServer();
        connectClient();
        Thread.sleep(50);
    }

    /**
     * Unbinds the server port
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws Throwable
     */
    protected void freeServer() throws Throwable
    {
        server.disconnect();
    }

    /**
     * Disconnects the client from the server
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws Throwable
     */
    protected void freeClient() throws Throwable
    {
        client.disconnect();
    }

    /**
     * Disconencts the server and client
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws Throwable
     */
    protected void free() throws Throwable
    {
        freeClient();
        freeServer();
    }

    /**
     * Gets the data that would be expected from a packet being received
     * 
     * @author Zach Deibert
     * @param p
     *            The packet that should be received
     * @return The data that should be in the packet
     * @since 1.0
     * @throws Throwable
     */
    protected Object getExpected(final Packet p) throws Throwable
    {
        return ((TrackerPacket) p).data;
    }

    /**
     * Tests the transmission of a single packet from a single node
     * 
     * @author Zach Deibert
     * @param p
     *            The packet to transmit
     * @param node
     *            The node to transmit from
     * @since 1.0
     * @throws Throwable
     */
    protected void testPacket(final Packet p, final NetworkNode node)
                    throws Throwable
    {
        node.sendPacket(p);
        Thread.sleep(100);
        Assert.assertEquals("Packet transmission failure.", getExpected(p),
                        TrackerPacketHandler.getPacketData());
    }

    /**
     * Tests the transmission of a single packet from both nodes
     * 
     * @author Zach Deibert
     * @param p
     *            The packet to transmit
     * @since 1.0
     * @throws Throwable
     */
    protected void testPacket(final Packet p) throws Throwable
    {
        testPacket(p, server);
        testPacket(p, client);
    }

    /**
     * Runs the test
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws Throwable
     */
    public void test() throws Throwable
    {
        setup();
        for (final Packet packet : packets)
        {
            if (verifyPacket(packet))
            {
                testPacket(packet);
            }
        }
        free();
    }

    /**
     * Default constructor
     * 
     * @author Zach Deibert
     * @param port
     *            The port to use to connect
     * @param packets
     *            The packets to test
     * @since 1.0
     */
    public TestRunner(short port, Packet packets[])
    {
        this.port = port;
        this.packets = packets;
        server = new NetworkServer();
        client = new NetworkClient();
    }
}

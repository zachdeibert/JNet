package com.gitlab.zachdeibert.jnet;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests to make sure the events work
 * 
 * @author Zach Deibert
 * @since 1.0
 * @version 1.0
 */
public class EventTest
{
    /**
     * A simple event handler to use
     * 
     * @author Zach Deibert
     * @since 1.0
     * @version 1.0
     */
    private class EventHandler implements ConnectEvent
    {
        /**
         * A pointer to a boolean for if the event was called
         * 
         * @author Zach Deibert
         * @since 1.0
         */
        private final Pointer<Boolean> connected;

        /**
         * Sets the connected flag to true
         * 
         * @author Zach Deibert
         * @param connected
         *            The node that was connected
         * @param connector
         *            The node that connected to it
         * @since 1.0
         */
        @Override
        public void onConnect(final LocalNetworkNode connected,
                        final NetworkNode connector)
        {
            this.connected.data = true;
        }

        /**
         * Default constructor
         * 
         * @author Zach Deibert
         * @param connected
         *            The pointer to use
         */
        EventHandler(final Pointer<Boolean> connected)
        {
            this.connected = connected;
        }
    }

    /**
     * Tests to make sure the onConnected event works on the client
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws Throwable
     */
    @Test
    public void testClientOnConnected() throws Throwable
    {
        final Pointer<Boolean> connected = new Pointer<Boolean>();
        connected.data = false;
        final TestRunner runner = new TestRunner((short) 4231,
                        new Packet[] { new Packet(31) })
        {
            /**
             * Accepts all packets to be tested
             * 
             * @author Zach Deibert
             * @param p
             *            The packet to be tested
             * @returns true
             * @since 1.0
             * @throws Throwable
             */
            @Override
            protected boolean verifyPacket(final Packet p) throws Throwable
            {
                return true;
            }

            /**
             * Adds the onConnect event handler to the client
             * 
             * @author Zach Deibert
             * @since 1.0
             * @throws Throwable
             */
            @Override
            protected void connectClient() throws Throwable
            {
                client.onConnect = new EventHandler(connected);
                client.connect(getLocalIp(), port);
            }

            /**
             * Makes sure to allow time for the network to finish delivering
             * 
             * @author Zach Deibert
             * @param p
             *            The packet
             * @since 1.0
             * @throws Throwable
             */
            @Override
            protected void testPacket(final Packet p) throws Throwable
            {
                Thread.sleep(100);
            }
        };
        runner.test();
        Assert.assertTrue("Client onConnect event failed", connected.data);
    }

    /**
     * Tests to make sure the onConnected event works on the server
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws Throwable
     */
    @Test
    public void testServerOnConnected() throws Throwable
    {
        final Pointer<Boolean> connected = new Pointer<Boolean>();
        connected.data = false;
        final TestRunner runner = new TestRunner((short) 4232,
                        new Packet[] { new Packet(32) })
        {
            /**
             * Accepts all packets to be tested
             * 
             * @author Zach Deibert
             * @param p
             *            The packet to be tested
             * @returns true
             * @since 1.0
             * @throws Throwable
             */
            @Override
            protected boolean verifyPacket(final Packet p) throws Throwable
            {
                return true;
            }

            /**
             * Adds the onConnect event handler to the server
             * 
             * @author Zach Deibert
             * @since 1.0
             * @throws Throwable
             */
            @Override
            protected void connectServer() throws Throwable
            {
                server.onConnect = new EventHandler(connected);
                server.connect(port);
            }

            /**
             * Makes sure to allow time for the network to finish delivering
             * 
             * @author Zach Deibert
             * @param p
             *            The packet
             * @since 1.0
             * @throws Throwable
             */
            @Override
            protected void testPacket(final Packet p) throws Throwable
            {
                Thread.sleep(100);
            }
        };
        runner.test();
        Assert.assertTrue("Server onConnect event failed", connected.data);
    }

    /**
     * Tests to make sure ConnectEvents can be added together
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws Throwable
     */
    @Test
    public void testOnConnectedAdd() throws Throwable
    {
        final Pointer<Boolean> p1 = new Pointer<Boolean>();
        p1.data = false;
        final Pointer<Boolean> p2 = new Pointer<Boolean>();
        p2.data = false;
        final ConnectEvent e1 = new EventHandler(p1);
        final ConnectEvent e2 = new EventHandler(p2);
        final ConnectEvent e3 = ConnectEvent.add(e1, e2);
        e3.onConnect(null, null);
        Assert.assertTrue("First operand to add did not execute", p1.data);
        Assert.assertTrue("Second operand to add did not execute", p2.data);
    }
}

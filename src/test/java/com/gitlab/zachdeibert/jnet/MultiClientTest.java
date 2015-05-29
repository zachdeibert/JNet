package com.gitlab.zachdeibert.jnet;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests multiple clients connecting to the server
 * 
 * @author Zach Deibert
 * @since 1.0
 * @version 1.2
 */
public class MultiClientTest
{
    /**
     * A simple packet handler to use for testing
     * 
     * @author Zach Deibert
     * @since 1.0
     * @version 1.2
     */
    private class TestPacketHandler extends PacketHandler
    {
        /**
         * A pointer to the number of packets recieved
         * 
         * @author Zach Deibert
         * @since 1.0
         */
        private final Pointer<Integer>     counter;
        /**
         * A pointer to the last node that recieved a packet
         * 
         * @author Zach Deibert
         * @since 1.0
         */
        private final Pointer<NetworkNode> lastSender;

        /**
         * Handles the packet
         * 
         * @author Zach Deiebrt
         * @param p
         *            The packet
         * @param sender
         *            The node that sent the packet
         * @since 1.0
         */
        @Override
        protected void handle(final Packet p, final NetworkNode sender)
        {
            if (lastSender != null)
            {
                if (sender == null || lastSender.data == sender)
                {
                    return;
                }
                lastSender.data = sender;
            }
            counter.data++;
        }

        /**
         * Creates a packet handler
         * 
         * @author Zach Deibert
         * @param id
         *            The Packet ID
         * @param counter
         *            A pointer to the counter
         * @since 1.0
         */
        public TestPacketHandler(final int id, final Pointer<Integer> counter)
        {
            this(id, counter, null);
        }

        /**
         * Creates a packet handler
         * 
         * @author Zach Deibert
         * @param id
         *            The Packet ID
         * @param counter
         *            A pointer to the counter
         * @param lastSender
         *            A pointer to the last sender node
         * @since 1.0
         */
        public TestPacketHandler(final int id, final Pointer<Integer> counter,
                        final Pointer<NetworkNode> lastSender)
        {
            super(id);
            this.counter = counter;
            this.lastSender = lastSender;
        }
    }

    /**
     * Tests to make sure the server can broadcast to all clients
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws Throwable
     */
    @Test
    public void testPacketBroadcast() throws Throwable
    {
        final Pointer<Integer> counter = new Pointer<Integer>();
        counter.data = new Integer(0);
        new TestPacketHandler(21, counter);
        final TestRunner runner = new TestRunner((short) 4221,
                        new Packet[] { new Packet(21) })
        {
            /**
             * The second network client
             * 
             * @author Zach Deibert
             * @since 1.0
             */
            private final NetworkClient client2 = new NetworkClient(); ;

            /**
             * Accepts all packets to be tested
             * 
             * @author Zach Deibert
             * @param p
             *            The packet to test
             * @since 1.0
             * @throws Throwable
             */
            @Override
            protected boolean verifyPacket(final Packet p) throws Throwable
            {
                return true;
            }

            /**
             * Connects both clients
             * 
             * @author Zach Deibert
             * @since 1.0
             * @throws Throwable
             */
            @Override
            protected void connectClient() throws Throwable
            {
                client.connect(getLocalIp(), port);
                client2.connect(getLocalIp(), port);
            }

            /**
             * Disconnects both clients
             * 
             * @author Zach Deibert
             * @since 1.0
             * @throws Throwable
             */
            @Override
            protected void freeClient() throws Throwable
            {
                client.disconnect();
                client2.disconnect();
            }

            /**
             * Broadcasts the packet from the server
             * 
             * @author Zach Deibert
             * @since 1.0
             * @throws Throwable
             */
            @Override
            protected void testPacket(final Packet p) throws Throwable
            {
                server.sendPacket(p);
                Thread.sleep(1000);
            }
        };
        runner.test();
        Assert.assertEquals("Not all packets arrived", 2L,
                        (long) (int) counter.data);
    }

    /**
     * Tests to make sure the server detects which client sent the packet
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws Throwable
     */
    @Test
    public void testSenderDetection() throws Throwable
    {
        final Pointer<NetworkNode> lastSender = new Pointer<NetworkNode>();
        final Pointer<Integer> counter = new Pointer<Integer>();
        counter.data = new Integer(0);
        new TestPacketHandler(22, counter, lastSender);
        final TestRunner runner = new TestRunner((short) 4222,
                        new Packet[] { new Packet(22) })
        {
            /**
             * The second network client
             * 
             * @author Zach Deibert
             * @since 1.0
             */
            private final NetworkClient client2 = new NetworkClient(); ;

            /**
             * Accepts all packets to be tested
             * 
             * @author Zach Deibert
             * @param p
             *            The packet to test
             * @since 1.0
             * @throws Throwable
             */
            @Override
            protected boolean verifyPacket(final Packet p) throws Throwable
            {
                return true;
            }

            /**
             * Connects both clients
             * 
             * @author Zach Deibert
             * @since 1.0
             * @throws Throwable
             */
            @Override
            protected void connectClient() throws Throwable
            {
                client.connect(getLocalIp(), port);
                client2.connect(getLocalIp(), port);
            }

            /**
             * Disconnects both clients
             * 
             * @author Zach Deibert
             * @since 1.0
             * @throws Throwable
             */
            @Override
            protected void freeClient() throws Throwable
            {
                client.disconnect();
                client2.disconnect();
            }

            /**
             * Sends the packet from both clients
             * 
             * @author Zach Deibert
             * @since 1.0
             * @throws Throwable
             */
            @Override
            protected void testPacket(final Packet p) throws Throwable
            {
                client.sendPacket(p);
                client2.sendPacket(p);
                Thread.sleep(1000);
            }
        };
        runner.test();
        Assert.assertEquals("Not all packets arrived", 2L,
                        (long) (int) counter.data);
    }
}

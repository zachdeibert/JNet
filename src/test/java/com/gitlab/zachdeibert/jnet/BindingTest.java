package com.gitlab.zachdeibert.jnet;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import org.junit.Test;

/**
 * Tests the binding of the server port
 * 
 * @author Zach Deibert
 * @since 1.0
 * @version 1.1
 */
public class BindingTest
{
    /**
     * Tests to make sure the ports are unbound after the server is disconnected
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws Throwable
     */
    @Test
    public void testPortFreeing() throws Throwable
    {
        new TrackerPacketHandler(11);
        TestRunner runner = new TestRunner((short) 4211,
                        new Packet[] { new TrackerPacket(42, 11) });
        runner.test();
        Thread.sleep(1000);
        runner = new TestRunner((short) 4211, new Packet[] { new TrackerPacket(
                        24, 11) });
        runner.test();
    }

    /**
     * Tests to make sure the server only accepts clients connecting to it
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws Throwable
     */
    @Test(expected = SocketTimeoutException.class)
    public void testIPFilteringPart1() throws Throwable
    {
        new TrackerPacketHandler(12);
        final TestRunner runner = new TestRunner((short) 4212,
                        new Packet[] { new TrackerPacket(42, 12) })
        {
            /**
             * Changes to IP to 192.0.2.1.
             * That IP is reserved for testing only, so it should not point to
             * this computer or any other computer.
             * 
             * @author Zach Deibert
             * @returns 192.0.2.1
             * @since 1.0
             * @throws Throwable
             */
            @Override
            protected String getLocalIp() throws Throwable
            {
                return "192.0.2.1";
            }

            /**
             * Connects the client with a timeout so the test does not take
             * forever
             * 
             * @author Zach Deibert
             * @since 1.0
             * @throws Throwable
             */
            @Override
            protected void connectClient() throws Throwable
            {
                client.connect(getLocalIp(), port, 1000);
            }
        };
        runner.test();
    }

    /**
     * Tests to make sure the server does accept clients connecting to it
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws Throwable
     */
    @Test
    public void testIPFilteringPart2() throws Throwable
    {
        new TrackerPacketHandler(13);
        final TestRunner runner = new TestRunner((short) 4213,
                        new Packet[] { new TrackerPacket(42, 13) })
        {
            /**
             * Connects the client with a timeout so the test does not take
             * forever
             * 
             * @author Zach Deibert
             * @since 1.0
             * @throws Throwable
             */
            @Override
            protected void connectClient() throws Throwable
            {
                client.connect(getLocalIp(), port, 1000);
            }
        };
        runner.test();
    }

    /**
     * Tests to make sure the server only accepts clients connecting to its port
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws Throwable
     */
    @Test(expected = ConnectException.class)
    public void testPortFilteringPart1() throws Throwable
    {
        new TrackerPacketHandler(14);
        final TestRunner runner = new TestRunner((short) 4214,
                        new Packet[] { new TrackerPacket(42, 14) })
        {
            /**
             * Connects the client with a timeout so the test does not take
             * forever
             * 
             * @author Zach Deibert
             * @since 1.0
             * @throws Throwable
             */
            @Override
            protected void connectClient() throws Throwable
            {
                client.connect(getLocalIp(), (short) (port + 1), 1000);
            }
        };
        runner.test();
    }

    /**
     * Tests to make sure the server does accept clients connecting to its port
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws Throwable
     */
    @Test
    public void testPortFilteringPart2() throws Throwable
    {
        new TrackerPacketHandler(16);
        final TestRunner runner = new TestRunner((short) 4216,
                        new Packet[] { new TrackerPacket(42, 16) });
        runner.test();
    }
}

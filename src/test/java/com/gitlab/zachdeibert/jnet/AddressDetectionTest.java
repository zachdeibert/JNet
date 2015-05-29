package com.gitlab.zachdeibert.jnet;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the address detection of the other node
 * 
 * @author Zach Deibert
 * @since 1.1
 * @version 1.2
 */
public class AddressDetectionTest
{
    /**
     * Tests the IP detection
     * 
     * @author Zach Deibert
     * @since 1.1
     */
    @Test
    public void testIPDetection() throws Throwable
    {
        final Map<NetworkNode, String> IPs = Collections
                        .synchronizedMap(new HashMap<NetworkNode, String>());
        new PacketHandler(41)
        {
            /**
             * Saves the IP of the remote
             * 
             * @author Zach Deibert
             * @since 1.1
             * @param p
             *            The packet
             * @param sender
             *            The remote node
             */
            @Override
            protected void handle(final Packet p, final NetworkNode sender)
            {
                synchronized (IPs)
                {
                    try
                    {
                        IPs.put(sender, sender.getIP());
                    }
                    catch (final IOException ex)
                    {
                        IPs.put(sender, null);
                        ex.printStackTrace();
                    }
                }
            }
        };
        final TestRunner runner = new TestRunner((short) 4241,
                        new Packet[] { new Packet(41) })
        {
            /**
             * Verifies all packets
             * 
             * @author Zach Deibert
             * @since 1.1
             * @param p
             *            The packet to verify
             * @returns true
             */
            @Override
            protected boolean verifyPacket(final Packet p) throws Throwable
            {
                return true;
            }

            /**
             * Tests the packet
             * 
             * @author Zach Deibert
             * @since 1.1
             * @param p
             *            The packet
             * @param node
             *            The node to test
             */
            @Override
            protected void testPacket(final Packet p, final NetworkNode node)
                            throws Throwable
            {
                node.sendPacket(p);
                boolean run = true;
                final long timeout = System.currentTimeMillis() + 2000;
                while (run)
                {
                    synchronized (IPs)
                    {
                        if (IPs.size() > 0)
                        {
                            for (final NetworkNode n : IPs.keySet())
                            {
                                if (n != node)
                                {
                                    final String IP = IPs.get(n);
                                    Assert.assertEquals(
                                                    "IP was not detected correctly",
                                                    getLocalIp(), IP);
                                    run = false;
                                    break;
                                }
                            }
                        }
                    }
                    if (System.currentTimeMillis() > timeout)
                    {
                        Assert.fail("Test timeout.");
                    }
                }
            }
        };
        runner.test();
    }

    /**
     * Tests the port detection
     * 
     * @author Zach Deibert
     * @since 1.1
     */
    @Test
    public void testPortDetection() throws Throwable
    {
        final Map<NetworkNode, Integer> ports = Collections
                        .synchronizedMap(new HashMap<NetworkNode, Integer>());
        new PacketHandler(42)
        {
            /**
             * Saves the port of the remote
             * 
             * @author Zach Deibert
             * @since 1.1
             * @param p
             *            The packet
             * @param sender
             *            The remote node
             */
            @Override
            protected void handle(final Packet p, final NetworkNode sender)
            {
                synchronized (ports)
                {
                    try
                    {
                        ports.put(sender, sender.getPort());
                    }
                    catch (final IOException ex)
                    {
                        ports.put(sender, null);
                        ex.printStackTrace();
                    }
                }
            }
        };
        final TestRunner runner = new TestRunner((short) 4242,
                        new Packet[] { new Packet(42) })
        {
            /**
             * Verifies all packets
             * 
             * @author Zach Deibert
             * @since 1.1
             * @param p
             *            The packet to verify
             * @returns true
             */
            @Override
            protected boolean verifyPacket(final Packet p) throws Throwable
            {
                return true;
            }

            /**
             * Tests the transmission of a single packet from only the server
             * 
             * @author Zach Deibert
             * @param p
             *            The packet to transmit
             * @since 1.1
             * @throws Throwable
             */
            @Override
            protected void testPacket(final Packet p) throws Throwable
            {
                testPacket(p, server);
            }

            /**
             * Tests the packet
             * 
             * @author Zach Deibert
             * @since 1.1
             * @param p
             *            The packet
             * @param node
             *            The node to test
             */
            @Override
            protected void testPacket(final Packet p, final NetworkNode node)
                            throws Throwable
            {
                node.sendPacket(p);
                boolean run = true;
                final long timeout = System.currentTimeMillis() + 2000;
                while (run)
                {
                    synchronized (ports)
                    {
                        if (ports.size() > 0)
                        {
                            for (final NetworkNode n : ports.keySet())
                            {
                                if (n != node)
                                {
                                    final Integer port = ports.get(n);
                                    Assert.assertEquals(
                                                    "Port was not detected correctly",
                                                    this.port, (int) port);
                                    run = false;
                                    break;
                                }
                            }
                        }
                    }
                    if (System.currentTimeMillis() > timeout)
                    {
                        Assert.fail("Test timeout.");
                    }
                }
            }
        };
        runner.test();
    }
}

package com.gitlab.zachdeibert.jnet;

import org.junit.Test;

/**
 * Tests the transmission of packets
 * 
 * @author Zach Deibert
 * @since 1.0
 * @version 1.2
 */
public class TransmissionTest
{
    /**
     * Tests to make sure an integer can be transmitted over the network
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws Throwable
     */
    @Test
    public void testIntegerTransmission() throws Throwable
    {
        new TrackerPacketHandler(1);
        final TestRunner runner = new TestRunner((short) 4201,
                        new Packet[] { new TrackerPacket(42, 1) });
        runner.test();
    }

    /**
     * Tests to make sure multiple integers can be transmitted over the network
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws Throwable
     */
    @Test
    public void testMultipleIntegerTransmissions() throws Throwable
    {
        new TrackerPacketHandler(2);
        final TestRunner runner = new TestRunner((short) 4202, new Packet[] {
                        new TrackerPacket(4, 2), new TrackerPacket(2, 2),
                        new TrackerPacket(42, 2), new TrackerPacket(4242, 2), });
        runner.test();
    }

    /**
     * Tests to make sure multiple types of data can be transmitted over the
     * network
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws Throwable
     */
    @Test
    public void testMultipleTypeTransmissions() throws Throwable
    {
        new TrackerPacketHandler(3);
        final TestRunner runner = new TestRunner((short) 4203, new Packet[] {
                        new TrackerPacket(4, 3), new TrackerPacket("+", 3),
                        new TrackerPacket(2, 3), new TrackerPacket('=', 3),
                        new TrackerPacket(42, 3) });
        runner.test();
    }

    /**
     * Tests to make sure multiple packet handlers can be used for different
     * packets
     * 
     * @author Zach Deibert
     * @since 1.0
     * @throws Throwable
     */
    @Test
    public void testMultipleTypesOfTransmissions() throws Throwable
    {
        new TrackerPacketHandler(4);
        new TrackerPacketHandler(5)
        {
            /**
             * Adds a + to the end of the packet data
             * 
             * @author Zach Deibert
             * @since 1.0
             */
            @Override
            protected void handle(final Packet p, final NetworkNode sender)
            {
                if (p instanceof TrackerPacket)
                {
                    synchronized (TrackerPacketHandler.recievedPackets)
                    {
                        recievedPackets.add(((String) ((TrackerPacket) p).data)
                                        .concat("+"));
                    }
                }
            }
        };
        final TestRunner runner = new TestRunner((short) 4204, new Packet[] {
                        new TrackerPacket("a", 4), new TrackerPacket("b", 5) })
        {
            /**
             * Adds the + to the end of the packet data for packets with id 5
             * 
             * @author Zach Deibert
             * @since 1.0
             */
            @Override
            protected Object getExpected(final Packet p) throws Throwable
            {
                if (p.id == 5)
                {
                    return ((String) ((TrackerPacket) p).data).concat("+");
                }
                else
                {
                    return ((TrackerPacket) p).data;
                }
            }
        };
        runner.test();
    }
}

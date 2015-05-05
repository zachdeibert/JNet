package com.gitlab.zachdeibert.jnet;

/**
 * An event handler for connection events
 * 
 * @author Zach Deibert
 * @since 1.0
 * @version 1.1
 */
public interface ConnectEvent
{
    /**
     * Handles the connection event
     * 
     * @author Zach Deibert
     * @param connected
     *            The node that was connected. This will always be either a
     *            NetworkClient or NetworkServer.
     * @param connector
     *            The node that started the connetion. This will always be
     *            either a NetworkClient or RemoteClient.
     * @since 1.0
     */
    void onConnect(LocalNetworkNode connected, NetworkNode connector);

    /**
     * Adds two connect events together so both run when the event is called
     * 
     * @author Zach Deibert
     * @param evt1
     *            The first event to call
     * @param evt2
     *            The second event to call
     * @return The new event that calls both evt1 and evt2, or null if both
     *         arguments are null
     * @since 1.0
     */
    public static ConnectEvent add(final ConnectEvent evt1,
                    final ConnectEvent evt2)
    {
        if (evt1 == null)
        {
            return evt2;
        }
        if (evt2 == null)
        {
            return evt1;
        }
        return new ConnectEvent()
        {
            @Override
            public void onConnect(LocalNetworkNode connected,
                            NetworkNode connector)
            {
                evt1.onConnect(connected, connector);
                evt2.onConnect(connected, connector);
            }
        };
    }
}

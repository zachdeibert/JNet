package com.gitlab.zachdeibert.jnet;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * An interface for a network node this computer can connect
 * 
 * @author Zach Deibert
 * @since 1.0
 * @version 1.0
 */
public interface LocalNetworkNode extends NetworkNode
{
    /**
     * Connects the socket
     * 
     * @author Zach Deibert
     * @param IP
     *            The IP or host name to connect to
     * @param port
     *            The port to connect to
     * @since 1.0
     * @throws UnknownHostException
     *             The host name was not found in DNS
     * @throws IOException
     *             An I/O error has occurred
     */
    public void connect(String IP, short port) throws UnknownHostException,
                    IOException;
}

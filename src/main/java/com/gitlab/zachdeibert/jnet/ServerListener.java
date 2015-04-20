package com.gitlab.zachdeibert.jnet;

import java.net.SocketException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A thread that will accept pending clients
 * 
 * @author Zach Deibert
 * @see NetworkServer
 * @since 1.0
 * @version 1.0
 */
final class ServerListener extends Thread
{
    /**
     * A list of all of the listener threads that are still running
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    private static final List<ServerListener> activeListeners = Collections.synchronizedList(new LinkedList<ServerListener>());
    /**
     * Contains whether the shutdown hook has been registered or not
     * 
     * @author Zach Deibert
     * @see init
     * @since 1.0
     */
    private static boolean                    inited          = false;
    /**
     * The server this thread is listening on
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    private final NetworkServer               server;
    /**
     * Contains whether the thread should keep iterating
     * 
     * @author Zach Deibert
     * @see run()
     * @since 1.0
     */
    private boolean                           running;

    /**
     * Adds the shutdown hook if it is not already added
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    private static void init()
    {
        if (!inited)
        {
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    synchronized (activeListeners)
                    {
                        for (final ServerListener listener : activeListeners)
                        {
                            listener.interrupt();
                        }
                        activeListeners.clear();
                    }
                }
            }));
        }
    }

    /**
     * Listens for connecting clients
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    @Override
    public void run()
    {
        while (running)
        {
            try
            {
                server.acceptClient();
            }
            catch (final SocketException ex)
            {
                if (!ex.getMessage().matches("Socket (is )?closed"))
                {
                    ex.printStackTrace();
                }
            }
            catch (final Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Stops the thread after the current iteration has finished
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    @Override
    public void interrupt()
    {
        running = false;
    }

    /**
     * Starts the thread
     * 
     * @author Zach Deibert
     * @see run
     * @since 1.0
     */
    @Override
    public void start()
    {
        super.start();
        activeListeners.add(this);
    }

    /**
     * Constructs a new server listener and starts the thread
     * 
     * @author Zach Deibert
     * @param server
     *            The server to listen on
     * @see init
     * @since 1.0
     */
    ServerListener(final NetworkServer server)
    {
        init();
        running = true;
        this.server = server;
        start();
    }
}

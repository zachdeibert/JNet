package com.gitlab.zachdeibert.jnet;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The thread that executes all of the asynchronous tasks
 * 
 * @author Zach Deibert
 * @since 1.0
 * @version 1.2.1
 */
final class AsyncRunner extends Thread
{
    /**
     * A list of all of the tasks that need to run
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    private static final List<AsyncReadable> readables = Collections.synchronizedList(new LinkedList<AsyncReadable>());
    /**
     * Contains the target state of the thread.
     * Null denotes that the thread has not started.
     * False denotes that the thread needs to be shutdown.
     * True denotes that the thread should be running
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    private static Boolean                   running   = null;

    /**
     * Starts the thread if it is not already running
     * 
     * @author Zach Deibert
     * @see running
     * @since 1.0
     */
    static void init()
    {
        if (running == null)
        {
            running = true;
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    running = false;
                }
            }));
            final Thread thread = new AsyncRunner();
            thread.start();
        }
    }

    /**
     * Adds a task to the asynchronous execution queue
     * 
     * @author Zach Deibert
     * @param ar
     *            The instance to add to the queue
     * @see readables
     * @since 1.0
     */
    static void add(final AsyncReadable ar)
    {
        synchronized (readables)
        {
            readables.add(ar);
        }
    }

    /**
     * Removes a task from the asynchronous execution queue
     * 
     * @author Zach Deibert
     * @param ar
     *            The instance to remove from the queue
     * @see readables
     * @since 1.0
     */
    static void remove(final AsyncReadable ar)
    {
        synchronized (readables)
        {
            readables.remove(ar);
        }
    }

    /**
     * Processes the asynchronous execution queue
     * 
     * @author Zach Deibert
     * @see readables
     * @since 1.0
     */
    @Override
    public void run()
    {
        final List<AsyncReadable> copy = new LinkedList<AsyncReadable>();
        while (running)
        {
            copy.clear();
            synchronized (readables)
            {
                copy.addAll(readables);
            }
            for (final AsyncReadable readable : copy)
            {
                if (readable.closed)
                {
                    remove(readable);
                }
                else
                {
                    try
                    {
                        readable.iteration();
                    }
                    catch (final Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}

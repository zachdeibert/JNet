package com.gitlab.zachdeibert.jnet;

/**
 * Points to another object
 * 
 * @author Zach Deibert
 * @param <T>
 *            The type to point to
 * @since 1.0
 */
final class Pointer<T>
{
    /**
     * The object that is being pointed to
     * 
     * @author Zach Deibert
     * @since 1.0
     */
    T data;

    /**
     * Constructs a pointer with the data
     * 
     * @author Zach Deibert
     * @since 1.1
     * @param data
     *            The object to point to
     */
    public Pointer(final T data)
    {
        this.data = data;
    }

    /**
     * Constructs a pointer with no data
     * 
     * @author Zach Deibert
     * @since 1.1
     */
    public Pointer()
    {
        this(null);
    }
}

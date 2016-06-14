package com.gitlab.zachdeibert.jnet;

/**
 * An exception that is thrown when there are not enough reserved packet ids for
 * a task to finish or when a packet with a reserved id is taken.
 * 
 * @author Zach Deibert
 * @since 1.2.2
 * @version 1.2.2
 */
public class PacketIdReservationException extends RuntimeException {
	private static final long serialVersionUID = 7628521394093580476L;

	/**
	 * Creates a new {@link PacketIdReservationException}
	 * 
	 * @see Exception#Exception()
	 * @since 1.2.2
	 */
	public PacketIdReservationException() {
	}

	/**
	 * Creates a new {@link PacketIdReservationException}
	 * 
	 * @param message
	 *            The message
	 * @param cause
	 *            The cause
	 * @param enableSuppression
	 *            If the exception can be suppressed
	 * @param writableStackTrace
	 *            If the stack trace is writable
	 * @see Exception#Exception(String, Throwable, boolean, boolean)
	 * @since 1.2.2
	 */
	public PacketIdReservationException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Creates a new {@link PacketIdReservationException}
	 * 
	 * @param message
	 *            The message
	 * @param cause
	 *            The cause
	 * @see Exception#Exception(String, Throwable)
	 * @since 1.2.2
	 */
	public PacketIdReservationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new {@link PacketIdReservationException}
	 * 
	 * @param message
	 *            The message
	 * @see Exception#Exception(String)
	 * @since 1.2.2
	 */
	public PacketIdReservationException(final String message) {
		super(message);
	}

	/**
	 * Creates a new {@link PacketIdReservationException}
	 * 
	 * @param cause
	 *            The cause
	 * @see Exception#Exception(Throwable)
	 * @since 1.2.2
	 */
	public PacketIdReservationException(final Throwable cause) {
		super(cause);
	}
}

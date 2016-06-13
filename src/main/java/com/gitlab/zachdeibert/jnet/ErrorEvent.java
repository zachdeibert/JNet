package com.gitlab.zachdeibert.jnet;

/**
 * An event handler for an event dispatched whenever an error occurs
 * 
 * @author Zach Deibert
 * @since 1.2.2
 * @version 1.2.2
 */
public interface ErrorEvent {
	/**
	 * Handles an error where the node is known
	 * 
	 * @param t
	 *            The error
	 * @param node
	 *            The node that caused the error
	 * @since 1.2.2
	 */
	void handleError(Throwable t, NetworkNode node);

	/**
	 * Handles an error where the node is unknown
	 * 
	 * @param t
	 *            The error
	 * @since 1.2.2
	 */
	void handleUnknownError(Throwable t);
}

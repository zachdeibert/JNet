package com.gitlab.zachdeibert.jnet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Class to handle the networking errors for an application
 * 
 * @author Zach Deibert
 * @since 1.2.2
 * @version 1.2.2
 */
public class NetworkErrors {
	private static final Map<NetworkNode, List<ErrorEvent>> handlers = new HashMap<NetworkNode, List<ErrorEvent>>();
	private static final Map<Predicate<NetworkNode>, List<ErrorEvent>> dynamicHandlers = new HashMap<Predicate<NetworkNode>, List<ErrorEvent>>();
	private static final List<ErrorEvent> unknownHandlers = new ArrayList<ErrorEvent>();

	/**
	 * Called after an error occurs in the networking code
	 * 
	 * @param t
	 *            The error
	 * @param node
	 *            The node with the error, or <code>null</code> if the node is
	 *            unknown
	 * @since 1.2.2
	 */
	static void networkError(final Throwable t, final NetworkNode node) {
		boolean handled = false;
		if ( node == null ) {
			unknownHandlers.forEach(e -> e.handleUnknownError(t));
			handled = !unknownHandlers.isEmpty();
		} else {
			for ( final NetworkNode n : handlers.keySet() ) {
				if ( n == node ) {
					handled = true;
					handlers.get(n).forEach(e -> e.handleError(t, node));
				}
			}
			for ( final Predicate<NetworkNode> n : dynamicHandlers.keySet() ) {
				if ( n.test(node) ) {
					handled = true;
					dynamicHandlers.get(n).forEach(e -> e.handleError(t, node));
				}
			}
		}
		if ( !handled ) {
			t.printStackTrace();
		}
	}

	/**
	 * Called after an error occurs in the networking code
	 * 
	 * @param t
	 *            The error
	 * @since 1.2.2
	 */
	static void networkError(final Throwable t) {
		networkError(t, null);
	}

	/**
	 * Registers a handler to handle errors for a {@link NetworkNode}.
	 * 
	 * @param node
	 *            The node to handle for
	 * @param handlers
	 *            The actions to handle with
	 * @since 1.2.2
	 */
	public static void registerHandler(final NetworkNode node, final ErrorEvent... handlers) {
		final List<ErrorEvent> list;
		if ( NetworkErrors.handlers.containsKey(node) ) {
			list = NetworkErrors.handlers.get(node);
		} else {
			list = new ArrayList<ErrorEvent>();
			NetworkErrors.handlers.put(node, list);
		}
		list.addAll(Arrays.asList(handlers));
	}

	/**
	 * Registers a handler to handle errors for a {@link NetworkNode} that is
	 * determined when an error occurs.
	 * 
	 * @param func
	 *            The function to determine whether or not the handler can
	 *            handle an error
	 * @param handlers
	 *            The actions to handle with
	 * @since 1.2.2
	 */
	public static void registerDynamicHandler(final Predicate<NetworkNode> func, final ErrorEvent... handlers) {
		final List<ErrorEvent> list;
		if ( dynamicHandlers.containsKey(func) ) {
			list = dynamicHandlers.get(func);
		} else {
			list = new ArrayList<ErrorEvent>();
			dynamicHandlers.put(func, list);
		}
		list.addAll(Arrays.asList(handlers));
	}

	/**
	 * Registers a handler to handle errors for unknown nodes.
	 * 
	 * @param handlers
	 *            The actions to handle with
	 * @since 1.2.2
	 */
	public static void registerUnknownHandler(final ErrorEvent... handlers) {
		unknownHandlers.addAll(Arrays.asList(handlers));
	}
}

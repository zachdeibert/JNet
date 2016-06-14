package com.gitlab.zachdeibert.jnet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A registry of the packet ids that are reserved for internal use
 * 
 * @author Zach Deibert
 * @since 1.2.2
 * @version 1.2.2
 */
public class ReservedIds {
	private static final List<PacketIdRange> allReservedRanges = new ArrayList<PacketIdRange>();
	private static final List<PacketIdRange> reservedRanges = new ArrayList<PacketIdRange>();

	/**
	 * Removes a range of ports from the reserved id mappings
	 * 
	 * @param range
	 *            The range to remove
	 * @since 1.2.2
	 */
	public static void removeReservedRange(final PacketIdRange range) {
		final List<PacketIdRange> newList = new ArrayList<PacketIdRange>();
		for ( final PacketIdRange reserved : reservedRanges ) {
			newList.addAll(Arrays.asList(reserved.subtract(range)));
		}
		reservedRanges.clear();
		reservedRanges.addAll(newList);
	}

	/**
	 * Adds a range of ports to the reserved id mappings
	 * 
	 * @param range
	 *            The range to add
	 * @since 1.2.2
	 */
	public static void addReservedRange(final PacketIdRange range) {
		if ( !range.isEmpty() ) {
			removeReservedRange(range);
			reservedRanges.add(range);
			allReservedRanges.add(range);
		}
	}

	/**
	 * Gets whether or not a packet id is reserved
	 * 
	 * @param id
	 *            The packet id
	 * @return If it is reserved
	 * @since 1.2.2
	 */
	public static boolean isReserved(final int id) {
		for ( final PacketIdRange range : allReservedRanges ) {
			if ( range.contains(id) ) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets a new reserved packet id and marks it as used (so it will not give
	 * the same id twice)
	 * 
	 * @return The packet id
	 * @see PacketIdRange#popPort()
	 * @since 1.2.2
	 */
	static int getReservedId() {
		if ( reservedRanges.isEmpty() ) {
			throw new PacketIdReservationException("No reserved id ranges found");
		} else {
			final PacketIdRange range = reservedRanges.get(0);
			final int id = range.popPort();
			if ( range.isEmpty() ) {
				reservedRanges.remove(range);
			}
			return id;
		}
	}

	static {
		addReservedRange(new PacketIdRange(-1000000, -999000));
	}
}

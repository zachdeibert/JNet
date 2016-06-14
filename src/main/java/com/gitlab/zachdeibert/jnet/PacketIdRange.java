package com.gitlab.zachdeibert.jnet;

/**
 * Represents a range of ports
 * 
 * @author Zach Deibert
 * @since 1.2.2
 * @version 1.2.2
 */
public class PacketIdRange {
	private int from;
	private int to;

	/**
	 * Gets the lower bound of this range (inclusive)
	 * 
	 * @return The lower bound
	 * @since 1.2.2
	 */
	public int getFrom() {
		return from;
	}

	/**
	 * Sets the lower bound of this range (inclusive)
	 * 
	 * @param fromPort
	 *            The lower bound
	 * @since 1.2.2
	 */
	public void setFrom(final int fromPort) {
		this.from = fromPort;
	}

	/**
	 * Gets the upper bound of this range (inclusive)
	 * 
	 * @return The upper bound
	 * @since 1.2.2
	 */
	public int getTo() {
		return to;
	}

	/**
	 * Sets the upper bound of this range (inclusive)
	 * 
	 * @param toPort
	 *            The upper bound
	 * @since 1.2.2
	 */
	public void setTo(final int toPort) {
		this.to = toPort;
	}

	/**
	 * Subtracts an id range from this id range
	 * 
	 * @param sub
	 *            The range to subtract
	 * @return The new port ranges
	 * @since 1.2.2
	 */
	public PacketIdRange[] subtract(final PacketIdRange sub) {
		if ( sub.from <= from && sub.to >= to ) {
			return new PacketIdRange[0];
		} else if ( sub.to < from || sub.from > to ) {
			return new PacketIdRange[] { this };
		} else if ( sub.from <= from ) {
			return new PacketIdRange[] { new PacketIdRange(sub.to, to) };
		} else if ( sub.to >= to ) {
			return new PacketIdRange[] { new PacketIdRange(from, sub.from) };
		} else {
			return new PacketIdRange[] { new PacketIdRange(from, sub.from), new PacketIdRange(sub.to, to) };
		}
	}

	/**
	 * Gets whether or not this range contains a specified id
	 * 
	 * @param id
	 *            The id
	 * @return If it contains the id
	 * @since 1.2.2
	 */
	public boolean contains(final int id) {
		return id >= from && id <= to;
	}

	/**
	 * Gets if this id range is empty
	 * 
	 * @return <code>true</code> if this id range contains no ids
	 * @since 1.2.2
	 */
	public boolean isEmpty() {
		return from == to;
	}

	/**
	 * Gets an id from this range and shortens this range so it does not include
	 * that id anymore
	 * 
	 * @return The removed port
	 * @since 1.2.2
	 */
	public int popPort() {
		if ( isEmpty() ) {
			return -1;
		} else {
			return from++;
		}
	}

	/**
	 * Default constructor
	 * 
	 * @param from
	 *            The lower bound of this range (inclusive)
	 * @param to
	 *            The upper bound of this range (inclusive)
	 * @since 1.2.2
	 */
	public PacketIdRange(final int from, final int to) {
		this.from = from;
		this.to = to;
	}
}

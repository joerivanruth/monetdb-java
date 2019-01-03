/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0.  If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright 1997 - July 2008 CWI, August 2008 - 2019 MonetDB B.V.
 */

package nl.cwi.monetdb.jdbc;

import java.sql.SQLException;

/**
 * A Wrapper class which provide the ability to retrieve the delegate instance
 * when the instance in question is in fact a proxy class.
 *
 * The wrapper pattern is employed by many JDBC driver implementations to provide
 * extensions beyond the traditional JDBC API that are specific to a data source.
 * Developers may wish to gain access to these resources that are wrapped (the delegates)
 * as proxy class instances representing the the actual resources.
 * This class contains a standard mechanism to access these wrapped resources
 * represented by their proxy, to permit direct access to the resource delegates.
 *
 * @author Fabian Groffen, Martin van Dinther
 * @version 1.1
 */
public class MonetWrapper implements java.sql.Wrapper {
	/**
	 * Returns true if this either implements the interface argument or
	 * is directly or indirectly a wrapper for an object that does.
	 * Returns false otherwise. If this implements the interface then
	 * return true, else if this is a wrapper then return the result of
	 * recursively calling <code>isWrapperFor</code> on the wrapped object.
	 * If this does not implement the interface and is not a wrapper, return
	 * false. This method should be implemented as a low-cost operation
	 * compared to <code>unwrap</code> so that callers can use this method to avoid
	 * expensive <code>unwrap</code> calls that may fail.
	 * If this method returns true then calling <code>unwrap</code> with the same argument should succeed.
	 *
	 * @param iface a Class defining an interface.
	 * @return true if this implements the interface or directly or indirectly wraps an object that does.
	 * @throws SQLException if an error occurs while determining whether this is a wrapper
	 * 	for an object with the given interface.
	 * @since 1.6
	 */
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return iface != null && iface.isAssignableFrom(getClass());
	}

	/**
	 * Returns an object that implements the given interface to allow
	 * access to non-standard methods, or standard methods not exposed by the proxy.
	 * The result may be either the object found to implement the interface
	 * or a proxy for that object.
	 * If the receiver implements the interface then the result is the receiver
	 * or a proxy for the receiver.
	 * If the receiver is a wrapper and the wrapped object implements the interface
	 * then the result is the wrapped object or a proxy for the wrapped object.
	 * Otherwise return the result of calling <code>unwrap</code> recursively on
	 * the wrapped object or a proxy for that result.
	 * If the receiver is not a wrapper and does not implement the interface,
	 * then an <code>SQLException</code> is thrown.
	 *
	 * @param iface A Class defining an interface that the result must implement.
	 * @return an object that implements the interface. May be a proxy for the actual implementing object.
	 * @throws SQLException If no object found that implements the interface
	 * @since 1.6
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T unwrap(Class<T> iface) throws SQLException {
		if (isWrapperFor(iface)) {
			return (T) this;
		}
		throw new SQLException("Cannot unwrap to interface: " + (iface != null ? iface.getName() : ""), "0A000");
	}
}

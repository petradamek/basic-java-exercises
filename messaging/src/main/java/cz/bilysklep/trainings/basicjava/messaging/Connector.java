package cz.bilysklep.trainings.basicjava.messaging;

import java.net.NoRouteToHostException;
import java.net.UnknownHostException;

/**
 * This interface represents a component which is capable to establish
 * connection with some computer. Established connection can be used for sending
 * data to the computer.
 *
 * @author Tomas Pitner
 * @author Petr Adamek
 */
public interface Connector {

    /**
     * Establish and returns connection to computer with given address.
     *
     * @param target address of target computer
     * @throws java.net.UnknownHostException if the address does not exist
     * @throws java.net.NoRouteToHostException if the connection with target
     * computer can't be established due to insufficient routing information
     * @return established connection
     */
    Connection getConnection(String target) throws UnknownHostException,
            NoRouteToHostException;
}
